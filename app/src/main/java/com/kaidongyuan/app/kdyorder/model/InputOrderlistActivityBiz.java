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
import com.kaidongyuan.app.kdyorder.bean.BillFee;
import com.kaidongyuan.app.kdyorder.bean.InPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.InputOrderListActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.OutputOrderListActivity;
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
 * Created by Administrator on 2017/6/29.
 */
public class InputOrderlistActivityBiz {

    private InputOrderListActivity mActivity;
    /**
     * 获取客户列表的网络请求标记
     */
    private final String mTagGetBillFeeList = "GetAppBillFeeList";
    /**
     * 存放从后台获取的所有客户数据集合
     */
    private List<InPutSimpleOrder> mOutputSimpleOrderList;
    /**
     * 从客户列表界面中所选择的客户id
     */
    private String PARTY_IDX;
    /**
     * 用户选中的账单月份
     */
    private BillFee mSelectedBillFee;
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

    public InputOrderlistActivityBiz(InputOrderListActivity activity, String PARTY_IDX) {
        try {
            this.mActivity = activity;
            this.PARTY_IDX=PARTY_IDX;
            this.mOutputSimpleOrderList = new ArrayList<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 初次加载，只加载第一页
     *
     * @return 发送请求是否成功
     */
    public boolean getinitOutPutOrders() {
        try {
            mPageIndex = mInitPagerIndex;
            return GetOutPutOrders();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 刷新出库订单列表数据
     * @return 发送网络请求是否成功
     */
    public boolean reFreshCompleteOrderData() {
        try {
            mPageIndex = mInitPagerIndex;
            return GetOutPutOrders();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 加载跟多的已完成订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreCompleteOrderData() {
        try {
            return GetOutPutOrders();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }



    /**
     * 获取已完成订单数据
     *
     * @return 发送请求是否成功
     */
    private boolean GetOutPutOrders() {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetInputList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(InputOrderlistActivityBiz.this.getClass() + ".GetInPutOrders:" + response);
                    GetOutPutOrdersSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(InputOrderlistActivityBiz.this.getClass() + ".GetInPutOrders:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("获取订单失败!");
                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("BUSINESS_IDX", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("ADD_USER",mActivity.addressIdx);
                    params.put("strPage", mPageIndex + "");
                    params.put("strPageCount", mPageSize + "");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetBillFeeList);
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
    private void GetOutPutOrdersSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex) {//刷新或初次加载，清除集合中的数据
                    mOutputSimpleOrderList.clear();
                }
                JSONObject jo=JSON.parseObject(object.getString("result"));
                List<InPutSimpleOrder> tmpSimpleOrderList = JSON.parseArray(jo.getString("List"), InPutSimpleOrder.class);
                mOutputSimpleOrderList.addAll(tmpSimpleOrderList);
                mActivity.loginSuccess();
                mPageIndex++;
            } else {
                mActivity.loginError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.loginError("获取订单失败!");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetBillFeeList);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 返回请求到的账单列表集合
     * @return
     */
    public List<InPutSimpleOrder> getmOutputSimpleOrderList() {
        return mOutputSimpleOrderList;
    }
}
