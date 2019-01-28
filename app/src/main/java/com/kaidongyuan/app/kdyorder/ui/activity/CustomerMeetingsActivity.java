package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.CustomerMeetingAdapter;
import com.kaidongyuan.app.kdyorder.adapter.FirstPartyChoiceAdapter;
import com.kaidongyuan.app.kdyorder.adapter.LineChoiceAdapter;
import com.kaidongyuan.app.kdyorder.adapter.StateChoiceAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.interfaces.OnClickListenerStrInterface;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingsActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 * 客户拜访列表界面
 */
public class CustomerMeetingsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener, OnClickListenerStrInterface {

    /**
     * 对应业务类
     */
    private CustomerMeetingsActivityBiz mBiz;

    /**
     * 显示最新资讯的 ListView
     */
    private XListView mXlistViewInformations;
    /**
     * 客户拜访列表 ListView 的 Adapter
     */
    private CustomerMeetingAdapter mAdapter;
    /**
     * 没有数据是显示的提示框
     */
    private TextView mTextViewNoData;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    private TextView tvTitleRight;
    private LinearLayout llMeetingType, llMeetingState, llmeetingFirstParty;
    private TextView tvMeetingType, tvMeetingState, tvMeetingFirstParty;
    private EditText edSearch;
    private ImageView ivSearch;
    public String strSearch = "";
    public String strLine = "";
    public String strState = "未拜访";//已拜访,未拜访,拜访中,全部
    public String strFartherPartyID = "";//供货商ID
    private List<String> statelist = new ArrayList<String>(Arrays.asList("未拜访", "拜访中", "已拜访", "全部"));
    /**
     * 选择供货商的 Dialog
     */
    private Dialog mChoiceFirstPartyDialog;
    /**
     * 显示供货商的 ListView
     */
    private ListView mListViewChoiceFirstPartys;
    /**
     * 选择供货商的 Adapter
     */
    private FirstPartyChoiceAdapter mFirstPartysChoiceAdapter;
    /**
     * 记录当前选中的报表在 ListView 中的位置
     */
    private int mCurrentFirstPartyIndex = 0;
    /**
     * 选择拜访线路的 Dialog
     */
    private Dialog mChoiceLineDialog;
    /**
     * 显示拜访线路的 ListView
     */
    private ListView mListViewChoiceLines;
    /**
     * 选择拜访线路的 Adapter
     */
    private LineChoiceAdapter mLinesChoiceAdapter;
    /**
     * 记录当前选中的报表在 ListView 中的位置
     */
    private int mCurrentLineIndex = 0;
    /**
     * 选择拜访状态的 Dialog
     */
    private Dialog mChoiceStateDialog;
    /**
     * 显示拜访状态的 ListView
     */
    private ListView mListViewChoiceStates;
    /**
     * 选择拜访状态的 Adapter
     */
    private StateChoiceAdapter mStatesChoiceAdapter;
    /**
     * 记录当前选中的状态在 ListView 中的位置
     */
    private int mCurrentStateIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_meetings);
        Log.d("LM", "onCreate: ");

        try {
            initData();
            setTop();
            initView();
            setListener();

            // 先请求供货商
            new Thread() {
                public void run() {
                    try {
                        sleep(100 * 3);
                        getMeetingLineDatas();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            getMeetingFirstPartyDatas();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LM", "onRestart: ");
        mBiz.reFreshCustomerMeetingDatas();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LM", "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LM", "onResume: ");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LM", "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LM", "onStop: ");
    }

    @Override
    protected void onDestroy() {
        Log.d("LM", "onDestroy: ");
        try {
            super.onDestroy();
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mBiz.cancelRequest();
            mBiz = null;
            mXlistViewInformations = null;
            mAdapter = null;
            mTextViewNoData = null;
            mImageViewGoBack = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        mBiz = new CustomerMeetingsActivityBiz(this);
    }

    private void setTop() {
        //版本4.4以上设置状态栏透明，界面布满整个界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View topView = findViewById(R.id.topview);
            ViewGroup.LayoutParams topParams = topView.getLayoutParams();
            topParams.height = DensityUtil.getStatusHeight() * 16 / 30;
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            tvMeetingFirstParty = (TextView) findViewById(R.id.tv_meeting_first_party);
            llmeetingFirstParty = (LinearLayout) findViewById(R.id.ll_meeting_first_party);
            tvTitleRight = (TextView) findViewById(R.id.tv_title_right);
            llMeetingType = (LinearLayout) findViewById(R.id.ll_meeting_type);
            tvMeetingType = (TextView) findViewById(R.id.tv_meeting_type);
            llMeetingState = (LinearLayout) findViewById(R.id.ll_meeting_state);
            tvMeetingState = (TextView) findViewById(R.id.tv_meeting_state);
            edSearch = (EditText) findViewById(R.id.ed_search);
            ivSearch = (ImageView) findViewById(R.id.iv_search);
            this.mXlistViewInformations = (XListView) this.findViewById(R.id.lv_customer_meetings);
            mXlistViewInformations.setPullLoadEnable(false);
            mXlistViewInformations.setPullRefreshEnable(false);
            mAdapter = new CustomerMeetingAdapter(this, null);
            mXlistViewInformations.setAdapter(mAdapter);
            this.mTextViewNoData = (TextView) this.findViewById(R.id.textview_nodata);
            this.mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mAdapter.setOnClickListenerStr(this);
            tvTitleRight.setOnClickListener(this);
            llMeetingType.setOnClickListener(this);
            llMeetingState.setOnClickListener(this);
            llmeetingFirstParty.setOnClickListener(this);
            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    strSearch = charSequence.toString().trim();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            ivSearch.setOnClickListener(this);
            mXlistViewInformations.setPullRefreshEnable(true);
            mXlistViewInformations.setPullLoadEnable(true);
            mXlistViewInformations.setXListViewListener(this);
            mXlistViewInformations.setOnItemClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取客户拜访线路数据集
     */
    private void getMeetingLineDatas() {
        try {
            if (mBiz.GetPartyVisitLines()) {
                //  showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取客户拜访供货商数据集
     */
    private void getMeetingFirstPartyDatas() {
        try {
            if (mBiz.GetFirstPartyList()) {
                //  showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
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
     * 获取客户拜访线路类型失败
     *
     * @param message 要显示的消息
     */
    public void getMeetingLinesError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取客户拜访列表失败
     *
     * @param message 要显示的消息
     */
    public void getMeetingsDataError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
            handleGetCustomerMeeingsData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void getMeetingFirstPartySuccess(List<CustomerMeeting> customerMeetings) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        if (customerMeetings != null && customerMeetings.size() > 0) {
            strFartherPartyID = customerMeetings.get(0).getIDX();
            tvMeetingFirstParty.setText(customerMeetings.get(0).getPARTY_NAME());
        }
    }

    public void getMeetingLinesSuccess(List<CustomerMeetingLine> customerMeetingLines) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        if (customerMeetingLines != null && customerMeetingLines.size() > 0) {
            // 拜访线路定位到当天
            for (int i = 0; i < customerMeetingLines.size(); i++) {
                String line = customerMeetingLines.get(i).getITEM_NAME();
                if (line.equals(DateUtil.GetCurrWeek())) {
                    strLine = line;
                    break;
                }
            }
            strLine = strLine.equals("") ? customerMeetingLines.get(0).getITEM_NAME() : strLine;
            tvMeetingType.setText(strLine);
            if (mBiz.reFreshCustomerMeetingDatas()) {
                showLoadingDialog();
            }

        }
    }

    /**
     * 显示选择供货商 Dialog
     */
    private void showChoiceFartherIdDialog() {
        try {
            if (mChoiceFirstPartyDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceFirstPartyDialog = builder.show();
                mChoiceFirstPartyDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceFirstPartyDialog.getWindow();
                window.setContentView(R.layout.dialog_state_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceFirstPartyDialog.dismiss();
                    }
                });
                mListViewChoiceFirstPartys = (ListView) window.findViewById(R.id.listView_chart_choice);
                mFirstPartysChoiceAdapter = new FirstPartyChoiceAdapter(null, this);
                mListViewChoiceFirstPartys.setAdapter(mFirstPartysChoiceAdapter);
                mListViewChoiceFirstPartys.setOnItemClickListener(new CustomerMeetingsActivity.InnerOnItemClickListener());
            }
            mChoiceFirstPartyDialog.show();
            mFirstPartysChoiceAdapter.notifyChange(mBiz.getMeetingFirstPartys());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示选择线路 Dialog
     */
    private void showChoiceLineDialog() {
        try {
            if (mChoiceLineDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceLineDialog = builder.show();
                mChoiceLineDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceLineDialog.getWindow();
                window.setContentView(R.layout.dialog_line_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceLineDialog.dismiss();
                    }
                });
                mListViewChoiceLines = (ListView) window.findViewById(R.id.listView_chart_choice);
                mLinesChoiceAdapter = new LineChoiceAdapter(null, this);
                mListViewChoiceLines.setAdapter(mLinesChoiceAdapter);
                mListViewChoiceLines.setOnItemClickListener(new CustomerMeetingsActivity.InnerOnItemClickListener1());
            }
            mChoiceLineDialog.show();
            mLinesChoiceAdapter.notifyChange(mBiz.getMeetingLines());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示选择拜访状态 Dialog
     */
    private void showChoiceStateDialog() {
        try {
            if (mChoiceStateDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceStateDialog = builder.show();
                mChoiceStateDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceStateDialog.getWindow();
                window.setContentView(R.layout.dialog_state_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceStateDialog.dismiss();
                    }
                });
                mListViewChoiceStates = (ListView) window.findViewById(R.id.listView_chart_choice);
                mStatesChoiceAdapter = new StateChoiceAdapter(null, this);
                mListViewChoiceStates.setAdapter(mStatesChoiceAdapter);
                mListViewChoiceStates.setOnItemClickListener(new CustomerMeetingsActivity.InnerOnItemClickListener2());
            }
            mChoiceStateDialog.show();
            mStatesChoiceAdapter.notifyChange(statelist);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onRefresh() {
        showLoadingDialog();
        mBiz.reFreshCustomerMeetingDatas();
    }

    @Override
    public void onLoadMore() {
        showLoadingDialog();
        mBiz.loadMoreCustomerMeetingDatas();
    }

    @Override
    public boolean onClick(int position, String... tags) {
        if (tags != null && tags.length > 0) {
            switch (tags[0]) {
                case "tv_read":
                    Intent intent1 = new Intent(this, CustomerMeetingShowStepActivity.class);
                    intent1.putExtra("CustomerMeeting", mBiz.getCustomerMeetingList().get(position));
                    intent1.putExtra("isShowStep", true);
                    startActivity(intent1);
                    break;
                case "tv_write":
                    visits(position);
                    break;
                case "tv_create":
                    Intent intent2 = new Intent(this, CustomerMeetingCreateActivity.class);
                    intent2.putExtra("CustomerMeeting", mBiz.getCustomerMeetingList().get(position));
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    /**
     * Dialog 中选择供货商的监听
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceFirstPartyDialog.dismiss();
                mCurrentFirstPartyIndex = position;

                List<CustomerMeeting> meetingFirstPartys = mBiz.getMeetingFirstPartys();
                if (meetingFirstPartys.size() <= 0) {//集合中没有数据网络请求数据
                    mBiz.GetFirstPartyList();
                    showLoadingDialog();
                } else {//集合中已有数据，直接显示
                    strFartherPartyID = mBiz.getMeetingFirstPartys().get(mCurrentFirstPartyIndex).getIDX();
                    tvMeetingFirstParty.setText(mBiz.getMeetingFirstPartys().get(mCurrentFirstPartyIndex).getPARTY_NAME());
                }
                showLoadingDialog();
                mBiz.reFreshCustomerMeetingDatas();

            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * Dialog 中选择拜访线路的监听
     */
    private class InnerOnItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceLineDialog.dismiss();
                if (mCurrentLineIndex == position && mBiz.getMeetingLines().size() > 0) {
                    return;
                }
                mCurrentLineIndex = position;

                List<CustomerMeetingLine> meetingLines = mBiz.getMeetingLines();
                if (meetingLines.size() <= 0) {//集合中没有数据网络请求数据
                    mBiz.GetPartyVisitLines();
                    showLoadingDialog();
                } else {//集合中已有数据，直接显示
                    strLine = mBiz.getMeetingLines().get(mCurrentLineIndex).getITEM_NAME();
                    tvMeetingType.setText(strLine);
                }
                showLoadingDialog();
                mBiz.reFreshCustomerMeetingDatas();

            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * Dialog 中选择拜访状态的监听
     */
    private class InnerOnItemClickListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceStateDialog.dismiss();
                if (mCurrentStateIndex == position && mBiz.getCustomerMeetingList().size() > 0) {
                    return;
                }
                mCurrentStateIndex = position;
                showLoadingDialog();
                strState = statelist.get(mCurrentStateIndex);
                tvMeetingState.setText(strState);
                mBiz.reFreshCustomerMeetingDatas();


            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 获取客户拜访列表成功
     */
    public void getMeetingsDataSuccess() {
        handleGetCustomerMeeingsData();
    }

    /**
     * 处理网络请求结果
     */
    private void handleGetCustomerMeeingsData() {
        try {
            mXlistViewInformations.stopRefresh();
            mXlistViewInformations.stopLoadMore();
            mLoadingDialog.dismiss();
            List<CustomerMeeting> customerMeetings = mBiz.getCustomerMeetingList();
            if (customerMeetings == null || customerMeetings.size() <= 0) {

                mTextViewNoData.setVisibility(View.VISIBLE);
            } else {
                mTextViewNoData.setVisibility(View.GONE);
                if (customerMeetings.size() < 20) {
                    mXlistViewInformations.setFooterViewTxt("无更多数据");
                }
            }
            mAdapter.notifyChange(customerMeetings);
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
                case R.id.tv_title_right:
                    if(tvMeetingFirstParty.getText().equals("")) {

                        ToastUtil.showToastBottom(String.valueOf("供应商不能为空"), Toast.LENGTH_SHORT);
                    }else {

                        Intent intent = new Intent(CustomerMeetingsActivity.this, CustomerCreateActivity.class);
                        intent.putExtra("fatherPartyAddressID", mBiz.getMeetingFirstPartys().get(mCurrentFirstPartyIndex).getADDRESS_IDX());
                        intent.putExtra("fatherPartyAddressName", mBiz.getMeetingFirstPartys().get(mCurrentFirstPartyIndex).getPARTY_NAME());
                        startActivity(intent);
                    }
                    break;
                case R.id.ll_meeting_type:
                    showChoiceLineDialog();
                    break;
                case R.id.ll_meeting_state:
                    showChoiceStateDialog();
                    break;
                case R.id.ll_meeting_first_party:
                    showChoiceFartherIdDialog();
                    break;
                case R.id.iv_search:
                    showLoadingDialog();
                    mBiz.reFreshCustomerMeetingDatas();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //位置为点击的-1 XListView 中有headView
        visits(position - 1);
    }

    private void visits(int position) {

        try {
            CustomerMeeting customerM = mBiz.getCustomerMeetingList().get(position);
            String status = customerM.getVISIT_STATES();
            if (status.equals("")) {

                Intent intent = new Intent(CustomerMeetingsActivity.this, CustomerMeetingCreateActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                startActivity(intent);
            } else if (status.equals("新建") || status.equals("确认客户信息")) {

                Intent intent = new Intent(CustomerMeetingsActivity.this, ArrivedStoreActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                startActivity(intent);
            } else if (status.equals("进店")) {

                Intent intent = new Intent(CustomerMeetingsActivity.this, CustomerMeetingCheckInventoryActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                startActivity(intent);
            } else if (status.equals("检查库存")) {

                Intent intent = new Intent(this, CustomerMeetingRecomOrderActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                startActivity(intent);
            } else if (status.equals("建议订单")) {

                Intent intent = new Intent(this, CustomerMeetingDisplayActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                startActivity(intent);
            } else if (status.equals("生动化陈列")) {

                Intent intent = new Intent(this, CustomerMeetingShowStepActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                intent.putExtra("isShowStep", false);
                startActivity(intent);
            } else if (status.equals("离店")) {

                Intent intent = new Intent(this, CustomerMeetingShowStepActivity.class);
                intent.putExtra("CustomerMeeting", customerM);
                intent.putExtra("isShowStep", true);
                startActivity(intent);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}