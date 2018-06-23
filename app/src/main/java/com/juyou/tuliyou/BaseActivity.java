package com.juyou.tuliyou;

import android.app.Activity;

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
}
