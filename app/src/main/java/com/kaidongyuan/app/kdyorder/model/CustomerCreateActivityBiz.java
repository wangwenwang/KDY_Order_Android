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
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.CustomerChannel;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerCreateActivity;
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
public class CustomerCreateActivityBiz {

    private CustomerCreateActivity mActivity;

    /**
     * 获取客户列表的网络请求标记
     */
    private final String mTagGetCustomerList = "mTagGetCustomerList";
    /**
     * 获取客户地址的网络请求标记
     */
    private final String mTagGetCustomerAddressInfo = "mTagGetCustomerAddressInfo";
    /**
     * 存放显示在 ListView 中的客户数据集合
     */
    private List<Party> mCustomerList;
    /**
     * 存放从后台获取的所有客户数据集合
     */
    private List<Party> mTotalCustomerList;
    /**
     * 存放从后台获取的客户的地址集合
     */
    private List<Address> mCustomerAddressList;
    /**
     * 用户选中的下单客户
     */
    private Party mSelectedParty;

    /**
     * 保存客户拜访线路类型集合
     */
    private List<CustomerMeetingLine> meetingLines;

    /**
     * 保存客户渠道集合
     */
    private List<CustomerChannel> channels;
    private final String mTagGetPartyVisitLine="mTagGetPartyVisitLine";
    private final String mTagGetPartyVisitChannel="mTagGetPartyVisitChannel";
    private final String mTagAddParty="mTagAddParty";
    private final String mTagAddPartyAddress="mTagAddPartyAddress";
    private final String mTagObtainPartyCode="mTagObtainPartyCode";
    public CustomerCreateActivityBiz(CustomerCreateActivity activity) {
        this.mActivity = activity;
        meetingLines=new ArrayList<>();
        this.mCustomerList = new ArrayList<>();
    }

    /**
     * 获取客户地址集合
     * @return 客户地址集合
     */
    public List<Address> getmCustomerAddress() {
        return mCustomerAddressList;
    }

    /**
     * 获取用户选择的客户
     * @return 用户选择的客户
     */
    public Party getmSelectedParty() {
        return mSelectedParty;
    }

