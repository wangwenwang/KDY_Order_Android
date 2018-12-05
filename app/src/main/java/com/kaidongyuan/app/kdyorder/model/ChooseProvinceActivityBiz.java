package com.kaidongyuan.app.kdyorder.model;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.ChooseProvinceActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyManageActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.RegisterActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/12.
 */
public class ChooseProvinceActivityBiz {
    private ChooseProvinceActivity mActivity;
    /**
     * 登录网络请求标记
     */
    private final String mTagNormalAdressList = "mTagNormalAdressList";
    private List<NormalAddress> normalAddressList;

    public ChooseProvinceActivityBiz(ChooseProvinceActivity mActivity) {
        this.mActivity = mActivity;
    }
    /**
     *
     * @return 是否成功发送请求
     */
    public boolean getAddresses(final String idx , final String type) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.NormalAdressList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(ChooseProvinceActivityBiz.this.getClass() + "loginSuccess:" + response);
                    getAddressesSuccess(response,type);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(ChooseProvinceActivityBiz.this.getClass() + "loginError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getAddressError("加载列表失败!",type);
                    } else {
                        mActivity.getAddressError("请检查网络是否正常连接！",type);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strPrentCode",idx);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagNormalAdressList);
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
    private void getAddressesSuccess(String response,String extra_type) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            if (type == 1) {
                normalAddressList = JSON.parseArray(object.getString("result"),NormalAddress.class);
                mActivity.getAddressSuccess(normalAddressList,extra_type);
            } else {
                mActivity.getAddressError(msg,extra_type);
            }
        } catch (Exception e) {
            mActivity.getAddressError("地址加载失败!",extra_type);
            ExceptionUtil.handlerException(e);
        }
    }

}
