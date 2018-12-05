package com.kaidongyuan.app.kdyorder.model;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.User;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;

import com.kaidongyuan.app.kdyorder.ui.activity.RegisterActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/12.
 */
public class RegisterActivityBiz {
    private RegisterActivity mActivity;
    /**
     * 登录网络请求标记
     */
    private final String mTagLogin = "mTagRegister";

    public RegisterActivityBiz( RegisterActivity mActivity) {
        this.mActivity = mActivity;
    }
    /**
     * 登录
     *
     * @param userName 用户姓名
     * @param userCode 用户帐号（手机号）
     * @param password 用户密码
     * @return 是否成功发送请求
     */
    public boolean register(final String userName, final String userCode, final String password,
                         final String partyName, final String partyCity, final String contactName, final String contactTel,
                            final String contactProvince,final String contactCity,final String contactArea,final String contactRural,
                            final String contactAds,final String contactAdsInfo ) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(RegisterActivityBiz.this.getClass() + "loginSuccess:" + response);
                    loginSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(RegisterActivityBiz.this.getClass() + "loginError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("登录失败!");
                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserCode", userCode.trim());
                    params.put("strUserName", userName.trim());
                    params.put("strPwd", password.trim());
                    params.put("PARTY_NAME",partyName.trim());
                    params.put("PARTY_CITY",partyCity.trim());
                    params.put("PARTY_REMARK","");
                    params.put("ADDRESS_PROVINCE",contactProvince);
                    params.put("ADDRESS_CITY",contactCity);
                    params.put("ADDRESS_AREA",contactArea);
                    params.put("ADDRESS_RURAL",contactRural);
                    params.put("ADDRESS_INFO",contactAdsInfo);
                    params.put("ADDRESS_ADDRESS",contactAds);
                    params.put("CONTACT_PERSON",contactName);
                    params.put("CONTACT_TEL",contactTel);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagLogin);
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
     * 注册成功
     * @param response 返回的消息
     */
    private void loginSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            if (type == 1) {//注册成功
                ToastUtil.showToastBottom("注册成功!", Toast.LENGTH_SHORT);
                mActivity.finish();
            } else {//登录失败
                mActivity.loginError(msg);
            }
        } catch (Exception e) {
            mActivity.loginError("登录失败!");
            ExceptionUtil.handlerException(e);
        }
    }

}
