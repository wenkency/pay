package cn.carhouse.pay.utils;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class WXSignUtils {

    private static Map<String, String> maps = new LinkedHashMap<>();

    /**
     * 发送支付请求
     */
    public static void sendPayReq(IWXAPI api, PayReq payReq) {
        api.sendReq(payReq);
    }

    /**
     * 微信----生成支付签名
     */
    public static void genPayReq(PayReq payReq, String prepayId) {
        payReq.appId = WXConstants.APP_ID;
        payReq.partnerId = WXConstants.MCH_ID;
        payReq.prepayId = prepayId;
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = genNonceStr();
        payReq.timeStamp = String.valueOf(genTimeStamp());
        maps.clear();
        maps.put("appid", payReq.appId);
        maps.put("noncestr", payReq.nonceStr);
        maps.put("package", payReq.packageValue);
        maps.put("partnerid", payReq.partnerId);
        maps.put("appid", payReq.appId);
        maps.put("prepayid", payReq.prepayId);
        maps.put("timestamp", payReq.timeStamp);
        payReq.sign = genAppSign(maps);
    }

    private static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private static String genAppSign(Map<String, String> maps) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> me : maps.entrySet()) {
            sb.append(me.getKey());
            sb.append('=');
            sb.append(me.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WXConstants.API_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        return appSign;
    }
}
