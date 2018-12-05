package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.SlideDateTimeListener;
import com.example.mylibrary.SlideDateTimePicker;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.OrderProductDetailAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderPromotionAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.OrderGift;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.bean.PromotionOrder;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.model.OrderConfirmActivityBiz;
import com.kaidongyuan.app.kdyorder.model.PreOrderConfirmActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.OrderUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 * 订单确认界面
 */
public class PreOrderConfirmActivity extends BaseFragmentActivity implements View.OnClickListener {

    /**
     * 跳转到添加赠品界面时用到的常量
     */
    private final int ADDGIFT_REQUESCODE = 12;

    /**
     * 对应的业务类
     */
    private PreOrderConfirmActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    /**
     * 下计划单客户基本信息
     */
    private TextView mTextViewPartyName,mTextVIewPartyAddress,mTextViewPartyCName,mTextViewPartyTel;


    /**
     * 用户选中的商品
     */
    private List<Product> mChoicedProducts;

    /**
     * 送货时间
     */
    private TextView mTextViewTime;
    /**
     * 产品总数
     */
    private TextView mTextViewTotalCount;
    /**
     * 原价
     */
    private TextView mTextViewOrgPrice;
    /**
     * 实际付款
     */
    private TextView mTextViewActPrice;
    /**
     * 支付价格
     */
    private TextView mTextViewPayPrice;
    /**
     * 产品列表
     */
    private MyListView mListViewProduct;
    /**
     * 产品列表适配器
     */
    private OrderProductDetailAdapter mProductAdapter;

    /**
     * 备注信息输入框
     */
    private EditText mEditTextMark;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 用户选择的送货时间
     */
    private Date mDate=null;

