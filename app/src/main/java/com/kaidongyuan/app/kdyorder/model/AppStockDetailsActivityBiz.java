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
import com.kaidongyuan.app.kdyorder.bean.AppStock;
import com.kaidongyuan.app.kdyorder.bean.Fleet;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.AppStockDetailsActivity;
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
 * Created by Administrator on 2017/6/7.
 */
public class AppStockDetailsActivityBiz {
    private AppStockDetailsActivity mActivity;
    private String stockIdx;
    /**
     * 保存Appstock数据的集合
     */
    private List<AppStock> mAppStocks;
    /**
     * 网络请求客户库存登记表时的标记
     */
    private final String TAG_GetAppStockList = "GetAppStockList";
    private Fleet tmpFleet;

    public AppStockDetailsActivityBiz(AppStockDetailsActivity mActivity, String stockIdx) {
       try {
           this.mActivity = mActivity;
           this.stockIdx=stockIdx;
           mAppStocks=new ArrayList<>();
           Logger.w(this.getClass() + ":onCreate");
       }catch (Exception ex){
           ex.printStackTrace();
       }

    }

    public List<AppStock> getmAppStocks() {
        return mAppStocks;
    }

    public Fleet getTmpFleet() {
        return tmpFleet;
    }

    public boolean cancelStock(){
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.CancelStock, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(AppStockDetailsActivityBiz.this.getClass() + "getStockData.Success:" + response);
                    cancelResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(AppStockDetailsActivityBiz.this.getClass() + "getStockData.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getStockError("撤销失败!");
                    } else {
                        mActivity.getStockError("撤销失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("StockIdx", stockIdx);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(TAG_GetAppStockList);
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

    private void cancelResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mActivity.cancelSuccess();
            } else {
                mActivity.getStockError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getStockError("撤销失败!");
        }
    }


    public boolean getStockData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetAppStockList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(AppStockDetailsActivityBiz.this.getClass() + "getStockData.Success:" + response);
                       getDataResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(AppStockDetailsActivityBiz.this.getClass() + "getStockData.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getStockError("获取客户库存详情登记表失败!");
                    } else {
                        mActivity.getStockError("获取失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("StockIdx", stockIdx);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(TAG_GetAppStockList);
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

    private void getDataResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mAppStocks.clear();
                JSONObject jo= JSON.parseObject(object.getString("result"));
                tmpFleet=JSON.parseObject(jo.getString("Stock"),Fleet.class);
                mAppStocks=JSON.parseArray(jo.getString("AppStock"),AppStock.class);
                mActivity.getStockSuccess();

            } else {
                mActivity.getStockError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getStockError("获取客户库存详情登记表失败!");
        }
    }

}
