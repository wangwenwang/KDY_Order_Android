package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.BillFeeListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OrderListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.PartyListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.BillFee;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.BillFeeListActivityBiz;
import com.kaidongyuan.app.kdyorder.model.InventoryPartyActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class BillFeeListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 下单业务类
     */
    private BillFeeListActivityBiz mBiz;
    /**
     * 客户列表 ListView
     */
    private XListView mBillFeeListView;
    /**
     * 客户列表
     */
    private BillFeeListAdapter mBillFeeListAdapter;
    /**
     * 没有数据时显示的控件，默认为不显示
     */
    private TextView mTextviewNodata;
    /**
     * 网络请求是的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    private String partyIdx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billfee_list);
        initData();
        initView();
        setListener();
        initBillFees();
    }

    private void initBillFees() {
        try {
            if (mBiz.getinitBillFees()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
        }
    }

    private void initView() {
        try {
            this.mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            this.mTextviewNodata = (TextView) this.findViewById(R.id.textview_nodata);
            mTextviewNodata.setVisibility(View.GONE);

            this.mBillFeeListView = (XListView) this.findViewById(R.id.lv_billfee_list);
            mBillFeeListView.setPullLoadEnable(true);
            mBillFeeListView.setPullRefreshEnable(true);
            this.mBillFeeListAdapter = new BillFeeListAdapter(null, BillFeeListActivity.this);
            mBillFeeListView.setAdapter(mBillFeeListAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            if (getIntent().hasExtra(EXTRAConstants.ORDER_PARTY_ID)){
                partyIdx = getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
                mBiz = new BillFeeListActivityBiz(this, partyIdx);
            }else {
                finish();
            }

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        mImageViewGoBack.setOnClickListener(this);
        mTextviewNodata.setOnClickListener(this);
        mBillFeeListView.setOnItemClickListener(this);
        mBillFeeListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {//刷新数据
                if (mBiz.reFreshCompleteOrderData()) {
                    showLoadingDialog();
                } else {
                    ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onLoadMore() {//加载更多数据
                if (mBiz.loadMoreCompleteOrderData()) {
                    showLoadingDialog();
                } else {
                    ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void loginError(String msg) {
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
            mBillFeeListView.stopRefresh();
            mBillFeeListView.stopLoadMore();
            mLoadingDialog.dismiss();
            List<BillFee> billfees = mBiz.getmTotalBillFeeList();
            if (billfees == null || billfees.size() <= 0) {
                mTextviewNodata.setVisibility(View.VISIBLE);
                return;
            } else {
                mTextviewNodata.setVisibility(View.GONE);
                if (billfees.size()<10){
                    mBillFeeListView.setFooterViewTxt("无更多数据");
                }
            }
            mBillFeeListAdapter.setData(billfees);

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据成功时业务类调用的方法
     */
    public void loginSuccess() {
        try {
            handleRequest();
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
            mBillFeeListAdapter = null;
            mBillFeeListView = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
        super.onDestroy();
    }
    /**
     * 显示 DownloadingDialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(BillFeeListActivity.this);
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
            case R.id.textview_nodata:
                try {
                    if (mBiz.getinitBillFees()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }catch (Exception ex){
                    ExceptionUtil.handlerException(ex);
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent businessFeeIntent = new Intent(BillFeeListActivity.this, BusinessFeeActivity.class);
            businessFeeIntent.putExtra(EXTRAConstants.EXTRA_MONTH_DATE, mBiz.getmTotalBillFeeList().get(position - 1).getBILL_DATE());
            businessFeeIntent.putExtra(EXTRAConstants.ORDER_PARTY_ID,partyIdx);
            this.startActivity(businessFeeIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
