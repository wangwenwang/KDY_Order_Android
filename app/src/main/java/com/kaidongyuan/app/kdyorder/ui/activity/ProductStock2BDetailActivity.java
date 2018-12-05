package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ProductStock2BListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.ProductStockListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.ProductStock2BDetailActivityBiz;
import com.kaidongyuan.app.kdyorder.model.ProductStockDetailActivityBiz;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/19.
 */
public class ProductStock2BDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_product_no,tv_product_name,tv_productInventory_count,tv_productKesum_count,
            tv_productQTYALLOCATED_count,tv_productWeiQTYALLOCATED_count,tv_EDIT_DATE;
    public String productNo,productName,inventoryCount,kesumCount;
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
    private ProductStock2BListAdapter mAdapter;
    private ProductStock2BDetailActivityBiz mBiz;
    private String unit;
    private String qtyallocatedCount;
    private String weiqtyallocatedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productstock2b_detail);
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
            kesumCount=intent.getStringExtra(EXTRAConstants.KESUM_COUNT);
            unit =intent.getStringExtra(EXTRAConstants.SKU_UNIT);
            qtyallocatedCount =intent.getStringExtra(EXTRAConstants.QTYALLOCATED_COUNT);
            weiqtyallocatedCount =intent.getStringExtra(EXTRAConstants.WeiQTYALLOCATED_COUNT);
            mBiz = new ProductStock2BDetailActivityBiz(this);
        } catch (Exception e) {
            ToastUtil.showToastBottom("数据丢失，请返回重进",Toast.LENGTH_SHORT);
            ExceptionUtil.handlerException(e);
            finish();
        }
    }
    private void initView() {
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        tv_product_no= (TextView) this.findViewById(R.id.tv_product_no);
        tv_product_no.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(productNo));
        tv_product_name= (TextView) this.findViewById(R.id.tv_product_name);
        tv_product_name.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(productName));
        tv_productInventory_count= (TextView) this.findViewById(R.id.tv_productInventory_count);
        tv_productInventory_count.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(inventoryCount+unit));
        tv_productKesum_count= (TextView) this.findViewById(R.id.tv_productKesum_count);
        tv_productKesum_count.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(kesumCount+unit));
        tv_productQTYALLOCATED_count= (TextView) this.findViewById(R.id.tv_productQTYALLOCATED_count);
        tv_productQTYALLOCATED_count.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(qtyallocatedCount+unit));
        tv_productWeiQTYALLOCATED_count= (TextView) this.findViewById(R.id.tv_productWeiQTYALLOCATED_count);
        tv_productWeiQTYALLOCATED_count.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(weiqtyallocatedCount+unit));
        tv_EDIT_DATE= (TextView) this.findViewById(R.id.tv_EDIT_DATE);
        lv_productStocks= (MyListView) this.findViewById(R.id.lv_productStocks_list);
        mTextViewNoData= (TextView) this.findViewById(R.id.textview_nodata);
        mAdapter=new ProductStock2BListAdapter(this);
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
            if (mLoadingDialog!=null&&mLoadingDialog.isShowing()){
                mLoadingDialog.cancel();
            }
            mTextViewNoData.setVisibility(View.GONE);
            mAdapter.setData(mBiz.getProductStocks());
            tv_EDIT_DATE.setText(mBiz.getProductStocks().get(0).getCasecnt()+"");
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
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
            default:
                break;
        }
    }
}
