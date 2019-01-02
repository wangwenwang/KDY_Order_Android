package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ChoicedProductAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderBrandsAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderTypesAdapter;
import com.kaidongyuan.app.kdyorder.adapter.PartyInventoryProductAdapter;
import com.kaidongyuan.app.kdyorder.adapter.PaymentTypeAdapter;
import com.kaidongyuan.app.kdyorder.bean.OutPutToAddress;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.OutputInventoryActivityBiz;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.OrderUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${tom} on 2017/9/21.
 */
public class OutputInventoryActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{
    /**
     * Handler 发送消息是已选赠品列表详情显时的 what
     */
    private static final int WHAT_CHOICE_DETIAL_IN = 1;
    /**
     * Handler 发送消息是已选赠品列表详情隐藏时的 what
     */
    private static final int WHAT_CHOICE_DETIAL_OUT = 2;
    /**
     * 跳转到选择收货地址页面的请求码
     */
    private static final int REQUESETCODE_OUTPUTPARTYLIST=10;

    /**
     * 屏幕高度 px
     */
    private static final int SCREEN_HEIGHT = DensityUtil.getHeight();
    /**
     * 已选赠品详情列表显示动画时每次移动的距离 px
     */
    private static final int DETIAL_PICE_PADDING_SIZE_IN = SCREEN_HEIGHT / 10;
    /**
     * 已选赠品详情列表隐藏动画时每次移动的距离 px
     */
    private static final int DETIAL_PICE_PADDING_SIZE_OUT = SCREEN_HEIGHT / 5;
    /**
     * 显示已选赠品列表时发送消息的时间间隔
     */
    private static final int DETIAL_IN_PICE_TIME = 4;
    /**
     * 隐藏已选赠品列表时发送消息的时间间隔
     */
    private static final int DETIAL_OUT_PICE_TIME = 6;

    /**
     * 对应的业务类
     */
    private OutputInventoryActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 用户id
     */
    private String mOrderPartyId;
    /**
     * 客户code
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
    public String mOrderAddressIdx;
    /**
     * 用户地址信息
     */
    private String mOrderAddressInformation;
    /**
     * 客户地址联系人
     */
    private String mOrderAddressContactPerson;
    /**
     * 客户地址联系人电话
     */
    private String mOrderAddressContactTel;
    /**
     * 出库客户编码
     */
    private String mOutPutToPartyCode;
    /**
     * 出库客户名称
     */
    private String mOutPutToPartyName;
    /**
     * 出库客户地址
     */
    private String mOutPutToPartyAddress;
    /**
     * 侧滑控件
     */
    private DrawerLayout mDrawerLayout;
    /**
     * 产品分类列表
     */
    private ListView mListViewProductType;
    /**
     * 产品分类适配器
     */
    private OrderTypesAdapter mOrderTypesAdapter;
    /**
     * 产品分类的按钮
     */
    private LinearLayout mLinearLayoutProductFenlei;
    /**
     * 品牌分类的按钮
     */
    private LinearLayout mLinearLayoutBrands;
    /**
     * 促销信息按钮
     */
    private TextView mTextViewPromotions;
    /**
     * 已选商品详情按钮
     */
    private ImageView mImageViewChoicedProduct;
    /**
     * 提交按钮
     */
    private TextView mTextViewCommit;
    /**
     * 其它信息
     */
    private TextView mTextViewOtherInformations;
    /**
     * 发货信息，收货地址
     */
    private TextView tv_outputfrom_info,tv_outputto_info;
    /**
     * 产品分类列表
     */
    private ExpandableListView mExpandableListViewProduct;
    /**
     * 产品 ListView 适配器
     */
    private PartyInventoryProductAdapter mOrderProductAdapter;
    /**
     * 产品分类下拉刷新控件
     */
    private SwipeRefreshLayout mSwiprefreshLayoutProduct;
    /**
     * 品牌分类列表
     */
    private ListView mListViewBrands;
    /**
     * 品牌分类列表适配器
     */
    private OrderBrandsAdapter mOrderBrandsAdapter;
    /**
     * 已选商品数量
     */
    private TextView mTextViewChoicedProductSize;
    /**
     * 已选商品金额总和
     */
    private TextView mTextViewChoicedProductPrice;
    /**
     * 网络加载时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 确认下单客户地址等信息
     */
    private Dialog mConfirmCustomerInformationDialog;
    /**
     * 选择支付类型的布局
     */
    private LinearLayout mLinearLayoutPaymentType;
    /**
     * 显示支付类型的 ListView
     */
    private ListView mListViewPaymentType;
    /**
     * 支付类型的适配器
     */
    private PaymentTypeAdapter mPaymentTypeAdapter;

