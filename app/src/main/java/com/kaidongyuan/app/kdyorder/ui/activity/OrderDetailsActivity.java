package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.OrderDetailsAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OrderDetails;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.OrderDetailsActivityBiz;
import com.kaidongyuan.app.kdyorder.ui.fragment.CheckOrderFragment;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.OrderUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 * 订单详情界面
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private OrderDetailsActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    /**
     * 订单编号
     */
    private TextView mTextViewOrderNumber;
    /**
     * 订单创建时间
     */
    private TextView mTextViewOrderCreateTime;
    /**
     * 订单客户名称
     */
    private TextView mTextViewOrderCustomerNumber;
    /**
     * 订单客户地址
     */
    private TextView mTextViewOrderCustomerAddress;
    /**
     * 订单起始地址
     */
    private TextView mTextViewOrderStart;
    /**
     * 下单总量
     */
    private TextView mTextViewOrderQuarity;
    /**
     * 订单总重
     */
    private TextView mTextViewOrderWeight;
    /**
     * 订单体积
     */
    private TextView mTextViewOrderVolume;
    /**
     * 订单流程
     */
    private TextView mTextViewOrderFlow;
    /**
     * 订单状态
     */
    private TextView mTextViewOrderState;
    /**
     * 订单支付方式
     */
    private TextView mTextViewOrderPayType;
    /**
     * 无赠品时显示的文本框
     */
    private TextView mTextViewOrderNoRecord;
    /**
     * 订单现价
     */
    private TextView mTextViewOrderOryPrice;
    /**
     * 订单满减价格
     */
    private TextView mTextViewOrderPriceMJ;
    /**
     * 订单付款价格
     */
    private TextView mTextViewOrderActPrice;
    /**
     * 订单备注信息
     */
    private TextView mTextViewOrderRemark;
    /**
     * 货物信息 ListView
     */
    private MyListView mListViewProduct;
    /**
     * 货物信息适配器
     */
    private OrderDetailsAdapter mOrderDetailsAdapter;
    /**
     * 赠品信息 ListView
     */
    private MyListView mListViewPromotion;
    /**
     * 赠品信息适配器
     */
    private OrderDetailsAdapter mPromotionApapter;
    /**
     * 查看物流信息按钮
     */
    private Button mButtonCheckOrderInformation;
    /**
     * 取消订单按钮
     */
    private Button mButtonCancelOrder;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order_details);
            initData();
            setTop();
            initView();
            setListener();
            getOrderTailsData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mBiz.cancelRequest();
            mBiz = null;
            mImageViewGoBack = null;
            this.mTextViewOrderNumber = null;
            this.mTextViewOrderCreateTime = null;
            this.mTextViewOrderCustomerNumber = null;
            this.mTextViewOrderCustomerAddress = null;
            this.mTextViewOrderStart = null;
            this.mTextViewOrderQuarity = null;
            this.mTextViewOrderWeight = null;
            this.mTextViewOrderVolume = null;
            this.mTextViewOrderFlow = null;
            this.mTextViewOrderState = null;
            this.mTextViewOrderPayType = null;
            this.mTextViewOrderNoRecord = null;
            this.mTextViewOrderOryPrice = null;
            this.mTextViewOrderPriceMJ = null;
            this.mTextViewOrderActPrice = null;
            this.mTextViewOrderRemark = null;
            this.mListViewProduct = null;
            this.mListViewPromotion = null;
            this.mButtonCheckOrderInformation = null;
            this.mButtonCancelOrder=null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new OrderDetailsActivityBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setTop () {
        //版本4.4以上设置状态栏透明，界面布满整个界面
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View topView = findViewById(R.id.topview);
            ViewGroup.LayoutParams topParams = topView.getLayoutParams();
            topParams.height = DensityUtil.getStatusHeight()*16/30;
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            this.mTextViewOrderNumber = (TextView) this.findViewById(R.id.tv_order_no);
            this.mTextViewOrderCreateTime = (TextView) this.findViewById(R.id.tv_order_creat_time);
            this.mTextViewOrderCustomerNumber = (TextView) this.findViewById(R.id.tv_customer_name);
            this.mTextViewOrderCustomerAddress = (TextView) this.findViewById(R.id.tv_customer_address);
            this.mTextViewOrderStart = (TextView) this.findViewById(R.id.tv_order_start);
            this.mTextViewOrderQuarity = (TextView) this.findViewById(R.id.tv_order_qty);
            this.mTextViewOrderWeight = (TextView) this.findViewById(R.id.tv_order_weight);
            this.mTextViewOrderVolume = (TextView) this.findViewById(R.id.tv_order_volume);
            this.mTextViewOrderFlow = (TextView) this.findViewById(R.id.tv_order_flow);
            this.mTextViewOrderState = (TextView) this.findViewById(R.id.tv_order_status);
            this.mTextViewOrderPayType = (TextView) this.findViewById(R.id.tv_payment_type);
            this.mTextViewOrderNoRecord = (TextView) this.findViewById(R.id.tv_promotion_no_record);
            this.mTextViewOrderOryPrice = (TextView) this.findViewById(R.id.tv_org_price);
            this.mTextViewOrderPriceMJ = (TextView) this.findViewById(R.id.tv_mj_remark);
            this.mTextViewOrderActPrice = (TextView) this.findViewById(R.id.tv_act_price);
            this.mTextViewOrderRemark = (TextView) this.findViewById(R.id.tv_remark);
            this.mButtonCheckOrderInformation = (Button) this.findViewById(R.id.button_check_info);
            this.mButtonCancelOrder= (Button) this.findViewById(R.id.button_cancel_order);
            this.mListViewProduct = (MyListView) this.findViewById(R.id.lv_product);
            mOrderDetailsAdapter = new OrderDetailsAdapter(this, null);
            mListViewProduct.setAdapter(mOrderDetailsAdapter);
            this.mListViewPromotion = (MyListView) this.findViewById(R.id.lv_promotion);
            mPromotionApapter = new OrderDetailsAdapter(this, null);
            mListViewPromotion.setAdapter(mPromotionApapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mButtonCheckOrderInformation.setOnClickListener(this);
            mButtonCancelOrder.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void getOrderTailsData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID)) {
                orderId = intent.getStringExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID);
                if (mBiz.getOrderDetailsData(orderId)) {
                    showLoadingDialog();
                } else {
                    ToastUtil.showToastBottom("发送网络请求失败！", Toast.LENGTH_SHORT);
                }
            } else {
                ToastUtil.showToastBottom("获取订单详情失败！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback://返回上一个界面
                    this.finish();
                    break;
                case R.id.button_check_info://查看物流信息
                    Intent orderTransInformationIntent = new Intent(this, OrderTransInformationActivity.class);
                    orderTransInformationIntent.putExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID, mBiz.getOrderDetails().getIDX());
                    startActivity(orderTransInformationIntent);
                    break;
                case R.id.button_cancel_order:
                    try {
                        if (mBiz.cancelOrder(mBiz.getOrderDetails().getIDX())){
                            showLoadingDialog();
                        }else {
                            ToastUtil.showToastBottom("发送网络请求失败！", Toast.LENGTH_SHORT);
                        }
                    }catch (Exception ex){
                        ExceptionUtil.handlerException(ex);
                    }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示 Dialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络获取订单详情失败
     *
     * @param message 显示的消息
     */
    public void getOrderDetailsDataError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络获取订单详情成功
     */
    public void getOrderDetailsDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            Order order = mBiz.getOrderDetails();
            if (order != null) {
//                if (OrderUtil.getBusinessType()== BusinessConstants.BUSINESS_TYPE_YIBAO){
//                    if (mButtonCancelOrder.getVisibility()==View.GONE&&mButtonCheckOrderInformation.getVisibility()==View.GONE){
//                        mButtonCheckOrderInformation.setVisibility(order.getORD_STATE().equals("PENDING") ? View.GONE : View.VISIBLE);
//                        mButtonCancelOrder.setVisibility(order.getORD_STATE().equals("PENDING")?View.VISIBLE:View.GONE);
//                    }else {
//                        //当取消订单后刷新页面将
//                        mButtonCancelOrder.setVisibility(View.GONE);
//                        mButtonCheckOrderInformation.setVisibility(View.GONE);
//                    }
//                }else {
                    mButtonCheckOrderInformation.setVisibility(order.getORD_STATE().equals("PENDING") ? View.GONE : View.VISIBLE);
                    mButtonCancelOrder.setVisibility(View.GONE);
//                }


                List<OrderDetails> orderDetails = order.getOrderDetails();
                if (orderDetails != null && orderDetails.size() > 0) {
                    mOrderDetailsAdapter.notifyChange(getPickedOrderDetails(orderDetails, false));
                } else {
                    ToastUtil.showToastBottom("商品信息为空！", Toast.LENGTH_SHORT);
                }

                List<OrderDetails> promotionDetails = getPickedOrderDetails(orderDetails, true);
                if (promotionDetails == null || promotionDetails.size() <= 0) {
                    mTextViewOrderNoRecord.setVisibility(View.VISIBLE);
                } else {
                    mTextViewOrderNoRecord.setVisibility(View.GONE);
                    mPromotionApapter.notifyChange(promotionDetails);
                }

                mTextViewOrderRemark.setText("\t" + (order.getORD_REMARK_CONSIGNEE() == null || order.getORD_REMARK_CONSIGNEE().equals("") ? "无" :
                        order.getORD_REMARK_CONSIGNEE()));

                String orderNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_NO());
                mTextViewOrderNumber.setText(orderNumber);

                String orderCreateTime = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_DATE_ADD());
                mTextViewOrderCreateTime.setText(orderCreateTime);

                String orderCustomerNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_TO_NAME());
                mTextViewOrderCustomerNumber.setText(orderCustomerNumber);

                String orderCustomerAddress = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_TO_ADDRESS());
                mTextViewOrderCustomerAddress.setText(orderCustomerAddress);

                String orderStart = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_FROM_NAME());
                mTextViewOrderStart.setText(orderStart);

                String orderQTY = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_QTY());
                int pointIndex = orderQTY.indexOf(".");
                if (pointIndex != -1) {
                    orderQTY = orderQTY.substring(0, pointIndex + 2);
                }
                mTextViewOrderQuarity.setText(orderQTY + "箱");

                String orderWeight = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_WEIGHT());
                mTextViewOrderWeight.setText(orderWeight + "吨");

                String orderVolume = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_VOLUME());
                mTextViewOrderVolume.setText(orderVolume + "m³");

                String orderFlow = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_WORKFLOW());
                mTextViewOrderFlow.setText(orderFlow);

                String orderState = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderStatus(order.getORD_STATE()));
                mTextViewOrderState.setText(orderState);

                String orderPayType = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(OrderUtil.getPaymentType(order.getPAYMENT_TYPE()));
                mTextViewOrderPayType.setText(orderPayType);

                if (order.getMJ_REMARK() == null || order.getMJ_REMARK().equals("") || order.getMJ_REMARK().equals("+|+")) {
                    mTextViewOrderPriceMJ.setText("无");
                    String orderActPrice = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(String.valueOf(order.getACT_PRICE()));
                    mTextViewOrderActPrice.setText("￥" + orderActPrice);
                } else {
                    String mjRemark = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(OrderUtil.getPromotionRemark(order.getMJ_REMARK(), true));
                    String mjPrice = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(String.valueOf(order.getMJ_PRICE()));
                    mTextViewOrderPriceMJ.setText(mjRemark + "满减总计 - ￥" + mjPrice);
                    mTextViewOrderActPrice.setText("￥" + (order.getACT_PRICE() - order.getMJ_PRICE()));
                }

                BigDecimal b = BigDecimal.valueOf(order.getACT_PRICE());//现价计算
                mTextViewOrderOryPrice.setText("￥" + b);
            } else {
                ToastUtil.showToastBottom("获取的订单详情为空！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 取消订单成功
     */
    public void cancelOrderSuccess(){
        try {
            ToastUtil.showToastBottom("返回查单界面后，请下拉刷新数据后再查看！",Toast.LENGTH_LONG);
            getOrderTailsData();
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取产品信息集合
     *
     * @param orderDetailList 产品信息结合
     * @param isPromotion     是否是赠品
     * @return 产品信息集合
     */
    private List<OrderDetails> getPickedOrderDetails(List<OrderDetails> orderDetailList, boolean isPromotion) {
        try {
            List<OrderDetails> result = new ArrayList<>();
            for (OrderDetails detail : orderDetailList) {
                if (detail.getPRODUCT_TYPE() != null && (isPromotion ? detail.getPRODUCT_TYPE().equals("GF") : (detail.getPRODUCT_TYPE().equals("NR") || detail.getPRODUCT_TYPE().equals("")))) {
                    result.add(detail);
                }
            }
            return result;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return new ArrayList<>();
        }
    }

}






















