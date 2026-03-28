package com.tools.fakeinfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SMSInfo {
    // 短信类型常量
    public static final int TYPE_INBOX = 1;    // 收件箱
    public static final int TYPE_SENT = 2;     // 已发送
    public static final int TYPE_DRAFT = 3;    // 草稿
    public static final int TYPE_OUTBOX = 4;   // 发件箱
    public static final int TYPE_FAILED = 5;   // 发送失败
    public static final int TYPE_QUEUED = 6;   // 待发送

    // 联系人类型常量
    public static final int CONTACT_FAMILY = 1;    // 家人
    public static final int CONTACT_FRIEND = 2;    // 朋友
    public static final int CONTACT_WORK = 3;      // 同事
    public static final int CONTACT_SERVICE = 4;   // 客服
    public static final int CONTACT_UNKNOWN = 5;   // 未知

    private static final Random RANDOM = new Random();
    private static final String[] NUMBER_PREFIXES = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "145", "147", "150", "151", "152", "153", "155", "156", "157", "158", "159", "166", "170", "171", "172", "173", "175", "176", "177", "178", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "198", "199"};

    private String number;      // 电话号码
    private String content;     // 短信内容
    private int type;           // 短信类型
    private long date;          // 短信时间
    private int contactType;    // 联系人类型

    // 完整构造函数
    public SMSInfo(String number, String content, int type, long date, int contactType) {
        this.number = number;
        this.content = content;
        this.type = type;
        this.date = date;
        this.contactType = contactType;
    }

    // 随机生成构造函数
    public SMSInfo() {
        this.number = generateRandomNumber();
        this.contactType = generateRandomContactType();
        this.content = SMSContentGenerator.generateMessageByContactType(contactType);
        this.type = generateRandomType();
        this.date = generateRandomDate();
    }

    // 指定联系人类型的随机生成构造函数
    public SMSInfo(int contactType) {
        this.number = generateRandomNumber();
        this.contactType = contactType;
        this.content = SMSContentGenerator.generateMessageByContactType(contactType);
        this.type = generateRandomType();
        this.date = generateRandomDate();
    }

    // 指定短信分类的随机生成构造函数
    public SMSInfo(int contactType, int messageCategory) {
        this.number = generateRandomNumber();
        this.contactType = contactType;

        // 根据联系人类型和消息分类生成内容
        if (contactType == CONTACT_SERVICE && messageCategory != SMSContentGenerator.CATEGORY_VERIFICATION) {
            this.content = SMSContentGenerator.generateMessageByContactType(contactType);
        } else {
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

    // 获取短信类型的文字描述
    public String getTypeDescription() {
        switch (type) {
            case TYPE_INBOX:
                return "收件箱";
            case TYPE_SENT:
                return "已发送";
            case TYPE_DRAFT:
                return "草稿";
            case TYPE_OUTBOX:
                return "发件箱";
            case TYPE_FAILED:
                return "发送失败";
            case TYPE_QUEUED:
                return "待发送";
            default:
                return "未知";
        }
    }

    // 获取联系人类型的文字描述
    public String getContactTypeDescription() {
        switch (contactType) {
            case CONTACT_FAMILY:
                return "家人";
            case CONTACT_FRIEND:
                return "朋友";
            case CONTACT_WORK:
                return "同事";
            case CONTACT_SERVICE:
                return "客服";
            case CONTACT_UNKNOWN:
                return "未知";
            default:
                return "其他";
        }
    }

    // 格式化日期显示
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

    // 生成随机电话号码
    private String generateRandomNumber() {
        String prefix = NUMBER_PREFIXES[RANDOM.nextInt(NUMBER_PREFIXES.length)];
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    // 生成随机短信类型
    private int generateRandomType() {
        // 调整概率分布，让收件箱和已发送更常见
        int[] types = {TYPE_INBOX, TYPE_SENT, TYPE_DRAFT, TYPE_OUTBOX, TYPE_FAILED, TYPE_QUEUED};
        double[] weights = {0.5, 0.3, 0.05, 0.05, 0.05, 0.05}; // 概率权重

        double rand = RANDOM.nextDouble();
        double cumulativeWeight = 0.0;

        for (int i = 0; i < types.length; i++) {
            cumulativeWeight += weights[i];
            if (rand < cumulativeWeight) {
                return types[i];
            }
        }

        return types[0]; // 默认返回收件箱
    }

    // 生成随机联系人类型
    private int generateRandomContactType() {
        int[] types = {CONTACT_FAMILY, CONTACT_FRIEND, CONTACT_WORK, CONTACT_SERVICE, CONTACT_UNKNOWN};
        double[] weights = {0.25, 0.3, 0.2, 0.15, 0.1}; // 概率权重

        double rand = RANDOM.nextDouble();
        double cumulativeWeight = 0.0;

        for (int i = 0; i < types.length; i++) {
            cumulativeWeight += weights[i];
            if (rand < cumulativeWeight) {
                return types[i];
            }
        }

        return types[0]; // 默认返回家人
    }

    // 生成随机日期（过去30天内）
    private long generateRandomDate() {
        long now = System.currentTimeMillis();
        long thirtyDaysAgo = now - 30L * 24 * 60 * 60 * 1000;
        return thirtyDaysAgo + (long) (RANDOM.nextDouble() * (now - thirtyDaysAgo));
    }

    // 判断是否为今天
    private boolean isToday(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date(time)).equals(format.format(new Date()));
    }

    // 判断是否为今年
    private boolean isThisYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
        return format.format(new Date(time)).equals(format.format(new Date()));
    }

    // 重写toString方法，便于调试
    @Override
    public String toString() {
        return "SMSInfo{" + "number='" + number + '\'' + ", content='" + content + '\'' + ", type=" + getType() + ", date=" + getFormattedDate() + ", contactType=" + getContactTypeDescription() + '}';
    }
}