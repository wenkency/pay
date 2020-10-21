# PaySample
1. 微信、支付宝支付封装库。

### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
			flatDir {
                 dirs 'libs'
            }
		}
}

implementation (name:'alipaySdk-15.7.9-20200727142814-noUtdid', ext:'aar')
implementation "com.github.wenkency:pay:1.6.0"


```
### 清单配置
```
        <activity
           android:name="支付的Activity"
            >
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                // 这个不能少
                <data android:scheme="wx123456789(微信支付ID)" />
            </intent-filter>
        </activity>

```

### 使用方式

```
        // 1. 在Application里面配置一下APP在微信上申请的一些信息
        WXConstants.API_KEY = "";
        WXConstants.APP_ID = "";

        // 2. 创建支付对象
        mPresenter = new PayPresenter(this);
        // 3. 设置回调监听
        mPresenter.setPayListener(new OnPayListener() {
            @Override
            public void onCompleted(PayType payType, String code, String msg) {

            }

            @Override
            public void onError(PayType payType, String code, String msg) {

            }
        });
        // 4. 发起支付
        // 后台生成的订单支付数据
        String paymentInfo = "";
        mPresenter.wxPay(paymentInfo);
        mPresenter.aliPay(paymentInfo);


        // 5. 重写Activity这个方法
        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            if (mPresenter != null) {
                mPresenter.onNewIntent(intent);
            }
        }

```

