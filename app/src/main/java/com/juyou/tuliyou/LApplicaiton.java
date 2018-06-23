package com.juyou.tuliyou;

import android.app.Application;
import android.util.Log;
import com.tencent.smtt.sdk.QbSdk;


/**
 * Created by xilinch on 18-6-22 下午2:14.
 * describe 该类主要完成以下功能
 * 1.applicaiotn
 */

public class LApplicaiton extends Application {

    private static LApplicaiton instance;

    @Override
    public void onCreate() {
        super.onCreate();
        initX5();
        instance = this;
    }

    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean finished) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
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
