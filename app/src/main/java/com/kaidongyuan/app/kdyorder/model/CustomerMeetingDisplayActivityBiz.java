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
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingDisplayActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingsActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMeetingDisplayActivityBiz {

    private CustomerMeetingDisplayActivity mActivity;
    /**
     * 保存客户拜访生动化陈列类型集合
     */
    private List<CustomerMeetingLine> meetingDisplay;

    private final String mTagVividDisplayCBX = "mTagVividDisplayCBX";

//    public List<CustomerMeetingLine> getMeetingDisplay() {
//        return meetingDisplay;
//    }


    public List<CustomerMeetingLine> getMeetingDisplay() {
        return meetingDisplay;
    }

    public void setMeetingDisplay(List<CustomerMeetingLine> meetingDisplay) {
        this.meetingDisplay = meetingDisplay;
    }

    public CustomerMeetingDisplayActivityBiz(CustomerMeetingDisplayActivity activity) {
        this.mActivity = activity;
        meetingDisplay = new ArrayList<>();
    }
    /**
     * 获取客户拜访线路类型
     */
    public boolean VividDisplayCBX() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.VividDisplayCBX, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".VividDisplayCBX----:" + response);
                    VividDisplayCBXSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".VividDisplayCBX:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.VividDisplayCBXError("获取生动化陈列|请求失败!");
                    } else {
                        mActivity.VividDisplayCBXError("请检查网络是否正常连接！");
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
            request.setTag(mTagVividDisplayCBX);
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
    private void VividDisplayCBXSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                meetingDisplay = new ArrayList<>();
                if (object.containsKey("result")) {
                    meetingDisplay = JSON.parseArray(object.getString("result"), CustomerMeetingLine.class);
                }
                mActivity.VividDisplayCBXSuccess(meetingDisplay);

            } else {
                mActivity.VividDisplayCBXError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.VividDisplayCBXError("获取生动化陈列|服务器返回数据异常!");
        }
    }
}
