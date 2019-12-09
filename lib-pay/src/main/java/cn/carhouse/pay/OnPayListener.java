package cn.carhouse.pay;

/**
 * 支付返回监听
 */
public interface OnPayListener {
    void onCompleted(PayType payType, String code, String msg);

    void onError(PayType payType, String code, String msg);
}
