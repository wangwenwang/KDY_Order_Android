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
import com.kaidongyuan.app.kdyorder.adapter.OutPutOrderProductListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.BusinessFee;
import com.kaidongyuan.app.kdyorder.bean.BusinessFeeItem;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderInfo;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderProduct;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.BusinessFeeActivityBiz;
import com.kaidongyuan.app.kdyorder.model.OutPutOrderDetailActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ListViewUtils;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/30.
 */
public class OutPutOrderDetailActivity extends BaseActivity implements View.OnClickListener {
   private String orderIdx;
   private OutPutOrderDetailActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    /**
     * 账单信息
     */
    private TextView tv_OUTPUT_NO,tv_ADD_DATE,tv_ADDRESS_NAME,tv_ADDRESS_INFO,tv_PARTY_NAME,
            tv_PARTY_INFO,textview_nodata;
    private ListView lv_outputorder_productlist;

    private Button bt_cancel,bt_confirm;
    /**
     * 账单费用条目适配器
     */
    private OutPutOrderProductListAdapter mAdapter;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outputorder_detail);
        initData();
        initView();
        setListener();
        getBusinessFee();
    }

    private void getBusinessFee() {
        try {
            if (mBiz.getOutPutOrderData()) {
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
            if (getIntent().hasExtra(EXTRAConstants.EXTRA_ORDER_IDX)){
                orderIdx = getIntent().getStringExtra(EXTRAConstants.EXTRA_ORDER_IDX);
                mBiz = new OutPutOrderDetailActivityBiz(this,orderIdx);
            }

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            this.tv_OUTPUT_NO = (TextView) this.findViewById(R.id.tv_OUTPUT_NO);
            this.tv_ADD_DATE = (TextView) this.findViewById(R.id.tv_ADD_DATE);
            this.tv_PARTY_NAME = (TextView) this.findViewById(R.id.tv_PARTY_NAME);
            this.tv_ADDRESS_NAME = (TextView) this.findViewById(R.id.tv_ADDRESS_NAME);
            this.tv_ADDRESS_INFO = (TextView) this.findViewById(R.id.tv_ADDRESS_INFO);
            this.tv_PARTY_NAME = (TextView) this.findViewById(R.id.tv_PARTY_NAME);
            this.tv_PARTY_INFO = (TextView) this.findViewById(R.id.tv_PARTY_INFO);
            this.textview_nodata= (TextView) this.findViewById(R.id.textview_nodata);
            this.lv_outputorder_productlist= (ListView) this.findViewById(R.id.lv_outputorder_productlist);
            mAdapter=new OutPutOrderProductListAdapter(null,OutPutOrderDetailActivity.this);
            this.lv_outputorder_productlist.setAdapter(mAdapter);
            this.bt_confirm= (Button) this.findViewById(R.id.bt_confirm);
            this.bt_cancel= (Button) this.findViewById(R.id.bt_cancel);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        mImageViewGoBack.setOnClickListener(this);
        textview_nodata.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
    }
    /**
     * 显示 DownloadingDialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(OutPutOrderDetailActivity.this);
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
            if (mAdapter==null||mAdapter.getCount()<=0){
                textview_nodata.setVisibility(View.VISIBLE);
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
             OutPutOrderInfo minfo = mBiz.getInfo();
            List<OutPutOrderProduct> mList=mBiz.getList();
            if (minfo == null||mList==null||mList.size()<=0 ) {
                textview_nodata.setVisibility(View.VISIBLE);
                return;
            } else {
                textview_nodata.setVisibility(View.GONE);
            }
            tv_OUTPUT_NO.setText(minfo.getOUTPUT_NO());
            tv_ADD_DATE.setText(minfo.getADD_DATE());
            tv_ADDRESS_NAME.setText(minfo.getADDRESS_NAME());
            tv_ADDRESS_INFO.setText(minfo.getADDRESS_INFO());
            tv_PARTY_NAME.setText(minfo.getPARTY_NAME());
            tv_PARTY_INFO.setText(minfo.getPARTY_INFO());
            mAdapter.setData(mList);
            if (minfo.getOUTPUT_WORKFLOW()!=null&&minfo.getOUTPUT_WORKFLOW().equals("新建")
                    &&minfo.getOUTPUT_STATE()!=null&&minfo.getOUTPUT_STATE().equals("OPEN")){
                bt_confirm.setVisibility(View.VISIBLE);
                bt_cancel.setVisibility(View.VISIBLE);
                if (minfo.getOUTPUT_TYPE()!=null&&minfo.getOUTPUT_TYPE().equals("出库退库")){
                    bt_confirm.setText("确认已退货");
                    bt_cancel.setText("取消此单");
                }
            }else {
                bt_confirm.setVisibility(View.GONE);
                bt_cancel.setVisibility(View.GONE);
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
                    if (mBiz.getOutPutOrderData()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }catch (Exception ex){
                    ExceptionUtil.handlerException(ex);
                }
                break;
            case R.id.bt_cancel:
                try {
                    if (mBiz.cancelOutPutOrder()) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }catch (Exception ex){
                    ExceptionUtil.handlerException(ex);
                }
                break;
            case R.id.bt_confirm:
                try {
                    if (mBiz.confirmOutPutOrder()) {
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

    public void cancelOutPutOrderSuccess() {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastMid("成功撤销此出库单，请刷新出库单列表数据！",Toast.LENGTH_LONG);
            this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    public void cancelOutPutOrderError(String msg){
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastMid(msg,Toast.LENGTH_LONG);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    public void confirmOutPutOrderSuccess() {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastMid("成功确认此出库单，请刷新出库单列表数据！",Toast.LENGTH_LONG);
            this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    public void confirmOutPutOrderError(String msg){
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastMid(msg,Toast.LENGTH_LONG);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
