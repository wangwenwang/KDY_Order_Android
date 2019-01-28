package com.kaidongyuan.app.kdyorder.model;

import android.util.Log;

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
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.constants.FileConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingShowStepActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kaidongyuan.app.kdyorder.constants.URLCostant.INFORMATION_PICTURE_URL;

public class CustomerMeetingShowStepActivityBiz {

    private CustomerMeetingShowStepActivity mActivity;

    private CustomerMeeting customerMeeting;

    // 进店图片
    public ArrayList<String> VisitUrls = new ArrayList<String>();

    // 生动化陈列图片
    public ArrayList<String> VisitVividDisplayUrls = new ArrayList<String>();

    private final String mTagGetPictureByVisitIdx = "mTagGetPictureByVisitIdx";

    /**
     * 获取线路内订单的网络请求标记
     */
    private final String mTagGetVisitAppOrder = "mTagGetVisitAppOrder";

    /**
     * 存放从后台获取的所有订单数据集合_经销商的出售单
     */
    private List<OutPutSimpleOrder> mOutputSimpleOrderList;
    /**
     * 保存订单数据的集合_经销商的入库单
     */
    private List<Order> mOrderList;

    public CustomerMeetingShowStepActivityBiz(CustomerMeetingShowStepActivity activity) {
        this.mActivity = activity;
        this.mOutputSimpleOrderList = new ArrayList<>();
        this.mOrderList = new ArrayList<>();
    }

    public CustomerMeeting getCustomerMeeting() {
        return customerMeeting;
    }

    public void setCustomerMeeting(CustomerMeeting customerMeeting) {
        this.customerMeeting = customerMeeting;
    }


    /**
     * 返回请求到的账单列表集合
     *
     * @return
     */
    public List<OutPutSimpleOrder> getmOutputSimpleOrderList() {
        return mOutputSimpleOrderList;
    }

    /**
     * 返回请求到的账单列表集合
     *
     * @return
     */
    public List<Order> getmOrderList() {
        return mOrderList;
    }


    /**
     * 获取拜访照片成功
     *
     * @return 是否成功发送请求
     */
    public boolean GetPictureByVisitIdx(final String strVisitIdx, final String strStep) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPictureByVisitIdx, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + "GetPictureByVisitIdx.Success:" + response);
                    GetPictureByVisitIdxSuccess(response, strStep);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + "GetPictureByVisitIdx.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.requestError("获取拜访照片|请求失败!");
                    } else {
                        mActivity.requestError("获取拜访照片|请求失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strVisitIdx", strVisitIdx);
                    params.put("strStep", strStep);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPictureByVisitIdx);
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
     * 处理网络请求返回拜访照片成功
     *
     * @param response 返回的数据
     */
    private void GetPictureByVisitIdxSuccess(String response, String strStep) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            if (type == 1) {
                JSONArray urlArray = object.getJSONArray("result");
                for (int i = 0; i < urlArray.size(); i++) {
                    JSONObject urlObject = (JSONObject) urlArray.get(i);
                    String urlString = urlObject.getString("PRODUCT_URL");
                    urlString = URLCostant.LOA_URL + FileConstants.SERVER_AUTOGRAPH_AND_PICTURE_FILE + File.separator + urlString;
                    if (strStep.equals("Visit")) {
                        Log.d("LM", "添加进店照片" + urlArray.size());
                        VisitUrls.add(urlString);
                    } else if (strStep.equals("VisitVividDisplay")) {
                        Log.d("LM", "添加生动化陈列照片" + urlArray.size());
                        VisitVividDisplayUrls.add(urlString);
                    }
                }
                mActivity.GetPictureByVisitIdxSuccess(msg, strStep);
            } else {
                mActivity.requestError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.requestError("请求照片失败!");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetPictureByVisitIdx);
            HttpUtil.cancelRequest(mTagGetVisitAppOrder);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单数据
     *
     * @return 发送请求是否成功
     */
    public boolean GetVisitAppOrder(final String strVisitIdx, final String strType) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitAppOrder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".GetVisitAppOrder:" + response);
                    GetVisitAppOrderSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".GetVisitAppOrder:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.requestError("获取订单失败!");
                    } else {
                        mActivity.requestError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strVisitIdx", strVisitIdx);
                    params.put("strType", strType);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetVisitAppOrder);
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
     * 获取订单数据
     *
     * @return 发送请求是否成功
     */
    public boolean GetVisitAppOrder_AGENT(final String strVisitIdx, final String strType) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitAppOrder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".GetVisitAppOrderSuccess_AGENT:" + response);
                    GetVisitAppOrderSuccess_AGENT(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".GetVisitAppOrderSuccess_AGENT:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.requestError("获取订单失败!");
                    } else {
                        mActivity.requestError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strVisitIdx", strVisitIdx);
                    params.put("strType", strType);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetVisitAppOrder);
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
    private void GetVisitAppOrderSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                mOutputSimpleOrderList.clear();
                JSONArray ar = JSON.parseArray(object.getString("result"));
                JSONObject jo = (JSONObject) ar.get(0);
                List<OutPutSimpleOrder> tmpSimpleOrderList = JSON.parseArray(jo.getString("List"), OutPutSimpleOrder.class);
                mOutputSimpleOrderList.addAll(tmpSimpleOrderList);
                mActivity.GetVisitAppOrderSuccess();
            } else {
                mActivity.requestError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.requestError("获取订单失败!");
        }
    }

    /**
     * 处理网络请求返回数据成功
     *
     * @param response 返回的数据
     */
    private void GetVisitAppOrderSuccess_AGENT(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                mOrderList.clear();
                JSONArray ar = JSON.parseArray(object.getString("result"));
                JSONObject jo = (JSONObject) ar.get(0);
                List<Order> orderList = JSON.parseArray(jo.getString("List"), Order.class);
                mOrderList.addAll(orderList);
                mActivity.GetVisitAppOrderSuccess_AGENT();
            } else {
                mActivity.requestError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.requestError("获取订单失败!");
        }
    }
}