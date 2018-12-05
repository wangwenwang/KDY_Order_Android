package com.kaidongyuan.app.kdyorder.model;

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
import com.kaidongyuan.app.kdyorder.ui.activity.OrderTimeNodeActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 * 订单进度的业务类
 */
public class OrderTimeNodeActivityBiz {

    private OrderTimeNodeActivity mActivity;

    /**
     * 网络获取订单进度的标记
     */
    private final String mTagGetOrderTimeNode = "mTagGetOrderTimeNode";
    /**
     * 保存订单信息
     */
    private Order mOrder;

    public OrderTimeNodeActivityBiz(OrderTimeNodeActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 网络请求订单进度数据
     * @return 发送请求是否成功
     * @param orderId 订单编号
     */
    public boolean getOrderTimeNodeData(final String orderId) {
        try {
            if (orderId == null || orderId.length() <= 0) {
                mActivity.getOrderTimeNodeError("获取订单进度失败！");
            }
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_ORDER_TMS_INFORMATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderTimeNodeActivityBiz.this.getClass() + "getOrderTimeNodeDataSuccess:" + response);
                    getOrderTimeNodeDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderTimeNodeActivityBiz.this.getClass() + "getOrderTimeNodeDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderTimeNodeError("获取订单进度失败！");
                    } else {
                        mActivity.getOrderTimeNodeError("获取订单进度失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strTmsOrderId", orderId);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetOrderTimeNode);
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
     * 网络请求成功返回数据
     * @param response 返回的信息
     */
    private void getOrderTimeNodeDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONArray array = JSON.parseArray(object.getString("result"));
                if (array.size() > 0) {
                    object = JSON.parseObject(array.get(0).toString());
                    mOrder = JSON.parseObject(object.getString("order"), Order.class);
                    mActivity.geOrderTimeNodeSuccess();
                } else {
                    mActivity.getOrderTimeNodeError("获取订单进度数据为空！");
                }
            } else {
                String msg = object.getString("msg");
                mActivity.getOrderTimeNodeError(msg);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getOrderTimeNodeError("获取订单进度失败！");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetOrderTimeNode);
    }

    /**
     * 获取订单信息
     * @return 订单信息
     */
    public Order getOrder() {
        return mOrder;
    }
}


















