package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
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
import com.kaidongyuan.app.kdyorder.bean.CustomerChannel;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerCreateActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingCreateActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingsActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户拜访列表业务类
 */
public class CustomerMeetingCreateActivityBiz {

    private CustomerMeetingCreateActivity mActivity;

    private CustomerMeeting customerMeeting;

    private final String mTagGetPartyVisitInsert = "mTagGetPartyVisitInsert";
    private final String mTagGetVisitConfirmCustomer = "mTagGetVisitConfirmCustomer";

    public CustomerMeetingCreateActivityBiz(CustomerMeetingCreateActivity activity) {
        this.mActivity = activity;
    }

    public CustomerMeeting getCustomerMeeting() {
        return customerMeeting;
    }

    public void setCustomerMeeting(CustomerMeeting customerMeeting) {
        this.customerMeeting = customerMeeting;
    }

    /**
     * 处理网络请求返回客户信息成功
     *
     * @param response 返回的数据
     */
    private void getPartyVisitInsertSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mActivity.getPartyVisitInsertSuccess();
            } else {
                mActivity.getPartyVisitInsertSuccess();
                mActivity.updataError(response);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError("上传客户信息失败!");
        }
    }

    /**
     * 处理网络请求返回客户地址成功
     *
     * @param response 返回的数据
     */
    private void getVisitConfirmCustomerSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                mActivity.getVisitConfirmCustomerSuccess();

            } else {
                mActivity.updataPartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError("上传客户地址失败!");
        }
    }

    /**
     * 上传客户信息
     */
    public boolean GetPartyVisitInsert() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyVisitInsert, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerMeetingCreateActivityBiz.this.getClass() + ".GetPartyVisitInsert:" + response);
                    getPartyVisitInsertSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerMeetingCreateActivityBiz.this.getClass() + ".GetPartyVisitInsert:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.updataError("上传信息失败!");
                    } else {
                        mActivity.updataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("PARTY_NO", customerMeeting.getPARTY_NO());
                    params.put("PARTY_NAME", customerMeeting.getPARTY_NAME());
                    params.put("CONTACTS", customerMeeting.getCONTACTS());
                    params.put("CONTACTS_TEL", customerMeeting.getCONTACTS_TEL());
                    params.put("PARTY_ADDRESS", customerMeeting.getPARTY_ADDRESS());
                    params.put("USER_NAME", customerMeeting.getUSER_NAME());
                    params.put("USER_NO", customerMeeting.getUSER_NO());
                    params.put("CHANNEL_NO", customerMeeting.getCHANNEL());
                    params.put("PARTY_LEVEL", customerMeeting.getPARTY_LEVEL());
                    params.put("WEEKLY_VISIT_FREQUENCY", customerMeeting.getWEEKLY_VISIT_FREQUENCY());
                    params.put("VISIT_DATE", customerMeeting.getVISIT_DATE());
                    params.put("OPERATOR_IDX", customerMeeting.getIDX());
                    params.put("PARTY_STATES", customerMeeting.getPARTY_STATES());
                    params.put("REACH_THE_SITUATION", customerMeeting.getREACH_THE_SITUATION());
                    params.put("BUSINESS_IDX", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("ORGANIZATION_IDX", customerMeeting.getIDX());
                    params.put("LINE", customerMeeting.getLINE());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPartyVisitInsert);
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
     * 上传客户地址
     */
    public boolean GetVisitConfirmCustomer(final String partyName, final String partyAddress, final String contactName, final String contactTel) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitConfirmCustomer, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerMeetingCreateActivityBiz.this.getClass() + ".GetVisitConfirmCustomer:" + response);
                    customerMeeting.setPARTY_NAME(partyName);
                    customerMeeting.setPARTY_ADDRESS(partyAddress);
                    customerMeeting.setCONTACTS(contactName);
                    customerMeeting.setCONTACTS_TEL(contactTel);
                    getVisitConfirmCustomerSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerMeetingCreateActivityBiz.this.getClass() + ".GetVisitConfirmCustomer:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.updataPartyError("上传修改失败!");
                    } else {
                        mActivity.updataPartyError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserIDX", MyApplication.getInstance().getUser().getIDX());
                    params.put("strPartyName", partyName);
                    params.put("strAddress", partyAddress);
                    params.put("strContacts", contactName);
                    params.put("strContactsTel", contactTel);
                    params.put("strBussenIdx", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strPartyCode", customerMeeting.getPARTY_NO());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetVisitConfirmCustomer);
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
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetPartyVisitInsert, mTagGetVisitConfirmCustomer);
    }

}










