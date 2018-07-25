package com.juyou.tuliyou;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import static com.juyou.tuliyou.GuiActivity.ACTION_FINISH;


/**
 * Created by xilinch on 18-6-22 下午2:14.
 * describe 该类主要完成以下功能
 * 1.applicaiotn
 */

public class LApplicaiton extends TinkerApplication {

    public LApplicaiton(){
        super(ShareConstants.TINKER_ENABLE_ALL, "com.juyou.tuliyou.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }


    private static LApplicaiton instance;

    public static String S_BUGLY_APPID = "f8f1c24f59";

    @Override
    public void onCreate() {
        super.onCreate();
        initHotfix();
//        Bugly.init(getApplication(), S_BUGLY_APPID, true);
        initX5();
        instance = this;
    }
//
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        MultiDex.install(base);
//        // 安装tinker
//        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);
//        Log.e("my","attachBaseContext  Beta.installTinker");
//    }


    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean finished) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("my","onViewInitFinished:" + finished);
                MainActivity.startMyActivity(getBaseContext());
                Intent intent = new Intent();
                intent.setAction(ACTION_FINISH);
                getBaseContext().sendBroadcast(intent);
            }

            @Override
            public void onCoreInitFinished() {
                Log.e("my","onCoreInitFinished:" );
            }
        };
        //x5内核初始化接口

        try {
            QbSdk.initX5Environment(getBaseContext(), cb);
        } catch (Exception e) {
        }
    }

//    public static LApplicaiton getInstance(){
//        if(instance == null){
//            instance = new LApplicaiton();
//        }
//        return instance;
//    }

    private void initHotfix(){
        Log.e("my","initHotfix  ---");
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = false;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;

        // 补丁回调接口，可以监听补丁接收、下载、合成的回调
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                //Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
                Log.e("my","attachBaseContext  patchFileUrl:" + patchFileUrl);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Log.e("my","onDownloadReceived ");
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                Log.e("my","onDownloadSuccess ");
            }

            @Override
            public void onDownloadFailure(String msg) {
                Log.e("my","onDownloadFailure :" + msg);
            }

            @Override
            public void onApplySuccess(String msg) {
                Log.e("my","onApplySuccess msg:" + msg);
            }

            @Override
            public void onApplyFailure(String msg) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Log.e("my","onApplyFailure msg:" + msg);
            }

            @Override
            public void onPatchRollback() {
                Log.e("my","onPatchRollback ");
//                Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
            }
        };
        Log.e("my","initHotfix  end");
    }

}
