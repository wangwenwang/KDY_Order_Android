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
import com.kaidongyuan.app.kdyorder.bean.PartyProductStock;
import com.kaidongyuan.app.kdyorder.bean.ProductStock;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.ProductStockDetailActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/19.
 */
public class ProductStockDetailActivityBiz {
    private ProductStockDetailActivity mActivity;
    private List<ProductStock> productStocks;

    /**
     * 网络请求支付类型是的标记
     */
    private final String mTagGetStockNoList="mTagGetStockNoList";
    public ProductStockDetailActivityBiz(ProductStockDetailActivity mActivity) {
        this.mActivity = mActivity;
        productStocks=new ArrayList<>();
    }

    public List<ProductStock> getProductStocks() {
        return productStocks;
    }

    /**
     * 获取产品库存批次数据
     * @return 发送请求是否成功
     */
    public boolean getProductStocksData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetStockNoList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(ProductStockDetailActivityBiz.this.getClass() + ".getProductDataSuccess:" + response);
                    getProductStocksDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(ProductStockDetailActivityBiz.this.getClass() + ".getProductError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getProductError("获取库存批次数量失败!");
                    } else {
                        mActivity.getProductError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("IDX", mActivity.idx);//PartyProductStock.IDX
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetStockNoList);
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

    private void getProductStocksDataSuccess(String response) {
        try {
            JSONObject jsonObject= JSON.parseObject(response);
            int type = jsonObject.getInteger("type");
            if (type == 1) {
                JSONObject jo=jsonObject.getJSONObject("result");
                productStocks = JSON.parseArray(jo.getString("Info"), ProductStock.class);
                mActivity.getProductSuccess();
            } else {
                mActivity.getProductError(jsonObject.getString("msg"));
            }
        }catch (Exception ex){
            mActivity.getProductError("GetStockNoList，网络数据返回值异常");
            ExceptionUtil.handlerException(ex);
        }

    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetStockNoList);
    }

}
