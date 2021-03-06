# PaySample
1. 微信、支付宝支付封装库。

### 1.下载代码，添加lib-pay依赖库。自己可以更新支付宝支付和微信支付到最新的SDK

### 2.app目录下的build.gradle配置。具体可以参考Demo
```
// 添加依赖 lib-pay下的libs目录
repositories {
    flatDir {
        dirs 'libs', project(':lib-pay').file('libs')
    }
}
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
    implementation project(path: ':lib-pay')
    ...
}
```
### 3.清单配置
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

### 4.使用方式

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

### 5. 混淆。自己去查看支付宝和微信SDK的文档。

