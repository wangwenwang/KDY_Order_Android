package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.TmsOrderListAdapter;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.TmsOrder;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.CheckTmsOrderListBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * Created by ${tom} on 2018/1/3.
 */
public class CheckTmsOrderListActivity extends BaseFragmentActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    private CheckTmsOrderListBiz mBiz;
    /**
     * 物流订单列表
     */
    private XListView mListview;
    private TmsOrderListAdapter mAdapter;
    private TextView textViewNodata,textViewSelect;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    private String partyAddressidx;
    //筛选客户订单列表的返回值请求码
    private final int SELECTPARTY_REQUESTCODE=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checktmsorders);
        initView();
        partyAddressidx="";
        initData();
        setListener();
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            textViewSelect.setOnClickListener(this);
            textViewNodata.setOnClickListener(this);
            mListview.setOnItemClickListener(this);
            mListview.setXListViewListener(new XListView.IXListViewListener() {
                @Override
                public void onRefresh() {
                    try {
                        if (mBiz.reFreshTmsOrderData(partyAddressidx)){
                            showLoadingDialog();
                        }else {
                            ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLoadMore() {
                    try {
                        if (mBiz.loadMoreTmsOrderData()){
                            showLoadingDialog();
                        }else {
                            ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent tmsOrderDetailsIntent = new Intent(this.getApplicationContext(), TmsOrderActivity.class);
            tmsOrderDetailsIntent.putExtra(EXTRAConstants.TMS_ORDER,mBiz.getOrderList().get(position-1));
            startActivity(tmsOrderDetailsIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
       try {
           if (mBiz==null){
               mBiz=new CheckTmsOrderListBiz(this);
           }
           if (mBiz.getTmsOrderData(partyAddressidx)){
                showLoadingDialog();
           }else {
               ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    /**
     * 显示网络请求是的 Dialog
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
    private void initView() {
        mImageViewGoBack= (ImageView) this.findViewById(R.id.button_goback);
        mListview= (XListView) this.findViewById(R.id.lv_tmsOrder);
        mListview.setPullLoadEnable(true);
        mListview.setPullRefreshEnable(true);
        mAdapter=new TmsOrderListAdapter(this,null);
        mListview.setAdapter(mAdapter);
        textViewNodata= (TextView) this.findViewById(R.id.textview_nodata);
        textViewSelect= (TextView) this.findViewById(R.id.textView_select);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_goback:
                finish();
                break;
            case R.id.textView_select:
                Intent selectPartyInfoIntent=new Intent(this,TmsOrderPartyActivity.class);
                startActivityForResult(selectPartyInfoIntent,SELECTPARTY_REQUESTCODE);
                break;
            case R.id.textview_nodata:
                try {
                    if (mBiz==null){
                        mBiz=new CheckTmsOrderListBiz(this);
                    }
                    if (mBiz.getTmsOrderData(partyAddressidx)){
                        showLoadingDialog();
                    }else {
                        ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void getDataSuccess() {
        try {
            handleRequest();
        }catch (Exception e){
            ToastUtil.showToastMid("返回数据展示失败",Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    public void getDataError(String msg) {
        try {
            handleRequest();
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理获取在途订单后的结果
     */
    private void handleRequest() {
        try {
            mListview.stopRefresh();
            mListview.stopLoadMore();
            mLoadingDialog.dismiss();
            List<TmsOrder> orders=mBiz.getOrderList();
            if (orders == null || orders.size() <= 0) {
                textViewNodata.setVisibility(View.VISIBLE);
            } else {
                textViewNodata.setVisibility(View.GONE);
            }
            mAdapter.notifyChange(orders);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       try {
           if (resultCode==RESULT_OK){
               switch (requestCode){
                   case SELECTPARTY_REQUESTCODE:
                       if (data!=null&&data.hasExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX)){
                           partyAddressidx=data.getStringExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX);
                           CheckTmsOrderListActivity.this.initData();
                       }else {
                           ToastUtil.showToastMid("所选客户条件丢失，请重新筛选",Toast.LENGTH_SHORT);
                       }
                       break;
                   default:
                       break;
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }

    }
}
