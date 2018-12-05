package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.PromotionInformationAdapter;
import com.kaidongyuan.app.kdyorder.bean.ProductPolicy;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.PromotionInformationActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 * 查看促销信息界面
 */
public class PromotionInformationActivity extends BaseActivity implements View.OnClickListener {

    /**
     * party id
     */
    private String mStrPartyId;
    /**
     * 对应的业务类
     */
    private PromotionInformationActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 没有数据是显示的 Dialog
     */
    private TextView mTextViewNoData;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 显示促销信息的 list
     */
    private ExpandableListView mExpandableListViewPromotionInformation;
    /**
     * 显示促销信息的 Adapter
     */
    private PromotionInformationAdapter mPromotionInformationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_infomation);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getPromotionInformationData();
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
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_ID)) {
                mStrPartyId = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
            }
            mBiz = new PromotionInformationActivityBiz(this);
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
            mTextViewNoData = (TextView) this.findViewById(R.id.textview_nodata);
            mExpandableListViewPromotionInformation = (ExpandableListView) findViewById(R.id.lv_policy);
            mPromotionInformationAdapter = new PromotionInformationAdapter(this);
            mExpandableListViewPromotionInformation.setAdapter(mPromotionInformationAdapter);
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
     * 获取促销产品信息
     */
    private void getPromotionInformationData() {
        try {
            if (mBiz.getPromotionInformatinData(mStrPartyId)) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求时显示的 Dialog
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
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取促销信息失败时回调方法
     *
     * @param message 显示的信息
     */
    public void getPromotionInformationDataError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取促销信息成功是回调的方法
     */
    public void getPromotionInformationDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            List<ProductPolicy> promotionInformations = mBiz.getPromotionInformations();
            mPromotionInformationAdapter.setData(promotionInformations);
            int size = promotionInformations.size();
            if (size <= 0) {
                mExpandableListViewPromotionInformation.setVisibility(View.GONE);
                mTextViewNoData.setVisibility(View.VISIBLE);
                mTextViewNoData.bringToFront();
            } else {
                mExpandableListViewPromotionInformation.setVisibility(View.VISIBLE);
                mTextViewNoData.setVisibility(View.GONE);
            }
            for (int i = 0; i < size; i++) {
                mExpandableListViewPromotionInformation.expandGroup(i);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}



















