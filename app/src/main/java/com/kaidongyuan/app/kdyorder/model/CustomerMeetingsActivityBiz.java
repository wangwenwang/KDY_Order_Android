package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
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
public class CustomerMeetingsActivityBiz {

    private CustomerMeetingsActivity mActivity;

    /**
     * 默认获取数据是为第一页
     */
    private final int mInitPagerIndex = 1;
    /**
     * 分页加载时加载的页数
     */
    private int mPageIndex = mInitPagerIndex;
    /**
     * 分页加载时每页加载的数据数量
     */
    public final int mPageSize = 20;
    /**
     * 保存客户拜访线路类型集合
     */
    private List<CustomerMeetingLine> meetingLines;

    /**
     * 保存客户拜访数据集合
     */
    private List<CustomerMeeting> customerMeetingList;
    private final String mTagGetPartyVisitLine = "mTagGetPartyVisitLine";
    private final String mTagGetMeetingList = "mTagGetMeetingList";

    public CustomerMeetingsActivityBiz(CustomerMeetingsActivity activity) {
        this.mActivity = activity;
        customerMeetingList = new ArrayList<>();
    }

    /**
     * 获取客户拜访线路类型
     */
    public boolean GetPartyVisitLines() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyVisitLine, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerMeetingsActivityBiz.this.getClass() + ".GetPartyVisitLine----:" + response);
                    GetCustomerMeetingLinesSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerMeetingsActivityBiz.this.getClass() + ".GetPartyVisitLine:" + error.toString());
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
     * 获取客户拜访列表数据
     *
     * @return 发送请求是否成功
     */
    private boolean GetCustomerMeetingDatas() {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyVisitList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CustomerMeetingsActivityBiz.this.getClass() + ".GetPartyVisitList:" + response);
                    GetCustomerMeetingDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CustomerMeetingsActivityBiz.this.getClass() + ".GetPartyVisitList:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getMeetingsDataError("获取客户拜访数据失败!");
                    } else {
                        mActivity.getMeetingsDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserID", MyApplication.getInstance().getUser().getIDX());
                    params.put("strSearch", mActivity.strSearch);
                    params.put("strLine", mActivity.strLine);
                    params.put("strStates", mActivity.strState);
                    params.put("strPage", mPageIndex + "");
                    params.put("strPageCount", mPageSize + "");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetMeetingList);
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
     * 刷新列表数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean reFreshCustomerMeetingDatas() {
        try {
            mPageIndex = mInitPagerIndex;
            return GetCustomerMeetingDatas();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    public List<CustomerMeetingLine> getMeetingLines() {
        return meetingLines;
    }

    public void setMeetingLines(List<CustomerMeetingLine> meetingLines) {
        this.meetingLines = meetingLines;
    }

    /**
     * 加载更多数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreCustomerMeetingDatas() {
        try {
            return GetCustomerMeetingDatas();
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
    private void GetCustomerMeetingLinesSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                meetingLines = new ArrayList<>();
                if (object.containsKey("result")) {
                    meetingLines = JSON.parseArray(object.getString("result"), CustomerMeetingLine.class);
                }
                // 添加『全部』
                CustomerMeetingLine allLine = new CustomerMeetingLine();
                allLine.setIDX("10086");
                allLine.setITEM_NAME("全部");
                meetingLines.add(allLine);
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
     * 处理网络请求返回数据成功
     *
     * @param response 返回的数据
     */
    private void GetCustomerMeetingDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex && customerMeetingList != null) {//刷新或初次加载，清除集合中的数据
                    customerMeetingList.clear();
                }
                List<CustomerMeeting> tmpCustomerMeetings = new ArrayList<>();
                if (object.containsKey("result")) {
                    tmpCustomerMeetings = JSON.parseArray(object.getString("result"), CustomerMeeting.class);
                }

                customerMeetingList.addAll(tmpCustomerMeetings);
                mActivity.getMeetingsDataSuccess();
                mPageIndex++;
            } else {
                mActivity.getMeetingsDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getMeetingsDataError("获取客户拜访数据失败!");
        }
    }


    public List<CustomerMeeting> getCustomerMeetingList() {
        return customerMeetingList;
    }


    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetMeetingList);
    }

}










