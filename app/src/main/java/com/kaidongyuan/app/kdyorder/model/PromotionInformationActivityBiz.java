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
import com.kaidongyuan.app.kdyorder.bean.ProductPolicy;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.PromotionInformationActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/3.
 * 促销信息界面的业务类
 */
public class PromotionInformationActivityBiz {

    private PromotionInformationActivity mActivity;

    /**
     * 获取促销产品数据信息是的网络标记
     */
    private final String mTagGetPromotionInformationData = "mTagGetPromotionInformationData";
    /**
     * 保存促销信息集合
     */
    private List<ProductPolicy> mPromotionInformationsList;

    public PromotionInformationActivityBiz(PromotionInformationActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取促销产品信息数据
     *
     * @param mStrPartyId party id
     * @return 发送请求是否成功
     */
    public boolean getPromotionInformatinData(final String mStrPartyId) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_PROMOTION_INFORMATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PromotionInformationActivityBiz.this.getClass() + "getPromotionInformatinDataSuccess:" + response);
                    getPromotionInformatinDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PromotionInformationActivityBiz.this.getClass() + "getPromotionInformatinDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getPromotionInformationDataError("获取促销信息失败！");
                    } else {
                        mActivity.getPromotionInformationDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strPartyIdx", mStrPartyId);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPromotionInformationData);
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
     * 获取促销信息成功返回数据
     *
     * @param response 返回的信息
     */
    private void getPromotionInformatinDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mPromotionInformationsList = JSON.parseArray(object.getString("result"), ProductPolicy.class);
                mActivity.getPromotionInformationDataSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getPromotionInformationDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getPromotionInformationDataError("获取促销信息失败！");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetPromotionInformationData);
    }

    /**
     * 获取促销信息集合
     *
     * @return 促销信息集合
     */
    public List<ProductPolicy> getPromotionInformations() {
        return mPromotionInformationsList;
    }
}











