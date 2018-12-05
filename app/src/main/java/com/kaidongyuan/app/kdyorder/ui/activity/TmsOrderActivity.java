package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.OrderTmsAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OrderTms;
import com.kaidongyuan.app.kdyorder.bean.TmsOrder;
import com.kaidongyuan.app.kdyorder.bean.TmsOrderItem;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.OrderTransInformationActivityBiz;
import com.kaidongyuan.app.kdyorder.model.TmsOrderActivityBiz;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.DoubleArithUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 * 物流信息详情界面
 */
public class TmsOrderActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {


    /**
     * 对应的业务类
     */
    private TmsOrderActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 订单客户名称
     */
    private TextView mTextViewCustomerName;
    /**
     * 订单目的地址
     */
    private TextView mTextViewOrderDestination;
    /**
     * 下单总量
     */
    private TextView mTextViewOrderQTY;
    /**
     * 发货总量
     */
    private TextView mTextViewIssueQTY;
    /**
     * 下单总重
     */
    private TextView mTextViewOrderWeight;
    /**
     * 发货总重
     */
    private TextView mTextViewIssueWeight;
    /**
     * 下单体积
     */
    private TextView mTextViewOrderVolume;
    /**
     * 发货体积
     */
    private TextView mTextViewIssueVolume;
    /**
     * 原单，拆单没有事显示的文本框
     */
    private TextView mTextViewNoRecord;
    /**
     * 显示原单拆单信息数据的 ListView
     */
    private MyListView mListViewTms;
    /**
     * 显示原单拆单 ListView 的适配器
     */
    private OrderTmsAdapter mOrderTmsAdapter;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 物流订单信息
     */
    private TmsOrder tmsOrder;
    private List<Order> tmsOrderItem2Orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tms_order_information);

            initData();
            setTop();
            initView();
            setListener();
            getData();
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
            this.mLoadingDialog = null;
            this.mBiz.cancelRequest();
            this.mBiz = null;
            this.mImageViewGoBack = null;
            this.mTextViewCustomerName = null;
            this.mTextViewOrderDestination = null;
            this.mTextViewOrderQTY = null;
            this.mTextViewIssueQTY = null;
            this.mTextViewOrderWeight = null;
            this.mTextViewIssueWeight = null;
            this.mTextViewOrderVolume = null;
            this.mTextViewIssueVolume = null;
            this.mTextViewNoRecord = null;
            this.mListViewTms = null;
            this.mOrderTmsAdapter = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new TmsOrderActivityBiz(this);
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
            this.mTextViewCustomerName = (TextView) this.findViewById(R.id.tv_customer_name);
            this.mTextViewOrderDestination = (TextView) this.findViewById(R.id.tv_destination);
            this.mTextViewOrderQTY = (TextView) this.findViewById(R.id.tv_order_qty);
            this.mTextViewIssueQTY = (TextView) this.findViewById(R.id.tv_issue_qty);
            this.mTextViewOrderWeight = (TextView) this.findViewById(R.id.tv_order_weight);
            this.mTextViewIssueWeight = (TextView) this.findViewById(R.id.tv_issue_weight);
            this.mTextViewOrderVolume = (TextView) this.findViewById(R.id.tv_order_volume);
            this.mTextViewIssueVolume = (TextView) this.findViewById(R.id.tv_issue_volume);
            this.mTextViewNoRecord = (TextView) this.findViewById(R.id.tv_no_record);
            this.mListViewTms = (MyListView) this.findViewById(R.id.lv_tms);
            mOrderTmsAdapter = new OrderTmsAdapter(this, null);
            mListViewTms.setAdapter(mOrderTmsAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mListViewTms.setOnItemClickListener(this);
            mOrderTmsAdapter.setInterface(new InnerOrderTmsAdapterListener());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.TMS_ORDER)) {
               tmsOrder = intent.getParcelableExtra(EXTRAConstants.TMS_ORDER);
                if (tmsOrder == null || tmsOrder.getIDX().isEmpty()) {
                    ToastUtil.showToastBottom("请重新进入该界面，所选物流订单数据丢失！", Toast.LENGTH_LONG);
                } else {
                    if (mBiz.getTmsOrderInformationData(tmsOrder.getIDX())) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }
            } else {
                ToastUtil.showToastBottom("请重新进入该界面，所选物流订单数据丢失！", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示网络请求是的 Dialog
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
                case R.id.button_goback://返回上一界面
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求失败是的方法
     *
     * @param message 要显示的信息
     */
    public void getTransInformationError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 网络请求订单物流信息成功
     */
    public void getTransInformationSuccess() {
        try {
            mLoadingDialog.dismiss();

            if (tmsOrder != null) {
                String orderToName = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsOrder.getORD_TO_NAME());
                mTextViewCustomerName.setText(orderToName);

                String orderToAddress = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsOrder.getORD_TO_ADDRESS());
                mTextViewOrderDestination.setText(orderToAddress);

                Double orderQty = DoubleArithUtil.round(tmsOrder.getORD_QTY(),3);
                mTextViewOrderQTY.setText(orderQty + "件");

                Double tmsQty = DoubleArithUtil.round(tmsOrder.getTMS_QTY(),3);
                mTextViewIssueQTY.setText(tmsQty + "件");

                Double orderWeight = DoubleArithUtil.round(tmsOrder.getORD_WEIGHT(),3);
                mTextViewOrderWeight.setText(orderWeight + "吨");

                Double tmsWeight = DoubleArithUtil.round(tmsOrder.getTMS_WEIGHT(),3);
                mTextViewIssueWeight.setText(tmsWeight + "吨");

                Double orderVolume = DoubleArithUtil.round(tmsOrder.getORD_VOLUME(),3);
                mTextViewOrderVolume.setText(orderVolume + "m³");

                Double tmsVolume = DoubleArithUtil.round(tmsOrder.getTMS_VOLUME(),3);
                mTextViewIssueVolume.setText(tmsVolume + "m³");
                List<TmsOrderItem> tmsOrderItems=mBiz.getTmsOrderList();
                if (tmsOrderItems.size() == 0) {
                    mTextViewNoRecord.setVisibility(View.VISIBLE);
                } else {
                    mTextViewNoRecord.setVisibility(View.GONE);
                    tmsOrderItem2Orders = new ArrayList<>();
                    for (TmsOrderItem tmsOrderItem:tmsOrderItems){
                        Order order=new Order();
                        order.setORD_IDX(tmsOrderItem.getORD_IDX());
                        order.setORD_WORKFLOW(tmsOrderItem.getORD_WORKFLOW());
                        order.setORD_NO(tmsOrderItem.getORD_NO());
                        order.setDRIVER_PAY(tmsOrderItem.getDRIVER_PAY());
                        order.setTMS_DATE_ISSUE(tmsOrderItem.getTMS_DATE_ISSUE());
                        order.setTMS_SHIPMENT_NO(tmsOrderItem.getTMS_SHIPMENT_NO());
                        order.setTMS_DATE_LOAD(tmsOrderItem.getTMS_DATE_LOAD());
                        order.setORD_ISSUE_QTY(tmsOrderItem.getORD_ISSUE_QTY()+"");
                        order.setORD_ISSUE_VOLUME(tmsOrderItem.getORD_ISSUE_VOLUME()+"");
                        order.setORD_ISSUE_WEIGHT(tmsOrderItem.getORD_ISSUE_WEIGHT()+"");
                        tmsOrderItem2Orders.add(order);
                    }
                }

                mOrderTmsAdapter.notifyChange(tmsOrderItem2Orders);
            } else {
                ToastUtil.showToastBottom("物流订单信息为空！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent orderTmsDetails = new Intent(this, OrderTmsDetailsActivity.class);
            orderTmsDetails.putExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID, mBiz.getTmsOrderList().get(position).getORD_IDX());
            startActivity(orderTmsDetails);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 原单、拆单里面三个按钮的回调接口实现类
     */
    private class InnerOrderTmsAdapterListener implements OrderTmsAdapter.OrderTmsAdapterOnclickInterface {
        @Override
        public void doCheckNodeClick(int position) {//跳转到查看进度
            try {
                String orderId = mBiz.getTmsOrderList().get(position).getORD_IDX();
                if (orderId == null || orderId.length() <= 0) {
                    ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
                } else {
                    Intent intent = new Intent(TmsOrderActivity.this, OrderTimeNodeActivity.class);
                    intent.putExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID, orderId);
                    startActivity(intent);
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void doCheckPathClick(int position) {//跳转到查看路线
            try {
                String orderId = mBiz.getTmsOrderList().get(position).getORD_IDX();
                if (orderId == null || orderId.length() <= 0) {
                    ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
                } else {
                    Intent intent = new Intent(TmsOrderActivity.this, OrderPathActivity.class);
                    intent.putExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID, orderId);
                    startActivity(intent);
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void doCheckPictureClick(int position) {//跳转到查看签名界面
            Order order = tmsOrderItem2Orders.get(position);
            String orderIdx = order.getORD_IDX();
            Intent checkOrderPictureIntent = new Intent(TmsOrderActivity.this, CheckOrderPictureActivity.class);
            checkOrderPictureIntent.putExtra(EXTRAConstants.EXTRA_ORDER_IDX, orderIdx);
            startActivity(checkOrderPictureIntent);
        }
    }
}


















