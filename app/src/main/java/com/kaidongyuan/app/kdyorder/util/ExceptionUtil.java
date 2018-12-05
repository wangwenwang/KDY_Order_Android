package com.kaidongyuan.app.kdyorder.util;

import com.kaidongyuan.app.kdyorder.app.MyApplication;

/**
 * Created by Administrator on 2016/5/16.
 * 异常处理工具类
 */
public class ExceptionUtil {

    /**
     * 异常处理
     * @param e 异常
     */
    public static void handlerException(Exception e){
        if (MyApplication.isRelas) {
            //发布后处理异常方式
        } else {//调试时处理异常方式-打印日志
            e.printStackTrace();
        }
    }

    /**
     * 处理未捕获的异常
     * @param ex 未捕获的异常
     */
    public static void handleUncaughtException(Throwable ex) {
        if (MyApplication.isRelas) {
            //发布后处理异常方式
        } else {//调试时处理异常方式-打印日志
            ex.printStackTrace();
        }
    }
}







