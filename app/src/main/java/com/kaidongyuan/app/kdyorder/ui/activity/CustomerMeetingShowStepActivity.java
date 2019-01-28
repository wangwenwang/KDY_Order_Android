package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.kaidongyuan.app.kdyorder.adapter.GridImageShowAdapter;
import com.kaidongyuan.app.kdyorder.adapter.OutputSimpleOrderListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.model.AgentOrderListAdapter;
import com.kaidongyuan.app.kdyorder.model.CheckOrderPictureActivityBiz;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingCreateActivityBiz;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingRecomOrderActivityBiz;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingShowStepActivityBiz;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingsActivityBiz;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMeetingShowStepActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /**
     * 对应业务类
     */
    private CustomerMeetingShowStepActivityBiz mBiz;

    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    private CustomerMeeting customerM;

    private final String mTagLeaveShop = "mTagLeaveShop";

    // 是否查看拜访（ture查看拜访/false离店）
    public Boolean isShowStep;

    private RecyclerView recyclerView_Visit;
    private RecyclerView recyclerView_VisitVividDisplay;
    private GridImageShowAdapter adapter;

    private TextView ACTUAL_VISITING_ADDRESS;
    private TextView PARTY_NO;
    private TextView PARTY_NAME;
    private TextView CONTACTS;
    private TextView CONTACTS_TEL;
    private TextView CHECK_INVENTORY;
    private TextView RECOMMENDED_ORDER;
    private TextView VIVID_DISPLAY_TEXT;
    private TextView VISIT_STATES;

    // 显示出库单
    private XListView mOutputOrderListView;
    private OutputSimpleOrderListAdapter mAdapter;
    private AgentOrderListAdapter mAgentAdapter;

    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_show_step);

        mContext = this;

        initData();
        initView();
        setData();
        setListener();

        fillInfo();
    }

    private void initData() {

        customerM = (CustomerMeeting) getIntent().getParcelableExtra("CustomerMeeting");
        if (customerM != null) {
            isShowStep = getIntent().getBooleanExtra("isShowStep", true);
            mBiz = new CustomerMeetingShowStepActivityBiz(this);
            Log.d("LM", "发出请求|进店照片");
            mBiz.GetPictureByVisitIdx(customerM.getVISIT_IDX(), "Visit");
            Log.d("LM", "发出请求|生动化陈列照片");
            mBiz.GetPictureByVisitIdx(customerM.getVISIT_IDX(), "VisitVividDisplay");
        } else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.no_data), Toast.LENGTH_SHORT);
            finish();
        }
    }

    private void setData() {

        ACTUAL_VISITING_ADDRESS.setText(customerM.getACTUAL_VISITING_ADDRESS());
        PARTY_NO.setText(customerM.getPARTY_NO());
        PARTY_NAME.setText(customerM.getPARTY_NAME());
        CONTACTS.setText(customerM.getCONTACTS());
        CONTACTS_TEL.setText(customerM.getCONTACTS_TEL());

        CHECK_INVENTORY.setText(customerM.getCHECK_INVENTORY());
        RECOMMENDED_ORDER.setText(customerM.getRECOMMENDED_ORDER());
        VIVID_DISPLAY_TEXT.setText(customerM.getVIVID_DISPLAY_TEXT());
        VISIT_STATES.setText(customerM.getVISIT_STATES());
    }

    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            TextView textView = (TextView) this.findViewById(R.id.textView_title);
            ACTUAL_VISITING_ADDRESS = (TextView) this.findViewById(R.id.ACTUAL_VISITING_ADDRESS);
            PARTY_NO = (TextView) this.findViewById(R.id.PARTY_NO);
            PARTY_NAME = (TextView) this.findViewById(R.id.PARTY_NAME);
            CONTACTS = (TextView) this.findViewById(R.id.CONTACTS);
            CONTACTS_TEL = (TextView) this.findViewById(R.id.CONTACTS_TEL);
            CHECK_INVENTORY = (TextView) this.findViewById(R.id.CHECK_INVENTORY);
            RECOMMENDED_ORDER = (TextView) this.findViewById(R.id.RECOMMENDED_ORDER);
            VIVID_DISPLAY_TEXT = (TextView) this.findViewById(R.id.VIVID_DISPLAY_TEXT);
            VISIT_STATES = (TextView) this.findViewById(R.id.VISIT_STATES);
            Button button_leave_shop = (Button) this.findViewById(R.id.button_leave_shop);
            View button_leave_shop_space = (View) this.findViewById(R.id.button_leave_shop_space);
            if (isShowStep) {
                textView.setText("查看拜访");
                button_leave_shop.setVisibility(View.GONE);
                button_leave_shop_space.setVisibility(View.GONE);
            } else {
                textView.setText("离店");
                button_leave_shop.setVisibility(View.VISIBLE);
                button_leave_shop_space.setVisibility(View.VISIBLE);
            }
            // 展示订单
