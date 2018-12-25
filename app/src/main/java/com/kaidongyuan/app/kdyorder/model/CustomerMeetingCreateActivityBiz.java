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

                // 服务器刚刚返回的『客户拜访IDX』写入传递的模型里
                JSONArray VISIT_IDXs = object.getJSONArray("result");
                JSONObject VISIT_IDXo = (JSONObject) VISIT_IDXs.get(0);
                String VISIT_IDX = VISIT_IDXo.getString("VISIT_IDX");
                CustomerMeeting customerM = this.getCustomerMeeting();
                customerM.setVISIT_IDX(VISIT_IDX);
                this.setCustomerMeeting(customerM);

                mActivity.getPartyVisitInsertSuccess();
            } else {
                mActivity.updataError(response);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError(response);
        }
    }

    /**
     * 处理网络请求返回修改客户信息成功
     *
     * @param response 返回的数据
     */
    private void getVisitConfirmCustomerSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            if (type == 1) {

                mActivity.getVisitConfirmCustomerSuccess(msg);

            } else {
                mActivity.updataPartyError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError("修改客户信息失败!");
        }
    }

    /**
     * 确认客户信息
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
                        mActivity.updataError("确认客户信息失败!");
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
                    params.put("USER_NAME", MyApplication.getInstance().getUser().getUSER_NAME()); // 业代姓名？
                    params.put("USER_NO", MyApplication.getInstance().getUser().getUSER_CODE());   // 业代编号？
                    params.put("CHANNEL", customerMeeting.getCHANNEL());
                    params.put("PARTY_LEVEL", customerMeeting.getPARTY_LEVEL());
                    params.put("WEEKLY_VISIT_FREQUENCY", customerMeeting.getWEEKLY_VISIT_FREQUENCY());
                    params.put("VISIT_DATE", customerMeeting.getVISIT_DATE());
                    params.put("OPERATOR_IDX", MyApplication.getInstance().getUser().getIDX());
                    params.put("PARTY_STATES", customerMeeting.getPARTY_STATES());
                    params.put("REACH_THE_SITUATION", customerMeeting.getREACH_THE_SITUATION());
                    params.put("BUSINESS_IDX", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("ORGANIZATION_IDX", "2");
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
     * 修改客户信息
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
                        mActivity.updataPartyError("修改客户信息失败!");
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










