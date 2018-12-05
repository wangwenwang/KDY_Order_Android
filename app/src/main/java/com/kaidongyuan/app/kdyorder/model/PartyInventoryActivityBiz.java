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
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.bean.PartyProductStock;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyInventoryActivity;
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
 * Created by Administrator on 2017/8/18.
 */
public class PartyInventoryActivityBiz {
    private PartyInventoryActivity mActivity;
    /**
     * 存放显示在 ListView 中的产品库存数据集合
     */
    private List<PartyProductStock> mInventoryProductList;
    /**
     * 网络请求支付类型是的标记
     */
    private final String mTagGetPartyStockList = "mTagGetPartyStockList";
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
    private final int mPageSize = 999;

    public PartyInventoryActivityBiz(PartyInventoryActivity mActivity) {
       try {
           this.mActivity=mActivity;
           mInventoryProductList=new ArrayList<>();
       }catch (Exception e){
           ExceptionUtil.handlerException(e);
       }
    }

    /**
     * 初次加载，只加载第一页
     *
     * @return 发送请求是否成功
     */
    public boolean getInventoryProductsData() {
        try {
            mPageIndex = mInitPagerIndex;
            return getProductData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 刷新已完成订单数据
     * @return 发送网络请求是否成功
     */
    public boolean reFreshInventoryProductsData() {
        try {
            mPageIndex = mInitPagerIndex;
            return getProductData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 加载跟多的已完成订单数据
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreInventoryProductsData() {
        try {
            return getProductData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 获取现有库存产品简表数据
     * @return 发送请求是否成功
     */
    private boolean getProductData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyStockList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyInventoryActivityBiz.this.getClass() + ".getProductDataSuccess:" + response);
                    getProductDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyInventoryActivityBiz.this.getClass() + ".getProductError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getProductError("获取库存数据失败!");
                    } else {
                        mActivity.getProductError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("BUSINESS_IDX", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("ADDRESS_IDX", mActivity.address_idx);
                    params.put("strPage", mPageIndex + "");
                    params.put("strPageCount", mPageSize + "");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPartyStockList);
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
        HttpUtil.cancelRequest(mTagGetPartyStockList);
    }
    private void getProductDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex) {//刷新或初次加载，清除集合中的数据
                    mInventoryProductList.clear();
                }
                JSONObject jo=object.getJSONObject("result");
                List<PartyProductStock> tmpPartyProductStockList = JSON.parseArray(jo.getString("List"), PartyProductStock.class);
                mInventoryProductList.addAll(tmpPartyProductStockList);
                mActivity.getProductSuccess();
                mPageIndex++;
            } else {
                mActivity.getProductError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getProductError("获取订单失败!");
        }
    }


    /**
     * 获取库存产品列表集合
     * @return 库存产品列表数据
     */
    public List<PartyProductStock> getInventoryProductList() {
        try {
            return mInventoryProductList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }

}
