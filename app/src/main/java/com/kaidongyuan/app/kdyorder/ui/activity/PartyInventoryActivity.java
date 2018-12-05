package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.InventoryProductAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.PartyProductStock;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.InventoryPartyListActivityBiz;
import com.kaidongyuan.app.kdyorder.model.PartyInventoryActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ListViewUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListViewFooter;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/8/17.
 */
public class PartyInventoryActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PartyInventoryActivityBiz mBiz;
    private ImageView mImageViewGoBack;

    /**
     * 显示库存产品列表的 ListView
     */
    private XListView mInventoryProductListView;
    /**
     * 库存产品列表 ListView 适配器
     */
    private InventoryProductAdapter mProductsApapter;
    private TextView tv_customer_name,tv_customer_city,tv_customer_address,tv_person_name,tv_person_tel;
    private PercentRelativeLayout percentrl_input_list,percentrl_input_return,percentrl_checkInventory,percentrl_input_other,
            percentrl_output_product,percentrl_output_manage,percentrl_output_return,percentrl_output_other;
    public String party_id,party_code,party_name,party_city,address_idx,address_code,address_info,contact_person,contact_tel;
    /**
     * 没有数据是显示的提示框
     */
    private TextView mTextViewNoData;

    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_inventory);
        initData();
        initView();
        initListener();
        initProductData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mBiz.reFreshInventoryProductsData()){
            showLoadingDialog();
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
        }
    }

    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            tv_customer_name= (TextView) this.findViewById(R.id.tv_customer_name);
            tv_customer_name.setText(party_name);
            tv_customer_city= (TextView) this.findViewById(R.id.tv_customer_city);
            tv_customer_city.setText(party_city);
            tv_customer_address= (TextView) this.findViewById(R.id.tv_customer_address);
            tv_customer_address.setText(address_info);
            tv_person_name= (TextView) this.findViewById(R.id.tv_person_name);
            tv_person_name.setText(contact_person);
            tv_person_tel= (TextView) this.findViewById(R.id.tv_person_tel);
            tv_person_tel.setText(contact_tel);
            percentrl_input_list= (PercentRelativeLayout) this.findViewById(R.id.percentrl_input_list);
            percentrl_input_return= (PercentRelativeLayout) this.findViewById(R.id.percentrl_input_return);
            percentrl_checkInventory= (PercentRelativeLayout) this.findViewById(R.id.percentrl_checkInventory);
            percentrl_input_other= (PercentRelativeLayout) this.findViewById(R.id.percentrl_input_other);
            percentrl_output_product= (PercentRelativeLayout) this.findViewById(R.id.percentrl_output_product);
            percentrl_output_manage= (PercentRelativeLayout) this.findViewById(R.id.percentrl_output_manage);
            percentrl_output_return= (PercentRelativeLayout) this.findViewById(R.id.percentrl_output_return);
            percentrl_output_other= (PercentRelativeLayout) this.findViewById(R.id.percentrl_output_other);
            mInventoryProductListView= (XListView) this.findViewById(R.id.lv_inventory_productlist);
            mInventoryProductListView.setPullLoadEnable(false);
            mInventoryProductListView.setPullUpMore(false);
            mProductsApapter=new InventoryProductAdapter(null,PartyInventoryActivity.this);
            mInventoryProductListView.setAdapter(mProductsApapter);
            mTextViewNoData= (TextView) this.findViewById(R.id.textview_nodata);
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }
    private void initListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            percentrl_input_list.setOnClickListener(this);
            percentrl_input_return.setOnClickListener(this);
            percentrl_checkInventory.setOnClickListener(this);
            percentrl_input_other.setOnClickListener(this);
            percentrl_output_product.setOnClickListener(this);
            percentrl_output_manage.setOnClickListener(this);
            percentrl_output_return.setOnClickListener(this);
            percentrl_output_other.setOnClickListener(this);
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
//                    if (mBiz.loadMoreInventoryProductsData()) {
//                        showLoadingDialog();
//                    } else {
//                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
//                    }
                }
            });
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            party_id=getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
            party_code=getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_NO);
            party_name=getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_NAME);
            party_city=getIntent().getStringExtra(EXTRAConstants.INVENTORY_PARTY_CITY);
            address_idx=getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX);
            address_code=getIntent().getStringExtra(EXTRAConstants.ORDER_ADDRESS_CODE);
            address_info=getIntent().getStringExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION);
            contact_person=getIntent().getStringExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson);
            contact_tel=getIntent().getStringExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel);
            mBiz = new PartyInventoryActivityBiz(this);

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
                mLoadingDialog = new MyLoadingDialog(PartyInventoryActivity.this);
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
                case R.id.percentrl_input_list:
                    //入库明细
                    Intent intent1=new Intent(this,InputOrderListActivity.class);
                    intent1.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent1.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    startActivity(intent1);
                    break;
                case R.id.percentrl_input_return:
                    //入库退货
                    Intent intent2=new Intent(this,InputInventoryActivity.class);
                    intent2.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent2.putExtra(EXTRAConstants.ORDER_PARTY_NO,party_code);
                    intent2.putExtra(EXTRAConstants.ORDER_PARTY_NAME,party_name);
                    intent2.putExtra(EXTRAConstants.INVENTORY_PARTY_CITY,party_city);
                    intent2.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    intent2.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE,address_code);
                    intent2.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION,address_info);
                    intent2.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,contact_person);
                    intent2.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,contact_tel);
                    intent2.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE,"input_return");
                    startActivity(intent2);
                    break;
                case R.id.percentrl_checkInventory:
                    break;
                case R.id.percentrl_input_other:
                    //其它入库
                    Intent intent3=new Intent(this,InputInventoryActivity.class);
                    intent3.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent3.putExtra(EXTRAConstants.ORDER_PARTY_NO,party_code);
                    intent3.putExtra(EXTRAConstants.ORDER_PARTY_NAME,party_name);
                    intent3.putExtra(EXTRAConstants.INVENTORY_PARTY_CITY,party_city);
                    intent3.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    intent3.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE,address_code);
                    intent3.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION,address_info);
                    intent3.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,contact_person);
                    intent3.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,contact_tel);
                    intent3.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE,"input_other");
                    startActivity(intent3);
                    break;
                case R.id.percentrl_output_product:
                    //出库
                    Intent intent4=new Intent(this,OutputInventoryActivity.class);
                    intent4.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent4.putExtra(EXTRAConstants.ORDER_PARTY_NO,party_code);
                    intent4.putExtra(EXTRAConstants.ORDER_PARTY_NAME,party_name);
                    intent4.putExtra(EXTRAConstants.INVENTORY_PARTY_CITY,party_city);
                    intent4.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE,address_code);
                    intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION,address_info);
                    intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,contact_person);
                    intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,contact_tel);
                    intent4.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE,"output_normal");
                    startActivity(intent4);
                    break;
                case R.id.percentrl_output_manage:
                    //出库明细
                    Intent intent5=new Intent(this,OutputOrderListActivity.class);
                    intent5.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent5.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    startActivity(intent5);
                    break;

                case R.id.percentrl_output_return:
                    //出库退货
                    Intent intent7=new Intent(this,OutputInventoryActivity.class);
                    intent7.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent7.putExtra(EXTRAConstants.ORDER_PARTY_NO,party_code);
                    intent7.putExtra(EXTRAConstants.ORDER_PARTY_NAME,party_name);
                    intent7.putExtra(EXTRAConstants.INVENTORY_PARTY_CITY,party_city);
                    intent7.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    intent7.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE,address_code);
                    intent7.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION,address_info);
                    intent7.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,contact_person);
                    intent7.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,contact_tel);
                    intent7.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE,"output_return");
                    startActivity(intent7);
                    break;
                case R.id.percentrl_output_other:
                    //其它出库
                    Intent intent8=new Intent(this,OutputInventoryActivity.class);
                    intent8.putExtra(EXTRAConstants.ORDER_PARTY_ID,party_id);
                    intent8.putExtra(EXTRAConstants.ORDER_PARTY_NO,party_code);
                    intent8.putExtra(EXTRAConstants.ORDER_PARTY_NAME,party_name);
                    intent8.putExtra(EXTRAConstants.INVENTORY_PARTY_CITY,party_city);
                    intent8.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,address_idx);
                    intent8.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE,address_code);
                    intent8.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION,address_info);
                    intent8.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,contact_person);
                    intent8.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,contact_tel);
                    intent8.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE,"output_other");
                    startActivity(intent8);
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
            List<PartyProductStock> productStocks= mBiz.getInventoryProductList();
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
            Intent intent =new Intent(this,ProductStockDetailActivity.class);
            intent.putExtra(EXTRAConstants.PRODUCT_STOCKIDX,mProductsApapter.getData().get(position-1).getIDX());
            intent.putExtra(EXTRAConstants.PRODUCT_NO,mProductsApapter.getData().get(position-1).getPRODUCT_NO());
            intent.putExtra(EXTRAConstants.PRODUCT_NAME,mProductsApapter.getData().get(position-1).getPRODUCT_NAME());
            intent.putExtra(EXTRAConstants.INVENTORY_COUNT,mProductsApapter.getData().get(position-1).getSTOCK_QTY());
            intent.putExtra(EXTRAConstants.EDIT_DATE,mProductsApapter.getData().get(position-1).getEDIT_DATE());
            startActivity(intent);
        }catch (Exception e){
            ToastUtil.showToastMid("获取产品库存id失败",Toast.LENGTH_SHORT);
            ExceptionUtil.handlerException(e);
        }

    }
}
