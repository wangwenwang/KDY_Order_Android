package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.TimeNodeAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.StateTack;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.OrderTimeNodeActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 * 订单进度 Activity
 */
public class OrderTimeNodeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private OrderTimeNodeActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 显示时间节点的 ListView
     */
    private XListView mListViewTimeNode;
    /**
     * 显示时间节点 ListView 的适配器
     */
    private TimeNodeAdapter mTimeNodeAdapter;
    /**
     * 无数据是显示的文本框
     */
    private TextView mTextViewNoData;
    /**
     * 网络加载时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_time_node);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getOrderTimeNodeData();
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
            this.mLoadingDialog = null;
            this.mBiz.cancelRequest();
            this.mBiz = null;
            this.mImageViewGoBack = null;
            this.mTextViewNoData = null;
            this.mListViewTimeNode = null;
            this.mTimeNodeAdapter = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new OrderTimeNodeActivityBiz(this);
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
            this.mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            this.mTextViewNoData = (TextView) this.findViewById(R.id.textview_nodata);

            this.mListViewTimeNode = (XListView) this.findViewById(R.id.lv_time_node);
            this.mTimeNodeAdapter = new TimeNodeAdapter(this, null);
            mListViewTimeNode.setAdapter(mTimeNodeAdapter);
            mListViewTimeNode.setPullLoadEnable(false);
            mListViewTimeNode.setPullRefreshEnable(false);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 获取订单时间界面数据
     */
    public void getOrderTimeNodeData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID)) {
                String orderId = intent.getStringExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID);
                if (orderId == null || orderId.length() <= 0) {
                    ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
                } else {
                    if (mBiz.getOrderTimeNodeData(orderId)) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }
            } else {
                ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
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
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求失败是调用的方法
     *
     * @param message 显示的信息
     */
    public void getOrderTimeNodeError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求成功调用的方法
     */
    public void geOrderTimeNodeSuccess() {
        try {
            mLoadingDialog.dismiss();
            Order order = mBiz.getOrder();
            if (order != null) {
                List<StateTack> states = order.getStateTack();
                mTimeNodeAdapter.notifyChange(states);
                if (states.size() > 0) {
                    mTextViewNoData.setVisibility(View.GONE);
                } else {
                    mTextViewNoData.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtil.showToastBottom("订单进度为空！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

}












