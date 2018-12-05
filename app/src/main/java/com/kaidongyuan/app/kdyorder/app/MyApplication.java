package com.kaidongyuan.app.kdyorder.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Process;

import com.baidu.mapapi.SDKInitializer;
import com.kaidongyuan.app.kdyorder.bean.Business;
import com.kaidongyuan.app.kdyorder.bean.User;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.logger.AndroidLogTool;
import com.kaidongyuan.app.kdyorder.util.logger.LogLevel;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.util.logger.Settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Administrator on 2016/5/16.
 * Application
 */
public class MyApplication extends Application {

    /**
     * 发布时改为 true 控制日志输出
     */
    public static final boolean isRelas = false;

    /**
     * 用于判断 app 是否为登录状态
     */
    public static boolean isLogin = false;

    /**
     * 日志输出标记
     */
    private final String mLoggerTag = "Tom";

    /**
     * MyApplication 实例
     */
    private static MyApplication instance;

    /**
     * MyApplication 的上下文
     */
    private static Context applicationContext;

    /**
     * 管理 Activity 的集合
     */
    private List<Activity> mActivityManagerList;

    /**
     * 记录用户是否为登录状态
     */
    public static boolean IS_LOGIN = false;

    /**
     * 用户信息实体类
     */
    private User user;

    /**
     * 用户业务类型实体类
     */
    private Business business;

    /**
     * app 资源
     */
    private static Resources mRes;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initCrashHandler();
            SDKInitializer.initialize(this);// 初始化百度地图
            initLoggerUtil();
            initData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 捕获未捕获的异常处理
     */
    private void initCrashHandler() {
        try {
            Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    ExceptionUtil.handleUncaughtException(ex);
                }
            };
            Thread.setDefaultUncaughtExceptionHandler(handler);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            MyApplication.instance = this;
            MyApplication.applicationContext = this;
            this.mActivityManagerList = new ArrayList<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初始化日志输出工具
     */
    private void initLoggerUtil() {
        try {
            Settings init = Logger.init(mLoggerTag)// default PRETTYLOGGER or use just init()
                    .methodCount(0)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(0)                // default 0
                    .logTool(new AndroidLogTool()); // custom log tool, optional
            if (isRelas) {
                init.logLevel(LogLevel.NONE)
                        .hideThreadInfo();// default shown
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 将 Activity 添加到管理集合中
     *
     * @param activity 需要添加的集合
     */
    public void addActivityToManager(Activity activity) {
        try {
            if (activity == null) {
                return;
            }
            mActivityManagerList.add(activity);
            Logger.w("MyApplication.exit.mActivitysManagerSize:" + mActivityManagerList.size());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Iterator iterator = mActivityManagerList.iterator(); iterator.hasNext(); ) {
            Activity activity = (Activity) iterator.next();
            if (activity.getClass().equals(cls)) {
                iterator.remove();
                activity.finish();
                break;
            }
        }
    }

    /**
     * 将 Activity 从管理集合中移除
     *
     * @param activity 要移除的 Activity
     */
    public void removeActivityFromManager(Activity activity) {
        try {
            if (activity == null) {
                return;
            }
            mActivityManagerList.remove(activity);
            Logger.w("MyApplication.exit.mActivitysManagerSize:" + mActivityManagerList.size());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 退出程序
     */
    public void exit() {
        try {
            Activity activity;
            for (int i = mActivityManagerList.size() - 1; i >= 0; i--) {
                try {
                    activity = mActivityManagerList.remove(i);
                    if (activity != null) {
                        activity.finish();
                    }
                } catch (Exception e) {
                    ExceptionUtil.handlerException(e);
                }
            }
            Logger.w("MyApplication.exit.mActivitysManagerSize:" + mActivityManagerList.size());
            android.os.Process.killProcess(Process.myPid());
            Logger.w("MyApplication.exit.退出程序");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取运行的所有的 Activity
     *
     * @return 运行的 Activity 集合
     */
    public List<Activity> getActivitysList() {
        return mActivityManagerList;
    }

    /**
     * 获取 Application 的实例
     *
     * @return Application 实例
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 获取 Application 的上下文
     *
     * @return Application 的上下文
     */
    public static Context getAppContext() {
        return applicationContext;
    }

    /**
     * 获取登录用户信息实体类
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * 设置用户登录信息实体类
     *
     * @param user User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取用户类型实体类
     *
     * @return Business
     */
    public Business getBusiness() {
        return business;
    }

    /**
     * 设置用户类型实体类
     *
     * @param business Business
     */
    public void setBusiness(Business business) {
        this.business = business;
    }

    /**
     * 获取 app 资源
     *
     * @return app 资源
     */
    public static Resources getmRes() {
        if (mRes == null) {
            mRes = MyApplication.getAppContext().getResources();
        }
        return mRes;
    }

}









