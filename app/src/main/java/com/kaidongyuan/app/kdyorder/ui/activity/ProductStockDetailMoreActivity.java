package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ProductStockListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.ProductStockMoreListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.ProductStockDetailActivityBiz;
import com.kaidongyuan.app.kdyorder.model.ProductStockDetailMoreActivityBiz;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/19.
 */
public class ProductStockDetailMoreActivity extends BaseActivity implements View.OnClickListener {

    private String productNo,productName,inventoryCount,editDate;
    public String idx;
    private ImageView mImageViewGoBack;

    /**
     * 没有数据是显示的提示框
     */
    private TextView mTextViewNoData;

    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    private MyListView lv_productStocks;
    private ProductStockMoreListAdapter mAdapter;
    private ProductStockDetailMoreActivityBiz mBiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productstock_detail_more);
        init();
        initView();
        initListener();
        getProductStocksData();
    }



    private void init() {
        try {
            Intent intent=getIntent();
            productNo=intent.getStringExtra(EXTRAConstants.PRODUCT_NO);
            productName=intent.getStringExtra(EXTRAConstants.PRODUCT_NAME);
            inventoryCount=intent.getStringExtra(EXTRAConstants.INVENTORY_COUNT);
            editDate=intent.getStringExtra(EXTRAConstants.EDIT_DATE);
            idx=intent.getStringExtra(EXTRAConstants.PRODUCT_STOCKIDX);
            mBiz = new ProductStockDetailMoreActivityBiz(this);
        } catch (Exception e) {
            ToastUtil.showToastBottom("数据丢失，请返回重进",Toast.LENGTH_SHORT);
            ExceptionUtil.handlerException(e);
            finish();
        }
    }
    private void initView() {
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        lv_productStocks= (MyListView) this.findViewById(R.id.lv_productStocks_list);
        mTextViewNoData= (TextView) this.findViewById(R.id.textview_nodata);
        mAdapter=new ProductStockMoreListAdapter(this);
        lv_productStocks.setAdapter(mAdapter);
    }

    private void initListener() {
        mImageViewGoBack.setOnClickListener(this);
        mTextViewNoData.setOnClickListener(this);
    }

    private void getProductStocksData(){
        try {
            if (mBiz.getProductStocksData()){
                showLoadingDialog();
            }else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }

        }catch (Exception ex){
            ToastUtil.showToastBottom("初始化失败，请返回重进",Toast.LENGTH_SHORT);
            ExceptionUtil.handlerException(ex);
        }

    }
    public void getProductSuccess() {
        try {
            mTextViewNoData.setVisibility(View.GONE);
            if (mLoadingDialog!=null&&mLoadingDialog.isShowing()){
                mLoadingDialog.cancel();
            }
            mAdapter.setData(mBiz.getProductStocks());
        }catch (Exception ex){

        }
    }
    public void getProductError(String msg) {
        try {
            mTextViewNoData.setVisibility(View.VISIBLE);
            if (mLoadingDialog!=null&&mLoadingDialog.isShowing()){
                mLoadingDialog.cancel();
            }
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (mLoadingDialog!=null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mBiz.cancelRequest();
            mBiz = null;
            mImageViewGoBack = null;
            mLoadingDialog = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示 DownloadingDialog
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
        switch (v.getId()){
            case R.id.button_goback://返回上一界面
                finish();
                break;
            case R.id.textview_nodata:
                getProductStocksData();
                break;
        }
    }



}
