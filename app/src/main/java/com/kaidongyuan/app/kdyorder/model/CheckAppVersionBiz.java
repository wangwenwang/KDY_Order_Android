package com.kaidongyuan.app.kdyorder.model;

import android.content.Context;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/30.
 * 检查 app 版本和下载更新 app 的业务类
 */
public class CheckAppVersionBiz {

    private CheckVersionInterface mCheckVersionInterface;
    /**
     * app 下载地址
     */
    private String mDownUrl;

    public boolean checkVersion(CheckVersionInterface checkVersionInterface) {
        this.mCheckVersionInterface = checkVersionInterface;
        RequestQueue requestQueue = HttpUtil.getRequestQueue();
        final StringRequest request = new StringRequest(StringRequest.Method.POST, URLCostant.CHECK_VERSION, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.w(CheckAppVersionBiz.this.getClass() + "checkVersionSuccess:" + response);
                checkVersionSuccess(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCheckVersionInterface.checkVersionError("获取最新版本信息失败!");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("checkVersion");
        requestQueue.add(request);
        return true;
    }

    private void checkVersionSuccess(String response) {
        try {
            com.alibaba.fastjson.JSONObject jsResponse = JSON.parseObject(response);
            int type = jsResponse.getInteger("type");
            if (type==1){
                JSONArray arr = jsResponse.getJSONArray("result");
                int size = arr.size();
                String version = null;
                com.alibaba.fastjson.JSONObject obj;
                String netDownUrl;
                int startIndex;
                for (int i=0; i<size; i++){
                    obj = arr.getJSONObject(i);
                    netDownUrl = obj.getString("DownLoadAddress");
                    startIndex = netDownUrl.indexOf('/')+1;
                    String appName = netDownUrl.substring(startIndex, netDownUrl.length());
                    if ("kdyorder.apk".equals(appName)){
                        version = obj.getString("VersionCode");
                        mDownUrl = "http://oms.kaidongyuan.com:8888/" + netDownUrl;
                        break;
                    }
                }
                if (version!=null) {
                    compareAppVersion(version);
                }
            }else {
                mCheckVersionInterface.checkVersionError(jsResponse.getString("msg"));
            }
        } catch (Exception e) {
            mCheckVersionInterface.checkVersionError("获取最新版本失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取 app 下载地址
     * @return app 下载的 URL
     */
    public String getAppDownAddress() {
        return mDownUrl;
    }

    /**
     * 比较 app 当前版本和网络的版本是不是同一个版本，不是同一个版本就开始下载更新
     * @param version 网络获取的版本号
     */
    private void compareAppVersion(String version){
        try {
            Context context = MyApplication.getAppContext();
            String currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (!currentVersion.equals(version)) {
                int current = (int) Float.parseFloat(currentVersion);
                int net = (int) Float.parseFloat(version);
                if (current==net) {
                    mCheckVersionInterface.canUpdataVersion(version);
                } else {
                    mCheckVersionInterface.mustUpdataVersion(version);
                }
            } else {
                mCheckVersionInterface.currentVersionIsNewestVersion();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查版本的接口
     */
    public interface CheckVersionInterface {
        /**
         * 当前版本已是最新版本回调方法
         */
        void currentVersionIsNewestVersion();

        /**
         * 可以更新版本
         * @param newestVerison 最新版本号
         */
        void canUpdataVersion(String newestVerison);

        /**
         * 必须更新 app 版本
         * @param newestVerison 最新版本号
         */
        void mustUpdataVersion(String newestVerison);
        /**
         * 检查版本出错
         * @param message 错误信息
         */
        void checkVersionError(String message);
    }

}

















