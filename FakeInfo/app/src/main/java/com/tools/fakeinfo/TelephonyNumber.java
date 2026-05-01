package com.tools.fakeinfo;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TelephonyNumber {
    // Call type constants
    public static final int TYPE_INCOMING = 1;  // Incoming
    public static final int TYPE_OUTGOING = 2;  // Outgoing
    public static final int TYPE_MISSED = 3;    // Missed
    public static final int TYPE_VOICEMAIL = 4; // Voicemail

    // Contact type constants
    public static final int CONTACT_FAMILY = 1;    // Family
    public static final int CONTACT_FRIEND = 2;    // Friend
    public static final int CONTACT_WORK = 3;      // Colleague
    public static final int CONTACT_SERVICE = 4;   // Customer Service
    public static final int CONTACT_UNKNOWN = 5;   // Unknown

    // Gender constants
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private static final Random RANDOM = new Random();
    private static final String[] NUMBER_PREFIXES = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "145", "147", "150", "151", "152", "153", "155", "156", "157", "158", "159", "166", "170", "171", "172", "173", "175", "176", "177", "178", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "198", "199"};

    // Built-in name generator
    private static class NameGenerator {
        // Common single surnames (top 100)
        private static final List<String> SINGLE_SURNAMES = Arrays.asList("王", "李", "张", "刘", "陈", "杨", "黄", "赵", "吴", "周", "徐", "孙", "马", "朱", "胡", "郭", "何", "高", "林", "罗", "郑", "梁", "谢", "宋", "唐", "许", "邓", "韩", "冯", "曹", "彭", "曾", "肖", "田", "董", "潘", "袁", "蔡", "蒋", "余", "于", "杜", "叶", "程", "魏", "苏", "吕", "丁", "任", "卢", "姚", "沈", "钟", "姜", "崔", "谭", "陆", "汪", "范", "金", "石", "廖", "贾", "夏", "韦", "付", "方", "白", "邹", "孟", "熊", "秦", "邱", "侯", "江", "尹", "薛", "阎", "段", "雷", "龙", "黎", "史", "陶", "贺", "毛", "郝", "龚", "邵", "万");

        // Common compound surnames
        private static final List<String> COMPOUND_SURNAMES = Arrays.asList("欧阳", "上官", "司马", "东方", "独孤", "南宫", "西门", "诸葛", "司徒", "呼延", "尉迟", "轩辕", "慕容", "长孙", "宇文");

        // Common male names
        private static final List<String> MALE_NAMES = Arrays.asList("伟", "明", "强", "军", "杰", "涛", "辉", "勇", "峰", "超", "华", "平", "海", "林", "飞", "文", "浩", "宇", "晨", "阳");

        // Common female names
        private static final List<String> FEMALE_NAMES = Arrays.asList("芳", "丽", "娜", "敏", "静", "娟", "艳", "玲", "霞", "梅", "婷", "佳", "雪", "琳", "颖", "琴", "璐", "瑶", "琪", "诗");

        // Double character names (gender neutral)
        private static final List<String> DOUBLE_NAMES = Arrays.asList("欣怡", "子涵", "雨桐", "浩然", "诗涵", "宇航", "嘉怡", "梓轩", "雨萱", "晨曦", "宇航", "浩宇", "思涵", "梓涵", "诗雨", "浩然", "紫萱", "宇轩", "梦琪", "嘉豪");

        // Generate random name
        public static String generateRandomName() {
            return generateRandomName(RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE);
        }

        // Generate name for specified gender
        public static String generateRandomName(int gender) {
            boolean isCompound = RANDOM.nextDouble() < 0.05; // 5% chance of compound surname
            String surname = isCompound ? COMPOUND_SURNAMES.get(RANDOM.nextInt(COMPOUND_SURNAMES.size())) : SINGLE_SURNAMES.get(RANDOM.nextInt(SINGLE_SURNAMES.size()));

            String givenName;
            if (RANDOM.nextDouble() < 0.7) { // 70% chance of double character name
                givenName = DOUBLE_NAMES.get(RANDOM.nextInt(DOUBLE_NAMES.size()));
            } else {
                givenName = gender == GENDER_MALE ? MALE_NAMES.get(RANDOM.nextInt(MALE_NAMES.size())) : FEMALE_NAMES.get(RANDOM.nextInt(FEMALE_NAMES.size()));
            }

            return surname + givenName;
        }
    }

    private String number;
    private String name;
    private int type;
    private long date;
    private int duration;
    private int contactType;
    private int gender; // Gender field

    // Original constructor
    public TelephonyNumber(String number, String name, int type, long date, int duration) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.contactType = getDefaultContactType(type);
        this.gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;
    }

    // Constructor for random call record generation
    public TelephonyNumber() {
        this.number = generateRandomNumber();
        this.name = NameGenerator.generateRandomName();
        this.type = generateRandomType();
        this.date = generateRandomDate();
        this.duration = generateRandomDuration(type);
        this.contactType = getDefaultContactType(type);
        this.gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;
    }

    // Constructor for random call record with specified contact type
    public TelephonyNumber(int contactType) {
        this.number = generateRandomNumber();
        this.name = generateNameByContactType(contactType);
        this.type = generateRandomType();
        this.date = generateRandomDate();
        this.duration = generateRandomDuration(type);
        this.contactType = contactType;
        this.gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;
    }

    // Getters
    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public long getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public int getContactType() {
        return contactType;
    }

    public int getGender() {
        return gender;
    }

    // Get gender text description
    public String getGenderDescription() {
        return gender == GENDER_MALE ? "Male" : "Female";
    }

    // Format date for display
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

    // Get call type text description
    public String getTypeDescription() {
        switch (type) {
            case TYPE_INCOMING:
                return "Incoming";
            case TYPE_OUTGOING:
                return "Outgoing";
            case TYPE_MISSED:
                return "Missed";
            case TYPE_VOICEMAIL:
                return "Voicemail";
            default:
                return "Unknown";
        }
    }

    // Get contact type text description
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

    // Generate matching name based on contact type
    private String generateNameByContactType(int contactType) {
        int gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;

        switch (contactType) {
            case CONTACT_FAMILY:
                // Family name with relative prefix
                String[] familyPrefix = gender == GENDER_MALE ? new String[]{"Dad", "Brother", "Uncle", "Grandpa"} : new String[]{"Mom", "Sister", "Aunt", "Grandma"};
                return familyPrefix[RANDOM.nextInt(familyPrefix.length)] + " " + NameGenerator.generateRandomName(gender);

            case CONTACT_SERVICE:
                // Service name with specific prefix
                String[] serviceNames = {"Carrier Service", "Courier", "Delivery", "Bank Service", "After-sales"};
                return serviceNames[RANDOM.nextInt(serviceNames.length)];

            case CONTACT_UNKNOWN:
                // Unknown number with random surname + Mr./Ms.
                String surname = NameGenerator.SINGLE_SURNAMES.get(RANDOM.nextInt(NameGenerator.SINGLE_SURNAMES.size()));
                return surname + (gender == GENDER_MALE ? " Mr." : " Ms.");

            default:
                // Friends and colleagues use normal names
                return NameGenerator.generateRandomName(gender);
        }
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

    // Generate random call type
    private int generateRandomType() {
        int[] types = {TYPE_INCOMING, TYPE_OUTGOING, TYPE_MISSED};
        return types[RANDOM.nextInt(types.length)];
    }

    // Generate random date (within past 30 days)
    private long generateRandomDate() {
        long now = System.currentTimeMillis();
        long thirtyDaysAgo = now - 30L * 24 * 60 * 60 * 1000;
        return thirtyDaysAgo + (long) (RANDOM.nextDouble() * (now - thirtyDaysAgo));
    }

    // Generate random call duration based on call type
    private int generateRandomDuration(int type) {
        if (type == TYPE_MISSED || type == TYPE_VOICEMAIL) {
            return 0; // Missed and voicemail duration is 0
        }
        return RANDOM.nextInt(300) + 1; // Between 1-300 seconds
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

    // Get default contact type based on call type
    private int getDefaultContactType(int type) {
        if (type == TYPE_MISSED) {
            // Missed calls are more likely to be unknown or service
            return RANDOM.nextDouble() < 0.7 ? CONTACT_UNKNOWN : CONTACT_SERVICE;
        }
        // Other types are randomly assigned, with higher probability for family and friends
        double rand = RANDOM.nextDouble();
        if (rand < 0.4) return CONTACT_FAMILY;
        if (rand < 0.7) return CONTACT_FRIEND;
        if (rand < 0.9) return CONTACT_WORK;
        return CONTACT_SERVICE;
    }

    // Override toString for debugging
    @NonNull
    @Override
    public String toString() {
        return "TelephonyNumber{" + "number='" + number + '\'' + ", name='" + name + '\'' + ", gender=" + getGenderDescription() + ", type=" + getTypeDescription() + ", date=" + getFormattedDate() + ", duration=" + duration + "s" + ", contactType=" + getContactTypeDescription() + '}';
    }
}
