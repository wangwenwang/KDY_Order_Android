package com.kaidongyuan.app.kdyorder.model;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrder;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.bean.PromotionOrder;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderConfirmActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.OutPutOrderConfirmActivity;
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
 * Created by Administrator on 2016/6/4.
 * 订单确认界面的业务类
 */
public class OutPutOrderConfirmActivityBiz {

    OutPutOrderConfirmActivity mActivity;
    /**
     * 获取促销信息是的网络请求标记
     */
    private final String mTagGetPromotionData = "mTagGetPromotionData";
    /**
     * 提交订单时的网络请求标记
     */
    private final String mTagConfrim = "mTagConfrim";
    /**
     * 从服务器获取的整个订单信息
     */
    public PromotionOrder mOrder;
    /**
     * 后台返回的赠品信息集合
     */
    private List<PromotionDetail> mPromotions;
    /**
     * 选择的产品信息,从服务器获取到的
     */
    private List<PromotionDetail> mSelectProducts;
    private List<PromotionDetail> selectProducts;

    public OutPutOrderConfirmActivityBiz(OutPutOrderConfirmActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取促销信息
     *
     * @param submitString 提交的订单信息
     * @return 发送请求是否成功
     */
    public boolean getPromotionData(final String submitString) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.SUBMIT_ORDER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OutPutOrderConfirmActivityBiz.this.getClass() + "getPromotionDataSuccess:" + response);
                    getPromotionDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OutPutOrderConfirmActivityBiz.this.getClass() + "getPromotionDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getDataError("获取促销信息失败!");
                    } else {
                        mActivity.getDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderInfo", submitString);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPromotionData);
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
     * 将选择的商品以后台要求的类型转换成字符串
     *
     * @return PromotionOrder实体转成Json
     */
    public String getSubmitString(List<Product> choicedProducts) {
        try {
            Intent intent = mActivity.getIntent();
            selectProducts = new ArrayList<>();
            Product product;
            int size = choicedProducts.size();
            long idx = Long.parseLong(MyApplication.getInstance().getUser().getIDX());
            for (int i = 0; i<size; i++) {//产品价格为 PRODUCT_CURRENT_PRICE 是根据用户选择支付类型设置的价格
                try {
                    product = choicedProducts.get(i);
                    selectProducts.add(OrderUtil.getPromotionDetailByProduct(product, BusinessConstants.PRODUCT_TYPE_NORMAL,
                            i + 1, product.getCHOICED_SIZE(), idx, product.getPRODUCT_CURRENT_PRICE(),product.getPRODUCT_UOM()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            PromotionOrder promotionOrder = new PromotionOrder();
            promotionOrder.OrderDetails = selectProducts;
            promotionOrder.PAYMENT_TYPE = intent.getStringExtra(EXTRAConstants.ORDER_PAYMENT_TYPE);
            promotionOrder.PARTY_IDX = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
            promotionOrder.TO_CODE = intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_CODE);
            promotionOrder.TO_IDX = Long.parseLong(intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX));
            promotionOrder.ENT_IDX = BusinessConstants.ENT_IDX;
            promotionOrder.BUSINESS_IDX = MyApplication.getInstance().getBusiness().getBUSINESS_IDX();
            promotionOrder.OPERATOR_IDX = Long.parseLong(MyApplication.getInstance().getUser().getIDX());
            return JSON.toJSONString(promotionOrder);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }

    /**
     * 获取促销信息成功返回数据
     *
     * @param response 返回的信息
     */
    private void getPromotionDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mOrder = JSON.parseObject(object.getString("result"), PromotionOrder.class);
                for (PromotionDetail detail : mOrder.OrderDetails) {
                    detail.OPERATOR_IDX = Long.parseLong(MyApplication.getInstance().getUser().getIDX());
                }
                mPromotions = getPromotions(mOrder.OrderDetails, false);
                mSelectProducts = getPromotions(mOrder.OrderDetails, true);
                mActivity.getPromotionDataSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("获取促销信息失败!");
        }
    }

    /**
     * 提交订单
     * 2016.10.27 Tom 修改重新提交最终订单时赠品条目重复的问题
     * @return 发送请求是否成功
     */
    public boolean confirm() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.SaveOutput, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OutPutOrderConfirmActivityBiz.this.getClass() + "confirmSuccess:" + response);
                    confirmSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OutPutOrderConfirmActivityBiz.this.getClass() + "confirmError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getDataError("提交单失败!");
                    } else {
                        mActivity.getDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    OutPutOrder outPutOrder=new OutPutOrder();
                    outPutOrder.Info=new OutPutOrder.OutPutInfo();
                    outPutOrder.Info.Result=new ArrayList<>();
                    for (int i=0;i<mOrder.OrderDetails.size();i++){
                        OutPutOrder.OupputModel model=new OutPutOrder.OupputModel();
                        model.ACT_PRICE=mOrder.OrderDetails.get(i).ACT_PRICE;
                        model.OUTPUT_VOLUME=mOrder.OrderDetails.get(i).PO_VOLUME;
                        model.OUTPUT_QTY=mOrder.OrderDetails.get(i).PO_QTY;
                        if (mActivity.strOutputOrderType!=null&&mActivity.strOutputOrderType.equals("output_return")){
                            model.OUTPUT_QTY=-model.OUTPUT_QTY;
                        }
                        model.OUTPUT_UOM=mOrder.OrderDetails.get(i).PO_UOM;
                        model.MJ_PRICE=mOrder.OrderDetails.get(i).MJ_PRICE;
                        model.PRODUCT_NO=mOrder.OrderDetails.get(i).PRODUCT_NO;
                        model.PRODUCT_TYPE=mOrder.OrderDetails.get(i).PRODUCT_TYPE;
                        model.PRODUCT_IDX=mOrder.OrderDetails.get(i).PRODUCT_IDX;
                        model.OUTPUT_WEIGHT=mOrder.OrderDetails.get(i).PO_WEIGHT;
                        model.ORG_PRICE=mOrder.OrderDetails.get(i).ORG_PRICE;
                        model.PRODUCT_WEIGHT=mOrder.OrderDetails.get(i).PO_WEIGHT;
                        model.PRODUCT_VOLUME=mOrder.OrderDetails.get(i).PO_VOLUME;
                        model.PRODUCT_STATE="";
                        model.SALE_REMARK="";
                        model.MJ_REMARK="";
                        model.BATCH_NUMBER="";
                        model.PRODUCT_DESC="";
                        model.PRODUCTION_DATE="";
                        model.OPER_USER=MyApplication.getInstance().getUser().getUSER_NAME();
                        model.PRODUCT_NAME=mOrder.OrderDetails.get(i).PRODUCT_NAME;
                        model.SUM=mOrder.OrderDetails.get(i).ACT_PRICE;
                        outPutOrder.Info.Result.add(model);
                    }
                    outPutOrder.OUTPUT_QTY=mOrder.TOTAL_QTY;
                    outPutOrder.OUTPUT_SUM=mOrder.ACT_PRICE;
                    outPutOrder.ADDRESS_CODE=mActivity.strAddressCode;
                    outPutOrder.ADDRESS_INFO=mActivity.strAddressInfo;
                    outPutOrder.ADD_DATE=mOrder.ADD_DATE;
                    outPutOrder.BUSINESS_IDX=mOrder.BUSINESS_IDX;
                    outPutOrder.ADDRESS_NAME=mActivity.strPartyName;
                    outPutOrder.OUTPUT_WEIGHT=mOrder.TOTAL_WEIGHT;
                    outPutOrder.ADD_USER=MyApplication.getInstance().getUser().getIDX();
                    outPutOrder.ADDRESS_IDX=mActivity.strAddressIDX;
                    outPutOrder.PARTY_CODE=mActivity.strToPartyCode;
                    outPutOrder.PARTY_NAME=mActivity.strToPartyName;
                    outPutOrder.PARTY_INFO=mActivity.strToAddress;
                    if (mActivity.strOutputOrderType!=null&&mActivity.strOutputOrderType.equals("output_return")){
                        outPutOrder.OUTPUT_TYPE="出库退库";
                        outPutOrder.OUTPUT_QTY=-outPutOrder.OUTPUT_QTY;
                    }else if (mActivity.strOutputOrderType!=null&&mActivity.strOutputOrderType.equals("output_other")){
                        outPutOrder.OUTPUT_TYPE="其它出库";
                        outPutOrder.PARTY_CODE="";
                        outPutOrder.PARTY_NAME="";
                        outPutOrder.PARTY_INFO="";
                    }else {
                        outPutOrder.OUTPUT_TYPE="销售出库";
                    }
                    // 如果是客户拜访，参数加上VISIT_IDX
                    if (mActivity.strOutputOrderType!=null&&mActivity.strOutputOrderType.equals("output_visit_sale")){
                        outPutOrder.VISIT_IDX=mActivity.VISIT_IDX;
                    }
                    outPutOrder.OPER_USER=MyApplication.getInstance().getUser().getUSER_NAME();
                    outPutOrder.PRICE=mOrder.ORG_PRICE;
                    outPutOrder.OUTPUT_VOLUME=mOrder.TOTAL_VOLUME;
                    outPutOrder.OUTPUT_DATE="";
                    outPutOrder.ADUT_MARK="";
                    outPutOrder.OUTPUT_NO="";
                    outPutOrder.PARTY_MARK="";
                    outPutOrder.INPUT_NO="";
                    String strorder=JSON.toJSONString(outPutOrder);
                    params.put("result",strorder);
                    params.put("strLicense", "");
                    Log.d("LM", "--" + params);
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
            if (type == 1) {
                mActivity.confirmSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("提交订单失败!");
        }
    }

    /**
     * 设置提交订单的订单信息
     * @param returnGiftData 用户手动添加的赠品集合
     * @param choicedProducts 用户在上一个商品选择界面选择的商品
     * @param tempTotalQTY 商品总数过度值
     * @param date 用户选择的送货时间
     * @param remark 用户填写的备注信息
     */
    public void setConfirmData(List<PromotionDetail> returnGiftData, List<Product> choicedProducts, int tempTotalQTY, Date date, String remark) {
        try {
            PromotionOrder order = getOrder();
            List<PromotionDetail> products = getProducts();
            int productsSize = products.size();
            //判断显示的产品数量与内存中的产品数量和客户从上一个界面选择的产品数量是否相同
            if (productsSize == choicedProducts.size()) {
                double mActPrice = 0;//总现价
                for (int i = 0; i < productsSize; i++) {
                    mActPrice = DoubleArithUtil.add(mActPrice, products.get(i).ACT_PRICE * products.get(i).PO_QTY);
                }
                order.ACT_PRICE = mActPrice;
            } else {
                ToastUtil.showToastBottom("调价策略配置错误，请重新下单", Toast.LENGTH_SHORT);
                return;
            }
            //添加赠品
            if (order.OrderDetails != null && returnGiftData != null&&!order.OrderDetails.containsAll(returnGiftData)&& returnGiftData.size() > 0) {
                order.OrderDetails.addAll(returnGiftData);
            } else if (order.OrderDetails == null && returnGiftData != null&&!order.OrderDetails.containsAll(returnGiftData)&& returnGiftData.size() > 0) {
                order.OrderDetails = returnGiftData;
            }
            //依据手动配置赠品情况，修改订单中的总原价、总体积、总重量和总数目
            if (returnGiftData != null && returnGiftData.size() > 0) {
                double mOrgPrice = 0;
                double mVolume = 0;
                double mWeight = 0;
                int prQty;
                for (PromotionDetail promotionDetail : returnGiftData) {
                    prQty = promotionDetail.PO_QTY;
                    mOrgPrice += (promotionDetail.ORG_PRICE * prQty);
                    mVolume += promotionDetail.PO_VOLUME * prQty;
                    mWeight += promotionDetail.PO_WEIGHT * prQty;
                }
                order.ORG_PRICE += mOrgPrice;
                order.TOTAL_VOLUME += mVolume;
                order.TOTAL_WEIGHT += mWeight;
            }
            if (tempTotalQTY > order.TOTAL_QTY) {
                order.TOTAL_QTY = tempTotalQTY;
            }
            if (date != null) {
                order.REQUEST_DELIVERY = DateUtil.formateWithTime(date);
            }
            order.CONSIGNEE_REMARK = remark;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetPromotionData, mTagConfrim);
    }


    public void setmOrder(PromotionOrder mOrder) {
        this.mOrder = mOrder;
    }
    /**
     * 获取整个订单信息
     * @return 后台返回的订单信息
     */
    public PromotionOrder getOrder() {
        return mOrder;
    }

    /**
     * 获取商品信息
     * @return 已选商品信息集合
     */
    public List<PromotionDetail> getProducts() {
        return mSelectProducts;
    }

    /**
     * 获取赠品信息
     *
     * @return 后台返回赠品信息集合
     */
    public List<PromotionDetail> getPromotions() {
        return mPromotions;
    }


    /**
     * 获取促销的产品列表
     *
     * @param iPromotions PromotionDetail
     * @param isOrigin    true 为获取商品， false 为获取赠品
     * @return PromotionDetail
     */
    private List<PromotionDetail> getPromotions(List<PromotionDetail> iPromotions, boolean isOrigin) {
        try {
            if (iPromotions == null || iPromotions.size() == 0) return null;
            List<PromotionDetail> details = new ArrayList<>();
            for (PromotionDetail promotionDetail : iPromotions) {
                if (promotionDetail.PRODUCT_TYPE != null && (isOrigin ? promotionDetail.PRODUCT_TYPE.equals("NR") : promotionDetail.PRODUCT_TYPE.equals("GF"))) {
                    details.add(promotionDetail);
                }
            }
            return details;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return new ArrayList<>();
        }
    }

    /**
     * 上调价格 0.1元
     *
     * @param dataIndex 在产品集合中的位置
     */
    public void raisePrice(int dataIndex) {
        try {
            PromotionDetail promotionDetail = mSelectProducts.get(dataIndex);
            double currentPrice = promotionDetail.ACT_PRICE;
            double tempPrice = DoubleArithUtil.add(currentPrice, 0.1F);
            tempPrice = DoubleArithUtil.round(tempPrice, 2);
            if (tempPrice <= promotionDetail.LOTTABLE12) {
                promotionDetail.ACT_PRICE = tempPrice;
            } else {
                ToastUtil.showToastBottom("超出产品可调价格上限！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 下调价格 0.1元
     *
     * @param dataIndex 在产品集合中的位置
     */
    public void cutPrice(int dataIndex) {
        try {
            PromotionDetail promotionDetail = mSelectProducts.get(dataIndex);
            double currentPrice = promotionDetail.ACT_PRICE;
            double tempPrice = DoubleArithUtil.sub(currentPrice, 0.1F);
            tempPrice = DoubleArithUtil.round(tempPrice, 2);
            if (tempPrice >= promotionDetail.LOTTABLE13 && tempPrice > 0) {
                promotionDetail.ACT_PRICE = tempPrice;
            } else {
                ToastUtil.showToastBottom("超出产品可调价格下限！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 手动设置产品价格
     *
     * @param dataIndex  在产品集合中的位置
     * @param inputPirce 手动输入的价格
     */
    public void setPrice(int dataIndex, double inputPirce) {
        try {
            inputPirce = DoubleArithUtil.round(inputPirce, 2);
            PromotionDetail promotionDetail = mSelectProducts.get(dataIndex);
            if (inputPirce >= promotionDetail.LOTTABLE13) {
                if (inputPirce <= promotionDetail.LOTTABLE12) {
                    promotionDetail.ACT_PRICE = inputPirce;
                } else {
                    ToastUtil.showToastBottom("输入的价格超出产品可调价格上限！", Toast.LENGTH_SHORT);
                }
            } else {
                ToastUtil.showToastBottom("输入的价格超出产品可调价格下限！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取已选产品的实际付款总价
     *
     * @return 已选产品总价
     */
    public String getActPrice() {
        try {
            double actPrice = 0F;
            for (PromotionDetail promotionDetail : mSelectProducts) {
                actPrice += DoubleArithUtil.mul(promotionDetail.ACT_PRICE, promotionDetail.PO_QTY);
            }
            actPrice = DoubleArithUtil.round(actPrice, 2);
            return String.valueOf(actPrice);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }

    /**
     * 获取选择赠品开始的 LINE_NO
     *
     * @return 选择赠品开始的 LINE_NO
     */
    public int getPromotionNumber() {
        try {
            int lineNumber = 0;
            List<PromotionDetail> orderDetails = getOrder().OrderDetails;
            if (orderDetails != null && orderDetails.size() > 0) {
                for (PromotionDetail promotionDetail : orderDetails) {
                    if (promotionDetail.LINE_NO > lineNumber) {
                        lineNumber = promotionDetail.LINE_NO;
                    }
                }
            }
            return lineNumber;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return 0;
        }
    }

    /**
     * 获取促销价格
     *
     * @param details PromotionDetails
     * @return 赠品价格
     */
    public double getPromotionPrice(List<PromotionDetail> details) {
        try {
            double promotionPrice = 0;
            for (PromotionDetail detail : details) {
                promotionPrice += detail.ORG_PRICE * detail.PO_QTY;
            }
            return promotionPrice;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return 0F;
        }
    }
}
















