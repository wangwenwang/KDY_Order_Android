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
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.FatherAddress;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingRecomOrderActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMeetingRecomOrderActivityBiz {


    private CustomerMeetingRecomOrderActivity mActivity;
    /**
     * 获取线路内订单的网络请求标记
     */
    private final String mTagGetVisitAppOrder = "mTagGetVisitAppOrder";

    private final String mTagGetCustomerAddressInfo = "mTagGetCustomerAddressInfo";
    /**
     * 存放从后台获取的所有订单数据集合
     */
    private List<OutPutSimpleOrder> mOutputSimpleOrderList;

    public FatherAddress fatherAddressM;
    /**
     * 保存订单数据的集合_经销商的入库单
     */
    private List<Order> mOrderList;
    /**
     * 存放从后台获取的客户的地址集合
     */
    private List<Address> mCustomerAddressList;

    public CustomerMeetingRecomOrderActivityBiz(CustomerMeetingRecomOrderActivity activity) {
        try {
            this.mActivity = activity;
            this.mOutputSimpleOrderList = new ArrayList<>();
            this.mOrderList = new ArrayList<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
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
     * 获取客户地址集合
     *
     * @return 客户地址集合
     */
    public List<Address> getmCustomerAddress() {
        return mCustomerAddressList;
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
//                        mActivity.loginError("获取订单失败!");
                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
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
                mActivity.loginSuccess();
            } else {
                mActivity.loginError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
//            mActivity.loginError("获取订单失败!");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetVisitAppOrder);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
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
     * 根据客户地址id，获取上级地址id
     *
     * @return 发送请求是否成功
     */
    public boolean GetFatherAddress(final String strAddressIdx) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetFatherAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".GetFatherAddress:" + response);
                    GetFatherAddressSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".GetFatherAddress:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("获取上级地址失败!");
                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strAddressIdx", strAddressIdx);
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
    private void GetFatherAddressSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                List<FatherAddress> fads = new ArrayList<>();
                if (object.containsKey("result")) {
                    fads = JSON.parseArray(object.getString("result"), FatherAddress.class);
                    if(fads.size()>0) {
                        fatherAddressM = fads.get(0);
                    }
                    mActivity.GetFatherAddressSuccess();
                }
            } else {
                mActivity.loginError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.loginError("获取上级信息失败!");
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

                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
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
    private void GetVisitAppOrderSuccess_AGENT(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                mOrderList.clear();
                JSONArray ar = JSON.parseArray(object.getString("result"));
                if (ar.size() > 0) {

                    JSONObject jo = (JSONObject) ar.get(0);
                    // 防止 List 是 ""
                    try {
                        List<Order> orderList = JSON.parseArray(jo.getString("List"), Order.class);
                        mOrderList.addAll(orderList);
                        mActivity.GetVisitAppOrderSuccess_AGENT();
                    } catch (Exception e) {
                        ExceptionUtil.handlerException(e);
                    }
                }
            } else {
                mActivity.loginError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.loginError("获取订单失败!");
        }
    }

    /**
     * 获取客户地址
     *
     * @param strPartyId 客户ID
     * @return 发送请求是否成功
     */
    public boolean getPartygetAddressInfo(final String strPartyId) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_ADDRESS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerMeetingRecomOrderActivityBiz.this.getClass() + "getPartygetAddressInfo.Success:" + response);
                    getCustomerAddressResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerMeetingRecomOrderActivityBiz.this.getClass() + "getPartygetAddressInfo.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("获取客户地址失败!");
                    } else {
                        mActivity.loginError("获取客户地址失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("strPartyId", strPartyId);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetCustomerAddressInfo);
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
     * 发送获取用户地址请求成功返回数据
     *
     * @param response 返回的消息
     */
    private void getCustomerAddressResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mCustomerAddressList = JSON.parseArray(object.getString("result"), Address.class);
                if (mCustomerAddressList.size() < 1) {
                    mActivity.loginError("没有获取到客户有效地址，请联系供应商！");
                } else {
                    mActivity.getCustomerAdressDataSuccess();
                }
            } else {
                mActivity.loginError("获取客户地址失败!" + object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.loginError("获取客户地址失败!");
        }
    }
}