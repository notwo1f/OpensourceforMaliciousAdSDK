package com.tools.fakeinfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SMSContentGenerator {
    // 短信分类
    public static final int CATEGORY_GENERAL = 0;      // 普通消息
    public static final int CATEGORY_FRIENDLY = 1;     // 友好问候
    public static final int CATEGORY_URGENT = 2;       // 紧急通知
    public static final int CATEGORY_INFORMATION = 3;  // 信息通知
    public static final int CATEGORY_PROMOTION = 4;    // 促销活动
    public static final int CATEGORY_VERIFICATION = 5; // 验证码
    public static final int CATEGORY_QUESTION = 6;     // 询问
    public static final int CATEGORY_REMINDER = 7;     // 提醒

    // 表情符号库
    private static final List<String> EMOJIS = Arrays.asList("😊", "😃", "🎉", "👍", "👋", "💪", "🤔", "🙏", "🔥", "💖", "🌟", "🎂", "🍔", "☕", "🎬", "🎮", "📚", "💼", "🚗", "🏠");

    // 常用短语库
    private static final List<String> PHRASES = Arrays.asList("最近怎么样？", "一切都好吗？", "好久不见", "记得吃饭", "注意休息", "有时间聚聚", "一起吃个饭吧", "帮我个忙", "谢谢", "不客气", "明天见", "再见", "晚安", "早上好", "很高兴认识你");

    // 常见问题库
    private static final List<String> QUESTIONS = Arrays.asList("在吗？", "你在哪里？", "什么时候有空？", "怎么了？", "发生什么事了？", "你知道怎么做吗？", "这个东西多少钱？", "你喜欢吃什么？", "你有时间吗？", "能帮我一下吗？");

    // 紧急通知库
    private static final List<String> URGENT_MESSAGES = Arrays.asList("紧急！请立即联系我", "有急事需要你帮忙", "请尽快回复", "需要你的紧急协助", "家里有急事", "请马上回电", "出了点意外，需要你的帮助");

    // 信息通知库
    private static final List<String> INFORMATION_MESSAGES = Arrays.asList("我已经出发了", "我到了", "东西已经寄出", "会议时间变更为明天上午10点", "你的订单已发货", "你的包裹已送达", "明天天气晴朗", "本周六团建", "请查收邮件", "资料已上传");

    // 促销活动库
    private static final List<String> PROMOTION_MESSAGES = Arrays.asList("限时优惠！全场8折", "新用户注册立减20元", "会员日大促销", "限时抢购，先到先得", "周年庆活动，全场包邮", "新品上市，欢迎选购", "充值100送50", "限时折扣，不要错过");

    // 验证码模板
    private static final List<String> VERIFICATION_TEMPLATES = Arrays.asList("您的验证码是：%s，请勿泄露给他人。", "%s 是您的验证码，有效期5分钟。", "验证码：%s，用于账户安全验证。", "【系统】您的验证码：%s，3分钟内有效。", "验证码 %s 用于登录验证，如非本人操作请忽略。");

    // 随机生成普通短信内容
    public static String generateGeneralMessage() {
        return PHRASES.get(randomIndex(PHRASES.size())) + randomEmoji();
    }

    // 随机生成友好问候
    public static String generateFriendlyMessage() {
        List<String> greetings = Arrays.asList("嗨，最近过得怎么样？" + randomEmoji(), "早上好！希望你今天过得愉快" + randomEmoji(), "晚上好！今天忙吗？" + randomEmoji(), "周末愉快！有什么计划吗？" + randomEmoji(), "好久不见，最近在忙什么？" + randomEmoji());
        return greetings.get(randomIndex(greetings.size()));
    }

    // 随机生成紧急通知
    public static String generateUrgentMessage() {
        return URGENT_MESSAGES.get(randomIndex(URGENT_MESSAGES.size())) + randomEmoji(2);
    }

    // 随机生成信息通知
    public static String generateInformationMessage() {
        return INFORMATION_MESSAGES.get(randomIndex(INFORMATION_MESSAGES.size()));
    }

    // 随机生成促销活动
    public static String generatePromotionMessage() {
        return PROMOTION_MESSAGES.get(randomIndex(PROMOTION_MESSAGES.size()));
    }

    // 随机生成验证码短信
    public static String generateVerificationCode() {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        String template = VERIFICATION_TEMPLATES.get(randomIndex(VERIFICATION_TEMPLATES.size()));
        return String.format(template, code);
    }

    // 随机生成询问
    public static String generateQuestion() {
        return QUESTIONS.get(randomIndex(QUESTIONS.size()));
    }

    // 随机生成提醒
    public static String generateReminder() {
        List<String> reminders = Arrays.asList("记得带钥匙", "别忘了明天的会议", "下午要去取快递", "该吃饭了", "记得吃药", "明天要下雨，记得带伞", "手机电量低，记得充电", "该睡觉了，不要熬夜");
        return reminders.get(randomIndex(reminders.size()));
    }

    // 随机生成指定分类的短信内容
    public static String generateMessageByCategory(int category) {
        switch (category) {
            case CATEGORY_FRIENDLY:
                return generateFriendlyMessage();
            case CATEGORY_URGENT:
                return generateUrgentMessage();
            case CATEGORY_INFORMATION:
                return generateInformationMessage();
            case CATEGORY_PROMOTION:
                return generatePromotionMessage();
            case CATEGORY_VERIFICATION:
                return generateVerificationCode();
            case CATEGORY_QUESTION:
                return generateQuestion();
            case CATEGORY_REMINDER:
                return generateReminder();
            case CATEGORY_GENERAL:
            default:
                return generateGeneralMessage();
        }
    }

    // 随机生成完全随机的短信内容
    public static String generateRandomMessage() {
        int category = randomIndex(8); // 0-7
        return generateMessageByCategory(category);
    }

    // 随机生成联系人类型相关的短信内容
    public static String generateMessageByContactType(int contactType) {
        switch (contactType) {
            case SMSInfo.CONTACT_FAMILY:
                List<String> familyMsgs = Arrays.asList("妈妈今天做了你最爱吃的红烧肉", "爸爸身体挺好的，不用担心", "弟弟考上大学了，庆祝一下！", "姐姐给你买了礼物，记得查收", "奶奶生日快到了，我们准备一下");
                return familyMsgs.get(randomIndex(familyMsgs.size()));

            case SMSInfo.CONTACT_FRIEND:
                List<String> friendMsgs = Arrays.asList("兄弟，今晚开黑吗？" + randomEmoji(), "姐妹，明天逛街去？" + randomEmoji(), "上次那家餐厅不错，再去吃一次？", "我有两张演唱会门票，一起去吗？", "你借我的书什么时候还？");
                return friendMsgs.get(randomIndex(friendMsgs.size()));

            case SMSInfo.CONTACT_WORK:
                List<String> workMsgs = Arrays.asList("项目进度如何？需要帮忙吗？", "客户反馈需要修改方案", "明天上午10点开会，请准备好资料", "这份报告需要在下班前完成", "刚刚接到紧急任务，需要加班");
                return workMsgs.get(randomIndex(workMsgs.size()));

            case SMSInfo.CONTACT_SERVICE:
                List<String> serviceMsgs = Arrays.asList("【慕玛火热送】买一送一，周二周三宠粉日！榴莲披萨、芝士披萨、汉堡等多款主食送小食/饮料，快来冲！速戳“慕玛火热送”威胁小程序，拒收请回复R","【哔哩哔哩】大会员新春福利，下单享年卡低价118，最高再赠366","【支付宝】您有一笔1000元的入账", "【银行】您的信用卡账单已出", "【快递】您的包裹已送达代收点", "【美团】您的外卖已送达", "【中国移动】您的套餐剩余流量不足1GB","【美团】您购买的沙人参鸡4人餐","【京东】买原研进口药上京东，种类齐全价格低，正品保障省更多，下单戳3.cn/v/ci-K7B1");
                return serviceMsgs.get(randomIndex(serviceMsgs.size()));

            case SMSInfo.CONTACT_UNKNOWN:
                List<String> unknownMsgs = Arrays.asList("您好，打扰了，请问您需要贷款吗？", "恭喜您获得抽奖资格，点击领取奖品", "免费领取流量，回复1即可", "尊敬的用户，您的账户存在风险", "这是测试短信，请忽略");
                return unknownMsgs.get(randomIndex(unknownMsgs.size()));

            default:
                return generateRandomMessage();
        }
    }

    // 生成随机表情符号
    private static String randomEmoji() {
        return EMOJIS.get(randomIndex(EMOJIS.size()));
    }

    // 生成指定数量的随机表情符号
    private static String randomEmoji(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(randomEmoji());
        }
        return sb.toString();
    }

    // 生成随机索引
    private static int randomIndex(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }
}