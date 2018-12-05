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
import com.kaidongyuan.app.kdyorder.adapter.AppStocksAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderProductDetailAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderPromotionAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.AppStock;
import com.kaidongyuan.app.kdyorder.bean.AppStockResult;
import com.kaidongyuan.app.kdyorder.bean.OrderGift;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.bean.PromotionOrder;
import com.kaidongyuan.app.kdyorder.bean.StockProduct;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.model.AppStockResultsAdapter;
import com.kaidongyuan.app.kdyorder.model.InventoryConfirmActivityBiz;
import com.kaidongyuan.app.kdyorder.model.OrderConfirmActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.OrderUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
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
public class InventoryConfirmActivity extends BaseFragmentActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private InventoryConfirmActivityBiz mBiz;


    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 对应的makeappstockactivity传入值
     */
    private String mPartyId,mPartyCode,mPartyName,submitdate,stockdate;
    /**
     * 用户选中的商品
     */
    private List<StockProduct> mChoicedProducts;

    /**
     * 客户库存表单头部基本信息
     */
    private TextView tv_PARTY_NAME,tv_PARTY_CODE,tv_BUSINESS_NAME,tv_STOCK_DATE,tv_SUBMIT_DATE;
    /**
     * 产品总数
     */
    private TextView mTextViewTotalCount;
    /**
     * 产品列表
     */
    private MyListView mListViewProduct;
    /**
     * 产品列表适配器
     */
    private AppStockResultsAdapter mAppStockResultsAdapter;

    /**
     * 备注信息输入框
     */
    private EditText mEditTextMark;
    /**
     * 确认提交库存登记表
     */
    private Button bt_confirm_appstock;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 用户选择的送货时间
     */
    private Date mDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inventory_confirm);
            initData();
          //  setTop();
            initView();
            setListener();
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
            mChoicedProducts = null;

            mTextViewTotalCount = null;
            mListViewProduct = null;
            mAppStockResultsAdapter=null;
            mEditTextMark = null;
            mDate = null;

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new InventoryConfirmActivityBiz(this);
            Intent intent = getIntent();
            mChoicedProducts = intent.getParcelableArrayListExtra(EXTRAConstants.EXTRA_STOCK_PRODUCT);
            mPartyId=intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
            mPartyCode=intent.getStringExtra(EXTRAConstants.ORDER_PARTY_NO);
            mPartyName=intent.getStringExtra(EXTRAConstants.ORDER_PARTY_NAME);
            submitdate=intent.getStringExtra(EXTRAConstants.EXTRA_SUBMIT_DATE);
            stockdate=intent.getStringExtra(EXTRAConstants.EXTRA_STOCK_DATE);
            mBiz.setConfirmData(mChoicedProducts,mPartyId,stockdate,submitdate);
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
            tv_PARTY_NAME= (TextView) this.findViewById(R.id.tv_PARTY_NAME);
            tv_PARTY_NAME.setText(mPartyName);
            tv_PARTY_CODE= (TextView) this.findViewById(R.id.tv_PARTY_CODE);
            tv_PARTY_CODE.setText(mPartyCode);
            tv_BUSINESS_NAME= (TextView) this.findViewById(R.id.tv_BUSINESS_NAME);
            tv_BUSINESS_NAME.setText(MyApplication.getInstance().getBusiness().getBUSINESS_NAME());
            tv_STOCK_DATE= (TextView) this.findViewById(R.id.tv_STOCK_DATE);
            tv_STOCK_DATE.setText(stockdate);
            tv_SUBMIT_DATE= (TextView) this.findViewById(R.id.tv_SUBMIT_DATE);
            tv_SUBMIT_DATE.setText(submitdate);
            mListViewProduct = (MyListView) this.findViewById(R.id.lv_appstock);
            mAppStockResultsAdapter=new AppStockResultsAdapter(InventoryConfirmActivity.this,mBiz.getAppStockResults());
            mListViewProduct.setAdapter(mAppStockResultsAdapter);
            bt_confirm_appstock= (Button) this.findViewById(R.id.bt_confirm_appstock);

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
            bt_confirm_appstock.setOnClickListener(this);
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
                case R.id.bt_confirm_appstock://提交库存登记表
                    confirm();
                    break;
                default:
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
            ToastUtil.showToastBottom("提交成功！", Toast.LENGTH_SHORT);
            MyApplication.getInstance().finishActivity(MakeAppStockActivity.class);
            MyApplication.getInstance().finishActivity(InventoryPartyActivity.class);
            InventoryManageActivity.isrefresh=true;
            this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }



    /**
     * 确认订单 最后一步提交到服务器
     */
    private void confirm() {
        try {
            if (mBiz.confirm()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}
