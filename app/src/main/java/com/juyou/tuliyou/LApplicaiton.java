package com.juyou.tuliyou;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;

import static com.juyou.tuliyou.GuiActivity.ACTION_FINISH;


/**
 * Created by xilinch on 18-6-22 下午2:14.
 * describe 该类主要完成以下功能
 * 1.applicaiotn
 */

public class LApplicaiton extends Application {

    private static LApplicaiton instance;

    public static String S_BUGLY_APPID = "f8f1c24f59";

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, S_BUGLY_APPID, false);
        initX5();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }


    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean finished) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("my","onViewInitFinished:" + finished);
                MainActivity.startMyActivity(getApplicationContext());
                Intent intent = new Intent();
                intent.setAction(ACTION_FINISH);
                sendBroadcast(intent);
            }

            @Override
            public void onCoreInitFinished() {
                Log.e("my","onCoreInitFinished:" );
            }
        };
        //x5内核初始化接口

        try {
            QbSdk.initX5Environment(getApplicationContext(), cb);
        } catch (Exception e) {
        }
    }

    public static LApplicaiton getInstance(){
        if(instance == null){
            instance = new LApplicaiton();
        }
        return instance;
    }

}
