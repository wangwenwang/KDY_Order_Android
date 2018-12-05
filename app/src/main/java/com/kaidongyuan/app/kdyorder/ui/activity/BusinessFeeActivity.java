package com.kaidongyuan.app.kdyorder.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.BusinessFeeListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderDetailsAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.BillFee;
import com.kaidongyuan.app.kdyorder.bean.BusinessFee;
import com.kaidongyuan.app.kdyorder.bean.BusinessFeeItem;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.BillFeeListActivityBiz;
import com.kaidongyuan.app.kdyorder.model.BusinessFeeActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ListViewUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/30.
 */
public class BusinessFeeActivity extends BaseActivity implements View.OnClickListener {
   private String mouth,partyIdx;
   private BusinessFeeActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    /**
     * 账单信息
     */
    private TextView tv_MOUTH_DATE,tv_PARTY_CODE,tv_PARTY_NAME,tv_BUSINESS_CODE,tv_BUSINESS_NAME,
            tv_LastMonth,tv_ThisMonthPostive,tv_ThisMonthMinus,tv_ThisMonth,textViewNoData,tv_check_businessfeecost;
    private ListView lv_businessfeelist;
    /**
     * 账单费用条目适配器
     */
    private BusinessFeeListAdapter mAdapter;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_fee);
        initData();
        initView();
        setListener();
        getBusinessFee();
    }

    private void getBusinessFee() {
        try {
            if (mBiz.getBusinessFeeData()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
        }
    }

    private void initData() {
        try {
            if (getIntent().hasExtra(EXTRAConstants.ORDER_PARTY_ID)&&getIntent().hasExtra(EXTRAConstants.EXTRA_MONTH_DATE)){
                partyIdx = getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
                mouth=getIntent().getStringExtra(EXTRAConstants.EXTRA_MONTH_DATE);
                mBiz = new BusinessFeeActivityBiz(this,mouth,partyIdx);
            }

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            this.tv_MOUTH_DATE = (TextView) this.findViewById(R.id.tv_MOUTH_DATE);
            this.tv_PARTY_CODE = (TextView) this.findViewById(R.id.tv_PARTY_CODE);
            this.tv_PARTY_NAME = (TextView) this.findViewById(R.id.tv_PARTY_NAME);
            this.tv_BUSINESS_CODE = (TextView) this.findViewById(R.id.tv_BUSINESS_CODE);
            this.tv_BUSINESS_NAME = (TextView) this.findViewById(R.id.tv_BUSINESS_NAME);
            this.tv_LastMonth = (TextView) this.findViewById(R.id.tv_LastMonth);
            this.tv_ThisMonthPostive = (TextView) this.findViewById(R.id.tv_ThisMonthPostive);
            this.tv_ThisMonthMinus = (TextView) this.findViewById(R.id.tv_ThisMonthMinus);
            this.tv_ThisMonth = (TextView) this.findViewById(R.id.tv_ThisMonth);
            this.textViewNoData= (TextView) this.findViewById(R.id.textview_nodata);
            this.tv_check_businessfeecost= (TextView) this.findViewById(R.id.tv_check_businessfeecost);
            this.lv_businessfeelist= (ListView) this.findViewById(R.id.lv_businessfeelist);
            mAdapter=new BusinessFeeListAdapter(null,BusinessFeeActivity.this);
            this.lv_businessfeelist.setAdapter(mAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        mImageViewGoBack.setOnClickListener(this);
        textViewNoData.setOnClickListener(this);
        tv_check_businessfeecost.setOnClickListener(this);
    }
    /**
     * 显示 DownloadingDialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(BusinessFeeActivity.this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mBiz.cancelRequest();
            mLoadingDialog = null;
            mBiz = null;

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
        super.onDestroy();
    }


    public void getfeeDetailsDataSuccess() {
            try {
                mLoadingDialog.dismiss();
                handleRequest();
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }

    }

    public void getfeeDetailsDataError(String message) {
        try {
            mLoadingDialog.dismiss();
            if (message!=null&&!message.isEmpty()){
                ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 处理获取在途订单后的结果
     */
    private void handleRequest() {
        try {

             BusinessFee mfee = mBiz.getfee();
            if (mfee == null ) {
                textViewNoData.setVisibility(View.VISIBLE);
                return;
            } else {
                textViewNoData.setVisibility(View.GONE);
            }
            tv_MOUTH_DATE.setText(mouth+"月份");
            tv_BUSINESS_CODE.setText(mfee.getBUSINESS_CODE());
            tv_BUSINESS_NAME.setText(mfee.getBUSINESS_NAME());
            tv_PARTY_CODE.setText(mfee.getPARTY_CODE());
            tv_PARTY_NAME.setText(mfee.getPARTY_NAME());
            tv_LastMonth.setText(mfee.getLastMonth());
            tv_ThisMonth.setText(mfee.getThisMonth());
            tv_ThisMonthMinus.setText(mfee.getThisMonthMinus());
            tv_ThisMonthPostive.setText(mfee.getThisMonthPostive());
            List<BusinessFeeItem> businessFeeItems=mBiz.getBusinessFeeItems();
            if (businessFeeItems!=null&&businessFeeItems.size()>0){
                tv_check_businessfeecost.setVisibility(View.VISIBLE);
                mAdapter.setData(businessFeeItems);
                ListViewUtils.setListViewHeightBasedOnChildren(lv_businessfeelist);
            }
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
            case R.id.textview_nodata:
                try {
                    if (mBiz.getBusinessFeeData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }catch (Exception ex){
                    ExceptionUtil.handlerException(ex);
                }
                break;
            case R.id.tv_check_businessfeecost:
                try {
                    if (mBiz.getBusinessFeeItems()==null&&mBiz.getBusinessFeeItems().size()<=0){
                        ToastUtil.showToastBottom("未获取到账单明细条目",Toast.LENGTH_SHORT);
                        return;
                    }else if (lv_businessfeelist.getVisibility()!=View.VISIBLE){
                        lv_businessfeelist.setVisibility(View.VISIBLE);
                    }else {
                        lv_businessfeelist.setVisibility(View.GONE);
                    }
                }catch (Exception ex){
                    ExceptionUtil.handlerException(ex);
                }

                break;
            default:
                return;
        }
    }
}
