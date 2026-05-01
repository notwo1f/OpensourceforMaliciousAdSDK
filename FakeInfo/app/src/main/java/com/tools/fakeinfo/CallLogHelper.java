package com.tools.fakeinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Date;

public class CallLogHelper {
    private final Context context;
    private static final int MAX_RETRIES = 3;

    public CallLogHelper(Context context) {
        this.context = context;
    }

    // ==================== Call Log Related Methods ====================

    /**
     * Insert a call log
     *
     * @param phoneNumber Phone number
     * @param callType    Call type (CallLog.Calls.INCOMING_TYPE, OUTGOING_TYPE, MISSED_TYPE)
     * @param callDate    Call date
     * @param duration    Call duration (seconds)
     * @return Returns the record ID if inserted successfully, -1 otherwise
     */
    public long insertCallLog(String phoneNumber, int callType, Date callDate, int duration) {
        if (!hasCallLogPermission()) {
            return -1;
        }
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        Uri insertUri = (callType == TelephonyNumber.TYPE_VOICEMAIL) ? Uri.parse(CallLog.Calls.CONTENT_URI + "?allow_voicemails=true") : CallLog.Calls.CONTENT_URI;

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(insertUri).withValue(CallLog.Calls.NUMBER, phoneNumber).withValue(CallLog.Calls.DATE, callDate.getTime()).withValue(CallLog.Calls.DURATION, duration).withValue(CallLog.Calls.TYPE, callType).withValue(CallLog.Calls.NEW, 1) // Mark as new record
                .withValue(CallLog.Calls.CACHED_NAME, getContactName(phoneNumber));

        operations.add(builder.build());

        try {
            resolver.applyBatch(CallLog.AUTHORITY, operations);
            return queryLastCallLogId(phoneNumber, callDate);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Check for call log permissions
     */
    public boolean hasCallLogPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Query the ID of the last inserted call log
     */
    @SuppressLint("Range")
    private long queryLastCallLogId(String phoneNumber, Date callDate) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = {CallLog.Calls._ID};
        String selection = CallLog.Calls.NUMBER + " = ? AND " + CallLog.Calls.DATE + " = ?";
        String[] selectionArgs = {phoneNumber, String.valueOf(callDate.getTime())};

        try (Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
            }
        }
        return -1;
    }

    /**
     * Get contact name by phone number
     */
    @SuppressLint("Range")
    private String getContactName(String phoneNumber) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        try (Cursor cursor = resolver.query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }
        }
        return null;
    }

    // ==================== Contact Related Methods ====================

    /**
     * Insert a contact
     *
     * @param name        Contact name
     * @param phoneNumber Phone number
     * @return Returns contact ID if inserted successfully, -1 otherwise
     */
    public long insertContact(String name, String phoneNumber) {
        if (!hasContactsPermission()) {
            return -1;
        }

        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        long contactId = -1;

        // Insert raw contact
        operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // Insert contact name
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());

        // Insert contact phone
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
            return queryContactId(name, phoneNumber);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Check for contact permissions
     */
    public boolean hasContactsPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Query contact ID
     */
    private long queryContactId(String name, String phoneNumber) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = {ContactsContract.Contacts._ID};
        String selection = ContactsContract.Contacts.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {name};

        try (Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // Verify if phone number matches
                String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
                String[] phoneSelectionArgs = {String.valueOf(contactId), phoneNumber};

                try (Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, phoneSelection, phoneSelectionArgs, null)) {
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        return contactId;
                    }
                }
            }
        }
        return -1;
    }

    // ==================== Other Helper Methods ====================

    /**
     * Check and request required permissions
     *
     * @param permissions Array of permissions
     * @return Returns true if all permissions are granted, false otherwise
     */
    public boolean checkAndRequestPermissions(String[] permissions) {
        boolean allGranted = true;
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                permissionsToRequest.add(permission);
            }
        }

        if (!allGranted && context instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) context;
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toArray(new String[0]), 1001);
        }

        return allGranted;
    }

    /**
     * Get all required permissions
     */
    public String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        } else {
            return new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        }
    }
}