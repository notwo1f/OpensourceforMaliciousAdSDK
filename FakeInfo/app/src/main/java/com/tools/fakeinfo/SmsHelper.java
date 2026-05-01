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

    // Check if it is the default app
    public boolean isDefaultSmsApp() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context));
    }

    // Default app request method with multi-version compatibility
    public void requestDefaultSmsApp(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ open default apps settings directly
            Intent intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
            activity.startActivity(intent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 - 9 use standard intent
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());

            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            } else {
                // Fallback: open app details page
                openAppDetails(activity);
            }
        }
    }

    // Open app details page
    private void openAppDetails(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        activity.startActivity(intent);
    }

    // Insert SMS (needs to be default app first)
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
            Log.e("SmsHelper", "Failed to insert SMS: " + e.getMessage());
            return -1;
        }
    }
}