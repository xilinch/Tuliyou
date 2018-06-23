package com.juyou.tuliyou;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.juyou.tuliyou.view.X5WebView;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class MainActivity extends Activity {

    private LinearLayout ll_contain;

    private X5WebView x5WebView;

    private String home = "https://m.tuliyou.com/h5/app";

    /**
     * 系统按键监听
     */
    private InnerRecevier innerReceiver;

    /**
     * 系统按键监听
     */
    class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    Intent activituy = new Intent(context, MainActivity.class);
                    if (reason.equalsIgnoreCase(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        Toast.makeText(MainActivity.this, "Home键被监听", Toast.LENGTH_SHORT).show();
                        context.startActivity(activituy);
                    } else if (reason.equalsIgnoreCase(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        Toast.makeText(MainActivity.this, "多任务键被监听", Toast.LENGTH_SHORT).show();
                        context.startActivity(activituy);
                    }
                }
            }
        }
    }

    /**
     * 服务链接
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideBottomUIMenu();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        registerBroadcast();
        initWebview();
        Intent intent = new Intent(this, CheckService.class);
        bindService(intent, serviceConnection ,BIND_AUTO_CREATE);
    }


    private void initWebview() {
        ll_contain = (LinearLayout) findViewById(R.id.ll_contain);
        x5WebView = new X5WebView(this, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        x5WebView.setLayoutParams(layoutParams);
        setWebviewClient();
        ll_contain.addView(x5WebView);
        x5WebView.loadUrl(home);
        ll_contain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //比较Activity根布局与当前布局的大小
                int heightDiff = ll_contain.getRootView().getHeight() - ll_contain.getHeight();
                Log.e("my", "onGlobalLayout:" + heightDiff);
                //其实这个heightDiff换成dp更靠谱一些
                hideBottomUIMenu();
                if (heightDiff > 100) {
//                    hideBottomUIMenu();
                    //大小超过100时，一般为显示虚拟键盘事件
                } else {
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏

                }
            }
        });
    }

    /**
     * 设置webview
     */
    private void setWebviewClient() {

        x5WebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                super.onShowCustomView(view, customViewCallback);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback geolocationPermissionsCallback) {
                geolocationPermissionsCallback.invoke(origin, true, true);
            }
        });

        x5WebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.e("my", "url:" + url);
                //统一做处理，如果是exit:///，则退出当前页面
                if ("exit:///".equals(url)) {
                    webView.loadUrl(home);
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }
        });
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        //创建广播
        innerReceiver = new InnerRecevier();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //启动广播
        registerReceiver(innerReceiver, intentFilter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("my", "keyCode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (x5WebView != null && x5WebView.canGoBack()) {
                x5WebView.goBack();

            }
            return true;
        }
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            Toast.makeText(getApplicationContext(), "HOME 键已被禁用...", Toast.LENGTH_SHORT).show();
            return true;//同理
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (innerReceiver != null) {
            unregisterReceiver(innerReceiver);
        }
        if(serviceConnection != null){
            unbindService(serviceConnection);
        }
    }
}
