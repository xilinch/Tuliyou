package com.juyou.tuliyou.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.juyou.tuliyou.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xilinch on 18-6-25 下午7:02.
 * describe 该类主要完成以下功能
 * 1.守护进程
 */

public class GuardService extends Service {
    public final static String TAG = "com.juyou.tuliyou.service.GuardService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(thread != null){
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    boolean b = MainActivity.isServiceWorked(GuardService.this, CheckService.TAG);
                    if(!b) {
                        Intent service = new Intent(GuardService.this, CheckService.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });


}
