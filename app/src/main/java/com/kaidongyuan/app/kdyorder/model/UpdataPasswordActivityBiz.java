package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.UpdataPasswordActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/1.
 * 修改密码 Activity 的业务类
 */
public class UpdataPasswordActivityBiz {


    private UpdataPasswordActivity mActivity;

    /**
     * 网络请求修改密码的标记
     */
    private final String mTagUpdataPassword = "mTagUpdataPassword";

    public UpdataPasswordActivityBiz(UpdataPasswordActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 修改密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 发送请求是否成功
     */
    public boolean updataPassword(final String oldPassword, final String newPassword) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.UPDATA_PASSWORD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(UpdataPasswordActivityBiz.this.getClass() + "updataPasswordSuccess:" + response);
                    updataPasswordSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(UpdataPasswordActivityBiz.this.getClass() + "updataPasswordVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.updataPasswordFailed("修改密码失败！");
                    } else {
                        mActivity.updataPasswordFailed(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserName", MyApplication.getInstance().getUser().getUSER_CODE());//传入的是用户手机号，而非用户名
                    params.put("strPassword", oldPassword);
                    params.put("strNewPassword", newPassword);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagUpdataPassword);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 修改密码后台成功返回数据
     * @param response 返回的信息
     */
    private void updataPasswordSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mActivity.updataPasswordSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.updataPasswordFailed(msg);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPasswordFailed("修改密码失败！");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagUpdataPassword);
    }

}













