package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.kaidongyuan.app.kdyorder.adapter.ChartChoiceAdapter;
import com.kaidongyuan.app.kdyorder.adapter.CustomerMeetingAdapter;
import com.kaidongyuan.app.kdyorder.adapter.InformationAdapter;
import com.kaidongyuan.app.kdyorder.adapter.LineChoiceAdapter;
import com.kaidongyuan.app.kdyorder.adapter.StateChoiceAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.interfaces.OnClickListenerStrInterface;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingsActivityBiz;
import com.kaidongyuan.app.kdyorder.model.NewestInformationActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
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
     * 显示最新资讯 ListView 的 Adapter
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
    private LinearLayout llMeetingType, llMeetingState;
    private TextView tvMeetingType, tvMeetingState;
    private EditText edSearch;
    private ImageView ivSearch;
    public String strSearch = "";
    public String strLine = "";
    public String strState = "全部";//已拜访,未拜访,拜访中,全部
    private List<String> statelist = new ArrayList<String>(Arrays.asList("全部", "未拜访", "拜访中", "已拜访"));
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
        try {
            initData();
            setTop();
            initView();
            setListener();
            getMeetingLineDatas();
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
            tvTitleRight= (TextView) findViewById(R.id.tv_title_right);
            llMeetingType = (LinearLayout) findViewById(R.id.ll_meeting_type);
            tvMeetingType = (TextView) findViewById(R.id.tv_meeting_type);
            llMeetingState = (LinearLayout) findViewById(R.id.ll_meeting_state);
            tvMeetingState = (TextView) findViewById(R.id.tv_meeting_state);
            edSearch= (EditText) findViewById(R.id.ed_search);
            ivSearch= (ImageView) findViewById(R.id.iv_search);
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
            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    strSearch=charSequence.toString().trim();
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
     * 获取客服拜访线路数据集
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

    public void getMeetingLinesSuccess(List<CustomerMeetingLine> customerMeetingLines) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        if (customerMeetingLines != null && customerMeetingLines.size() > 0) {
            // 拜访线路定位到当天
            for (int i = 0; i < customerMeetingLines.size(); i++) {
                String line = customerMeetingLines.get(i).getITEM_NAME();
                if(line.equals(DateUtil.GetCurrWeek())) {
                    strLine = line;
                    break;
                }
            }
            strLine = strLine.equals("") ? customerMeetingLines.get(0).getITEM_NAME() : strLine;
            tvMeetingType.setText(strLine);
            if (mBiz.reFreshCustomerMeetingDatas()){
                showLoadingDialog();
            }

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
                mListViewChoiceLines.setOnItemClickListener(new CustomerMeetingsActivity.InnerOnItemClickListener());
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
                mListViewChoiceStates.setOnItemClickListener(new CustomerMeetingsActivity.InnerOnItemClickListener1());
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
        if (tags!=null&&tags.length>0){
            switch (tags[0]){
                case "tv_read":

                    break;
                case "tv_write":
                    break;
                case "tv_create":
                    Intent intent0=new Intent(CustomerMeetingsActivity.this,CustomerMeetingCreateActivity.class);
                    intent0.putExtra("CustomerMeeting",mBiz.getCustomerMeetingList().get(position));
                    startActivity(intent0);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    /**
     * Dialog 中选择拜访线路的监听
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceLineDialog.dismiss();
                if (mCurrentLineIndex == position&&mBiz.getCustomerMeetingList().size()>0) {
                    return;
                }
                mCurrentLineIndex = position;
                if(position == mBiz.getCustomerMeetingList().size() + 1) {

                    // 全部
                    tvMeetingType.setText("全部");
                }else {

                    List<CustomerMeetingLine> meetingLines = mBiz.getMeetingLines();
                    if (meetingLines.size() <= 0) {//集合中没有数据网络请求数据
                        mBiz.GetPartyVisitLines();
                        showLoadingDialog();
                    } else {//集合中已有数据，直接显示
                        strLine = mBiz.getMeetingLines().get(mCurrentLineIndex).getITEM_NAME();
                        tvMeetingType.setText(strLine);
                    }
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
    private class InnerOnItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceStateDialog.dismiss();
                if (mCurrentStateIndex == position&&mBiz.getCustomerMeetingList().size()>0) {
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
                    startActivity(new Intent(CustomerMeetingsActivity.this,CustomerCreateActivity.class));
                    break;
                case R.id.ll_meeting_type:
                    showChoiceLineDialog();
                    break;
                case R.id.ll_meeting_state:
                    showChoiceStateDialog();
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
        try {
            Intent intent = new Intent(this, NewestInformationDetailsActivity.class);
            CustomerMeeting meeting = mBiz.getCustomerMeetingList().get(position - 1);//位置为点击的-1 XListView 中有headView
            intent.putExtra(EXTRAConstants.CUSTOMERMEETING_ID, meeting.getIDX());
            startActivity(intent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}














