package com.kaidongyuan.app.kdyorder.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.SearchOrderTrajectoryActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/21.
 * 订单轨迹查询的业务类
 */
public class SearchOrderTrajectoryActivityBiz {

    private SearchOrderTrajectoryActivity mActivity;

    /**
     * 获取订单轨迹请求是的标记
     */
    private final String mTagGetOrderTrajectory = "mTagGetOrderTrajectory";
    /**
     * 订单信息
     */
    private Order mOrder;

    public SearchOrderTrajectoryActivityBiz(SearchOrderTrajectoryActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 根据订单编号获取订单轨迹
     * @param text 订单编号
     * @return 发送请求是否成功
     */
    public boolean getOrderDetailTrajectory(final String text) {
        try {
            if (TextUtils.isEmpty(text)) {
                mActivity.getOrderTrajectory("请输入订单编号!");
                return false;
            }
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_ORDER_TRAJECTORY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(SearchOrderTrajectoryActivityBiz.this.getClass() + "getOrderTrajectoryListSuccess:" + response);
                    getOrderTrajectorySuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(SearchOrderTrajectoryActivityBiz.this.getClass() + "getOrderTrajectoryListVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderTrajectory("获取订单轨迹失败！");
                    } else {
                        mActivity.getOrderTrajectory(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderId", text);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetOrderTrajectory);
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
     * 网络请求订单轨迹成功返回结果
     * @param response 返回的结果信息
     */
    private void getOrderTrajectorySuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONArray ja = JSON.parseArray(object.getString("result"));
                object = JSON.parseObject(ja.get(0).toString());
                mOrder = JSON.parseObject(object.getString("order"), Order.class);
                mActivity.getOrderTrajectorySuccess();
            } else {
                mActivity.getOrderTrajectory(object.getString("msg"));
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getOrderTrajectory("获取订单轨迹失败！");
        }
    }

    /**
     * 取消所有的网络请求
     */
    public void cancelAllRequest(){
        try {
            HttpUtil.cancelRequest(mTagGetOrderTrajectory);
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单信息
     * @return 订单信息
     */
    public Order getOrder() {
        return mOrder;
    }

}











