package com.juyou.tuliyou;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by xilinch on 18-6-23 上午10:15.
 * describe 该类主要完成以下功能
 * 1.检查是否在前台进程
 */

public class CheckService extends Service {

    private boolean isFirst = true;

    private Thread checkThread ;

    public CheckService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(checkThread == null){
            checkThread = new Thread(){
                @Override
                public void run() {
                    while (true){
                        //每隔3s执行检查一次
                        Log.e("my","onStartCommand:" + isAppOnForeground(CheckService.this));
                        if(!isAppOnForeground(CheckService.this)){
                            Intent intent1 = new Intent(CheckService.this, MainActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                        }
                        try{
                            Thread.sleep(3000);
                        } catch (Exception exception){
                            exception.printStackTrace();

                        } finally{

                        }
                        if(isFirst){
                            isFirst = false;
                        }
                    }
                }
            };
        }
        checkThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



    public boolean isAppOnForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String currentPackageName = "";
        if (am.getRunningTasks(1).size() > 0) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            currentPackageName = cn.getPackageName();
        }
        Log.e("my", "isAppOnForeground  :" + currentPackageName + "   getPackageName:" + getPackageName());
        return !TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(getPackageName());
    }
}
