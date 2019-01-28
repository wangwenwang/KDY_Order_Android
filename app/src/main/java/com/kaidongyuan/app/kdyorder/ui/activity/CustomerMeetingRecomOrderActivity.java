package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.OutputSimpleOrderListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.FatherAddress;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.bean.OutPutToAddress;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.model.AgentOrderListAdapter;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingRecomOrderActivityBiz;
import com.kaidongyuan.app.kdyorder.model.OutputOrderlistActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerMeetingRecomOrderActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener   {

    // 显示出库单
    private CustomerMeetingRecomOrderActivityBiz mBiz;
    private XListView mOutputOrderListView;
    private OutputSimpleOrderListAdapter mAdapter;
    private AgentOrderListAdapter mAgentAdapter;

    CustomerMeeting customerM;

    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    // 备注
    private EditText remark;

    private Context mContext;

    private final String mTagRecomOrder = "mTagRecomOrder";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_customer_recommend_order);
        initData();
        initView();
        setListener();
        mBiz.GetFatherAddress(customerM.getADDRESS_IDX());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (customerM.getGRADE().equals("0")) {

            ToastUtil.showToastBottom(String.valueOf("当前被拜访的客户为供货商，无出库单"), Toast.LENGTH_SHORT);
        }
        //『供货商』对『经销商』的入库单
        else if (customerM.getGRADE().equals("1")) {

            this.mAgentAdapter = new AgentOrderListAdapter(null, CustomerMeetingRecomOrderActivity.this);
            mOutputOrderListView.setAdapter(mAgentAdapter);
            mBiz.GetVisitAppOrder_AGENT(customerM.getVISIT_IDX(), "AppOrder");
        }
        //『经销商』对『门店』的出库单
        else if (customerM.getGRADE().equals("2")) {

            this.mAdapter = new OutputSimpleOrderListAdapter(null, CustomerMeetingRecomOrderActivity.this);
            mOutputOrderListView.setAdapter(mAdapter);
            mBiz.GetVisitAppOrder(customerM.getVISIT_IDX(), "OutPut");
        } else {

            ToastUtil.showToastBottom(String.valueOf("未知客户类型，字段GRADE"), Toast.LENGTH_SHORT);
        }
    }

    private void initData() {
        customerM = (CustomerMeeting) getIntent().getParcelableExtra("CustomerMeeting");
        mBiz = new CustomerMeetingRecomOrderActivityBiz(this);
    }

    private void initView() {
        try {
            remark = (EditText) findViewById(R.id.recommend_mark);
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);

            this.mOutputOrderListView = (XListView) this.findViewById(R.id.lv_outputorder_list);
            mOutputOrderListView.setPullRefreshEnable(false);

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void makeOrderOnclick(View view) {

        if (customerM.getGRADE().equals("0")) {

            ToastUtil.showToastBottom(String.valueOf("当前被拜访的客户为供货商，无出库单"), Toast.LENGTH_SHORT);
        }
        //『供货商』对『经销商』的入库单
        else if (customerM.getGRADE().equals("1")) {

            try {
                if (mBiz.getPartygetAddressInfo(customerM.getIDX())) {//发送请求成功,集合中数据的 Index 比 ListView 中的Index 小 1，考虑 headerView
                    showLoadingDialog();
                } else {//发送请求失败
                    ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
        //『经销商』对『门店』的出库单
        else if (customerM.getGRADE().equals("2")) {

            FatherAddress FM = mBiz.fatherAddressM;

            // 建议订单
            if (FM != null) {
                // 收货信息
                OutPutToAddress OT = new OutPutToAddress();
                OT.setADDRESS_INFO(customerM.getPARTY_ADDRESS());
                OT.setCONTACT_PERSON(customerM.getCONTACTS());
                OT.setCONTACT_TEL(customerM.getCONTACTS_TEL());
                OT.setIDX(customerM.getADDRESS_IDX());
                OT.setITEM_CODE(customerM.getPARTY_NO());
                OT.setPARTY_NAME(customerM.getPARTY_NAME());

                // 发货信息
                Intent intent4 = new Intent(this, OutputInventoryActivity.class);
                intent4.putExtra(EXTRAConstants.ORDER_PARTY_ID, FM.getIDX());
                intent4.putExtra(EXTRAConstants.ORDER_PARTY_NO, FM.getPARTY_CODE());
                intent4.putExtra(EXTRAConstants.ORDER_PARTY_NAME, FM.getPARTY_NAME());
                intent4.putExtra(EXTRAConstants.INVENTORY_PARTY_CITY, FM.getPARTY_CITY());
                intent4.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX, FM.getADDRESS_IDX());
                intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE, FM.getADDRESS_CODE());
                intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION, FM.getADDRESS_INFO());
                intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson, FM.getCONTACT_PERSON());
                intent4.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel, FM.getCONTACT_TEL());
                intent4.putExtra(EXTRAConstants.OUTPUT_ORDER_TYPE, "output_visit_sale");
                intent4.putExtra("OutPutToAddress", (Parcelable) OT);
                intent4.putExtra(EXTRAConstants.OUTPUT_VISIT_IDX, customerM.getVISIT_IDX());

                startActivity(intent4);
            } else {
                ToastUtil.showToastBottom(String.valueOf("找不到上级"), Toast.LENGTH_SHORT);
            }
        } else {

            ToastUtil.showToastBottom(String.valueOf("未知客户类型，字段GRADE"), Toast.LENGTH_SHORT);
        }
    }

    public void confirmOnclick(View view) {
        showLoadingDialog();
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitRecommendedOrder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".RecomOrder:" + response);
                    RecomOrderSuccess(response, customerM);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".RecomOrder:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mLoadingDialog.dismiss();
                        ToastUtil.showToastBottom(String.valueOf("请求失败!"), Toast.LENGTH_SHORT);
                    } else {
                        mLoadingDialog.dismiss();
                        ToastUtil.showToastBottom(String.valueOf("请检查网络是否正常连接！"), Toast.LENGTH_SHORT);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strVisitIdx", customerM.getVISIT_IDX());
                    params.put("strRecommendedOrder", remark.getText().toString());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagRecomOrder);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            super.onDestroy();
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            HttpUtil.cancelRequest(mTagRecomOrder);
            mImageViewGoBack = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求是显示 Dilaog
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

    /**
     * 处理网络请求返回数据成功
     *
     * @param response 返回的数据
     */
    private void RecomOrderSuccess(String response, CustomerMeeting customerM) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            mLoadingDialog.dismiss();
            if (type == 1) {

                Intent intent = new Intent(this, CustomerMeetingDisplayActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                startActivity(intent);
            } else {

                ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            ToastUtil.showToastBottom("服务器返回数据异常！", Toast.LENGTH_SHORT);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener((View.OnClickListener) this);
            mOutputOrderListView.setOnItemClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback:
                    this.finish();
                    break;
                default:
                    break;
            }
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
     * 获取数据失败时业务类调用的方法
     */
    public void loginError(String msg) {
        try {
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
            List<OutPutSimpleOrder> outPutSimpleOrders = mBiz.getmOutputSimpleOrderList();
            mAdapter.setData(outPutSimpleOrders);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void GetVisitAppOrderSuccess() {

    }

    public void GetFatherAddressSuccess() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //『供货商』对『经销商』的入库单
        if (mBiz.getmOrderList().size() > 0) {
            try {
                Intent orderDetailsIntent = new Intent(mContext, OrderDetailsActivity.class);
                orderDetailsIntent.putExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID, mBiz.getmOrderList().get(position - 1).getIDX());
                mContext.startActivity(orderDetailsIntent);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
        //『经销商』对『门店』的出库单
        else{
            try {
                Intent outputOrderDetailIntent = new Intent(this, OutPutOrderDetailActivity.class);
                outputOrderDetailIntent.putExtra(EXTRAConstants.EXTRA_ORDER_IDX, mBiz.getmOutputSimpleOrderList().get(position - 1).getIDX());
                this.startActivity(outputOrderDetailIntent);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }
    // 入库单
    public void GetVisitAppOrderSuccess_AGENT() {
        try {
            List<Order> orders = mBiz.getmOrderList();
            mAgentAdapter.setData(orders);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 处理获取客户地址成功
     */
    public void getCustomerAdressDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            List<Address> customerAddress = mBiz.getmCustomerAddress();
            int customerAddressSize = customerAddress.size();
            if (customerAddressSize == 1) {
                jumpToMakeOrderActivity(customerAddress.get(0));
            } else if (customerAddressSize > 1) {
                jumpToMakeOrderActivity(customerAddress.get(0));
                ToastUtil.showToastBottom(String.valueOf("客户有多个地址，已默认第1个"), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 跳转到编辑库存列表页面
     * @param address 下单客户地址
     */
    private void jumpToMakeOrderActivity(Address address) {
        try {
            Intent makeOrderIntent = new Intent(CustomerMeetingRecomOrderActivity.this, MakeOrderActivity.class);
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_PARTY_ID,customerM.getIDX());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_PARTY_NAME, customerM.getPARTY_NAME());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE, address.getADDRESS_CODE());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX, address.getIDX());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION, address.getADDRESS_INFO());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,address.getCONTACT_PERSON());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,address.getCONTACT_TEL());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_VISIT_ID,customerM.getVISIT_IDX());
            startActivity(makeOrderIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}