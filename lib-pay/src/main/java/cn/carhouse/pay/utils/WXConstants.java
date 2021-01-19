package cn.carhouse.pay.utils;

/**
 * 支付配置
 * 自己在工程Application配置一下这个类
 */
public class WXConstants {
    /**
     * 支付信息后台有没有Base64编码
     * 默认是没有
     */
    public static boolean isBase64;

    public static String APP_ID = "";
    // 商户号
    public static String MCH_ID = "";
    // API密钥，在商户平台设置
    public static String API_KEY = "";
    // 应用密钥AppSecret
    public static final String SECRET = "";
}
