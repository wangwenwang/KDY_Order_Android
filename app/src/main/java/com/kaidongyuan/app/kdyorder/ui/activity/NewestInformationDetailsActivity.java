package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.NewestInformationDetailsActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

/**
 * Created by Administrator on 2016/5/24.
 * 最新资讯详情界面
 */
public class NewestInformationDetailsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private NewestInformationDetailsActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mTextViewGoBack;
    /**
     * 资讯标题
     */
    private TextView mTextViewTitle;
    /**
     * 无
     */
    private TextView mTextViewContent;
    /**
     * 资讯内容
     */
    private TextView mTextViewInformation;
    /**
     * 已读按钮
     */
    private Button mButtonIsRead;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newest_information_details);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getInformationDetailsData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mBiz.cancelRequest();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new NewestInformationDetailsActivityBiz(this);
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
            mTextViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mTextViewTitle = (TextView) this.findViewById(R.id.tv_title);
            mTextViewContent = (TextView) this.findViewById(R.id.tv_content);
            mTextViewInformation = (TextView) this.findViewById(R.id.tv_info);
            mButtonIsRead = (Button) this.findViewById(R.id.btn_read);
            mButtonIsRead.setVisibility(View.GONE);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mTextViewGoBack.setOnClickListener(this);
            mButtonIsRead.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取资讯详情
     */
    private void getInformationDetailsData() {
        try {
            String title = "";
            String id = "";
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.INFORMATIONDETAILACTIVITY_TITLE)) {
                title = intent.getStringExtra(EXTRAConstants.INFORMATIONDETAILACTIVITY_TITLE);
            }
            if (intent.hasExtra(EXTRAConstants.INFORMATIONDETAILACTIVITY_ID)) {
                id = intent.getStringExtra(EXTRAConstants.INFORMATIONDETAILACTIVITY_ID);
            }
            if (mBiz.getInformationDetailsData(id)) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示 Dialog
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
                case R.id.button_goback://返回上一个界面
                    this.finish();
                    break;
                case R.id.btn_read://已读按钮，发送给后台
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取资讯详情失败
     *
     * @param message 显示的信息
     */
    public void getInformationDetailsDataError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
            mLoadingDialog.dismiss();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取资讯详情成功
     */
    public void getInformationDetailsDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            mButtonIsRead.setVisibility(View.VISIBLE);
            Information information = mBiz.getmInformation();
            mTextViewTitle.setText(information.getITitle());
            mTextViewContent.setText(Html.fromHtml(information.getIContent()));
            mTextViewInformation.setText("阅读量：" + information.getIReadCount() + "\n来自："
                    + information.getIFrom() + "\n时间：" + information.getIAddTime());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}




















