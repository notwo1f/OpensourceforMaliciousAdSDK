package com.tools.fakeinfo;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;

import java.util.Date;

public class SmsHelper {
    private final Context context;

    public SmsHelper(Context context) {
        this.context = context;
    }

    // 检查是否为默认应用
    public boolean isDefaultSmsApp() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context));
    }

    // 多版本兼容的默认应用请求方法
    public void requestDefaultSmsApp(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ 直接打开应用设置
            Intent intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
            activity.startActivity(intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 - 9 使用标准intent
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());

            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            } else {
                // 备用方案：打开应用详情页
                openAppDetails(activity);
            }
        }
    }

    // 打开应用详情页
    private void openAppDetails(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        activity.startActivity(intent);
    }

    // 插入短信（需先成为默认应用）
    public long insertSms(String address, String body, Date date, int type) {
        if (!isDefaultSmsApp()) return -1;

        ContentValues values = new ContentValues();
        values.put(Telephony.Sms.ADDRESS, address);
        values.put(Telephony.Sms.BODY, body);
        values.put(Telephony.Sms.DATE, date.getTime());
        values.put(Telephony.Sms.TYPE, type);

        try {
            Uri uri = context.getContentResolver().insert(Telephony.Sms.CONTENT_URI, values);
            return uri != null ? ContentUris.parseId(uri) : -1;
        } catch (Exception e) {
            Log.e("SmsHelper", "插入短信失败: " + e.getMessage());
            return -1;
        }
    }
}