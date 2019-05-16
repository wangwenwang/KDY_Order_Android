package com.kaidongyuan.app.kdyorder.model;

import android.widget.Toast;

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
import com.kaidongyuan.app.kdyorder.ui.activity.OrderTmsDetailsActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 * 物流信息详情业务类
 */
public class OrderTmsDetailsActivityBiz {

    private OrderTmsDetailsActivity mActivity;

    /**
     * 网络请求时的标记
     */
    private final String mTagGetEmsOrderDetailsData = "mTagGetEmsOrderDetailsData";

    /**
     * 存放订单物流信息
     */
    private Order mOrder;

    public OrderTmsDetailsActivityBiz(OrderTmsDetailsActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 网络获取订单物流详情数据
     * @return 发送请求是否成功
     * @param orderId 订单编号
     */
    public boolean getOrderDetailsData(final String orderId) {
        try {
            if (orderId == null || orderId.length() <= 0) {
                mActivity.getOrderDetailsDataError("获取订单物流详情失败！");
            }

            String url = "";
            if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("Y")) {

                url = URLCostant.GET_ORDER_TMS_INFORMATION_SAAS;
            }else if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("N")) {

                url = URLCostant.GET_ORDER_TMS_INFORMATION;
            }else {

                ToastUtil.showToastBottom("IS_SAAS不合法", Toast.LENGTH_SHORT);
            }


            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderTmsDetailsActivityBiz.this.getClass() + "getTransInformationDataSuccess:" + response);
                    getOrderDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderTmsDetailsActivityBiz.this.getClass() + "getTransInformationDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderDetailsDataError("获取订单物流详情失败！");
                    } else {
                        mActivity.getOrderDetailsDataError("获取订单物流详情失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strTmsOrderId", orderId);//strOrderId , 13464  22
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetEmsOrderDetailsData);
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
    private void getOrderDetailsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONArray array = JSON.parseArray(object.getString("result"));
                if (array.size() > 0) {
                    object = JSON.parseObject(array.get(0).toString());
                    mOrder = JSON.parseObject(object.getString("order"), Order.class);
                    mActivity.getOrderDetailsDtaSuccess();
                } else {
                    mActivity.getOrderDetailsDataError("获取到的订单物流详情为空！");
                }
            } else {
                String msg = object.getString("msg");
                mActivity.getOrderDetailsDataError(msg);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getOrderDetailsDataError("获取订单物流详情失败！");
        }
    }

    /**
     * 获取物流信息详情
     * @return 物流信息详情
     */
    public Order getOrder() {
        return mOrder;
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetEmsOrderDetailsData);
    }
}
