//            this.mTextviewNodata = (TextView) this.findViewById(R.id.textview_nodata);
//            mTextviewNodata.setVisibility(View.GONE);
            this.mOutputOrderListView = (XListView) this.findViewById(R.id.lv_outputorder_list);
            mOutputOrderListView.setPullRefreshEnable(false);


            if (customerM.getGRADE().equals("0")) {

                ToastUtil.showToastBottom(String.valueOf("当前被拜访的客户为供货商，无出库单"), Toast.LENGTH_SHORT);
            }
            //『供货商』对『经销商』的入库单
            else if (customerM.getGRADE().equals("1")) {

                this.mAgentAdapter = new AgentOrderListAdapter(null, CustomerMeetingShowStepActivity.this);
                mOutputOrderListView.setAdapter(mAgentAdapter);
                mBiz.GetVisitAppOrder_AGENT(customerM.getVISIT_IDX(), "AppOrder");
            }
            //『经销商』对『门店』的出库单
            else if (customerM.getGRADE().equals("2")) {

                this.mAdapter = new OutputSimpleOrderListAdapter(null, CustomerMeetingShowStepActivity.this);
                mOutputOrderListView.setAdapter(mAdapter);
                mBiz.GetVisitAppOrder(customerM.getVISIT_IDX(), "OutPut");
            } else {

                ToastUtil.showToastBottom(String.valueOf("未知客户类型，字段GRADE"), Toast.LENGTH_SHORT);
            }

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void confirmOnclick(View view) {

        final CustomerMeeting customerM = (CustomerMeeting) getIntent().getParcelableExtra("CustomerMeeting");
        showLoadingDialog();
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetVisitLeaveShop, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".LeaveShop:" + response);
                    LeaveShopSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + ".LeaveShop:" + error.toString());
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
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagLeaveShop);
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
            HttpUtil.cancelRequest(mTagLeaveShop);
            mImageViewGoBack = null;
            mBiz.cancelRequest();
            mBiz = null;
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
    private void LeaveShopSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            mLoadingDialog.dismiss();
            if (type == 1) {

                Intent intent = new Intent(this, CustomerMeetingsActivity.class);
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
            mImageViewGoBack.setOnClickListener(this);
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
     * 返回拜访照片成功
     *
     * @param
     */
    public void GetPictureByVisitIdxSuccess(String msg, String strStep) {
        try {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);

            if (strStep.equals("Visit")) {
                final ArrayList<LocalMedia> selectList = new ArrayList<LocalMedia>();
                for (int i = 0; i < mBiz.VisitUrls.size(); i++) {
                    LocalMedia LM = new LocalMedia();
                    LM.setPath(mBiz.VisitUrls.get(i));
                    selectList.add(LM);
                }
                recyclerView_Visit = (RecyclerView) findViewById(R.id.recycler_Visit);
                FullyGridLayoutManager manager = new FullyGridLayoutManager(CustomerMeetingShowStepActivity.this, 4, GridLayoutManager.VERTICAL, false);
                recyclerView_Visit.setLayoutManager(manager);
                adapter = new GridImageShowAdapter(CustomerMeetingShowStepActivity.this, null);
                adapter.setList(selectList);
                adapter.setSelectMax(0);
                recyclerView_Visit.setAdapter(adapter);
                adapter.setOnItemClickListener(new GridImageShowAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if (selectList.size() > 0) {
                            LocalMedia media = selectList.get(position);
                            String pictureType = media.getPictureType();
                            int mediaType = PictureMimeType.pictureToVideo(pictureType);
                            switch (mediaType) {
                                case 1:
                                    // 预览图片 可自定长按保存路径
                                    PictureSelector.create(CustomerMeetingShowStepActivity.this).externalPicturePreview(position, selectList);
                                    break;
                            }
                        }
                    }
                });
            } else if (strStep.equals("VisitVividDisplay")) {
                final ArrayList<LocalMedia> selectList = new ArrayList<LocalMedia>();
                for (int i = 0; i < mBiz.VisitVividDisplayUrls.size(); i++) {
                    LocalMedia LM = new LocalMedia();
                    LM.setPath(mBiz.VisitVividDisplayUrls.get(i));
                    selectList.add(LM);
                }
                recyclerView_VisitVividDisplay = (RecyclerView) findViewById(R.id.recycler_VisitVividDisplay);
                FullyGridLayoutManager manager = new FullyGridLayoutManager(CustomerMeetingShowStepActivity.this, 4, GridLayoutManager.VERTICAL, false);
                recyclerView_VisitVividDisplay.setLayoutManager(manager);
                adapter = new GridImageShowAdapter(CustomerMeetingShowStepActivity.this, null);
                adapter.setList(selectList);
                adapter.setSelectMax(0);
                recyclerView_VisitVividDisplay.setAdapter(adapter);
                adapter.setOnItemClickListener(new GridImageShowAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if (selectList.size() > 0) {
                            LocalMedia media = selectList.get(position);
                            String pictureType = media.getPictureType();
                            int mediaType = PictureMimeType.pictureToVideo(pictureType);
                            switch (mediaType) {
                                case 1:
                                    // 预览图片 可自定长按保存路径
                                    PictureSelector.create(CustomerMeetingShowStepActivity.this).externalPicturePreview(position, selectList);
                                    break;
                            }
                        }
                    }
                });
            }


        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 请求报错
     *
     * @param
     */
    public void requestError(String msg) {
        try {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(msg, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据成功时业务类调用的方法
     */
    // 出库单
    public void GetVisitAppOrderSuccess() {
        try {
            List<OutPutSimpleOrder> outPutSimpleOrders = mBiz.getmOutputSimpleOrderList();
            mAdapter.setData(outPutSimpleOrders);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
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

    //为listview动态设置高度（有多少条目就显示多少条目）
    public void setListViewHeight() {
//        //获取listView的adapter
//        OutputSimpleOrderListAdapter adapter_lm = (OutputSimpleOrderListAdapter) mOutputOrderListView.getAdapter();
//        if (adapter_lm == null) {
//            return;
//        }
//        int totalHeight = 0;
//        //listAdapter.getCount()返回数据项的数目
//        for (int i = 0,len = adapter_lm.getCount(); i < len; i++) {
//            View listItem = adapter_lm.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        // listView.getDividerHeight()获取子项间分隔符占用的高度
//        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = mOutputOrderListView.getLayoutParams();
//        params.height = totalHeight + (mOutputOrderListView.getDividerHeight() *  (listAdapter .getCount() - 1));
        params.height = 300;
        mOutputOrderListView.setLayoutParams(params);
    }

    void fillInfo() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetPartyVisitList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + ".GetPartyVisitList:" + response);
                    JSONObject object = JSON.parseObject(response);
                    int type = object.getInteger("type");
                    if (type == 1) {
                        List<CustomerMeeting> tmpCustomerMeetings = new ArrayList<>();
                        if (object.containsKey("result")) {
                            tmpCustomerMeetings = JSON.parseArray(object.getString("result"), CustomerMeeting.class);
                            for (int i = 0; i < tmpCustomerMeetings.size(); i++) {
                                CustomerMeeting m = tmpCustomerMeetings.get(i);
                                Log.d("LM", "寻找拜访id: " + customerM.getVISIT_IDX());
                                if (customerM.getVISIT_IDX().equals(m.getVISIT_IDX())) {

                                    Log.d("LM", "已找到" + m.getVISIT_IDX());
                                    customerM.setACTUAL_VISITING_ADDRESS(m.getACTUAL_VISITING_ADDRESS());
                                    customerM.setCHECK_INVENTORY(m.getCHECK_INVENTORY());
                                    customerM.setRECOMMENDED_ORDER(m.getRECOMMENDED_ORDER());
                                    customerM.setVIVID_DISPLAY_TEXT(m.getVIVID_DISPLAY_TEXT());
                                    customerM.setVISIT_STATES(m.getVISIT_STATES());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            ACTUAL_VISITING_ADDRESS.setText(customerM.getACTUAL_VISITING_ADDRESS());
                                            CHECK_INVENTORY.setText(customerM.getCHECK_INVENTORY());
                                            RECOMMENDED_ORDER.setText(customerM.getRECOMMENDED_ORDER());
                                            VIVID_DISPLAY_TEXT.setText(customerM.getVIVID_DISPLAY_TEXT());
                                            VISIT_STATES.setText(customerM.getVISIT_STATES());
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserID", MyApplication.getInstance().getUser().getIDX());
                    params.put("strSearch", "");
                    params.put("strLine", "全部");
                    params.put("strStates", "全部");
                    params.put("strPage", "1");
                    params.put("strPageCount", "99");
                    params.put("strFartherPartyID", customerM.getFARTHER_ADDRESS_ID());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);

        }
    }
}