    /**
     * 商品总数过度值
     */
    private int mTempTotalQTY = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_preorder_confirm);
            setTop();
            initView();
            initData();
            setListener();
            getPromotionData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mBiz.cancelRequest();
            mBiz = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mImageViewGoBack = null;
            mTextViewPartyName=null;
            mTextVIewPartyAddress=null;
            mTextViewPartyCName=null;
            mTextViewPartyTel=null;
            mChoicedProducts = null;
            mTextViewTime = null;
            mTextViewTotalCount = null;
            mTextViewOrgPrice = null;
            mTextViewActPrice = null;
            mTextViewPayPrice = null;
            mListViewProduct = null;
            mProductAdapter = null;
            mEditTextMark = null;
            mDate = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new PreOrderConfirmActivityBiz(this);
            Intent intent = getIntent();
            mChoicedProducts = intent.getParcelableArrayListExtra(EXTRAConstants.CHOICED_PRODUCT_LIST);
            mTextViewPartyName.setText(intent.getStringExtra(EXTRAConstants.ORDER_PARTY_NAME));
            mTextVIewPartyAddress.setText(intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION));
            mTextViewPartyCName.setText(intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson));
            mTextViewPartyTel.setText(intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel));
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
            setTieltLayoutHeight();
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mTextViewPartyName= (TextView) this.findViewById(R.id.tv_party_name);
            mTextVIewPartyAddress= (TextView) this.findViewById(R.id.tv_party_address);
            mTextViewPartyCName= (TextView) this.findViewById(R.id.tv_party_cname);
            mTextViewPartyTel= (TextView) this.findViewById(R.id.tv_party_tel);
            mTextViewTime = (TextView) this.findViewById(R.id.tv_time);
            mTextViewTotalCount = (TextView) this.findViewById(R.id.tv_total_count);
            mTextViewOrgPrice = (TextView) this.findViewById(R.id.tv_org_price);
            mTextViewActPrice = (TextView) this.findViewById(R.id.tv_act_price);
            mTextViewPayPrice = (TextView) this.findViewById(R.id.tv_pay_price);
            mListViewProduct = (MyListView) this.findViewById(R.id.lv_product);
            mProductAdapter = new OrderProductDetailAdapter(this, null);
            mListViewProduct.setAdapter(mProductAdapter);
            mEditTextMark = (EditText) this.findViewById(R.id.et_mark);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 输入框在底部，所以要动态调整标题栏的高度
     * 设置标题栏高度，默认是50dp，根据百分百设置，统一标题栏高度
     */
    private void setTieltLayoutHeight() {
        try {
            String strHeight = getString(R.string.title_height);//百分百布局格式 如 1%h
            int index = strHeight.indexOf("%");
            if (index != -1) {
                int height = Integer.parseInt(strHeight.substring(0, index));
                int titleLayoutHeight = DensityUtil.getHeight() * height / 100;
                RelativeLayout relativeLayoutTitle = (RelativeLayout) findViewById(R.id.percentRL_title);
                ViewGroup.LayoutParams params = relativeLayoutTitle.getLayoutParams();
                params.height = titleLayoutHeight;
                relativeLayoutTitle.setLayoutParams(params);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            findViewById(R.id.btn_confirm).setOnClickListener(this);
            findViewById(R.id.rl_select_time).setOnClickListener(this);
            mProductAdapter.setInterface(new OrderProductDetailAdapter.OrderProductDetailAdapterModifyPriceInterface() {
                @Override
                public void raisePrice(int dataIndex) {//上调价格 0.1元
                    mBiz.raisePrice(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void cutPrice(int dataIndex) {//下调价格 0.1元
                    mBiz.cutPrice(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void setProductPrice(int dataIndex, double inputPirce) {//手动设置价格
                    mBiz.setPrice(dataIndex, inputPirce);
                    notifyDataChange();
                }
            });
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 发送订单信息到后台，获取后台返回的订单信息
     */
    private void getPromotionData() {
        try {
            if (mBiz.getPromotionData(mBiz.getSubmitString(mChoicedProducts))) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示的 Dialog
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
                    finish();
                    break;
                case R.id.btn_confirm://提交订单
                    confirm();
                    break;
                case R.id.rl_select_time://选择送货时间
                    new SlideDateTimePicker.Builder((getSupportFragmentManager()))
                            .setListener(new DateHandler(0))
                            .setInitialDate(new Date())
                            .setMinDate(new Date())
                            .build()
                            .show();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取促销信息失败时回调的方法
     *
     * @param msg 显示的信息
     */
    public void getDataError(String msg) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }



    /**
     * 成功提交订单时回调的方法
     */
    public void confirmSuccess() {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("提交订单成功！", Toast.LENGTH_SHORT);
            MyApplication.getInstance().finishActivity(MakePreOrderActivity.class);
            this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置空间的信息
     */
    private void setViewData() {
        try {
            PromotionOrder order = mBiz.getOrder();

            List<PromotionDetail> products = mBiz.getProducts();
            mProductAdapter.notifyChange(products);
            mTextViewTotalCount.setText(String.valueOf(order.TOTAL_QTY));
            mTextViewOrgPrice.setText("￥" + order.ORG_PRICE + "");
            mTextViewActPrice.setText("￥" + order.ACT_PRICE);

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 选择送货时间的监听
     */
    class DateHandler extends SlideDateTimeListener {
        int which;

        DateHandler(int which) {
            this.which = which;
        }

        @Override
        public void onDateTimeCancel() {//不设置送货时间
            try {
                super.onDateTimeCancel();
                mDate = null;
                mTextViewTime.setText(null);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void onDateTimeSet(Date date) {//设置送货时间
            try {
                mDate = date;
                if (date != null) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    mTextViewTime.setText(StringUtils.subyyMM(df.format(date)));
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }
    /**
     * 获取促销信息成功时回调的方法
     */
    public void getPromotionDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            PromotionOrder promotionOrder = mBiz.getOrder();
            if (promotionOrder != null) {
                setViewData();
            } else {
                ToastUtil.showToastBottom("确认订单信息失败！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 确认订单 最后一步提交到服务器
     */
    private void confirm() {
        try {
            if (mDate==null){
                ToastUtil.showToastMid("请选择所下计划单月份！",Toast.LENGTH_SHORT);
                return;
            }
            String remark = mEditTextMark.getText().toString().trim();
            mBiz.setConfirmData(mChoicedProducts, mTempTotalQTY, mDate, remark);
            if (mBiz.confirm()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 产品数据变动是刷新 listView
     */
    private void notifyDataChange() {
        try {
            mProductAdapter.notifyChange(mBiz.getProducts());
            mTextViewActPrice.setText(mBiz.getActPrice());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

}
