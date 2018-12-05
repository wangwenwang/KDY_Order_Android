package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.FleetListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Fleet;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.InventoryManageActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * 库存登记列表activity
 * Created by Administrator on 2017/6/7.
 */
public class InventoryManageActivity extends BaseActivity implements View.OnClickListener ,AdapterView.OnItemClickListener {
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;

    private InventoryManageActivityBiz mBiz;
    /**
     * 显示已取消订单的 ListView
     */
    private XListView mFleetSocklistListView;
    private FleetListAdapter mfleetListAdapter;
    public static boolean isrefresh=false;
    private LinearLayout ll_no_record;

    private TextView makeAppStock;//创建客户库存表
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_manage);
        init();
        initView();
        setListner();
        initData();
    }

    private void initData() {
        try {
            if (mBiz.getFirstFleetData()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListner() {
        mImageViewGoBack.setOnClickListener(this);
        makeAppStock.setOnClickListener(this);
        mFleetSocklistListView.setOnItemClickListener(this);
        mFleetSocklistListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {//刷新数据
                if (mBiz.reFreshFleetData()) {
                    showLoadingDialog();
                } else {
                    ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onLoadMore() {//加载更多数据
                if (mBiz.loadMoreFleetData()) {
                    showLoadingDialog();
                } else {
                    ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
            }
        });
            ll_no_record.setOnClickListener(this);
    }

    private void init() {
        try {
            mBiz = new InventoryManageActivityBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    private void initView() {
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        makeAppStock= (TextView) this.findViewById(R.id.tv_makeAppStock);
        ll_no_record= (LinearLayout) this.findViewById(R.id.ll_no_record);
        ll_no_record.setVisibility(View.GONE);
        mFleetSocklistListView = (XListView) this.findViewById(R.id.lv_stock_fleet);
        mFleetSocklistListView.setPullLoadEnable(true);
        mFleetSocklistListView.setPullRefreshEnable(true);
        mfleetListAdapter=new FleetListAdapter(null,InventoryManageActivity.this);
        mFleetSocklistListView.setAdapter(mfleetListAdapter);
    }


    /**
     * 显示 DownloadingDialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(InventoryManageActivity.this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isrefresh){
            initData();
        }
    }

    /**
     *
     * @param hasmore 大于0时表示不会再有更多数据了
     */
    public void getStockListSuccess(Integer hasmore) {
        try {
            if (hasmore>0){
                mFleetSocklistListView.setPullLoadEnable(false);
            }
            handleRequest();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void getStockListError(String msg){
        try {
            handleRequest();
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }

    }
    /**
     * 处理获取已取消订单后的结果
     */
    private void handleRequest() {
        try {
            mFleetSocklistListView.stopRefresh();
            mFleetSocklistListView.stopLoadMore();
            mLoadingDialog.dismiss();
            List<Fleet> fleets = mBiz.getmFleets();
            if (fleets == null || fleets.size() <= 0) {
                ll_no_record.setVisibility(View.VISIBLE);
            } else {
                ll_no_record.setVisibility(View.GONE);
            }
            mfleetListAdapter.setData(fleets);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }



    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mBiz.cancelRequest();
            mBiz = null;
            mImageViewGoBack = null;
            mFleetSocklistListView = null;
            mfleetListAdapter = null;
            ll_no_record = null;
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
            case R.id.ll_no_record:
                initData();
                break;
            case R.id.tv_makeAppStock:
                Intent intent=new Intent(InventoryManageActivity.this,InventoryPartyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //进入查看客户库存详细登记表
        try {
            Intent appStockDetailsIntent = new Intent(InventoryManageActivity.this, AppStockDetailsActivity.class);
            appStockDetailsIntent.putExtra(EXTRAConstants.EXTRA_STOCK_IDX, mBiz.getmFleets().get(position-1).getIDX());
            startActivity(appStockDetailsIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
