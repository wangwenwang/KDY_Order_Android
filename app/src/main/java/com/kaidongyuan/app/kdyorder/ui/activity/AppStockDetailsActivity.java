package com.kaidongyuan.app.kdyorder.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.AppStocksAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Fleet;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.AppStockDetailsActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.Date;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/7.
 */
public class AppStockDetailsActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;

    private AppStockDetailsActivityBiz mBiz;

    private TextView tv_PARTY_NAME,tv_PARTY_CODE,tv_BUSINESS_NAME,tv_STOCK_DATE,
            tv_SUBMIT_DATE,tv_STOCK_STATE,tv_STOCK_WORKFLOW;//客户库存表单头部基本信息

    //库存表撤销相关控件
    private EditText ed_cancel_explaination;
    private Button bt_cancel_appstock;
    /**
     * 显示已取消订单的 ListView
     */
    private MyListView mAppStocksListView;
    private AppStocksAdapter mAppStockListAdapter;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 是否是第一次点击撤销库存
     */
    private boolean isfirst=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstock_details);
        init();
        initView();
        setListener();
        initData();
    }

    private void initData() {
        try {
            if (mBiz.getStockData()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
          mImageViewGoBack.setOnClickListener(this);
          bt_cancel_appstock.setOnClickListener(this);
    }

    private void initView() {
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        tv_PARTY_NAME= (TextView) this.findViewById(R.id.tv_PARTY_NAME);
        tv_PARTY_CODE= (TextView) this.findViewById(R.id.tv_PARTY_CODE);
        tv_BUSINESS_NAME= (TextView) this.findViewById(R.id.tv_BUSINESS_NAME);
        tv_STOCK_DATE= (TextView) this.findViewById(R.id.tv_STOCK_DATE);
        tv_SUBMIT_DATE= (TextView) this.findViewById(R.id.tv_SUBMIT_DATE);
        tv_STOCK_STATE= (TextView) this.findViewById(R.id.tv_STOCK_STATE);
        tv_STOCK_WORKFLOW= (TextView) this.findViewById(R.id.tv_STOCK_WORKFLOW);
        ed_cancel_explaination= (EditText) this.findViewById(R.id.ed_cancel_explaination);
        bt_cancel_appstock= (Button) this.findViewById(R.id.bt_cancel_appstock);
        mAppStocksListView= (MyListView) this.findViewById(R.id.lv_appstock);
        mAppStockListAdapter=new AppStocksAdapter(null,AppStockDetailsActivity.this);
        mAppStocksListView.setAdapter(mAppStockListAdapter);
    }

    private void init() {
        try {
           String stockIdx=getIntent().getStringExtra(EXTRAConstants.EXTRA_STOCK_IDX);
            mBiz = new AppStockDetailsActivityBiz(this,stockIdx );
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }

    }

    public void cancelSuccess(){
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("此客户库存登记表撤销成功", Toast.LENGTH_SHORT);
            InventoryManageActivity.isrefresh=true;
            this.finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void getStockSuccess() {
        try {
            mLoadingDialog.dismiss();
            Fleet fleet=mBiz.getTmpFleet();
            if (fleet!=null&&fleet.getSUBMIT_DATE()!=null&&!fleet.getSUBMIT_DATE().isEmpty()){
                Date submitDate=StringUtils.str2date(fleet.getSUBMIT_DATE());
                Date currentDate=new Date(System.currentTimeMillis());
                if ((currentDate.getTime()-submitDate.getTime())>24*60*60*1000L||currentDate.getDay()!=submitDate.getDay()){
                    bt_cancel_appstock.setText("已不可撤销");
                    bt_cancel_appstock.setTextColor(getResources().getColor(R.color.gray_holo_light));
                    bt_cancel_appstock.setBackgroundColor(getResources().getColor(R.color.checkorder_topbutton_background_unSelected));
                    bt_cancel_appstock.setOnClickListener(null);
                }
            }
            tv_PARTY_NAME.setText(fleet.getPARTY_NAME());
            tv_PARTY_CODE.setText(fleet.getPARTY_CODE());
            tv_BUSINESS_NAME.setText(fleet.getBUSINESS_NAME());
            tv_STOCK_DATE.setText(fleet.getSTOCK_DATE());
            tv_SUBMIT_DATE.setText(fleet.getSUBMIT_DATE());
            tv_STOCK_STATE.setText(StringUtils.getStockState(fleet.getSTOCK_STATE()));
            tv_STOCK_WORKFLOW.setText(fleet.getSTOCK_WORKFLOW());
            mAppStockListAdapter.setData(mBiz.getmAppStocks());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void getStockError(String msg) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
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
                mLoadingDialog = new MyLoadingDialog(AppStockDetailsActivity.this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_goback:
                    this.finish();
                    break;
                case R.id.bt_cancel_appstock:
                    try {

                        if (isfirst){
                            ToastUtil.showToastBottom("再点击一次将撤销此客户库存详情登记表！", Toast.LENGTH_SHORT);
                            bt_cancel_appstock.setText("确\t认\t撤\t销");
                            isfirst=false;
                        }else {
                            if (mBiz.cancelStock()) {
                                showLoadingDialog();
                            } else {
                                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                            }
                        }

                    }catch (Exception e) {
                        ExceptionUtil.handlerException(e);
                    }
                    break;
            }
    }


}
