package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.OrderDetailsSimpleAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.OrderTmsDetailsActivityBiz;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

/**
 * Created by Administrator on 2016/5/26.
 * 物流信息详情界面
 */
public class OrderTmsDetailsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private OrderTmsDetailsActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 订单编号
     */
    private TextView mTextViewOrderNumber;
    /**
     * 装运编号
     */
    private TextView mTextViewOrderShipmentNumber;
    /**
     * 装运时间
     */
    private TextView mTextViewOrderLoadDate;
    /**
     * 出库时间
     */
    private TextView mTextViewOrderIssueDate;
    /**
     * 承运商名
     */
    private TextView mTextViewOrderFleetName;
    /**
     * 司机姓名
     */
    private TextView mTextViewOrderDriverName;
    /**
     * 车牌号码
     */
    private TextView mTextViewOrderDriverCarNumber;
    /**
     * 司机号码
     */
    private TextView mTextViewOrderPhoneNumber;
    /**
     * 下单数量
     */
    private TextView mTextViewOrderQty;
    /**
     * 下单重量
     */
    private TextView mTextViewOrderWeight;
    /**
     * 下单体积
     */
    private TextView mTextViewOrderVolume;
    /**
     * 订单流程
     */
    private TextView mTextViewOrderFlow;
    /**
     * 订单状态
     */
    private TextView mTextViewOrderStatus;
    /**
     * 付款方式
     */
    private TextView mTextViewOrderPaymentType;
    /**
     * 货物信息
     */
    private MyListView mListViewTms;
    /**
     * 货物信息的 Adapter
     */
    private OrderDetailsSimpleAdapter mOrderDetailsSimpleAdapter;
    /**
     * 网络氢气是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tms_details);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getOrderDetailsData();
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
            this.mTextViewOrderNumber = null;
            this.mTextViewOrderShipmentNumber = null;
            this.mTextViewOrderLoadDate = null;
            this.mTextViewOrderIssueDate = null;
            this.mTextViewOrderFleetName = null;
            this.mTextViewOrderDriverName = null;
            this.mTextViewOrderDriverCarNumber = null;
            this.mTextViewOrderPhoneNumber = null;
            this.mTextViewOrderQty = null;
            this.mTextViewOrderWeight = null;
            this.mTextViewOrderVolume = null;
            this.mTextViewOrderFlow = null;
            this.mTextViewOrderStatus = null;
            this.mTextViewOrderPaymentType = null;
            this.mListViewTms = null;
            this.mOrderDetailsSimpleAdapter = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new OrderTmsDetailsActivityBiz(this);
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

            this.mTextViewOrderNumber = (TextView) this.findViewById(R.id.tv_tms_order_no);
            this.mTextViewOrderShipmentNumber = (TextView) this.findViewById(R.id.tv_tms_shipment_no);
            this.mTextViewOrderLoadDate = (TextView) this.findViewById(R.id.tv_tms_date_load);
            this.mTextViewOrderIssueDate = (TextView) this.findViewById(R.id.tv_tms_date_issue);
            this.mTextViewOrderFleetName = (TextView) this.findViewById(R.id.tv_tms_fleet_name);
            this.mTextViewOrderDriverName = (TextView) this.findViewById(R.id.tv_tms_driver_name);
            this.mTextViewOrderDriverCarNumber = (TextView) this.findViewById(R.id.tv_tms_plate_number);
            this.mTextViewOrderPhoneNumber = (TextView) this.findViewById(R.id.tv_tms_driver_tel);
            this.mTextViewOrderQty = (TextView) this.findViewById(R.id.tv_order_qty);
            this.mTextViewOrderWeight = (TextView) this.findViewById(R.id.tv_order_weight);
            this.mTextViewOrderVolume = (TextView) this.findViewById(R.id.tv_order_volume);
            this.mTextViewOrderFlow = (TextView) this.findViewById(R.id.tv_order_flow);
            this.mTextViewOrderStatus = (TextView) this.findViewById(R.id.tv_order_status);
            this.mTextViewOrderPaymentType = (TextView) this.findViewById(R.id.tv_payment_type);

            this.mListViewTms = (MyListView) this.findViewById(R.id.lv_details);
            mOrderDetailsSimpleAdapter = new OrderDetailsSimpleAdapter(this, null);
            mListViewTms.setAdapter(mOrderDetailsSimpleAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单物流详情数据
     */
    private void getOrderDetailsData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID)) {
                String orderId = intent.getStringExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID);
                if (orderId == null || orderId.length() <= 0) {
                    ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
                } else {
                    if (mBiz.getOrderDetailsData(orderId)) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }
            } else {
                ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示网络请求时的 Dialog
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

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback://返回到上一界面
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单物流信息详情失败是调用方法
     *
     * @param message 要显示的信息
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
     * 获取订单物流信息成功时调用的方法
     */
    public void getOrderDetailsDtaSuccess() {
        try {
            mLoadingDialog.dismiss();
            Order order = mBiz.getOrder();
            if (order != null) {
                String orderNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_NO());
                mTextViewOrderNumber.setText(orderNumber);

                String shipmentNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_SHIPMENT_NO());
                mTextViewOrderShipmentNumber.setText(shipmentNumber);

                String loadDate = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_DATE_LOAD());
                mTextViewOrderLoadDate.setText(loadDate);

                String issueDate = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_DATE_ISSUE());
                mTextViewOrderIssueDate.setText(issueDate);

                String fleetName = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_FLEET_NAME());
                mTextViewOrderFleetName.setText(fleetName);

                String plageNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_PLATE_NUMBER());
                mTextViewOrderDriverCarNumber.setText(plageNumber);

                String driverName = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_DRIVER_NAME());
                mTextViewOrderDriverName.setText(driverName);

                String driverPhone = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_DRIVER_TEL());
                mTextViewOrderPhoneNumber.setText(driverPhone);

                String issueQty = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_ISSUE_QTY());
                int index = issueQty.indexOf(".");
                if (index != -1) {
                    issueQty = issueQty.substring(0, index + 2);
                }
                mTextViewOrderQty.setText(issueQty + "件");

                String issueWeight = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_ISSUE_WEIGHT());
                mTextViewOrderWeight.setText(issueWeight + "吨");

                String issueVolume = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_ISSUE_VOLUME());
                mTextViewOrderVolume.setText(issueVolume + "m³");

                String orderState = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderStatus(order.getORD_STATE()));
                mTextViewOrderStatus.setText(orderState);

                String orderWorkFlow = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderState(order.getORD_WORKFLOW()));
                mTextViewOrderFlow.setText(orderWorkFlow);

                String driverPay = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderState(order.getDRIVER_PAY()));
                mTextViewOrderPaymentType.setText(driverPay);

                mOrderDetailsSimpleAdapter.notifyChange(order.getOrderDetails());
            } else {
                ToastUtil.showToastBottom("获取订单物流详情失败！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}


















