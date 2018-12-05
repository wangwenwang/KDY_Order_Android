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
import com.kaidongyuan.app.kdyorder.bean.Business;
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.NewestInformationActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/24.
* 最新资讯业务类
 */
public class NewestInformationActivityBiz {

    private NewestInformationActivity mActivity;


    /**
     * 保存最新资讯数据集合
     */
    private List<Information> mInformationList;
    private final String mTagGetNewestInformation = "mTagGetNewestInformation";

    public NewestInformationActivityBiz(NewestInformationActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取最新资讯信息
     * @return 发送网络请求是否成功
     */
    public boolean getInformationData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_NEWEST_INFORMATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(NewestInformationActivityBiz.this.getClass() + ".getInformationDataSuccess:" + response);
                    getInformationDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(NewestInformationActivityBiz.this.getClass() + ".getInformationDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getInformationDataError("获取最新资讯失败!");
                    } else {
                        mActivity.getInformationDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strLicense", "");
                    Business business = MyApplication.getInstance().getBusiness();
                    String id = business.getBUSINESS_IDX();
                    params.put("strBusinessId", id);
                    return params;
                }
            };
            request.setTag(mTagGetNewestInformation);
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
     * 获取最新资讯网络请求成功返回数据
     * @param response 返回的信息
     */
    private void getInformationDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mInformationList = JSON.parseArray(object.getString("result"), Information.class);
                mActivity.getInformationDataSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getInformationDataError(msg);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getInformationDataError("获取最新资讯失败!");
        }
    }

    /**
     * 获取最新资讯信息
     * @return 资讯数据集合
     */
    public List<Information> getmInformationList() {
        return mInformationList==null? new ArrayList<Information>():mInformationList;
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetNewestInformation);
    }

}










