package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

/**
 * Created by Administrator on 2016/5/16.
 * BaseActivity
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.w(this.getClass() + ":onCreate");
        MyApplication.getInstance().addActivityToManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Logger.w(this.getClass() + ":onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Logger.w(this.getClass() + ":onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.w(this.getClass() + ":onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Logger.w(this.getClass() + ":onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Logger.w(this.getClass() + ":onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.w(this.getClass() + ":onDestroy");
        MyApplication.getInstance().removeActivityFromManager(this);
    }

    /* ********************************************** 设置 Activity 动画 **************************************************** */

    @Override
    public void onBackPressed() {//设置 Activity 退出动画
        String className = this.getClass().toString();
        if (WelcomeActivity.class.toString().equals(className)
                || LoginActivity.class.toString().equals(className)
                || MainActivity.class.toString().equals(className)) {
            super.onBackPressed();
        } else {
            this.finish();
            overridePendingTransition(R.anim.activity_startorexit_withnoanimation,
                    R.anim.activity_exit_with_transright);
        }
    }

    @Override
    public void startActivity(Intent intent) {// 设置 Activity 的启动动画
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_start_with_scal,
                R.anim.activity_startorexit_withnoanimation);
    }

    @Override
    public void finish() {//设置 Activity 退出动画
        super.finish();
        String className = this.getClass().toString();
        if (!WelcomeActivity.class.toString().equals(className)
                && !LoginActivity.class.toString().equals(className)
                && !MainActivity.class.toString().equals(className)) {
            overridePendingTransition(R.anim.activity_startorexit_withnoanimation,
                    R.anim.activity_exit_with_transright);
        }
    }

    /*********************************************** 点击外部隐藏软键盘 **************************************************** */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v     View
     * @param event Event
     * @return boolean
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token IBinder
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
