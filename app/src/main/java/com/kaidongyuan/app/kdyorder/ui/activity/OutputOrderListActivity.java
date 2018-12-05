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
import com.kaidongyuan.app.kdyorder.adapter.OutputSimpleOrderListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.BillFee;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.BillFeeListActivityBiz;
import com.kaidongyuan.app.kdyorder.model.OutputOrderlistActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * Created by ${tom} on 2017/9/25.
 */
public class OutputOrderListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 出库单业务类
     */
    private OutputOrderlistActivityBiz mBiz;
    /**
     * 客户列表 ListView
     */
    private XListView mOutputOrderListView;
    /**
     * 客户列表
     */
    private OutputSimpleOrderListAdapter mAdapter;
    /**
     * 没有数据时显示的控件，默认为不显示
     */
    private TextView mTextviewNodata;
    /**
     * 网络请求是的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    private String partyIdx;
    public String addressIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_orderlist);
        initData();
        initView();
        setListener();
        initOutputOrderlist();
    }

    private void initOutputOrderlist() {
        try {
            if (mBiz.getinitOutPutOrders()) {
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

            this.mOutputOrderListView = (XListView) this.findViewById(R.id.lv_outputorder_list);
            mOutputOrderListView.setPullLoadEnable(true);
            mOutputOrderListView.setPullRefreshEnable(true);
            this.mAdapter = new OutputSimpleOrderListAdapter(null, OutputOrderListActivity.this);
            mOutputOrderListView.setAdapter(mAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            if (getIntent().hasExtra(EXTRAConstants.ORDER_PARTY_ID)&&getIntent().hasExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX)){
                partyIdx = getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
                addressIdx=getIntent().getStringExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX);
                mBiz = new OutputOrderlistActivityBiz(this, partyIdx);
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
        mOutputOrderListView.setOnItemClickListener(this);
        mOutputOrderListView.setXListViewListener(new XListView.IXListViewListener() {
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
            mOutputOrderListView.stopRefresh();
            mOutputOrderListView.stopLoadMore();
            mLoadingDialog.dismiss();
            List<OutPutSimpleOrder> outPutSimpleOrders = mBiz.getmOutputSimpleOrderList();
            if (outPutSimpleOrders == null || outPutSimpleOrders.size() <= 0) {
                mTextviewNodata.setVisibility(View.VISIBLE);
                return;
            } else {
                mTextviewNodata.setVisibility(View.GONE);
                if (outPutSimpleOrders.size()<20){
                    mOutputOrderListView.setFooterViewTxt("无更多数据");
                }
            }
            mAdapter.setData(outPutSimpleOrders);

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
            mAdapter = null;
            mOutputOrderListView = null;
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
                mLoadingDialog = new MyLoadingDialog(OutputOrderListActivity.this);
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
                    if (mBiz.getinitOutPutOrders()) {
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
            Intent outputOrderDetailIntent = new Intent(OutputOrderListActivity.this, OutPutOrderDetailActivity.class);
            outputOrderDetailIntent.putExtra(EXTRAConstants.EXTRA_ORDER_IDX,mBiz.getmOutputSimpleOrderList().get(position-1).getIDX());
            this.startActivity(outputOrderDetailIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
