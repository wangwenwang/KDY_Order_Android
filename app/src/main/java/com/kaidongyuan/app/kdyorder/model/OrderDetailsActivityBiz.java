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
import com.kaidongyuan.app.kdyorder.ui.activity.OrderDetailsActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/25.
 * 订单详情界面
 */
public class OrderDetailsActivityBiz {

    private OrderDetailsActivity mActivity;

    /**
     * 网络请求订单详情数据的标记
     */
    private final String mTagGetOrderDetailsData = "mTagGetOrderDetailsData",mTagCancelOrder="mTagCancelOrder";


    /**
     * 订单详情
     */
    private Order mOrder;

    public OrderDetailsActivityBiz(OrderDetailsActivity activity) {
        this.mActivity = activity;
    }

    public boolean cancelOrder(final String orderid){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.OrderCancel, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    cancelOrderSuccess(response);
                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderDetailsDataError("取消订单失败！");
                    } else {
                        mActivity.getOrderDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", orderid);//strOrderId , 13464
                    params.put("strUserIdx",MyApplication.getInstance().getUser().getIDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagCancelOrder);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private void cancelOrderSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                mActivity.cancelOrderSuccess();
            } else {
                mActivity.getOrderDetailsDataError(object.getString("msg"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 网络请求订单详情数据
     *
     * @param orderId 订单编号
     * @return 发送请求是否成功
     */
    public boolean getOrderDetailsData(final String orderId) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_ORDER_DETAIL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    getOrderDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderDetailsDataError("获取订单详情失败！");
                    } else {
                        mActivity.getOrderDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderId", orderId);//strOrderId , 13464
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetOrderDetailsData);
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
     * 网络请求订单详情数据成功返回信息
     *
     * @param response 返回的信息
     */
    private void getOrderDetailsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONArray ja = JSON.parseArray(object.getString("result"));
                if (ja.size() > 0) {
                    object = JSON.parseObject(ja.get(0).toString());
                    mOrder = JSON.parseObject(object.getString("order"), Order.class);
                    mActivity.getOrderDetailsDataSuccess();
                } else {
                    mActivity.getOrderDetailsDataError("获取订单详情数据失败,返回数据为空！");
                }
            } else {
                mActivity.getOrderDetailsDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetOrderDetailsData);
    }

    /**
     * 获取订单详情
     *
     * @return 订单详情
     */
    public Order getOrderDetails() {
        return mOrder;
    }

}
