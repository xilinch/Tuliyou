package com.juyou.tuliyou.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.juyou.tuliyou.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.juyou.tuliyou.broadcast.BootBroadcast.ACTION_DESTROY;

/**
 * Created by xilinch on 18-6-23 上午10:15.
 * describe 该类主要完成以下功能
 * 1.检查是否在前台进程
 */

public class CheckService extends Service {

    public final static String TAG = "com.juyou.tuliyou.service.CheckService";

    private boolean isFirst = true;

    private Thread checkThread ;

    public CheckService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("my","onStartCommand-------");
        if(checkThread == null){
            checkThread = new Thread(){
                @Override
                public void run() {
                    while (true){
                        //每隔3s执行检查一次
                        Log.e("my","onStartCommand:" + isAppOnForeground(CheckService.this));
                        if(!isAppOnForeground(CheckService.this)){
                            startMyActivity();
                        }
                        try{
                            Thread.sleep(1000);
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
            try{
                checkThread.start();
            } catch (Exception exception){
                exception.printStackTrace();

            } finally{

            }
        }
        if(thread != null){
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    /**
     * 是否在前台
     */
    private boolean isForGround = false;
    public class MyBinder extends Binder{

        public void setIsForGround(boolean isForGround){
            CheckService.this.isForGround = isForGround;
            //不在前台就要启动应用
            if(!isForGround ){
                startMyActivity();
            }
        }
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.setAction(ACTION_DESTROY);
        sendBroadcast(intent);
        super.onDestroy();
    }

    /**
     * 启动服务
     */
    private void startMyActivity(){
        Intent intent1 = new Intent(CheckService.this, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent1);
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

    /**
     * 互相唤醒
     */
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    boolean b = MainActivity.isServiceWorked(CheckService.this, GuardService.TAG);
                    if(!b) {
                        Intent service = new Intent(CheckService.this, GuardService.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });
}
