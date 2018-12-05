package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.SlideDateTimeListener;
import com.example.mylibrary.SlideDateTimePicker;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ChoicedProductAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderProductAdapter;
import com.kaidongyuan.app.kdyorder.adapter.StockProductAdapter;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.StockProduct;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.MakeAppStockActivityBiz;
import com.kaidongyuan.app.kdyorder.model.MakeOrderActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/8.
 */
public class MakeAppStockActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    private MakeAppStockActivityBiz mBiz;
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 库存月份，填表日期
     */
    private TextView tv_STOCK_DATE,tv_SUBMIT_DATE;
    /**
     * 用户id
     */
    private String mOrderPartyId;
    /**
     * 用户代码
     */
    private String mOrderPartyCode;
    /**
     * 用户名
     */
    private String mOrderPartyName;
    /**
     * 用户地址代码
     */
    private String mOrderAddressCode;
    /**
     * 用户地址 idx
     */
    private String mOrderAddressIdx;
    /**
     * 用户地址信息
     */
    private String mOrderAddressInformation;
    /**
     * 产品分类列表
     */
    private ListView mExpandableListViewProduct;
    /**
     * 产品 ListView 适配器
     */
    private StockProductAdapter mStockProductAdapter;
    /**
     * 底部提交汇总栏textview
     */
    private TextView tv_totalcount,tv_commit;
    /**
     * 网络加载时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     *  填表日期 库存月份
     */
    private Date submitDate,stockDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appstock);
        init();
        initData();
        initView();
        setListener();
    }

    private void initData() {
        try {
            mBiz.setCurrentProductBrand(getString(R.string.all));
            mBiz.setCurrentProductType(getString(R.string.all));
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_ID)) {
                mOrderPartyId = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
            }
            if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_NO)){
                mOrderPartyCode=intent.getStringExtra(EXTRAConstants.ORDER_PARTY_NO);
            }
            if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_NAME)) {
                mOrderPartyName = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_NAME);
            }
            if (intent.hasExtra(EXTRAConstants.ORDER_ADDRESS_CODE)) {
                mOrderAddressCode = intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_CODE);
            }
            if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX)) {
                mOrderAddressIdx = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX);
            }
            if (intent.hasExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION)) {
                mOrderAddressInformation = intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION);
            }
            if (mBiz.getProductsData(mOrderPartyId,mOrderAddressIdx,0)){
              showLoadingDialog();
            }
            submitDate=new Date();
            stockDate=new Date();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initView() {
        mImageViewGoBack= (ImageView) this.findViewById(R.id.button_goback);
        mExpandableListViewProduct = (ListView) this.findViewById(R.id.lv_product);
        mStockProductAdapter = new StockProductAdapter(this, null, null);
        mExpandableListViewProduct.setAdapter(mStockProductAdapter);
        tv_STOCK_DATE= (TextView) this.findViewById(R.id.tv_STOCK_DATE);
        tv_SUBMIT_DATE= (TextView) this.findViewById(R.id.tv_SUBMIT_DATE);
        tv_totalcount= (TextView) this.findViewById(R.id.textview_choiced_product_totalcount);
        tv_commit= (TextView) findViewById(R.id.textview_commit);
    }

    private void init() {
        try {
            mBiz =new MakeAppStockActivityBiz(this);
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
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
    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            this.findViewById(R.id.ll_STOCK_DATE).setOnClickListener(this);
            this.findViewById(R.id.ll_SUBMIT_DATE).setOnClickListener(this);
            tv_commit.setOnClickListener(this);
            this.findViewById(R.id.bt_hidproductdetail).setOnClickListener(this);
            mStockProductAdapter.setInterface(new StockProductAdapter.StockProductAdapterInterface() {
                @Override
                public void addProduct(int dataIndex) {
                    mBiz.addProductSize(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void deleteProduct(StockProduct product) {
                    mBiz.deleteProductSize(product);
                    notifyDataChange();
                }

                @Override
                public void setProductCount(StockProduct product,int count, Date giftCount) {
                    mBiz.setProductSize(product,count,giftCount);
                    notifyDataChange();
                }
            });

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
                super.onDateTimeCancel();
        }

        @Override
        public void onDateTimeSet(Date date) {//设置送货时间
            try {
                switch (which){
                    case R.id.ll_STOCK_DATE:
                        stockDate = date;
                        if (date != null) {
                            String tempdate=DateUtil.formateWithoutTime(date);
                            tv_STOCK_DATE.setText(StringUtils.subyyMM(tempdate));
                        }
                        break;
                    case R.id.ll_SUBMIT_DATE:
                        submitDate=date;
                        if (date!=null){
                            tv_SUBMIT_DATE.setText(DateUtil.formateWithTime(date));
                        }
                        break;

                }



            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }


    /**
     * 数据发生变化，刷新每个 ListView 中的数据
     */
    private void notifyDataChange() {
        try {
            mStockProductAdapter.notifyChange(mBiz.getmCurrentProductData(), mBiz.getChoiceProducts());
            int totalcount=0;
            for (int i=0;i<mBiz.getChoiceProducts().size();i++){
                for (int j=0;j<mBiz.getChoiceProducts().get(i).getPRODUCT_POLICY().size();j++){
                    totalcount=totalcount+mBiz.getChoiceProducts().get(i).getPRODUCT_POLICY().get(j).getSTOCK_QTY();
                }
            }
            tv_totalcount.setText(totalcount+"");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    public void getProductDataSuccess() {
        mLoadingDialog.dismiss();
        List<StockProduct> products = mBiz.getmCurrentProductData();
        if (products.size() <= 0) {
            ToastUtil.showToastBottom("产品数据为空!", Toast.LENGTH_SHORT);
        }
        notifyDataChange();
    }
    /**
     * 网络请求失败时回调方法
     *
     * @param message 要显示的信息
     */
    public void getDataError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_STOCK_DATE:
                new SlideDateTimePicker.Builder((getSupportFragmentManager()))
                        .setListener(new DateHandler(v.getId()))
                        .setInitialDate(new Date())
                        .setMaxDate(new Date(System.currentTimeMillis()+30*24*60*60*1000L))
                        .build()
                        .show();
                break;
            case R.id.ll_SUBMIT_DATE:
                new SlideDateTimePicker.Builder((getSupportFragmentManager()))
                        .setListener(new DateHandler(v.getId()))
                        .setInitialDate(new Date())
                        .setMaxDate(new Date(System.currentTimeMillis()+30*24*60*60*1000L))
                        .build()
                        .show();
                break;
            case R.id.textview_commit:
                if (mBiz.getChoiceProducts().size()>0&&stockDate!=null&&submitDate!=null){
                    Intent intent=new Intent(MakeAppStockActivity.this,InventoryConfirmActivity.class);
                    intent.putExtra(EXTRAConstants.ORDER_PARTY_ID, mOrderPartyId);
                    intent.putExtra(EXTRAConstants.ORDER_PARTY_NO,mOrderPartyCode);
                    intent.putExtra(EXTRAConstants.ORDER_PARTY_NAME, mOrderPartyName);
//                  intent.putExtra(EXTRAConstants.EXTRA_STOCK_DATE,tv_STOCK_DATE.getText()+"");
//                  intent.putExtra(EXTRAConstants.EXTRA_SUBMIT_DATE,tv_SUBMIT_DATE.getText()+"");
                    intent.putExtra(EXTRAConstants.EXTRA_STOCK_DATE,StringUtils.subyyMM(DateUtil.formateWithoutTime(stockDate))+"");
                    intent.putExtra(EXTRAConstants.EXTRA_SUBMIT_DATE,DateUtil.formateWithTime(submitDate)+"");
                    intent.putParcelableArrayListExtra(EXTRAConstants.EXTRA_STOCK_PRODUCT, (ArrayList<? extends Parcelable>) mBiz.getChoiceProducts());
                    startActivity(intent);
                }else {
                    Toast.makeText(MakeAppStockActivity.this,"请至少填一条产品库存信息",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_goback:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
