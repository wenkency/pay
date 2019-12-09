package cn.carhouse.paysample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.carhouse.pay.OnPayListener;
import cn.carhouse.pay.PayPresenter;
import cn.carhouse.pay.PayType;
import cn.carhouse.pay.utils.WXConstants;

public class MainActivity extends AppCompatActivity {

    private PayPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 配置APP在微信上申请的一些信息
        WXConstants.API_KEY = "";
        WXConstants.APP_ID = "";

        // 创建支付对象
        mPresenter = new PayPresenter(this);
        // 设置回调监听
        mPresenter.setPayListener(new OnPayListener() {
            @Override
            public void onCompleted(PayType payType, String code, String msg) {

            }

            @Override
            public void onError(PayType payType, String code, String msg) {

            }
        });
        // 发起支付
        // 后台生成的订单支付数据
        String paymentInfo = "";
        mPresenter.wxPay(paymentInfo);
        mPresenter.aliPay(paymentInfo);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mPresenter != null) {
            mPresenter.onNewIntent(intent);
        }
    }
}
