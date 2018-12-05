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
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.HotSellProductActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 * 热销产品的 Biz
 */
public class HotSellProductActivityBiz {

    private HotSellProductActivity mActivity;

    /**
     * 网络请求热销产品数据是的标记
     */
    private final String mTagGetHotSellProduct = "mTagGetHotSellProduct";
    /**
     * 热销产品集合
     */
    private List<Product> mHotSellProduct;


    public HotSellProductActivityBiz(HotSellProductActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取热销产品数据
     *
     * @return 发送请求是否成功
     */
    public boolean getHotSellProductData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_HOT_SELL_PRODUCT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(HotSellProductActivityBiz.this.getClass() + ".getHotSellProductData.Success:" + response);
                    getHotSellProductDataResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(HotSellProductActivityBiz.this.getClass() + ".getHotSellProductData.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getHotSellProductDataError("获取热销产品列表失败!");
                    } else {
                        mActivity.getHotSellProductDataError("获取热销产品列表失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    String strPartyId = "2254";
                    params.put("strPartyIdx", strPartyId);
                    params.put("strPartyAddressIdx", "2254");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetHotSellProduct);
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
     * 网络请求热销产品成功返回结果
     *
     * @param response 返回的结果
     */
    private void getHotSellProductDataResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mHotSellProduct = JSON.parseArray(object.getString("result"), Product.class);
                mActivity.getHotSellProductDataSuccess();
            } else {
                mActivity.getHotSellProductDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getHotSellProductDataError("获取热销产品列表失败!");
        }
    }

    /**
     * 获取热销产品集合
     *
     * @return 热销产品集合
     */
    public List<Product> getHotSellProductDataList() {
        return mHotSellProduct == null ? new ArrayList<Product>() : mHotSellProduct;
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetHotSellProduct);
    }

}
