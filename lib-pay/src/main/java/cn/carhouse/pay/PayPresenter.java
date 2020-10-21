package cn.carhouse.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.carhouse.pay.utils.Base64;
import cn.carhouse.pay.utils.HandlerUtils;
import cn.carhouse.pay.utils.WXConstants;
import cn.carhouse.pay.utils.WXSignUtils;

/**
 * ================================================================
 * 版权: 爱车小屋所有（C） 2019
 * <p>
 * 作者：刘付文 （61128910@qq.com）
 * <p>
 * 时间: 2019-12-09 16:33
 * <p>
 * 描述：支付的业务类
 * ================================================================
 */
public class PayPresenter implements IWXAPIEventHandler {

    private Activity mActivity;
    private OnPayListener mPayListener;
    private PayReq mPayReq;
    private IWXAPI mWXApi;

    public PayPresenter(Activity activity) {
        this.mActivity = activity;
        mWXApi = WXAPIFactory.createWXAPI(activity, WXConstants.APP_ID);
        mPayReq = new PayReq();
        mPayReq.options = new PayReq.Options();
        // 支付回调的类
        mPayReq.options.callbackClassName = activity.getClass().getName();
    }

    // 阿里支付==============================================================================

    /**
     * 阿里支付
     */
    public void aliPay(final String paymentInfo) {
        if (mActivity == null
                || TextUtils.isEmpty(paymentInfo)
                || mActivity.isFinishing()) {
            return;
        }
        // 开启线程去请求支付
        new Thread(new Runnable() {
            @Override
            public void run() {
                doAliPay(paymentInfo);
            }
        }).start();
    }

    private void doAliPay(String paymentInfo) {
        String payInfo = new String(Base64.decode(paymentInfo));
        // 构造PayTask 对象
        PayTask payTask = new PayTask(mActivity);
        // 调用支付接口，获取支付结果
        String result = payTask.pay(payInfo, false);
        PayResult payResult = new PayResult(result);
        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
        final String status = payResult.getResultStatus();
        HandlerUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                doAliPayResult(status);
            }
        });
    }

    /**
     * 结果处理
     */
    private void doAliPayResult(String status) {
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(status, "9000")) {// 支付成功
            if (mPayListener != null) {
                mPayListener.onCompleted(PayType.ALIPAY, status, "支付成功");
            }
        } else {
            if (mPayListener != null) {
                mPayListener.onError(PayType.ALIPAY, status, "支付失败");
            }
        }
    }
    // 阿里支付==============================================================================

    // 微信支付==============================================================================

    /**
     * 微信支付
     */
    public void wxPay(String paymentInfo) {
        String payInfo = paymentInfo;
        if (WXConstants.isBase64) {
            payInfo = new String(Base64.decode(paymentInfo));
        }
        if (!TextUtils.isEmpty(payInfo)) {
            if (payInfo.contains(";")) {
                String[] split = payInfo.split(";");
                WXSignUtils.genPayReq(mPayReq, split[1]);
                // 发送支付请求
                WXSignUtils.sendPayReq(mWXApi, mPayReq);
            } else {
                WXSignUtils.genPayReq(mPayReq, payInfo);
                // 发送支付请求
                WXSignUtils.sendPayReq(mWXApi, mPayReq);
            }
        }
    }

    // 微信回调
    @Override
    public void onReq(BaseReq baseReq) {
    }

    // 微信回调
    @Override
    public void onResp(BaseResp resp) {
        String text;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                text = "支付成功";
                if (mPayListener != null) {
                    mPayListener.onCompleted(PayType.WXPAY, resp.errCode + "", text);
                }
                return;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                text = "支付失败";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                text = "认证拒绝";
                break;
            default:
                text = "未知错误";
                break;
        }
        if (mPayListener != null) {
            mPayListener.onError(PayType.WXPAY, resp.errCode + "", text);
        }
    }

    public void onNewIntent(Intent intent) {
        mWXApi.handleIntent(intent, this);
    }

    // 微信支付==============================================================================

    /**
     * 设置支付监听
     */
    public void setPayListener(OnPayListener payListener) {
        this.mPayListener = payListener;
    }
}