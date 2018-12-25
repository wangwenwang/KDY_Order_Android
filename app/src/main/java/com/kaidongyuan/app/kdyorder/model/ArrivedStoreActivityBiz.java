package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.ArrivedStoreActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户拜访_进店业务类
 */
public class ArrivedStoreActivityBiz {
    private ArrivedStoreActivity mActivity;
    /**
     * 获取客户列表的网络请求标记
     */
    private final String mTagGetVisitEnterShop = "mTagGetVisitEnterShop";

    public ArrivedStoreActivityBiz(ArrivedStoreActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 进店
     *
     * @return 发送请求是否成功
     */
    public boolean GetVisitEnterShop(final String strVisitIdx, final String strpicture, final String strpicture2, final String strAddress) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitEnterShop, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(ArrivedStoreActivityBiz.this.getClass() + ".GetVisitEnterShop:" + response);
                    EnterShopSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(ArrivedStoreActivityBiz.this.getClass() + ".GetVisitEnterShop:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.EnterShopError("请求失败!");
                    } else {
                        mActivity.EnterShopError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strVisitIdx", strVisitIdx);
                    params.put("PictureFile1", strpicture);
                    params.put("PictureFile2", strpicture2);
                    params.put("strAddress", strAddress);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetVisitEnterShop);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 处理网络请求返回数据成功
     *
     * @param response 返回的数据
     */
    private void EnterShopSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            if (type == 1) {
                mActivity.EnterShopSuccess(msg);
            } else {
                mActivity.EnterShopError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.EnterShopError("服务器返回数据异常！");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetVisitEnterShop);
    }
}
