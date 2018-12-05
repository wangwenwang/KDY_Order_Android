package com.kaidongyuan.app.kdyorder.model;

import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.AddStockResult;
import com.kaidongyuan.app.kdyorder.bean.AppStock;
import com.kaidongyuan.app.kdyorder.bean.AppStockResult;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.bean.PromotionOrder;
import com.kaidongyuan.app.kdyorder.bean.StockProduct;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.InventoryConfirmActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderConfirmActivity;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.DoubleArithUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.OrderUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/9.
 * 库存登记表确认界面的业务类
 */
public class InventoryConfirmActivityBiz {

    InventoryConfirmActivity mActivity;

    /**
     * 提交订单时的网络请求标记
     */
    private final String mTagConfrim = "mTagConfrim";
    /**
     * 选择的产品信息,从服务器获取到的
     */

    private AddStockResult addStockResult;
    private List<AppStockResult> appStockResults;

    public InventoryConfirmActivityBiz(InventoryConfirmActivity activity) {
        this.mActivity = activity;
    }




    /**
     * 提交订单
     * 2016.10.27 Tom 修改重新提交最终订单时赠品条目重复的问题
     * @return 发送请求是否成功
     */
    public boolean confirm() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.AddStock, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(InventoryConfirmActivityBiz.this.getClass() + "confirmSuccess:" + response);
                    confirmSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(InventoryConfirmActivityBiz.this.getClass() + "confirmError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getDataError("提交订单失败!");
                    } else {
                        mActivity.getDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String strAddStockResult=JSON.toJSONString(addStockResult);
                    params.put("reuslt",strAddStockResult);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagConfrim);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 提交订单成功返回信息
     *
     * @param response 返回的信息
     */
    private void confirmSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type ==1) {
                mActivity.confirmSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("提交失败!");
        }
    }

    /**
     * 设置提交订单的订单信息
     * @param choiceStockProduct 用户在上一个商品选择界面选择的商品
     * @param partyid 客户ID
     * @param STOCK_DATE 用户选择的库存月份
     * @param SUBMIT_DATE 用户选择的填表日期
     */
    public void setConfirmData(List<StockProduct> choiceStockProduct, String partyid, String STOCK_DATE, String SUBMIT_DATE) {
        try {
            addStockResult = new AddStockResult();
            addStockResult.setBUSINESS_IDX(MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
            addStockResult.setPARTY_IDX(partyid);
            addStockResult.setSTOCK_DATE(STOCK_DATE);
            addStockResult.setSUBMIT_DATE(SUBMIT_DATE);
            addStockResult.setUSER_NAME(MyApplication.getInstance().getUser().getUSER_NAME());
            appStockResults = new ArrayList<>();
            if (choiceStockProduct==null||partyid==null||STOCK_DATE==null||SUBMIT_DATE==null){
                Toast.makeText(mActivity,"数据有误，请返回首页重新添加",Toast.LENGTH_SHORT).show();
                return;
            }else {
                for (int i=0;i<choiceStockProduct.size();i++){
                    for (int j=0;j<choiceStockProduct.get(i).getPRODUCT_POLICY().size();j++){
                        AppStockResult appStockResult=new AppStockResult();
                        appStockResult.setPRODUCT_NO(choiceStockProduct.get(i).getPRODUCT_NO());
                        appStockResult.setPRODUCT_IDX(choiceStockProduct.get(i).getIDX()+"");
                        appStockResult.setPRODUCT_NAME(choiceStockProduct.get(i).getPRODUCT_NAME());
                        appStockResult.setPRODUCTION_DATE(choiceStockProduct.get(i).getPRODUCT_POLICY().get(j).getPRODUCTION_DATE());
                        appStockResult.setSTOCK_QTY(choiceStockProduct.get(i).getPRODUCT_POLICY().get(j).getSTOCK_QTY()+"");
                        appStockResults.add(appStockResult);
                    }
                }
                JSONArray jsonArray=new JSONArray();
                jsonArray.addAll(appStockResults);
                JSONObject jo=new JSONObject();
                jo.put("result",jsonArray);
                addStockResult.setAppStock(jo);
            }

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagConfrim);
    }


    public AddStockResult getAddStockResult() {
        return addStockResult;
    }

    public List<AppStockResult> getAppStockResults() {
        return appStockResults;
    }



}
















