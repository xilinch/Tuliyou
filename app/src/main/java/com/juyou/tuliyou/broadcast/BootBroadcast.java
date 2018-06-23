package com.juyou.tuliyou.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.juyou.tuliyou.MainActivity;
import com.juyou.tuliyou.service.CheckService;

/**
 * Created by xilinch on 18-6-23 下午4:23.
 * describe 该类主要完成以下功能
 * 自启动
 */

public class BootBroadcast extends BroadcastReceiver {

    public static final String ACTION_DESTROY = "com.juyou.tuliyou.destroy";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
            //启动完成
            startMyActivity(context);
            Toast.makeText(context, "启动完成---",Toast.LENGTH_SHORT).show();
        } else if(Intent.CATEGORY_HOME.equals(action)){
            //主页
            startMyActivity(context);
            Toast.makeText(context, "主页---",Toast.LENGTH_SHORT).show();
        } else if(Intent.CATEGORY_LAUNCHER.equals(action)){
            //启动
            startMyActivity(context);
            Toast.makeText(context, "桌面组件---",Toast.LENGTH_SHORT).show();
        } else if(action.equals(ACTION_DESTROY)){
            //被杀死
//            startMyActivity(context);
            Toast.makeText(context, "桌面组件---",Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context, CheckService.class);
            context.startService(intent1);
        }

    }


    /**
     * 启动服务
     */
    private void startMyActivity(Context context){

        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent1);
    }
}
