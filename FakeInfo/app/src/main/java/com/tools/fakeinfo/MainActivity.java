package com.tools.fakeinfo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.permissionx.guolindev.PermissionX;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private CallLogHelper callLogHelper;

    private SmsHelper smsHelper;

    private final String[] permission = {"android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"};// "android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button call_log = findViewById(R.id.call_log);
        //Button set_default = findViewById(R.id.set_default);
        callLogHelper = new CallLogHelper(this);
        smsHelper = new SmsHelper(this);

        PermissionX.init(this).permissions(permission).onExplainRequestReason((scope, deniedList) -> {
            scope.showRequestReasonDialog(deniedList, "Permission Required", "Allow");
        }).onForwardToSettings((scope, deniedList) -> {
            scope.showForwardToSettingsDialog(deniedList, "Permission Required", "Allow");
        }).request((allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Some permissions denied", Toast.LENGTH_SHORT).show();
            }
        });
        //set_default.setOnClickListener(v -> {
            //smsHelper.requestDefaultSmsApp(this);
       // });

        call_log.setOnClickListener(v -> {
            insertCallLog();
            //insertSmsLog();
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
            Toast.makeText(MainActivity.this, "CallLog inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Failed to insert CallLog", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertSmsLog() {
        long smsId = 0;
        for (int i = 0; i < 10; i++) {
            SMSInfo smsInfo = new SMSInfo(SMSInfo.CONTACT_SERVICE, SMSContentGenerator.CATEGORY_URGENT);
            smsId = smsHelper.insertSms(smsInfo.getNumber(), smsInfo.getContent(), new Date(smsInfo.getDate()), smsInfo.getType());
            Log.e("TEST","number:"+smsInfo.getNumber()+"\tcontent"+smsInfo.getContent()+"\t"+smsInfo.getType());
        }
        for (int i = 0; i < 10; i++) {
            SMSInfo smsInfo = new SMSInfo();
            smsId = smsHelper.insertSms(smsInfo.getNumber(), smsInfo.getContent(), new Date(smsInfo.getDate()), smsInfo.getType());
            Log.e("TEST","number:"+smsInfo.getNumber()+"\tcontent"+smsInfo.getContent()+"\t"+smsInfo.getType());
        }
        if (smsId != -1) {
            Toast.makeText(MainActivity.this, "SMS inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Failed to insert SMS", Toast.LENGTH_SHORT).show();
        }
    }
}