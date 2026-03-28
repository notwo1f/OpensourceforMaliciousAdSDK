package com.tools.fakeinfo;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TelephonyNumber {
    // 通话类型常量
    public static final int TYPE_INCOMING = 1;  // 来电
    public static final int TYPE_OUTGOING = 2;  // 去电
    public static final int TYPE_MISSED = 3;    // 未接
    public static final int TYPE_VOICEMAIL = 4; // 语音信箱

    // 联系人类型常量
    public static final int CONTACT_FAMILY = 1;    // 家人
    public static final int CONTACT_FRIEND = 2;    // 朋友
    public static final int CONTACT_WORK = 3;      // 同事
    public static final int CONTACT_SERVICE = 4;   // 客服
    public static final int CONTACT_UNKNOWN = 5;   // 未知

    // 性别常量
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private static final Random RANDOM = new Random();
    private static final String[] NUMBER_PREFIXES = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "145", "147", "150", "151", "152", "153", "155", "156", "157", "158", "159", "166", "170", "171", "172", "173", "175", "176", "177", "178", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "198", "199"};

    // 内置的姓名生成器
    private static class NameGenerator {
        // 常见单姓（前100大姓氏）
        private static final List<String> SINGLE_SURNAMES = Arrays.asList("王", "李", "张", "刘", "陈", "杨", "黄", "赵", "吴", "周", "徐", "孙", "马", "朱", "胡", "郭", "何", "高", "林", "罗", "郑", "梁", "谢", "宋", "唐", "许", "邓", "韩", "冯", "曹", "彭", "曾", "肖", "田", "董", "潘", "袁", "蔡", "蒋", "余", "于", "杜", "叶", "程", "魏", "苏", "吕", "丁", "任", "卢", "姚", "沈", "钟", "姜", "崔", "谭", "陆", "汪", "范", "金", "石", "廖", "贾", "夏", "韦", "付", "方", "白", "邹", "孟", "熊", "秦", "邱", "侯", "江", "尹", "薛", "阎", "段", "雷", "龙", "黎", "史", "陶", "贺", "毛", "郝", "龚", "邵", "万");

        // 常见复姓
        private static final List<String> COMPOUND_SURNAMES = Arrays.asList("欧阳", "上官", "司马", "东方", "独孤", "南宫", "西门", "诸葛", "司徒", "呼延", "尉迟", "轩辕", "慕容", "长孙", "宇文");

        // 男性常用名
        private static final List<String> MALE_NAMES = Arrays.asList("伟", "明", "强", "军", "杰", "涛", "辉", "勇", "峰", "超", "华", "平", "海", "林", "飞", "文", "浩", "宇", "晨", "阳");

        // 女性常用名
        private static final List<String> FEMALE_NAMES = Arrays.asList("芳", "丽", "娜", "敏", "静", "娟", "艳", "玲", "霞", "梅", "婷", "佳", "雪", "琳", "颖", "琴", "璐", "瑶", "琪", "诗");

        // 双字名（不分性别）
        private static final List<String> DOUBLE_NAMES = Arrays.asList("欣怡", "子涵", "雨桐", "浩然", "诗涵", "宇航", "嘉怡", "梓轩", "雨萱", "晨曦", "宇航", "浩宇", "思涵", "梓涵", "诗雨", "浩然", "紫萱", "宇轩", "梦琪", "嘉豪");

        // 生成随机姓名
        public static String generateRandomName() {
            return generateRandomName(RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE);
        }

        // 生成指定性别的姓名
        public static String generateRandomName(int gender) {
            boolean isCompound = RANDOM.nextDouble() < 0.05; // 5%概率复姓
            String surname = isCompound ? COMPOUND_SURNAMES.get(RANDOM.nextInt(COMPOUND_SURNAMES.size())) : SINGLE_SURNAMES.get(RANDOM.nextInt(SINGLE_SURNAMES.size()));

            String givenName;
            if (RANDOM.nextDouble() < 0.7) { // 70%概率双字名
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
    private int gender; // 新增性别字段

    // 原始构造函数
    public TelephonyNumber(String number, String name, int type, long date, int duration) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.contactType = getDefaultContactType(type);
        this.gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;
    }

    // 随机生成通话记录的构造函数
    public TelephonyNumber() {
        this.number = generateRandomNumber();
        this.name = NameGenerator.generateRandomName();
        this.type = generateRandomType();
        this.date = generateRandomDate();
        this.duration = generateRandomDuration(type);
        this.contactType = getDefaultContactType(type);
        this.gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;
    }

    // 随机生成带指定联系人类型的通话记录
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

    // 获取性别文字描述
    public String getGenderDescription() {
        return gender == GENDER_MALE ? "男" : "女";
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

    // 获取通话类型的文字描述
    public String getTypeDescription() {
        switch (type) {
            case TYPE_INCOMING:
                return "来电";
            case TYPE_OUTGOING:
                return "去电";
            case TYPE_MISSED:
                return "未接";
            case TYPE_VOICEMAIL:
                return "语音信箱";
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

    // 根据联系人类型生成匹配的姓名
    private String generateNameByContactType(int contactType) {
        int gender = RANDOM.nextBoolean() ? GENDER_MALE : GENDER_FEMALE;

        switch (contactType) {
            case CONTACT_FAMILY:
                // 家人姓名添加亲属关系前缀
                String[] familyPrefix = gender == GENDER_MALE ? new String[]{"爸爸", "哥哥", "叔叔", "爷爷"} : new String[]{"妈妈", "姐姐", "姑姑", "奶奶"};
                return familyPrefix[RANDOM.nextInt(familyPrefix.length)] + NameGenerator.generateRandomName(gender);

            case CONTACT_SERVICE:
                // 客服姓名使用特定前缀
                String[] serviceNames = {"中国移动客服", "快递员", "外卖配送员", "银行客服", "售后专员"};
                return serviceNames[RANDOM.nextInt(serviceNames.length)];

            case CONTACT_UNKNOWN:
                // 未知号码使用随机姓氏+先生/女士
                String surname = NameGenerator.SINGLE_SURNAMES.get(RANDOM.nextInt(NameGenerator.SINGLE_SURNAMES.size()));
                return surname + (gender == GENDER_MALE ? "先生" : "女士");

            default:
                // 朋友和同事使用普通姓名
                return NameGenerator.generateRandomName(gender);
        }
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

    // 生成随机通话类型
    private int generateRandomType() {
        int[] types = {TYPE_INCOMING, TYPE_OUTGOING, TYPE_MISSED};
        return types[RANDOM.nextInt(types.length)];
    }

    // 生成随机日期（过去30天内）
    private long generateRandomDate() {
        long now = System.currentTimeMillis();
        long thirtyDaysAgo = now - 30L * 24 * 60 * 60 * 1000;
        return thirtyDaysAgo + (long) (RANDOM.nextDouble() * (now - thirtyDaysAgo));
    }

    // 根据通话类型生成随机通话时长
    private int generateRandomDuration(int type) {
        if (type == TYPE_MISSED || type == TYPE_VOICEMAIL) {
            return 0; // 未接和语音信箱时长为0
        }
        return RANDOM.nextInt(300) + 1; // 1-300秒之间
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

    // 根据通话类型获取默认联系人类型
    private int getDefaultContactType(int type) {
        if (type == TYPE_MISSED) {
            // 未接电话更可能是未知号码或客服
            return RANDOM.nextDouble() < 0.7 ? CONTACT_UNKNOWN : CONTACT_SERVICE;
        }
        // 其他类型随机分配，但家人和朋友比例更高
        double rand = RANDOM.nextDouble();
        if (rand < 0.4) return CONTACT_FAMILY;
        if (rand < 0.7) return CONTACT_FRIEND;
        if (rand < 0.9) return CONTACT_WORK;
        return CONTACT_SERVICE;
    }

    // 重写toString方法，便于调试
    @NonNull
    @Override
    public String toString() {
        return "TelephonyNumber{" + "number='" + number + '\'' + ", name='" + name + '\'' + ", gender=" + getGenderDescription() + ", type=" + getTypeDescription() + ", date=" + getFormattedDate() + ", duration=" + duration + "秒" + ", contactType=" + getContactTypeDescription() + '}';
    }
}