    /**
     * 获取客户拜访线路类型
     */
    public boolean GetPartyVisitLines(){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyVisitLine, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".GetPartyVisitLine:" + response);
                    GetCustomerMeetingLinesSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".GetPartyVisitLine:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getMeetingLinesError("获取客户拜访数据失败!");
                    } else {
                        mActivity.getMeetingLinesError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserID ", MyApplication.getInstance().getUser().getIDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPartyVisitLine);
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
     * 获取客户渠道列表类型
     */
    public boolean GetPartyVisitChannel(){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyVisitChannel, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".GetPartyVisitLine:" + response);
                    GetCustomerChannelsSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".GetPartyVisitLine:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getMeetingLinesError("获取客户渠道列表失败!");
                    } else {
                        mActivity.getMeetingLinesError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPartyVisitChannel);
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



    public List<CustomerMeetingLine> getMeetingLines() {
        return meetingLines;
    }

    public List<CustomerChannel> getChannels() {
        return channels;
    }

    public void setMeetingLines(List<CustomerMeetingLine> meetingLines) {
        this.meetingLines = meetingLines;
    }
    /**
     * 处理网络请求返回拜访线路成功
     * @param response 返回的数据
     */
    private void GetCustomerChannelsSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                channels=new ArrayList<>();
                if (object.containsKey("result")){
                    channels= JSON.parseArray(object.getString("result"), CustomerChannel.class);
                }

                mActivity.getChannelsSuccess(channels);

            } else {
                mActivity.getChannelsError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getChannelsError("获取客户渠道数据失败!");
        }
    }

    /**
     * 处理网络请求返回客户信息成功
     * @param response 返回的数据
     */
    private void updataPartySuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                if (object.containsKey("result")){
                    JSONObject jo= JSON.parseObject(object.getString("result"));
                    AddAddress(jo.getString("IDX"),jo.getString("strPartyCode"));
                }else {
                    mActivity.updataPartyError(response);
                }
            } else {
                mActivity.updataPartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError("上传客户信息失败!");
        }
    }
    /**
     * 处理网络请求返回客户地址成功
     * @param response 返回的数据
     */
    private void updataPartyAddressSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                mActivity.updataPartySuccess();

            } else {
                mActivity.updataPartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError("上传客户地址失败!");
        }
    }
    /**
     * 处理网络请求返回拜访线路成功
     * @param response 返回的数据
     */
    private void GetCustomerMeetingLinesSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                meetingLines=new ArrayList<>();
                if (object.containsKey("result")){
                    meetingLines= JSON.parseArray(object.getString("result"), CustomerMeetingLine.class);
                }

                mActivity.getMeetingLinesSuccess(meetingLines);

            } else {
                mActivity.getMeetingLinesError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getMeetingLinesError("获取客户拜访数据失败!");
        }
    }

    /**
     * 处理网络请求返回客户渠道成功
     * @param response 返回的数据
     */
    private void GetPartyVisitChannelsSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                meetingLines=new ArrayList<>();
                if (object.containsKey("result")){
                    meetingLines= JSON.parseArray(object.getString("result"), CustomerMeetingLine.class);
                }

                mActivity.getMeetingLinesSuccess(meetingLines);

            } else {
                mActivity.getMeetingLinesError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getMeetingLinesError("获取客户拜访数据失败!");
        }
    }

    /**
     * 上传客户信息
     */
    public boolean AddParty(){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.AddParty, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".AddParty:" + response);
                    updataPartySuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".AddParty:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.updataPartyError("上传客户信息失败!");
                    } else {
                        mActivity.updataPartyError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("PARTY_CODE",mActivity.edPartyCode.getText().toString().trim());
                    params.put("PARTY_NAME",mActivity.edPartyName.getText().toString().trim());
                    params.put("PARTY_CITY",mActivity.ct.getITEM_NAME());
                    params.put("PARTY_REMARK",mActivity.edPartyRemark.getText().toString().trim());
                    params.put("BUSINESS_IDX",MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strLINE",mActivity.linesParmas);
                    params.put("strCHANNEL",mActivity.strChannel);
                    params.put("strLicense", "");
                    Log.d("LM", "接口AddParty|参数: " + params);
                    return params;
                }
            };
            request.setTag(mTagAddParty);
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
    public boolean AddAddress(final String partyId, final String partyCode){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.AddAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".AddAddress:" + response);
                    updataPartyAddressSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".AddAddress:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.updataPartyError("上传客户地址失败!");
                    } else {
                        mActivity.updataPartyError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("PARTY_IDX",partyId);
                    params.put("ADDRESS_CODE",partyCode);
                    params.put("ADDRESS_PROVINCE",mActivity.pv.getITEM_IDX());
                    params.put("ADDRESS_CITY",mActivity.ct.getITEM_IDX());
                    params.put("ADDRESS_AREA",mActivity.ar.getITEM_IDX());
                    params.put("ADDRESS_RURAL",mActivity.ru.getITEM_IDX());
                    params.put("ADDRESS_ADDRESS",mActivity.edAddressDetail.getText().toString().trim());
                    params.put("CONTACT_PERSON",mActivity.edContactPerson.getText().toString().trim());
                    params.put("CONTACT_TEL",mActivity.edContactTel.getText().toString().trim());
                    params.put("ADDRESS_INFO",mActivity.pv.getITEM_NAME()+mActivity.ct.getITEM_NAME()+mActivity.ar.getITEM_NAME()+mActivity.ru.getITEM_NAME()+mActivity.edAddressDetail.getText().toString());
                    params.put("strFatherPartyIDX",mActivity.fatherPartyAddressID);
                    params.put("LONGITUDE",mActivity.longitude);
                    params.put("LATITUDE",mActivity.latitude);
                    params.put("strLicense", "");
                    Log.d("LM", "接口AddAddress|参数: " + params);
                    return params;
                }
            };
            request.setTag(mTagAddPartyAddress);
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
     * 根据业务代码，获取客户编号
     */
    public boolean ObtainPartyCode(final String strBusinessCode, final String strBusinessIDX){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.ObtainPartyCode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".ObtainPartyCode:" + response);
                    ObtainPartyCodeSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerCreateActivityBiz.this.getClass() + ".ObtainPartyCode:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.updataPartyError("无推荐客户代码");
                    } else {
                        mActivity.updataPartyError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strBusinessCode",strBusinessCode);
                    params.put("strBusinessIDX",strBusinessIDX);
                    params.put("strLicense", "");
                    Log.d("LM", "ObtainPartyCode|参数: " + params);
                    return params;
                }
            };
            request.setTag(mTagObtainPartyCode);
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
     * 处理网络请求返回客户地址成功
     * @param response 返回的数据
     */
    private void ObtainPartyCodeSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {

                JSONArray list = object.getJSONArray("result");
                if (list.size() > 0) {

                    JSONObject o = (JSONObject) list.get(0);
                    mActivity.ObtainPartyCodeSuccess(o.getString("PartyCode"));
                }

            } else {
                mActivity.updataPartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.updataPartyError("推荐客户代码失败!");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetPartyVisitLine);
    }

}










