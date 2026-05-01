package com.tools.fakeinfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SMSInfo {
    // SMS type constants
    public static final int TYPE_INBOX = 1;    // Inbox
    public static final int TYPE_SENT = 2;     // Sent
    public static final int TYPE_DRAFT = 3;    // Draft
    public static final int TYPE_OUTBOX = 4;   // Outbox
    public static final int TYPE_FAILED = 5;   // Failed
    public static final int TYPE_QUEUED = 6;   // Queued

    // Contact type constants
    public static final int CONTACT_FAMILY = 1;    // Family
    public static final int CONTACT_FRIEND = 2;    // Friend
    public static final int CONTACT_WORK = 3;      // Colleague
    public static final int CONTACT_SERVICE = 4;   // Customer Service
    public static final int CONTACT_UNKNOWN = 5;   // Unknown

    private static final Random RANDOM = new Random();
    private static final String[] NUMBER_PREFIXES = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "145", "147", "150", "151", "152", "153", "155", "156", "157", "158", "159", "166", "170", "171", "172", "173", "175", "176", "177", "178", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "198", "199"};
    private static final String[] SERVICE_PREFIXES = {
            "1065", "1068", "1069"
    };

    private String number;      // Phone number
    private String content;     // SMS content
    private int type;           // SMS type
    private long date;          // SMS date
    private int contactType;    // Contact type

    // Complete constructor
    public SMSInfo(String number, String content, int type, long date, int contactType) {
        this.number = number;
        this.content = content;
        this.type = type;
        this.date = date;
        this.contactType = contactType;
    }

    // Random generation constructor
    public SMSInfo() {

        this.contactType = generateRandomContactType();
        this.number = generateRandomNumber();
        this.content = SMSContentGenerator.generateMessageByContactType(contactType);
        if (contactType==SMSInfo.CONTACT_SERVICE)
        {
            this.number = generateServiceNumber();
        }
        this.type = generateRandomType();
        this.date = generateRandomDate();
    }

    // Random generation constructor with specified contact type
    public SMSInfo(int contactType) {
        this.number = generateRandomNumber();
        this.contactType = contactType;
        this.content = SMSContentGenerator.generateMessageByContactType(contactType);
        this.type = generateRandomType();
        this.date = generateRandomDate();
    }

    // Random generation constructor with specified contact type and message category
    public SMSInfo(int contactType, int messageCategory) {
        this.number = generateRandomNumber();
        this.contactType = contactType;

        // Generate content based on contact type and message category
        if (contactType == CONTACT_SERVICE && messageCategory != SMSContentGenerator.CATEGORY_VERIFICATION) {
            this.content = SMSContentGenerator.generateMessageByContactType(contactType);
                this.number = generateServiceNumber();

        } else {
            if (messageCategory==SMSContentGenerator.CATEGORY_PROMOTION)
            {
                this.number = generateServiceNumber();
            }
            this.content = SMSContentGenerator.generateMessageByCategory(messageCategory);
        }

        this.type = generateRandomType();
        this.date = generateRandomDate();
    }

    // Getters
    public String getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public long getDate() {
        return date;
    }

    public int getContactType() {
        return contactType;
    }

    // Get text description of SMS type
    public String getTypeDescription() {
        switch (type) {
            case TYPE_INBOX:
                return "Inbox";
            case TYPE_SENT:
                return "Sent";
            case TYPE_DRAFT:
                return "Draft";
            case TYPE_OUTBOX:
                return "Outbox";
            case TYPE_FAILED:
                return "Failed";
            case TYPE_QUEUED:
                return "Queued";
            default:
                return "Unknown";
        }
    }

    // Get text description of contact type
    public String getContactTypeDescription() {
        switch (contactType) {
            case CONTACT_FAMILY:
                return "Family";
            case CONTACT_FRIEND:
                return "Friend";
            case CONTACT_WORK:
                return "Colleague";
            case CONTACT_SERVICE:
                return "Customer Service";
            case CONTACT_UNKNOWN:
                return "Unknown";
            default:
                return "Other";
        }
    }

    // Format date display
    public String getFormattedDate() {
        SimpleDateFormat format;
        if (isToday(date)) {
            format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else if (isThisYear(date)) {
            format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        }
        return format.format(new Date(date));
    }

    // Generate random phone number
    private String generateRandomNumber() {
        String prefix = NUMBER_PREFIXES[RANDOM.nextInt(NUMBER_PREFIXES.length)];
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    private String generateServiceNumber() {
        String prefix = SERVICE_PREFIXES[RANDOM.nextInt(SERVICE_PREFIXES.length)];
        StringBuilder sb = new StringBuilder(prefix);

        // Calculate remaining digits to pad to 19 characters
        int remaining = 19 - prefix.length();
        for (int i = 0; i < remaining; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        return sb.toString();
    }

    // Generate random SMS type
    private int generateRandomType() {
        // Adjust probability distribution to make inbox and sent more common
        int[] types = {TYPE_INBOX, TYPE_SENT, TYPE_DRAFT, TYPE_OUTBOX, TYPE_FAILED, TYPE_QUEUED};
        double[] weights = {0.5, 0.3, 0.05, 0.05, 0.05, 0.05}; // Probability weights

        double rand = RANDOM.nextDouble();
        double cumulativeWeight = 0.0;

        for (int i = 0; i < types.length; i++) {
            cumulativeWeight += weights[i];
            if (rand < cumulativeWeight) {
                return types[i];
            }
        }

        return types[0]; // Default to inbox
    }

    // Generate random contact type
    private int generateRandomContactType() {
        int[] types = {CONTACT_FAMILY, CONTACT_FRIEND, CONTACT_WORK, CONTACT_SERVICE, CONTACT_UNKNOWN};
        double[] weights = {0.25, 0.3, 0.2, 0.15, 0.1}; // Probability weights

        double rand = RANDOM.nextDouble();
        double cumulativeWeight = 0.0;

        for (int i = 0; i < types.length; i++) {
            cumulativeWeight += weights[i];
            if (rand < cumulativeWeight) {
                return types[i];
            }
        }

        return types[0]; // Default to family
    }

    // Generate random date (within the last 30 days)
    private long generateRandomDate() {
        long now = System.currentTimeMillis();
        long thirtyDaysAgo = now - 30L * 24 * 60 * 60 * 1000;
        return thirtyDaysAgo + (long) (RANDOM.nextDouble() * (now - thirtyDaysAgo));
    }

    // Check if it is today
    private boolean isToday(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date(time)).equals(format.format(new Date()));
    }

    // Check if it is this year
    private boolean isThisYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
        return format.format(new Date(time)).equals(format.format(new Date()));
    }

    // Override toString method for easy debugging
    @Override
    public String toString() {
        return "SMSInfo{" + "number='" + number + '\'' + ", content='" + content + '\'' + ", type=" + getType() + ", date=" + getFormattedDate() + ", contactType=" + getContactTypeDescription() + '}';
    }
}