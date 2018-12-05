package com.kaidongyuan.app.kdyorder.model;

import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerChart;
import com.kaidongyuan.app.kdyorder.bean.ProductChart;
import com.kaidongyuan.app.kdyorder.bean.User;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.ChartCheckActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 * 查看报表业务类
 */
public class ChartCheckActivityBiz {

    private ChartCheckActivity mActivity;

    /**
     * 存放报表名集合
     */
    private List<String> mChartNames;
    /**
     * 存放报表数据集合的数据
     */
    private HashMap<String, List> mChartData;
    /**
     * 网络获取客户报表数据是的标记
     */
    private final String mTagGetCustomerChartDataList = "mTagGetCustomerChartDataList";
    /**
     * 网络获取产品报表数据时的标记
     */
    private final String mTagGetProductChartDataList = "mTagGetProductChartDataList";

    public ChartCheckActivityBiz(ChartCheckActivity activity) {
        this.mActivity = activity;
        initData();
    }

    private void initData() {
        try {
            mChartNames = new ArrayList<>();
            mChartNames.add(0, BusinessConstants.CUSTOMER_CHART_NAME);
            mChartNames.add(1, BusinessConstants.PRODUCT_CHART_NAME);
            mChartData = new HashMap<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public List<String> getChartNames() {
        return mChartNames;
    }

    /**
     * 获取报表数据集合
     *
     * @param mCurrentChartIndex 在报表数据数组中的位置
     * @return 对应的报表数据集合
     */
    public List getCurrentChartDataList(int mCurrentChartIndex) {
        try {
            String chartName = mChartNames.get(mCurrentChartIndex);
            List list = mChartData.get(chartName);
            return list == null ? new ArrayList() : list;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return new ArrayList();
        }
    }

    /**
     * 网络获取对应报表数据
     *
     * @param mCurrentChartIndex 在报表名集合中的位置
     * @return 发送请求是否成功
     */
    public boolean getChartDataList(int mCurrentChartIndex) {
        try {
            String chartName = mChartNames.get(mCurrentChartIndex);
            if (BusinessConstants.CUSTOMER_CHART_NAME.equals(chartName)) {//获取客户报表数据
                return getChartDataList(URLCostant.GET_CUSTOMER_CHART_DATA, mTagGetCustomerChartDataList);
            } else if (BusinessConstants.PRODUCT_CHART_NAME.equals(chartName)) {//获取产品报表数据
                return getChartDataList(URLCostant.GET_PRODUCT_CHART_DATA, mTagGetProductChartDataList);
            } else {
                return false;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * * 获取客户报表数据
     *
     * @param url Url
     * @param tag 发送请求是的标记
     * @return 发送请求是否成功
     */
    private boolean getChartDataList(String url, final String tag) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(ChartCheckActivityBiz.this.getClass() + ".getCustomerChartDataList:" + response);
                    getChartDataListSuccess(response, tag);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(ChartCheckActivityBiz.this.getClass() + ".getCustomerChartDataList:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getChartDataError("获取报表数据失败!");
                    } else {
                        mActivity.getChartDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(tag);
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
     * 网络请求报表数据成功返回数据
     *
     * @param response 返回的数据
     */
    private void getChartDataListSuccess(String response, String tag) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//获取报表数据成功
                if (mTagGetCustomerChartDataList.equals(tag)) {//获取的数据为客户报表数据
                    List customerChartDataList = JSON.parseArray(object.getString("result"), CustomerChart.class);
                    mChartData.put(BusinessConstants.CUSTOMER_CHART_NAME, customerChartDataList);
                    mActivity.getCustomerChartDataListSuccess();
                } else if (mTagGetProductChartDataList.equals(tag)) {//获取的数据为产品报表数据
                    List productChartDataList = JSON.parseArray(object.getString("result"), ProductChart.class);
                    mChartData.put(BusinessConstants.PRODUCT_CHART_NAME, productChartDataList);
                    mActivity.getProductChartDataListSuccess();
                } else {
                    mActivity.getChartDataError("获取报表数据失败!");
                }
            } else {//登录失败
                String msg = object.getString("msg");
                mActivity.getChartDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getChartDataError("获取报表数据失败! ");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetProductChartDataList, mTagGetCustomerChartDataList);
    }
}














