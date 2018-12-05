package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.InventoryProduct2BAdapter;
import com.kaidongyuan.app.kdyorder.adapter.InventoryProductAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.BusinessProductStock;
import com.kaidongyuan.app.kdyorder.bean.PartyProductStock;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.BusinessInventoryActivityBiz;
import com.kaidongyuan.app.kdyorder.model.PartyInventoryActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ListViewUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/8/17.
 */
public class BusinessInventoryActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private BusinessInventoryActivityBiz mBiz;
    private ImageView mImageViewGoBack;
    /**
     * 显示库存产品列表的 ListView
     */
    private XListView mInventoryProductListView;
    /**
     * 库存产品列表 ListView 适配器
     */
    private InventoryProduct2BAdapter mProductsApapter;
     /**
     * 没有数据是显示的提示框
     */
    private TextView mTextViewNoData;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    public String productNo;//产品代号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_inventory);
        initData();
        initView();
        initListener();
        initProductData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mBiz!=null&&mBiz.getInventoryProductList()!=null&&mBiz.getInventoryProductList().size()<=0&&mBiz.reFreshInventoryProductsData()){
            showLoadingDialog();
        }
    }

    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mInventoryProductListView= (XListView) this.findViewById(R.id.lv_inventory_productlist);
            mInventoryProductListView.setPullLoadEnable(true);
            mInventoryProductListView.setPullUpMore(true);
            mProductsApapter=new InventoryProduct2BAdapter(null,BusinessInventoryActivity.this);
            mInventoryProductListView.setAdapter(mProductsApapter);
            mTextViewNoData= (TextView) this.findViewById(R.id.textview_nodata);
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }
    private void initListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mTextViewNoData.setOnClickListener(this);
            mInventoryProductListView.setOnItemClickListener(this);
//            mInventoryProductListView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    XListViewFooter footerview=mInventoryProductListView.getmFooterView();
//                    int[] location=new int[2];
//                    footerview.mHintView.getLocationOnScreen(location);
//                    Logger.w("x:"+location[0]+"\ty:"+location[1]+"\tscreenHight:"+ DensityUtil.getHeight()+"\tfootviewHight"+footerview.getHeight());
//                    if (location[1]<=DensityUtil.getHeight()+footerview.getHeight()){
//                        mInventoryProductListView.getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                    return false;
//                }
//            });

            mInventoryProductListView.setXListViewListener(new XListView.IXListViewListener() {
                @Override
                public void onRefresh() { //刷新数据

                    mInventoryProductListView.stopRefresh();
                    if (mBiz.reFreshInventoryProductsData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onLoadMore() {//加载更多数据
//                   // if (mInventoryProductListView.getLastVisiblePosition()=)
//
                    if (mBiz.loadMoreInventoryProductsData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }
            });
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            if (mBiz==null){
                mBiz = new BusinessInventoryActivityBiz(this);
            }
            productNo="";
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            finish();
        }
    }

    private void initProductData() {
        if (mBiz.getInventoryProductsData()){
            showLoadingDialog();
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
        }
    }
    /**
     * 显示 DownloadingDialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(BusinessInventoryActivity.this);
            }
            mLoadingDialog.showDialog();
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

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.button_goback://返回上一界面
                    finish();
                    break;
                case R.id.textview_nodata:
                    mInventoryProductListView.stopRefresh();
                    if (mBiz.reFreshInventoryProductsData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                    break;
                default:
                    break;
            }
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
        }
    }
    /**
     * 获取数据成功时业务类调用的方法
     */
    public void getProductSuccess(){
        try {
            handleRequest();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 加载失败时业务类回调方法
     * @param msg 显示的信息
     */
    public void getProductError(String msg) {
            try {
                handleRequest();
                ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
    }

    /**
     * 处理获取在途订单后的结果
     */
    private void handleRequest() {
        try {
            mInventoryProductListView.stopRefresh();
            mInventoryProductListView.stopLoadMore();
            mLoadingDialog.dismiss();
            List<BusinessProductStock> productStocks= mBiz.getInventoryProductList();
            if (productStocks == null || productStocks.size() <= 0) {
                mTextViewNoData.setVisibility(View.VISIBLE);
            } else {
                mTextViewNoData.setVisibility(View.GONE);
            }
            mProductsApapter.setData(productStocks);

            ListViewUtils.setListViewHeightBasedOnChildren(mInventoryProductListView);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent =new Intent(this,ProductStock2BDetailActivity.class);
            intent.putExtra(EXTRAConstants.PRODUCT_NO,mProductsApapter.getData().get(position-1).getSku());
            intent.putExtra(EXTRAConstants.PRODUCT_NAME,mProductsApapter.getData().get(position-1).getDescr());
            intent.putExtra(EXTRAConstants.KESUM_COUNT,mProductsApapter.getData().get(position-1).getKesum());
            intent.putExtra(EXTRAConstants.INVENTORY_COUNT,mProductsApapter.getData().get(position-1).getQTY());
            intent.putExtra(EXTRAConstants.SKU_UNIT,mProductsApapter.getData().get(position-1).getSusr2());
            intent.putExtra(EXTRAConstants.QTYALLOCATED_COUNT,mProductsApapter.getData().get(position-1).getQTYALLOCATED());
            intent.putExtra(EXTRAConstants.WeiQTYALLOCATED_COUNT,mProductsApapter.getData().get(position-1).getWeiQTYALLOCATED());
            startActivity(intent);
        }catch (Exception e){
            ToastUtil.showToastMid("获取产品库存信息失败",Toast.LENGTH_SHORT);
            ExceptionUtil.handlerException(e);
        }

    }
}
