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
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.NewestInformationDetailsActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/24.
 * 资讯详情界面的业务类
 */
public class NewestInformationDetailsActivityBiz {

    private NewestInformationDetailsActivity mActivity;

    /**
     * 保存资讯详情
     */
    private Information mInformation;
    /**
     * 网络请求资讯详情的标记
     */
    private final String mTagGetInformationDetailsData = "mTagGetInformationDetailsData";

    public NewestInformationDetailsActivityBiz(NewestInformationDetailsActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 网络获取资讯详情
     * @return 发送请求是否成功
     * @param id 后台查找资讯的 id
     */
    public boolean getInformationDetailsData(final String id) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_INFORMATION_DETAILS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(NewestInformationDetailsActivityBiz.this.getClass() + "getInformationDetailsDataSuccess:" + response);
                    getInformationDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(NewestInformationDetailsActivityBiz.this.getClass() + "getInformationDetailsDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getInformationDetailsDataError("获取资讯详情失败！");
                    } else {
                        mActivity.getInformationDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strLicense", "");
                    params.put("strNewsId", id);
                    return params;
                }
            };
            request.setTag(mTagGetInformationDetailsData);
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
     * 获取资讯详情成功返回数据
     * @param response 返回的信息
     */
    private void getInformationDetailsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                List<Information> notifies = JSON.parseArray(object.getString("result"), Information.class);
                if (notifies.size() > 0) {
                    mInformation = notifies.get(0);
                    mActivity.getInformationDetailsDataSuccess();
                } else {
                    mActivity.getInformationDetailsDataError("获取资讯详情失败，无内容！");
                }
            } else {
                String msg = object.getString("msg");
                mActivity.getInformationDetailsDataError(msg);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getInformationDetailsDataError("获取资讯详情失败！");
        }
    }

    /**
     * 获取资讯详情信息
     * @return 资讯详情
     */
    public Information getmInformation() {
        return mInformation;
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetInformationDetailsData);
    }

}








