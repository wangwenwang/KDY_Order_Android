package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.InformationAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.model.NewestInformationActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 * 最新资讯界面
 */
public class NewestInformationActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /**
     * 对应业务类
     */
    private NewestInformationActivityBiz mBiz;

    /**
     * 显示最新资讯的 ListView
     */
    private XListView mXlistViewInformations;
    /**
     * 显示最新资讯 ListView 的 Adapter
     */
    private InformationAdapter mInformationAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newest_information);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getInformationData();
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
            mInformationAdapter = null;
            mTextViewNoData = null;
            mImageViewGoBack = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        mBiz = new NewestInformationActivityBiz(this);
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
            this.mXlistViewInformations = (XListView) this.findViewById(R.id.lv_newest_information);
            mXlistViewInformations.setPullLoadEnable(false);
            mXlistViewInformations.setPullRefreshEnable(false);
            mInformationAdapter = new InformationAdapter(this, null);
            mXlistViewInformations.setAdapter(mInformationAdapter);
            this.mTextViewNoData = (TextView) this.findViewById(R.id.textview_nodata);
            this.mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mXlistViewInformations.setOnItemClickListener(this);
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取最新资讯数据
     */
    private void getInformationData() {
        try {
            if (mBiz.getInformationData()) {
                showLoadingDialog();
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
     * 获取最新资讯失败
     *
     * @param message 要显示的消息
     */
    public void getInformationDataError(String message) {
        try {
           // ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
            handleGetInformationData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取最新资讯成功
     */
    public void getInformationDataSuccess() {
        handleGetInformationData();
    }

    /**
     * 处理网络请求结果
     */
    private void handleGetInformationData() {
        try {
            mLoadingDialog.dismiss();
            List<Information> informations = mBiz.getmInformationList();
            if (informations == null || informations.size() <= 0) {
                    String businessType = SharedPreferencesUtil.getValueByName(SharedPreferenceConstants.BUSSINESS_CODE,
                            SharedPreferenceConstants.NAME, SharedPreferencesUtil.STRING);
                    if (businessType.contains(BusinessConstants.TYPE_YIBAO)) {
                        mTextViewNoData.setGravity(Gravity.START);
                        mTextViewNoData.setText(getString(R.string.news_no_data));
                        mTextViewNoData.setTextColor(getResources().getColor(R.color.details_text));
                    }
                mTextViewNoData.setVisibility(View.VISIBLE);
            } else {
                mTextViewNoData.setVisibility(View.GONE);
            }
            mInformationAdapter.notifyChange(informations);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()) {
                case R.id.button_goback:
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            Intent intent = new Intent(this, NewestInformationDetailsActivity.class);
            Information information = mBiz.getmInformationList().get(position-1);//位置为点击的-1 XListView 中有headView
            intent.putExtra(EXTRAConstants.INFORMATIONDETAILACTIVITY_TITLE, information.getITitle());
            intent.putExtra(EXTRAConstants.INFORMATIONDETAILACTIVITY_ID, information.getIID());
            startActivity(intent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}














