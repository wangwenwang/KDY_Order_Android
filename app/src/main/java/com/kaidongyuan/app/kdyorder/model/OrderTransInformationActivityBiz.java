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
import com.kaidongyuan.app.kdyorder.bean.OrderTms;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderTransInformationActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/26.
 * 查看物流信息界面业务类
 */
public class OrderTransInformationActivityBiz {

    private OrderTransInformationActivity mActivity;

    /**
     * 网络请求订单物流信息时候的标记
     */
    private final String mTagGetTransInformationData = "mTagGetTransInformationData";
    /**
     * 订单物流信息
     */
    private OrderTms mOrderTms;

    public OrderTransInformationActivityBiz(OrderTransInformationActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取订单在途数据
     *
     * @param orderId 订单编号
     * @return 发送请求是否成功
     */
    public boolean getTransInformationData(final String orderId) {
        try {
            if (orderId == null || orderId.length() <= 0) {
                mActivity.getTransInformationError("获取订单物流信息失败！");
            }

            String url = "";
            if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("Y")) {

                url = URLCostant.GET_ORDER_TMSLIST_SAAS;
            }else if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("N")) {

                url = URLCostant.GET_ORDER_TMSLIST;
            }else {

                ToastUtil.showToastBottom("IS_SAAS不合法", Toast.LENGTH_SHORT);
            }

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderTransInformationActivityBiz.this.getClass() + "getTransInformationDataSuccess:" + response);
                    getTransInformationDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderTransInformationActivityBiz.this.getClass() + "getTransInformationDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getTransInformationError("获取订单物流信息失败！");
                    } else {
                        mActivity.getTransInformationError("获取订单物流信息失败！"+MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderId", orderId);//strOrderId , 13464  22
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetTransInformationData);
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
     * 网络请求成功返回数据
     *
     * @param response 返回的信息
     */
    private void getTransInformationDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONArray array = JSON.parseArray(object.getString("result"));
                mOrderTms = JSON.parseObject(array.get(0).toString(), OrderTms.class);
                mActivity.getTransInformationSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getTransInformationError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetTransInformationData);
    }

    /**
     * 获取订单物流信息
     *
     * @return 物流信息实体类
     */
    public OrderTms getOrderTms() {
        return mOrderTms;
    }

}














