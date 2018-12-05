package com.kaidongyuan.app.kdyorder.ui.pagerinviewpage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.OrderListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.PreOrderListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OrderPlanSimple;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.CheckOrderFragmentIntransOrderPagerBiz;
import com.kaidongyuan.app.kdyorder.model.CheckPreOrderFragmentUnAuditOrderPagerBiz;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderDetailsActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PreOrderDetailsActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;


/**
 * Created by Administrator on 2016/4/16.
 * 在途订单的界面
 */
public class CheckPreOrderFragmentUnAuditOrderPage implements PagerNecessaryInterface, AdapterView.OnItemClickListener, View.OnClickListener {

    private Context mContext;
    private View mViewParent;

    /**
     * 在途订单界面业务类
     */
    private CheckPreOrderFragmentUnAuditOrderPagerBiz mBiz;

    /**
     * 用于 Pager 初次显示是加载数据的判断
     */
    private boolean mShouldInitData = true;
    /**
     * 显示在途订单的 ListView
     */
    private XListView mInTransOrderListView;
    /**
     * 在途订单 ListView 适配器
     */
    private PreOrderListAdapter mOrderListApapter;
    /**
     * 没有数据是显示的提示框
     */
    private TextView mTextViewNoData;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;


    public CheckPreOrderFragmentUnAuditOrderPage(Context context) {
        try {
            this.mContext = context;
            init();
            initView();
            setListener();
            Logger.w(this.getClass() + ":onCreate");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void init() {
        try {
            mBiz = new CheckPreOrderFragmentUnAuditOrderPagerBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public View getSelf() {
        return mViewParent;
    }

    private void initView() {
        try {
            this.mViewParent = View.inflate(mContext, R.layout.page_checkorderfragment_intranspager, null);
            this.mTextViewNoData = (TextView) mViewParent.findViewById(R.id.textview_nodata);
            mTextViewNoData.setVisibility(View.GONE);

            this.mInTransOrderListView = (XListView) mViewParent.findViewById(R.id.lv_inTransOrder);
            mInTransOrderListView.setPullLoadEnable(true);
            mInTransOrderListView.setPullRefreshEnable(true);
            this.mOrderListApapter = new PreOrderListAdapter(mContext, null);
            mInTransOrderListView.setAdapter(mOrderListApapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mInTransOrderListView.setOnItemClickListener(this);
            mTextViewNoData.setOnClickListener(this);
            mInTransOrderListView.setXListViewListener(new XListView.IXListViewListener() {
                @Override
                public void onRefresh() {//刷新数据
                    if (mBiz.reFreshIntransOrderData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onLoadMore() {//加载更多数据
                    if (mBiz.loadMoreIntransOrderData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }
            });
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void initData() {
        try {
            if (mShouldInitData) {
                getOrderData();
                Logger.w(this.getClass() + ":initData");
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取在途订单信息
     */
    private void getOrderData() {
        if (mBiz.getInTransOrderData()) {
            showLoadingDialog();
            mShouldInitData = false;
        } else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onDestroy() {//销毁时移除请求，将 Dialog 隐藏
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mBiz.cancelRequest();
            mLoadingDialog = null;
            mBiz = null;
            mOrderListApapter = null;
            mInTransOrderListView = null;
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
                mLoadingDialog = new MyLoadingDialog(mContext);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 加载失败时业务类回调方法
     *
     * @param msg 显示的信息
     */
    public void loginError(String msg) {
        try {
            handleRequest();
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
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

    /**
     * 处理获取在途订单后的结果
     */
    private void handleRequest() {
        try {
            mInTransOrderListView.stopRefresh();
            mInTransOrderListView.stopLoadMore();
            mLoadingDialog.dismiss();
            List<OrderPlanSimple> orders = mBiz.getOrderList();
            if (orders == null || orders.size() <= 0) {
                mTextViewNoData.setVisibility(View.VISIBLE);
            } else {
                mTextViewNoData.setVisibility(View.GONE);
            }
            mOrderListApapter.notifyChange(orders);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//跳转到订单详情界面,选择的为position-1，headview
        try {
            Intent orderDetailsIntent = new Intent(mContext, PreOrderDetailsActivity.class);
            orderDetailsIntent.putExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID, mBiz.getOrderList().get(position - 1).getIDX());
            mContext.startActivity(orderDetailsIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_nodata://重新加载数据
                getOrderData();
                break;
        }
    }
}





















