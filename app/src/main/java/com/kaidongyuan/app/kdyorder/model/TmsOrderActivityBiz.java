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
import com.kaidongyuan.app.kdyorder.bean.OrderTms;
import com.kaidongyuan.app.kdyorder.bean.TmsOrderItem;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderTransInformationActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.TmsOrderActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/26.
 * 查看物流信息界面业务类
 */
public class TmsOrderActivityBiz {

    private TmsOrderActivity mActivity;

    /**
     * 网络请求订单物流信息时候的标记
     */
    private final String mTagGetTransInformationData = "mTagGetTransInformationData";
    /**
     * 订单物流信息
     */
    private List<TmsOrderItem> mTmsOrderList;

    public TmsOrderActivityBiz(TmsOrderActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取订单在途数据
     *
     * @param orderId 订单编号
     * @return 发送请求是否成功
     */
    public boolean getTmsOrderInformationData(final String orderId) {
        try {
            if (orderId == null || orderId.length() <= 0) {
                mActivity.getTransInformationError("获取订单物流信息失败！");
            }
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetOrderTms, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(TmsOrderActivityBiz.this.getClass() + "getTmsOrderInformationDataSuccess:" + response);
                    getTransInformationDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(TmsOrderActivityBiz.this.getClass() + "getTmsOrderInformationDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getTransInformationError("获取物流订单信息失败！");
                    } else {
                        mActivity.getTransInformationError("获取物流订单信息失败！"+MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("OrderIdx", orderId);//strOrderId , 13464  22
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
                JSONObject jo=object.getJSONObject("result");
                mTmsOrderList = JSON.parseArray(jo.getString("TmsList"), TmsOrderItem.class);
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
    public List<TmsOrderItem> getTmsOrderList() {
        return mTmsOrderList;
    }

}














