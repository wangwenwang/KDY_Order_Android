package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.adapter.PreOrderListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OrderPlanSimple;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.CheckOrderFragmentCancelOrderPager;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.CheckPreOrderFragmentCancelOrderPager;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/19.
 * 在途订单界面的业务类
 */
public class CheckPreOrderFragmentCancelOrderPagerBiz {

    private CheckPreOrderFragmentCancelOrderPager mPager;

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
    private final int mPageSize = 20;
    /**
     * 保存已取消订单数据的集合
     */
    private List<OrderPlanSimple> mOrderList;
    /**
     * 网络请求订单数据是的标记
     */
    private final String TAG_GET_CANCELORDER_DATA = "TAG_GET_CANCELORDER_DATA";

    public CheckPreOrderFragmentCancelOrderPagerBiz(CheckPreOrderFragmentCancelOrderPager page) {
        try {
            this.mPager = page;
            mOrderList = new ArrayList<>();
            Logger.w(this.getClass() + ":onCreage");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初次加载，只加载第一页
     *
     * @return 发送请求是否成功
     */
    public boolean getCancelOrderData() {
        try {
            mPageIndex = mInitPagerIndex;
            return getOrderData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 刷新已取消订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean reFreshCancelOrderData() {
        try {
            mPageIndex = mInitPagerIndex;
            return getOrderData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 加载跟多的已取消订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreCancelOrderData() {
        try {
            return getOrderData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 获取已取消订单数据
     *
     * @return 发送请求是否成功
     */
    private boolean getOrderData() {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetOrderPlanList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CheckPreOrderFragmentCancelOrderPagerBiz.this.getClass() + ".getOrdersDataSuccess:" + response);
                    getOrderDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CheckPreOrderFragmentCancelOrderPagerBiz.this.getClass() + ".getOrdersDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mPager.loginError("获取订单失败!");
                    } else {
                        mPager.loginError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("strPartyType", "");
                    params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strPartyId", "");
                    params.put("strStartDate", "");
                    params.put("strEndDate", "");
                    params.put("strState", "CANCEL");
                    params.put("strPage", mPageIndex + "");
                    params.put("strPageCount", mPageSize + "");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(TAG_GET_CANCELORDER_DATA);
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
     * @param response 返回的数据
     */
    private void getOrderDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex) {//刷新或初次加载，清除集合中的数据
                    mOrderList.clear();
                }
                List<OrderPlanSimple> tmpOrderList = JSON.parseArray(object.getString("result"), OrderPlanSimple.class);
                mOrderList.addAll(tmpOrderList);
                mPager.loginSuccess();
                mPageIndex++;
            } else {
                mPager.loginError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mPager.loginError("获取订单失败!");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(TAG_GET_CANCELORDER_DATA);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取已取消订数据单集合
     * @return 在已取消单数据
     */
    public List<OrderPlanSimple> getOrderList() {
        try {
            return mOrderList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }


}















