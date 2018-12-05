package com.kaidongyuan.app.kdyorder.util;


import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kaidongyuan.app.kdyorder.app.MyApplication;

/**
 * Created by Administrator on 2016/5/16.
 * 网络请求工具类
 */
public class HttpUtil {

    /**
     * 网络请求队列
     */
    private static RequestQueue mRequestQueue;

    private HttpUtil(){}

    /**
     * 取消网络请求
     * @param tags 请求标记
     */
    public static synchronized void cancelRequest(String... tags) {
        int size = tags.length;
        String tag;
        for (int i=0; i<size; i++) {
            tag = tags[i];
            if (!TextUtils.isEmpty(tag) && mRequestQueue!=null) {
                mRequestQueue.cancelAll(tag);
            }
        }
    }

    /**
     * 获取请求队列
     * @return 请求队列
     */
    public static synchronized RequestQueue getRequestQueue() {
        if (mRequestQueue==null) {
            mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        }
        return mRequestQueue;
    }


}