    /**
     * 记录此界面是否是第一次显示
     */
    private boolean mIsFirstTimeShow;
    /**
     * 确认用户信息 Dialog 中的取消按钮
     */
    private Button mButtonCancelInConfirmDialog;
    /**
     * 显示支付类型文本框
     */
    private TextView mTextViewPaymentType;
    /**
     * 用户选择的当前品牌
     */
    private TextView mTtextViewCurrentOrderBrand;
    /**
     * 用户选择的当前品类
     */
    private TextView mTextViewCurrentOrderType;
    /**
     * 已选商品布局
     */
    private RelativeLayout mRlChoiceProductdetial;
    /**
     * 已选商品明细列表
     */
    private ListView mListViewChoicegiftdetial;
    /**
     * 已选商品明细列表适配器
     */
    private ChoicedProductAdapter mChoicedProductAdapter;
    /**
     * 处理消息的 Handler
     */
    private Handler mHandler;

    private String strOutputOrderType;
    /**
     * 客户拜访，拜访ID
     */
    private String VISIT_IDX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_output_inventory);
            initData();
            setTop();
            initView();
            setListener();
            getPayTypeData();
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
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            if (mConfirmCustomerInformationDialog != null && mConfirmCustomerInformationDialog.isShowing()) {
                mConfirmCustomerInformationDialog.dismiss();
            }
            mConfirmCustomerInformationDialog = null;
            mImageViewGoBack = null;
            mDrawerLayout = null;
            mListViewProductType = null;
            mOrderTypesAdapter = null;
            mLinearLayoutProductFenlei = null;
            mLinearLayoutBrands = null;
            mTextViewPromotions = null;
            mImageViewChoicedProduct = null;
            mTextViewCommit = null;
            mTextViewOtherInformations = null;
            mExpandableListViewProduct = null;
            mOrderProductAdapter = null;
            mSwiprefreshLayoutProduct = null;
            mListViewBrands = null;
            mOrderBrandsAdapter = null;
            mTextViewChoicedProductSize = null;
            mTextViewChoicedProductPrice = null;
            mLinearLayoutPaymentType = null;
            mListViewPaymentType = null;
            mPaymentTypeAdapter = null;
            mButtonCancelInConfirmDialog = null;
            mTextViewPaymentType = null;
            mTtextViewCurrentOrderBrand = null;
            mTextViewCurrentOrderType = null;
            mRlChoiceProductdetial = null;
            mListViewChoicegiftdetial = null;
            mChoicedProductAdapter = null;
            VISIT_IDX = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mDrawerLayout.isDrawerOpen(mListViewBrands) || mDrawerLayout.isDrawerOpen(mListViewProductType)) {
                mDrawerLayout.closeDrawers();
            } else if (mRlChoiceProductdetial.getVisibility() == View.VISIBLE) {
                choiceProductDetialStrartOut();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mIsFirstTimeShow = true;
            mBiz = new OutputInventoryActivityBiz(this);
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
            if (intent.hasExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson)){
                mOrderAddressContactPerson=intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson);
            }
            if (intent.hasExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel)){
                mOrderAddressContactTel=intent.getStringExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel);
            }
            if (intent.hasExtra(EXTRAConstants.OUTPUT_ORDER_TYPE)){
                strOutputOrderType=intent.getStringExtra(EXTRAConstants.OUTPUT_ORDER_TYPE);
            }
            if(strOutputOrderType.equals("output_visit_sale")) {
                if (intent.hasExtra("OutPutToAddress")) {
                    OutPutToAddress OT = intent.getParcelableExtra("OutPutToAddress");
                    mOutPutToPartyCode = OT.getITEM_CODE();
                    mOutPutToPartyName = OT.getPARTY_NAME();
                    mOutPutToPartyAddress = OT.getADDRESS_INFO();
                }
            }
            if (intent.hasExtra(EXTRAConstants.OUTPUT_VISIT_IDX)){
                VISIT_IDX=intent.getStringExtra(EXTRAConstants.OUTPUT_VISIT_IDX);
            }
            mHandler = new InnerHandler(this);
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
            mLinearLayoutProductFenlei = (LinearLayout) this.findViewById(R.id.linearlayout_producttype);
            mLinearLayoutBrands = (LinearLayout) this.findViewById(R.id.textview_brands);
            mTextViewPromotions = (TextView) this.findViewById(R.id.textview_promotions);
            mTextViewOtherInformations = (TextView) this.findViewById(R.id.textview_other_informations);
            tv_outputfrom_info= (TextView) this.findViewById(R.id.tv_outputfrom_info);

            tv_outputto_info= (TextView) this.findViewById(R.id.tv_outputto_info);
            mListViewProductType = (ListView) this.findViewById(R.id.lv_types);
            mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawerlayout_products);
            mExpandableListViewProduct = (ExpandableListView) this.findViewById(R.id.lv_product);
            if (strOutputOrderType!=null&&strOutputOrderType.equals("output_return")){
                tv_outputfrom_info.setText("收货信息："+String.valueOf(mOrderAddressInformation));
                tv_outputto_info.setText("发货地址：");
                mOrderProductAdapter = new PartyInventoryProductAdapter(this, null, null,false);
            }else if (strOutputOrderType!=null&&strOutputOrderType.equals("output_other")){
                tv_outputfrom_info.setText("发货信息："+String.valueOf(mOrderAddressInformation));
                tv_outputto_info.setVisibility(View.INVISIBLE);
                mOutPutToPartyCode="";
                mOutPutToPartyName="其它出库客户";
                mOutPutToPartyAddress="其它出库客户地址信息";
                mOrderProductAdapter = new PartyInventoryProductAdapter(this, null, null,true);
            }else {
                tv_outputfrom_info.setText("发货信息："+String.valueOf(mOrderAddressInformation));
                tv_outputto_info.setText("收货地址：");
                mOrderProductAdapter = new PartyInventoryProductAdapter(this, null, null,true);
            }
            mExpandableListViewProduct.setAdapter(mOrderProductAdapter);
            mSwiprefreshLayoutProduct = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefreshLayout_lv_product);
            mListViewProductType = (ListView) this.findViewById(R.id.lv_types);
            mOrderTypesAdapter = new OrderTypesAdapter(this, null, 0);
            mListViewProductType.setAdapter(mOrderTypesAdapter);
            mListViewBrands = (ListView) this.findViewById(R.id.lv_brands);
            mOrderBrandsAdapter = new OrderBrandsAdapter(this, null, 0);
            mListViewBrands.setAdapter(mOrderBrandsAdapter);
            mImageViewChoicedProduct = (ImageView) this.findViewById(R.id.iv_shopping_car);
            mTextViewCommit = (TextView) this.findViewById(R.id.textview_commit);
            mTextViewChoicedProductSize = (TextView) this.findViewById(R.id.textview_choiced_product_size);
            mTextViewChoicedProductPrice = (TextView) this.findViewById(R.id.textview_total_price);
            mTtextViewCurrentOrderBrand = (TextView) this.findViewById(R.id.textview_current_brand);
            mTextViewCurrentOrderType = (TextView) this.findViewById(R.id.textview_current_ordertype);
            mRlChoiceProductdetial = (RelativeLayout) this.findViewById(R.id.rl_choiceproductdetial);
            mListViewChoicegiftdetial = (ListView) this.findViewById(R.id.listview_choiceproductdetial);
            mChoicedProductAdapter = new ChoicedProductAdapter(this, null);
            mListViewChoicegiftdetial.setAdapter(mChoicedProductAdapter);
            setProductListViewWidth();

            // 客户拜访 --> 建议订单（销售出库）
            if(strOutputOrderType.equals("output_visit_sale")) {
                tv_outputto_info.setText("发货地址：" + CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mOutPutToPartyAddress));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 根据用户的类型设置分类列表的宽度
     */
    private void setProductListViewWidth() {
        int businessType = OrderUtil.getBusinessType();
        switch (businessType) {
            case BusinessConstants.BUSINESS_TYPE_YIBAO:
                setProductListViewWith(mListViewProductType, 120);
                break;
            case BusinessConstants.BUSINESS_TYPE_DIKUI:
                setProductListViewWith(mListViewProductType, 120);
                break;
            case BusinessConstants.BUSINESS_TYPE_KANGSHIFU:
                setProductListViewWith(mListViewProductType, 200);
                break;
            default:
                setProductListViewWith(mListViewProductType, 120);
                break;
        }
    }

    /**
     * 设置ListView的宽度
     * @param listView 需要设置宽度的 ListView
     * @param widthDp 设置的宽度 dp
     */
    private void setProductListViewWith(ListView listView, int widthDp) {
        try {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.width = DensityUtil.dip2px(widthDp);
            listView.setLayoutParams(params);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mLinearLayoutProductFenlei.setOnClickListener(this);
            mLinearLayoutBrands.setOnClickListener(this);
            mTextViewPromotions.setOnClickListener(this);
            mTextViewOtherInformations.setOnClickListener(this);
            tv_outputto_info.setOnClickListener(this);
            mImageViewChoicedProduct.setOnClickListener(this);
            mTextViewCommit.setOnClickListener(this);
            mSwiprefreshLayoutProduct.setOnRefreshListener(this);
            mListViewBrands.setOnItemClickListener(this);
            mListViewProductType.setOnItemClickListener(this);
            this.findViewById(R.id.bt_hidproductdetail).setOnClickListener(this);
            mOrderProductAdapter.setInterface(new PartyInventoryProductAdapter.OrderProductAdapterInterface() {
                @Override
                public void addProduct(int dataIndex) {
                    mBiz.addProductSize(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void deleteProduct(int dataIndex) {
                    mBiz.deleteProductSize(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void setProductCount(int dataIndex, int giftCount) {
                    mBiz.setProductSize(dataIndex, giftCount);
                    notifyDataChange();
                }
            });
            mChoicedProductAdapter.setInterface(new ChoicedProductAdapter.choicedProductAdapterInterface() {
                @Override
                public void addProduct(int dataIndex) {
                    mBiz.addProductSizeInChoicedProducts(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void deleteProduct(int dataIndex) {
                    mBiz.deleteProductSizeInChoicedProducts(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void setProductCount(int dataIndex, int giftCount) {
                    mBiz.setProductSizeInChoicedProducts(dataIndex, giftCount);
                    notifyDataChange();
                }
            });
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 获取支付类型
     */
    private void getPayTypeData() {
        try {
            if (mBiz.getPayTypeData()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom("获取支付类型" + getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
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
                    this.finish();
                    break;
                case R.id.linearlayout_producttype://选择产品分类
                    if (mDrawerLayout.isDrawerOpen(mListViewProductType)) {
                        mDrawerLayout.closeDrawer(mListViewProductType);
                    } else {
                        mDrawerLayout.openDrawer(mListViewProductType);
                        mDrawerLayout.closeDrawer(mListViewBrands);
                    }
                    break;
                case R.id.textview_brands://选择品牌分类
                    if (mDrawerLayout.isDrawerOpen(mListViewBrands)) {
                        mDrawerLayout.closeDrawer(mListViewBrands);
                    } else {
                        mDrawerLayout.openDrawer(mListViewBrands);
                        mDrawerLayout.closeDrawer(mListViewProductType);
                    }
                    break;
                case R.id.textview_promotions://查看促销信息
                    Intent promotionInformationIntent = new Intent(this, PromotionInformationActivity.class);
                    promotionInformationIntent.putExtra(EXTRAConstants.ORDER_PARTY_ID, mOrderPartyId);
                    startActivity(promotionInformationIntent);
                    break;
                case R.id.textview_other_informations://查看其它信息
                    mLinearLayoutPaymentType.setVisibility(View.GONE);
                    showConfirmDialog();
                    break;
                case R.id.iv_shopping_car://查看已选商品详情
                    if (mRlChoiceProductdetial.getVisibility() == View.VISIBLE) {
                        choiceProductDetialStrartOut();
                    } else {
                        choiceProductDetialStartIn();
                    }
                    break;
                case R.id.bt_hidproductdetail://隐藏已选商品详情
                    choiceProductDetialStrartOut();
                    break;
                case R.id.textview_commit://提交订单，跳转到订单确认界面
                    confirmOrder();
                    break;
                case R.id.button_cancel://点击了确认下单客户信息的取消按钮
                    mConfirmCustomerInformationDialog.dismiss();
                    this.finish();
                    break;
                case R.id.button_confirm://点击了确认下单客户信息的确定按钮
                    mIsFirstTimeShow = false;
                    mConfirmCustomerInformationDialog.dismiss();
                    notifyDataChange();
                    break;
                case R.id.linearlayout_choicepaymenttype://点击了确认下单客户信息中的选择支付方式
                    if (mLinearLayoutPaymentType.getVisibility() == View.VISIBLE) {
                        mLinearLayoutPaymentType.setVisibility(View.GONE);
                    } else {
                        mLinearLayoutPaymentType.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.tv_outputto_info://跳转到选择收货地址的页面
                    // 客户拜访 --> 建议订单（销售出库），不能更改收货信息
                    if(strOutputOrderType.equals("output_visit_sale")) {
                        return;
                    }
                    Intent intent=new Intent(this,OutputPartyListActivity.class);
                    intent.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX,mOrderAddressIdx);
                    startActivityForResult(intent,REQUESETCODE_OUTPUTPARTYLIST);
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 提交订单,到订单确认界面
     */
    private void confirmOrder() {
        try {
            List<Product> choicedProducts = mBiz.getChoiceProducts();
            if (choicedProducts.size() <= 0) {
                ToastUtil.showToastBottom("至少选择一种产品！", Toast.LENGTH_SHORT);
                return;
            }else if (mOutPutToPartyAddress==null||mOutPutToPartyAddress.isEmpty()){
                ToastUtil.showToastBottom("请先选择地址信息！", Toast.LENGTH_LONG);
                return;
            }
            mBiz.setProdcutCurrentPrice();
            Intent intent = new Intent(this, OutPutOrderConfirmActivity.class);
            intent.putParcelableArrayListExtra(EXTRAConstants.CHOICED_PRODUCT_LIST, (ArrayList<? extends Parcelable>) choicedProducts);
            intent.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE, mOrderAddressCode);
            intent.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX, mOrderAddressIdx);
            intent.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION,mOrderAddressInformation);
            intent.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,mOrderAddressContactPerson);
            intent.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,mOrderAddressContactTel);
            intent.putExtra(EXTRAConstants.ORDER_PARTY_NAME,mOrderPartyName);
            intent.putExtra(EXTRAConstants.ORDER_PAYMENT_TYPE, mBiz.getCurrentPayType().getKey());
            intent.putExtra(EXTRAConstants.ORDER_PARTY_ID, mOrderPartyId);
            intent.putExtra(EXTRAConstants.ORDER_PARTY_NO,mOrderPartyCode);
            intent.putExtra(EXTRAConstants.OUTPUT_TOPARTYCODE,mOutPutToPartyCode);
            intent.putExtra(EXTRAConstants.OUTPUT_TOPARTYNAME,mOutPutToPartyName);
            intent.putExtra(EXTRAConstants.OUTPUT_TOADDRESS,mOutPutToPartyAddress);
            intent.putExtra("VISIT_IDX",VISIT_IDX);
            if (strOutputOrderType!=null){
                intent.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE,strOutputOrderType);
            }
            startActivity(intent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onRefresh() {//产品列表下拉刷新
        try {
            mSwiprefreshLayoutProduct.setRefreshing(false);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
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

    /**
     * 获取支付类型数据成功
     */
    public void getPayTypeDataSuccess() {//支付类型获取成功后获取产品类型和品牌类型
        try {
            if (!mBiz.getProductTypesData()) {
                ToastUtil.showToastBottom("获取产品类型和品牌类型" + getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取产品类型和品牌类型成功回调方法
     */
    public void getProductTypesDataSuccess() {//获取产品类型和品牌类型成功后获取产品信息
        try {
            if (mBiz.getProductBrandsType().size() > 0) {
                if (!mBiz.getProductsData(mOrderPartyId, mOrderAddressIdx, 0)) {
                    mLoadingDialog.dismiss();
                    ToastUtil.showToastBottom("获取产品数据" + getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
                mOrderBrandsAdapter.notifyChange(mBiz.getOrderBrands(), mBiz.getCurrentSelectedBrandIndex());
                mTtextViewCurrentOrderBrand.setText(mBiz.getCurrentOrderBrand());
                mOrderTypesAdapter.notifyChange(mBiz.getOrderTypes(), mBiz.getCurrentSelectedOrderTypeIndex());
                mTextViewCurrentOrderType.setText(mBiz.getCurrentProductType());
            } else {
                mLoadingDialog.dismiss();
                ToastUtil.showToastBottom("产品品牌和品类为空！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取产品数据成功时回调的方法
     */
    public void getProductDataSuccess() {//支付类型，产品品牌类型产品品类，产品数据度获取成功后显示数据
        try {
            mDrawerLayout.closeDrawers();
            mLoadingDialog.dismiss();
            List<Product> products = mBiz.getmCurrentProductData();
            if (products.size() <= 0) {
                ToastUtil.showToastBottom("产品数据为空!", Toast.LENGTH_SHORT);
            }
            notifyDataChange();
            mExpandableListViewProduct.setSelectedGroup(0);
            if (mIsFirstTimeShow) {
                showConfirmDialog();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示让用户确认下单客户地址，支付方式等的 Dialog
     */
    private void showConfirmDialog() {
        try {
            TextView textViewPartyName = null;
            TextView textViewPartyAddress = null;
            TextView textViewPartyAddressContact=null;
            if (mConfirmCustomerInformationDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                mConfirmCustomerInformationDialog = builder.create();
                mConfirmCustomerInformationDialog.setCanceledOnTouchOutside(false);
                mConfirmCustomerInformationDialog.setCancelable(false);
                mConfirmCustomerInformationDialog.show();
                Window window = mConfirmCustomerInformationDialog.getWindow();
                window.setContentView(R.layout.dialog_confirm_customer_information);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.CENTER;
                lp.width = DensityUtil.dip2px(DensityUtil.getWidth_dp()*0.8f);//宽高可设置具体大小
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                window.findViewById(R.id.button_confirm).setOnClickListener(this);
                mButtonCancelInConfirmDialog = (Button) window.findViewById(R.id.button_cancel);
                mButtonCancelInConfirmDialog.setOnClickListener(this);
                textViewPartyName = (TextView) window.findViewById(R.id.textview_partyname);
                textViewPartyAddress = (TextView) window.findViewById(R.id.textview_address);
                textViewPartyAddressContact= (TextView) window.findViewById(R.id.textview_contact);
                window.findViewById(R.id.linearlayout_choicepaymenttype).setVisibility(View.GONE);
                mLinearLayoutPaymentType = (LinearLayout) window.findViewById(R.id.linearlayout_paymenttype);
                mLinearLayoutPaymentType.setVisibility(View.GONE);
                mListViewPaymentType = (ListView) window.findViewById(R.id.listView_paymentType);
                mPaymentTypeAdapter = new PaymentTypeAdapter(this, mBiz.getPayTypes());
                mListViewPaymentType.setAdapter(mPaymentTypeAdapter);
                mListViewPaymentType.setOnItemClickListener(this);
                mTextViewPaymentType = (TextView) window.findViewById(R.id.textView_paymentType);
            }
            if (!mIsFirstTimeShow && mButtonCancelInConfirmDialog != null) {
                mButtonCancelInConfirmDialog.setVisibility(View.GONE);
            }
            if (textViewPartyName != null) {
                textViewPartyName.setText(String.valueOf(mOrderPartyName));
            }
            if (textViewPartyAddress != null) {
                textViewPartyAddress.setText(String.valueOf(mOrderAddressInformation));
            }
            if (textViewPartyAddressContact!=null){
                textViewPartyAddressContact.setText(mOrderAddressContactPerson+"\t"+mOrderAddressContactTel);
            }
            if (mTextViewPaymentType != null) {
                String paymentType = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mBiz.getPayType().getText());
                mTextViewPaymentType.setText(paymentType);
            }
            if (mPaymentTypeAdapter != null) {
                mPaymentTypeAdapter.notifyChange(mBiz.getPayTypes());
            }
            if (!mIsFirstTimeShow) {
                mConfirmCustomerInformationDialog.setCancelable(true);
            }
            mConfirmCustomerInformationDialog.show();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 数据发生变化，刷新每个 ListView 中的数据
     */
    private void notifyDataChange() {
        try {
            mDrawerLayout.closeDrawers();
            mChoicedProductAdapter.notifyChange(mBiz.getChoiceProducts());
            mOrderBrandsAdapter.notifyChange(mBiz.getOrderBrands(), mBiz.getCurrentSelectedBrandIndex());
            mOrderTypesAdapter.notifyChange(mBiz.getOrderTypes(), mBiz.getCurrentSelectedOrderTypeIndex());
            mOrderProductAdapter.notifyChange(mBiz.getmCurrentProductData(), mBiz.getChoiceProducts());
            mTextViewCurrentOrderType.setText(String.valueOf(mBiz.getCurrentProductType()));
            mTtextViewCurrentOrderBrand.setText(String.valueOf(mBiz.getCurrentOrderBrand()));
            mTextViewChoicedProductSize.setText(String.valueOf(mBiz.getChoicedProductSize()));
            mTextViewChoicedProductPrice.setText(String.valueOf(mBiz.getChoicedProductPrice()));
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int parentId = parent.getId();
            if (parentId == mListViewPaymentType.getId()) {//更换支付类型
                mBiz.setPaymentType(position);
                String paymentType = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mBiz.getPayType().getText());
                mTextViewPaymentType.setText(paymentType);
                mLinearLayoutPaymentType.setVisibility(View.GONE);
            } else if (parentId == mListViewBrands.getId()) {//更换品牌
                mBiz.setCurrentProductBrand(position);
                mBiz.setCurrentProductType(getString(R.string.all));
                if (mBiz.getProductsData(mOrderPartyId, mOrderAddressIdx, 0)) {
                    showLoadingDialog();
                } else {
                    ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
            } else if (parentId == mListViewProductType.getId()) {//更换品类
                mBiz.setCurrentProductType(position);
                if (mBiz.getProductsData(mOrderPartyId, mOrderAddressIdx, position)) {
                    showLoadingDialog();
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            try {
                switch (requestCode) {
                    case REQUESETCODE_OUTPUTPARTYLIST:
                         mOutPutToPartyCode=data.getStringExtra(EXTRAConstants.OUTPUT_TOPARTYCODE);
                         mOutPutToPartyName=data.getStringExtra(EXTRAConstants.OUTPUT_TOPARTYNAME);
                         mOutPutToPartyAddress=data.getStringExtra(EXTRAConstants.OUTPUT_TOADDRESS);
                        if (strOutputOrderType!=null&&strOutputOrderType.equals("output_return")){
                            tv_outputto_info.setText("发货地址："+CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mOutPutToPartyAddress));
                        }else {
                            tv_outputto_info.setText("收货地址："+CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mOutPutToPartyAddress));
                        }
                        break;
                    default:
                        ToastUtil.showToastBottom(REQUESETCODE_OUTPUTPARTYLIST+"传输失败，请重试", Toast.LENGTH_LONG);
                        break;
                }
            }catch (Exception e){
                ExceptionUtil.handlerException(e);
            }

        }
    }

    /**
     * 处理消息的 Handler 防止内存泄漏
     */
    private static class InnerHandler extends Handler {
        private WeakReference<OutputInventoryActivity> makeOrderActivityWeakReference;

        public InnerHandler(OutputInventoryActivity mActivity) {
            this.makeOrderActivityWeakReference = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                super.handleMessage(msg);
                OutputInventoryActivity activity = makeOrderActivityWeakReference.get();
                if (activity != null) {
                    switch (msg.what) {
                        case WHAT_CHOICE_DETIAL_IN://商品详细信息列表进入
                            choiceProductDetialIn(msg, activity);
                            break;
                        case WHAT_CHOICE_DETIAL_OUT://商品详细列表退出
                            choiceProductDetialOut(msg, activity);
                            break;
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        /**
         * 已选商品信息进入动画
         *
         * @param msg      Message
         * @param activity MakeOrderActivity
         */
        private void choiceProductDetialIn(Message msg, OutputInventoryActivity activity) {
            try {
                int paddingTopIn = msg.arg1 - OutputInventoryActivity.DETIAL_PICE_PADDING_SIZE_IN;
                activity.mRlChoiceProductdetial.setPadding(0, paddingTopIn, 0, 0);
                if (paddingTopIn > 0) {
                    Message message = activity.mHandler.obtainMessage();
                    message.what = OutputInventoryActivity.WHAT_CHOICE_DETIAL_IN;
                    message.arg1 = paddingTopIn;
                    activity.mHandler.sendMessageDelayed(message, OutputInventoryActivity.DETIAL_IN_PICE_TIME);
                } else {
                    activity.mRlChoiceProductdetial.setPadding(0, 0, 0, 0);
                    Logger.w("AddGiftActivity:已选商品详细列表进入动画结束");
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        /**
         * 已选商品信息退出动画
         *
         * @param msg Message
         * @param activity MakeOrderActivity
         */
        private void choiceProductDetialOut(Message msg, OutputInventoryActivity activity) {
            try {
                int paddingTopOut = msg.arg1 + OutputInventoryActivity.DETIAL_PICE_PADDING_SIZE_OUT;
                activity.mRlChoiceProductdetial.setPadding(0, paddingTopOut, 0, 0);
                if (paddingTopOut < OutputInventoryActivity.SCREEN_HEIGHT) {
                    Message messageOut = activity.mHandler.obtainMessage();
                    messageOut.what = OutputInventoryActivity.WHAT_CHOICE_DETIAL_OUT;
                    messageOut.arg1 = paddingTopOut;
                    activity.mHandler.sendMessageDelayed(messageOut, OutputInventoryActivity.DETIAL_OUT_PICE_TIME);
                } else {
                    activity.mRlChoiceProductdetial.setVisibility(View.GONE);
                    Logger.w("OutputInventoryActivity:已选商品详细列退出动画结束");
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 开始已选商品信息进入界面
     */
    private void choiceProductDetialStartIn() {
        try {
            mRlChoiceProductdetial.setVisibility(View.VISIBLE);
            int paddingTop = DensityUtil.getHeight();
            mRlChoiceProductdetial.setPadding(0, paddingTop, 0, 0);
            Message message = mHandler.obtainMessage();
            message.what = WHAT_CHOICE_DETIAL_IN;
            message.arg1 = paddingTop - DETIAL_PICE_PADDING_SIZE_IN;
            mHandler.sendMessageDelayed(message, DETIAL_OUT_PICE_TIME);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 开始已选商品信息退出界面
     */
    private void choiceProductDetialStrartOut() {
        try {
            int paddintTop = 0;
            Message message = mHandler.obtainMessage();
            message.what = WHAT_CHOICE_DETIAL_OUT;
            message.arg1 = paddintTop;
            mHandler.sendMessageDelayed(message, DETIAL_OUT_PICE_TIME);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
