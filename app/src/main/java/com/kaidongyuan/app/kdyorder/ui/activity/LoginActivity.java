package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.BusinessAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Business;
import com.kaidongyuan.app.kdyorder.model.LoginActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 用户输入的用户名
     */
    private EditText mEtUserName;
    /**
     * 用户输入的密码
     */
    private EditText mEtPassword;
    /**
     * 登录按钮
     */
    private Button mBtLogin,mBtRegister;
    /**
     * 登录界面业务类
     */
    private LoginActivityBiz mBiz;
    /**
     * 用户业务类型 Dialog
     */
    private Dialog mBusinessDialog;
    /**
     * 用户业务类型的 ListView
     */
    private ListView mListViewBusiness;
    /**
     * 取消登录按钮
     */
    private Button mButtonCancelLogin;
    /**
     * 用户业务类型 Adapter
     */
    private BusinessAdapter mBusinessAdapter;
    /**
     * 用户业务类型集合
     */
    private List<Business> mBusinessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            initData();
            setTop();
            initView();
            initListener();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (mBusinessDialog!=null && mBusinessDialog.isShowing()) {
                mBusinessDialog.dismiss();
            }
            mBusinessDialog = null;
            mBiz.cancelAllRequest();
            mEtUserName = null;
            mEtPassword = null;
            mBtLogin = null;
            mBtRegister=null;
            mBiz = null;
            mListViewBusiness = null;
            mButtonCancelLogin = null;
            mBusinessAdapter = null;
            mBusinessList = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new LoginActivityBiz(this);
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
            Logger.w("topView.Height:"+DensityUtil.getStatusHeight()*16/30+"");
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            mEtUserName = (EditText) this.findViewById(R.id.et_username);
            mEtPassword = (EditText) this.findViewById(R.id.et_pwd);
            mBtLogin = (Button) this.findViewById(R.id.btn_login);
            mBtRegister= (Button) this.findViewById(R.id.btn_register);
            //设置上次登录的用户名和密码
            mEtUserName.setText(String.valueOf(mBiz.getUserName()));
            mEtPassword.setText(String.valueOf(mBiz.getPassword()));
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initListener() {
        try {
            mBtLogin.setOnClickListener(this);
            mBtRegister.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_login:
                    login();
                    break;
                case R.id.btn_register:
                    Intent registterIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(registterIntent);
                    break;
                case R.id.button_cancelLogin:
                    mBusinessDialog.dismiss();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 登录
     */
    private void login() {
        try {
            String userName = mEtUserName.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToastBottom("用户名为空！", Toast.LENGTH_SHORT);
                return;
            }
            String password = mEtPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                ToastUtil.showToastBottom("密码为空！", Toast.LENGTH_SHORT);
                return;
            }
            if (mBiz.login(userName, password)) {//发送请求成功
                mBtLogin.setEnabled(false);
               // mBtRegister.setEnabled(false);
                mBtLogin.setText("登录中...");//发送请求失败
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 登录成功
     */
    public synchronized void loginSuccess() {
        try {
            MyApplication.isLogin = true;
            mBtLogin.setText("登录成功");
            mBtLogin.setEnabled(true);
            mBtRegister.setEnabled(true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 登录失败
     *
     * @param message 显示的信息
     */
    public void loginError(String message) {
        try {
            if (message != null) {
                ToastUtil.showToastBottom(message, Toast.LENGTH_SHORT);
            }
            mBtLogin.setEnabled(true);
            mBtRegister.setEnabled(true);
            mBtLogin.setText("重新登录");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示业务类型选择 Dialog
     *
     * @param businessList 用户的业务类型集合
     */
    public void showBusinessDialog(List<Business> businessList) {
        try {
            mBusinessList = businessList == null ? new ArrayList<Business>() : businessList;
            if (mBusinessDialog == null) {
                createBusinessDialog();
            } else {
                mBusinessDialog.show();
                mBusinessAdapter.notifyChange(businessList);
            }
            initLoginButton();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 创建用户业务类型的 Dialog
     */
    private void createBusinessDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mBusinessDialog = builder.create();
            mBusinessDialog.setCanceledOnTouchOutside(false);
            mBusinessDialog.show();
            Window window = mBusinessDialog.getWindow();
            window.setContentView(R.layout.dialog_business_choice);
            mListViewBusiness = (ListView) window.findViewById(R.id.listView_business);
            mListViewBusiness.setOnItemClickListener(new InnerOnItemClickListener());
            mButtonCancelLogin = (Button) window.findViewById(R.id.button_cancelLogin);
            mButtonCancelLogin.setOnClickListener(this);
            mBusinessAdapter = new BusinessAdapter(mBusinessList, this);
            mListViewBusiness.setAdapter(mBusinessAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取用户名输入编辑框
     *
     * @return 用户名输入框
     */
    public EditText getmEtUserNameEditText() {
        try {
            return mEtUserName;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }

    /**
     * 将登录按钮状态设置成初始状态
     */
    private void initLoginButton() {
        try {
            mBtLogin.setEnabled(true);
            mBtRegister.setEnabled(true);
            mBtLogin.setText("登录");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 用户业务列表 Item 点击监听实现类
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mBiz.writeBusinessToApplicationAndSharedPreference(mBusinessList.get(position));
                mBusinessDialog.dismiss();
                loginSuccess();
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }


}












