package com.kaidongyuan.app.kdyorder.util;

import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.app.MyApplication;

/**
 * Created by Administrator on 2016/5/16.
 * Toast 工具类
 */
public class ToastUtil {

    /**
     * 显示在屏幕上部
     * @param msg 要显示的信息
     * @param time 显示的时间 Toast.LENGTH_LONG or Toast.LENGTH_SHORT
     */
    public static void showToastTop(Object msg, int time){
        String message = String.valueOf(msg);
        Toast.makeText(MyApplication.getAppContext(), message, time).show();
    }

    /**
     * 显示在屏幕中部
     * @param msg 要显示的信息
     * @param time 显示的时间 Toast.LENGTH_LONG or Toast.LENGTH_SHORT
     */
    public static void showToastMid(Object msg, int time){
        String message = String.valueOf(msg);
        Toast.makeText(MyApplication.getAppContext(), message, time).show();
    }

    /**
     * 显示在屏幕底部
     * @param msg 要显示的信息
     * @param time 显示的时间 Toast.LENGTH_LONG or Toast.LENGTH_SHORT
     */
    public static void showToastBottom(Object msg, int time){
        String message = String.valueOf(msg);
        Toast.makeText(MyApplication.getAppContext(), message, time).show();
    }

}














