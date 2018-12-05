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
import com.kaidongyuan.app.kdyorder.bean.InPutOrder;
import com.kaidongyuan.app.kdyorder.bean.InPutOrderInfo;
import com.kaidongyuan.app.kdyorder.bean.InPutOrderProduct;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrder;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderInfo;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderProduct;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.InPutOrderDetailActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.OutPutOrderDetailActivity;
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
 * Created by Administrator on 2017/6/30.
 */
public class InPutOrderDetailActivityBiz {
    private InPutOrderDetailActivity mActivity;
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
    private InPutOrderInfo info;

    /**
     * 出库单详情产品信息条目
     */
    private List<InPutOrderProduct> outPutOrderProductList;
    public InPutOrderDetailActivityBiz(InPutOrderDetailActivity mActivity, String orderIdx) {
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
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetInputInfo, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(InPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    getOrderDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(InPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
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
                    params.put("OutputIdx", orderIdx);
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
                info=JSON.parseObject(jo.getString("Info"),InPutOrderInfo.class);
                outPutOrderProductList=JSON.parseArray(jo.getString("List"),InPutOrderProduct.class);
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
                    Logger.w(InPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    cancelOutPutOrderSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(InPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
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
                    params.put("OutputIdx", orderIdx);//strOrderId , 13464
                    params.put("OPER_USER",MyApplication.getInstance().getUser().getIDX());
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
     * @param minfo
     * @param mList
     */
    public boolean confirmOutPutOrder(final InPutOrderInfo minfo, final List<InPutOrderProduct> mList) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.InPutWorkflow, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(InPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    confirmOutPutOrderSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(InPutOrderDetailActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.confirmOutPutOrderError("确认入库单失败，请返回重进！");
                    } else {
                        mActivity.confirmOutPutOrderError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Input_idx",minfo.getIDX());
                    params.put("ADUT_USER",MyApplication.getInstance().getUser().getUSER_NAME());
                    params.put("strLicense","");
//                    InPutOrder inPutOrder=new InPutOrder();
//                    inPutOrder.Info=new InPutOrder.InPutInfo();
//                    inPutOrder.Info.Result=new ArrayList<>();
//                    for (int i=0;i<mList.size();i++){
//                        InPutOrder.InputModel model=new InPutOrder.InputModel();
//                        model.ACT_PRICE=Double.valueOf(mList.get(i).getPRICE());
//                        model.INPUT_VOLUME= Double.valueOf(mList.get(i).getPRODUCT_VOLUME());
//                        model.INPUT_QTY= (int) mList.get(i).getINPUT_QTY();
//                        model.INPUT_UOM=mList.get(i).getINPUT_UOM();
//                        model.MJ_PRICE= Double.valueOf(mList.get(i).getPRICE());
//                        model.PRODUCT_NO=mList.get(i).getPRODUCT_NO();
//                        model.PRODUCT_TYPE=mList.get(i).getPRODUCT_TYPE();
//                        model.PRODUCT_IDX= Long.valueOf(mList.get(i).getPRODUCT_IDX());
//                        model.INPUT_WEIGHT= Double.valueOf(mList.get(i).getPRODUCT_WEIGHT());
//                        model.ORG_PRICE= Double.valueOf(mList.get(i).getPRICE());
//                        model.PRODUCT_WEIGHT=Double.valueOf(mList.get(i).getPRODUCT_WEIGHT());
//                        model.PRODUCT_VOLUME=Double.valueOf(mList.get(i).getPRODUCT_VOLUME());
//                        model.PRODUCT_STATE="";
//                        model.SALE_REMARK="";
//                        model.MJ_REMARK="";
//                        model.BATCH_NUMBER="";
//                        model.PRODUCT_DESC="";
//                        model.PRODUCTION_DATE="";
//                        model.OPER_USER=MyApplication.getInstance().getUser().getUSER_NAME();
//                        model.PRODUCT_NAME=mList.get(i).getPRODUCT_NAME();
//                        model.SUM= Double.valueOf(mList.get(i).getSUM());
//                        inPutOrder.Info.Result.add(model);
//                    }
//                    inPutOrder.INPUT_QTY= Double.valueOf(minfo.getINPUT_QTY());
//                    inPutOrder.INPUT_SUM= Double.valueOf(minfo.getINPUT_SUM());
//                    inPutOrder.ADDRESS_CODE=minfo.getADDRESS_CODE();
//                    inPutOrder.ADDRESS_INFO=minfo.getADDRESS_INFO();
//                    inPutOrder.ADD_DATE=minfo.getADD_DATE();
//                    inPutOrder.BUSINESS_IDX=minfo.getBUSINESS_IDX();
//                    inPutOrder.ADDRESS_NAME=minfo.getADDRESS_NAME();
//                    inPutOrder.INPUT_WEIGHT= Double.valueOf(minfo.getINPUT_WEIGHT());
//                    inPutOrder.ADD_USER=MyApplication.getInstance().getUser().getIDX();
//                    inPutOrder.ADDRESS_IDX=minfo.getADDRESS_IDX();
//                    inPutOrder.SUPPLIER_CODE=minfo.getSUPPLIER_CODE();
//                    inPutOrder.SUPPLIER_NAME=minfo.getSUPPLIER_NAME();
//                    inPutOrder.SUPPLIER_ADDRESS=minfo.getSUPPLIER_ADDRESS();
//                    inPutOrder.INPUT_TYPE=minfo.getINPUT_TYPE();
//                    inPutOrder.OPER_USER=MyApplication.getInstance().getUser().getUSER_NAME();
//                    inPutOrder.PRICE= Double.valueOf(minfo.getINPUT_SUM());
//                    inPutOrder.INPUT_VOLUME= Double.valueOf(minfo.getINPUT_VOLUME());
//                    inPutOrder.INPUT_DATE="";
//                    inPutOrder.ADUT_MARK="";
//                    inPutOrder.OUTPUT_NO="";
//                    inPutOrder.PARTY_MARK="";
//                    inPutOrder.INPUT_NO="";
//                    String strorder=JSON.toJSONString(inPutOrder);
//                    params.put("result",strorder);
//                    params.put("strLicense", "");
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
            mActivity.confirmOutPutOrderError("确认入库单失败，请返回重进！");
            mActivity.finish();
        }
    }

    public InPutOrderInfo getInfo() {
        return info;
    }

    public List<InPutOrderProduct> getList(){return outPutOrderProductList;}

    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetAppBusinessFeeList,mTagCancelOrder,mTagConfirmOrder);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
