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
import com.kaidongyuan.app.kdyorder.bean.BusinessFeeItem;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderInfo;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderProduct;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.OutPutOrderDetailActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/30.
 */
public class OutPutOrderDetailActivityBiz {
    private OutPutOrderDetailActivity mActivity;
    private String orderIdx;
    /**
     * 网络请求订单详情数据的标记
     */
    private final String mTagGetAppBusinessFeeList="GetAppBusinessFeeList";

    private final String mTagCancelOrder="mTagCancelOrder";

    private final String mTagConfirmOrder="mTagConfirmOrder";
    /**
     * 出库单详情基本信息
     */
    private OutPutOrderInfo info;

    /**
     * 出库单详情产品信息条目
     */
    private List<OutPutOrderProduct> outPutOrderProductList;
    public OutPutOrderDetailActivityBiz(OutPutOrderDetailActivity mActivity, String orderIdx) {
        this.mActivity = mActivity;
        this.orderIdx = orderIdx;
        outPutOrderProductList=new ArrayList<>();
    }

    /**
     * 网络请求出库单详情数据
     * @return 发送请求是否成功
     */
    public boolean getOutPutOrderData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetOupputInfo, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OutPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    getOrderDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OutPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getfeeDetailsDataError("获取出库单详情失败！");
                    } else {
                        mActivity.getfeeDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("OutputIdx", orderIdx);//订单id
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetAppBusinessFeeList);
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
     * 网络请求费用详情数据成功返回信息
     *
     * @param response 返回的信息
     */
    private void getOrderDetailsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONObject jo = JSON.parseObject(object.getString("result"));
                info=JSON.parseObject(jo.getString("Info"),OutPutOrderInfo.class);
                outPutOrderProductList=JSON.parseArray(jo.getString("List"),OutPutOrderProduct.class);
                mActivity.getfeeDetailsDataSuccess();
            } else {
                mActivity.getfeeDetailsDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getfeeDetailsDataError("没有获取到此出库单！");
            mActivity.finish();
        }
    }
    /**
     * 网络请求撤销此出库单
     * @return 发送请求是否成功
     */
    public boolean cancelOutPutOrder() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.OutPutCancel, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OutPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    cancelOutPutOrderSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OutPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.cancelOutPutOrderError("撤销出库单失败，请返回重进！");
                    } else {
                        mActivity.cancelOutPutOrderError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("OutputIdx", orderIdx);//出库单id
                    params.put("OPER_USER",MyApplication.getInstance().getUser().getIDX());//App登录用户id
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagCancelOrder);
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

    private void cancelOutPutOrderSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mActivity.cancelOutPutOrderSuccess();
            } else {
                mActivity.cancelOutPutOrderError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.cancelOutPutOrderError("撤销出库单失败，请返回重进！");
            mActivity.finish();
        }
    }

    /**
     * 网络请求撤销此出库单
     * @return 发送请求是否成功
     */
    public boolean confirmOutPutOrder() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.OutPutWorkflow, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OutPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    confirmOutPutOrderSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OutPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.confirmOutPutOrderError("确认出库单失败，请返回重进！");
                    } else {
                        mActivity.confirmOutPutOrderError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Output_idx", orderIdx);
                    params.put("ADUT_USER",MyApplication.getInstance().getUser().getIDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagCancelOrder);
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

    private void confirmOutPutOrderSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mActivity.confirmOutPutOrderSuccess();
            } else {
                mActivity.confirmOutPutOrderError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.confirmOutPutOrderError("确认此出库单失败，请返回重进！");
            mActivity.finish();
        }
    }

    public OutPutOrderInfo getInfo() {
        return info;
    }

    public List<OutPutOrderProduct> getList(){return outPutOrderProductList;}

    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetAppBusinessFeeList,mTagCancelOrder,mTagConfirmOrder);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
