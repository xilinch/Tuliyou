package com.juyou.tuliyou.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.juyou.tuliyou.MainActivity;
import java.util.List;
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

    private Thread checkThread;

    public CheckService() {
    }

    boolean isStart = false;
    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("my", "onStartCommand-------");
        if (checkThread == null) {
            checkThread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        //每隔1s执行检查一次
//                        Log.e("my", "onStartCommand:" + isAppOnForeground(CheckService.this));
//                        if (!isAppOnForeground(CheckService.this)) {
                            MainActivity.startMyActivity(CheckService.this);
//                        }
                        try {
                            Thread.sleep(2000);
                        } catch (Exception exception) {
                            exception.printStackTrace();

                        } finally {

                        }
                        if (isFirst) {
                            isFirst = false;
                        }
                    }
                }
            };
            try {
                if(!isStart){
                    checkThread.start();
                    isStart = true;
                }

            } catch (Exception exception) {
                exception.printStackTrace();

            } finally {

            }
        }
        try{
            if (thread != null && !isThreadStarted) {
                isThreadStarted = true;
                thread.start();
            }
        } catch (Exception exception){
           exception.printStackTrace();

        } finally{

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

    public class MyBinder extends Binder {

        public void setIsForGround(boolean isForGround) {
            CheckService.this.isForGround = isForGround;
            //不在前台就要启动应用
            if (!isForGround) {
                MainActivity.startMyActivity(CheckService.this);
            }
        }
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.setAction(ACTION_DESTROY);
        sendBroadcast(intent);
        try{
            checkThread.stop();
            checkThread = null;
        } catch (Exception exception){
           exception.printStackTrace();

        } finally{

        }


        super.onDestroy();
    }



    public boolean isAppOnForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean isOnForground = false;
        List<ActivityManager.RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
        if(runnings != null){
            for (ActivityManager.RunningAppProcessInfo running : runnings) {
                if (running.processName.equals(getPackageName())) {
                    if (running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            || running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        //前台显示...
//                    Log.e("my", "前台显示");
                        isOnForground = true;
                    } else {
                        //后台显示...
//                        Log.e("my", "后台显示");
                        isOnForground = false;
                    }
                    break;
                }
            }
        }
        String currentPackageName = "";
        if (am.getRunningTasks(1).size() > 0) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            currentPackageName = cn.getPackageName();
        }
//        Log.e("my", "isAppOnForeground  :" + currentPackageName + "   getPackageName:" + getPackageName());
//        return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName());
        return isOnForground;
    }


    private boolean isThreadStarted = false;
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
                    if (!b) {
                        Intent service = new Intent(CheckService.this, GuardService.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });
}
