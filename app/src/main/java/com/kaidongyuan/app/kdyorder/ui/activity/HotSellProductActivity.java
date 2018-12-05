package com.kaidongyuan.app.kdyorder.ui.activity;

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
import com.kaidongyuan.app.kdyorder.adapter.HotSellProductAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.model.HotSellProductActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 * 热销产品界面
 */
public class HotSellProductActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 热销产品的业务类
     */
    private HotSellProductActivityBiz mBiz;
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 热销产品 ListView
     */
    private XListView mListViewHotSellProduct;
    /**
     * 热销产品 ListView 的适配器
     */
    private HotSellProductAdapter mHotSellProductAdapter;
    /**
     * ListView 没有数据时显示的文本框
     */
    private TextView mTextViewNoData;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_sell_product);
        try {
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
            mLoadingDialog = null;
            mBiz.cancelRequest();
            mBiz = null;
            mImageViewGoBack = null;
            mListViewHotSellProduct = null;
            mHotSellProductAdapter = null;
            mTextViewNoData = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        mBiz = new HotSellProductActivityBiz(this);
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
            mListViewHotSellProduct = (XListView) this.findViewById(R.id.lv_hotsell_product);
            mHotSellProductAdapter = new HotSellProductAdapter(this, null);
            mListViewHotSellProduct.setAdapter(mHotSellProductAdapter);
            mListViewHotSellProduct.setPullLoadEnable(false);
            mListViewHotSellProduct.setPullRefreshEnable(false);
            mTextViewNoData = (TextView) this.findViewById(R.id.textview_nodata);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        mImageViewGoBack.setOnClickListener(this);
    }

    private void getData() {
        try {
            if (mBiz.getHotSellProductData()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
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

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback://返回上一个界面
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取热销产品列表失败
     *
     * @param message 显示的消息
     */
    public void getHotSellProductDataError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
            handleGetHotSellProductRequest();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取热销产品成功
     */
    public void getHotSellProductDataSuccess() {
        handleGetHotSellProductRequest();
    }

    /**
     * 处理获取热销产品网络请求
     */
    private void handleGetHotSellProductRequest() {
        try {
            mLoadingDialog.dismiss();
            List<Product> list = mBiz.getHotSellProductDataList();
            if (list == null || list.size() <= 0) {
                mTextViewNoData.setVisibility(View.VISIBLE);
            } else {
                mTextViewNoData.setVisibility(View.GONE);
            }
            mHotSellProductAdapter.notifyChange(list);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}
















