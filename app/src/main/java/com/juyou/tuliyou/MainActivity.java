package com.juyou.tuliyou;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initWebview();
    }


    private void initWebview() {
        ll_contain = (LinearLayout) findViewById(R.id.ll_contain);
        x5WebView = new X5WebView(this,null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        x5WebView.setLayoutParams(layoutParams);
        setWebviewClient();
        ll_contain.addView(x5WebView);
        x5WebView.loadUrl(home);
    }

    private void setWebviewClient(){

        x5WebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                super.onShowCustomView(view, customViewCallback);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback geolocationPermissionsCallback) {
                geolocationPermissionsCallback.invoke(origin, true, true);
            }
        });

        x5WebView.setWebViewClient(new WebViewClient(){
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(x5WebView != null && x5WebView.canGoBack()){
                x5WebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
