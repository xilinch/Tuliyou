package com.juyou.tuliyou;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by xilinch on 18-6-23 下午6:18.
 * describe 该类主要完成以下功能
 * 1.基类
 */

public class BaseActivity extends Activity {


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 是否存活
     * @return
     */
    protected boolean isAlive(){
        boolean isAlive = false;
        if(!isFinishing() && !isDestroyed()){
            isAlive = true;
        }
        return isAlive;
    }
}
