package com.juyou.tuliyou;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by xilinch on 18-6-27 下午2:40.
 * describe 该类主要完成以下功能
 * 1.引导页面
 */

public class GuiActivity extends BaseActivity {

    public static final String ACTION_FINISH = "com.juyou.tuliyou.ACTION_FINISH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        hideBottomUIMenu();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FINISH);
        registerReceiver(finishBroadcast, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(finishBroadcast != null){
            unregisterReceiver(finishBroadcast);
        }
    }

    BroadcastReceiver finishBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isAlive()){
                GuiActivity.this.finish();
            }
        }
    };




}
