package com.kaidongyuan.app.kdyorder.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.ui.activity.AboutActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.LoginActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.MainActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyManageActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.UpdataPasswordActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;


/**
 * Created by Administrator on 2016/4/1.
 * Mine Fragment
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View mParentView;

    /**
     * 显示用户姓名文本框
     */
    private TextView mTextViewUserName;
    /**
     * 显示用户角色文本框
     */
    private TextView mTextViewUserRole;
    /**
     * 显示用户业务类型文本框
     */
    private TextView mTextViewUserBusiness;
    /**
     * 显示当前 app 版本
     */
    private TextView mTextViewCurrentVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mParentView = inflater.inflate(R.layout.fragment_mine, null);
            initView();
            setListener();
            return mParentView;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return mParentView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            mParentView = null;
            mTextViewUserName = null;
            mTextViewUserRole = null;
            mTextViewUserBusiness = null;
            mTextViewCurrentVersion = null;
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initView() {
        try {
            mTextViewUserName = (TextView) mParentView.findViewById(R.id.tv_name);
            mTextViewUserRole = (TextView) mParentView.findViewById(R.id.tv_role);
            mTextViewUserBusiness = (TextView) mParentView.findViewById(R.id.tv_business);
            mTextViewCurrentVersion = (TextView) mParentView.findViewById(R.id.textview_current_version);
            mTextViewUserName.setText(MyApplication.getInstance().getUser().getUSER_NAME());
            mTextViewUserRole.setText(StringUtils.getRoleName(MyApplication.getInstance().getUser().getUSER_TYPE()));
            mTextViewUserBusiness.setText(MyApplication.getInstance().getBusiness().getBUSINESS_NAME());
            if (MyApplication.getInstance().getBusiness().getBUSINESS_CODE().equals("DKI06")){
                mParentView.findViewById(R.id.ll_party_manage).setVisibility(View.VISIBLE);
            }else {
                mParentView.findViewById(R.id.ll_party_manage).setVisibility(View.GONE);
            }
            Context appComtext = MyApplication.getAppContext();
            String currentVersion = appComtext.getPackageManager().getPackageInfo(appComtext.getPackageName(), 0).versionName;
            mTextViewCurrentVersion.setText("V "+currentVersion);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mParentView.findViewById(R.id.ll_party_manage).setOnClickListener(this);
            mParentView.findViewById(R.id.ll_update_pwd).setOnClickListener(this);
            mParentView.findViewById(R.id.ll_manage_info).setOnClickListener(this);
            mParentView.findViewById(R.id.ll_my_evaluate).setOnClickListener(this);
            mParentView.findViewById(R.id.ll_feed_back).setOnClickListener(this);
            mParentView.findViewById(R.id.ll_check_version).setOnClickListener(this);
            mParentView.findViewById(R.id.ll_about).setOnClickListener(this);
            mParentView.findViewById(R.id.switch_btn).setOnClickListener(this);
            mParentView.findViewById(R.id.exit_btn).setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ll_party_manage://管理下单客户信息
                    Intent partyManageIntent=new Intent(getActivity(), PartyManageActivity.class);
                    startActivity(partyManageIntent);
                    break;
                case R.id.ll_update_pwd://跳转到修改密码界面
                    Intent updataPasswordIntent = new Intent(getActivity(), UpdataPasswordActivity.class);
                    startActivity(updataPasswordIntent);
                    break;
                case R.id.ll_manage_info://跳转到管理信息界面
                    ToastUtil.showToastBottom("跳转到管理信息界面", Toast.LENGTH_SHORT);
                    break;
                case R.id.ll_my_evaluate://跳转到我的评价界面
                    ToastUtil.showToastBottom("跳转到我的评价界面", Toast.LENGTH_SHORT);
                    break;
                case R.id.ll_feed_back://跳转到意见反馈界面
                    ToastUtil.showToastBottom("跳转到意见反馈界面", Toast.LENGTH_SHORT);
                    break;
                case R.id.ll_check_version://检查版本更新
                    MainActivity activity = (MainActivity) getActivity();
                    activity.checkVersion(false);
                    break;
                case R.id.ll_about://跳转到关于界面
                    Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(aboutIntent);
                    break;
                case R.id.switch_btn://切换账号
                    changeAccount();
                    break;
                case R.id.exit_btn://退出账号
                    MyApplication.isLogin = false;
                    MyApplication.getInstance().exit();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 切换账户，跳转到登录界面
     */
    private void changeAccount() {
        try {
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            this.getActivity().startActivity(intent);
            this.getActivity().finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

}























