package com.tools.fakeinfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.permissionx.guolindev.PermissionX;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private CallLogHelper callLogHelper;

    private SmsHelper smsHelper;

    private final String[] permission = {"android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button call_log = findViewById(R.id.call_log);
        Button set_default = findViewById(R.id.set_default);
        callLogHelper = new CallLogHelper(this);
        smsHelper = new SmsHelper(this);

        PermissionX.init(this).permissions(permission).onExplainRequestReason((scope, deniedList) -> {
            scope.showRequestReasonDialog(deniedList, "需要权限", "允许");
        }).onForwardToSettings((scope, deniedList) -> {
            scope.showForwardToSettingsDialog(deniedList, "需要权限", "允许");
        }).request((allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                Toast.makeText(this, "所有权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "部分权限未授予", Toast.LENGTH_SHORT).show();
            }
        });
        set_default.setOnClickListener(v -> {
            smsHelper.requestDefaultSmsApp(this);
        });

        call_log.setOnClickListener(v -> {
            insertCallLog();
            insertSmsLog();
        });
    }


    private void insertCallLog() {
        long callId = 0;
        for (int i = 0; i < 10; i++) {
            TelephonyNumber call = new TelephonyNumber();
            callLogHelper.insertContact(call.getName(), call.getNumber());
            int time = (int) (Math.random() * 1000) + 10;
            callLogHelper.insertCallLog(call.getNumber(), call.getType(), new Date(call.getDate()), time);
            SMSInfo smsInfo = new SMSInfo();
            smsHelper.insertSms(call.getNumber(), smsInfo.getContent(), new Date(smsInfo.getDate()), smsInfo.getType());
        }

        for (int i = 0; i < 10; i++) {
            TelephonyNumber call = new TelephonyNumber();
            int time = (int) (Math.random() * 1000) + 10;
            callId = callLogHelper.insertCallLog(call.getNumber(), call.getType(), new Date(call.getDate()), time);
        }
        if (callId != -1) {
            Toast.makeText(MainActivity.this, "CallLog插入成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "CallLog插入失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertSmsLog() {
        long smsId = 0;
        for (int i = 0; i < 10; i++) {
            SMSInfo smsInfo = new SMSInfo(SMSInfo.CONTACT_SERVICE, SMSContentGenerator.CATEGORY_URGENT);
            smsId = smsHelper.insertSms(smsInfo.getNumber(), smsInfo.getContent(), new Date(smsInfo.getDate()), smsInfo.getType());
        }
        for (int i = 0; i < 10; i++) {
            SMSInfo smsInfo = new SMSInfo();
            smsId = smsHelper.insertSms(smsInfo.getNumber(), smsInfo.getContent(), new Date(smsInfo.getDate()), smsInfo.getType());
        }
        if (smsId != -1) {
            Toast.makeText(MainActivity.this, "短信插入成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "短信插入失败", Toast.LENGTH_SHORT).show();
        }
    }
}