package com.kaidongyuan.app.kdyorder.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.model.UpdataPasswordActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

/**
 * Created by Administrator on 2016/6/1.
 * 修改密码界面
 */
public class UpdataPasswordActivity extends BaseActivity implements View.OnClickListener {

    private UpdataPasswordActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 原密码输入框
     */
    private EditText mEditTextOldPassword;
    /**
     * 新密码输入框
     */
    private EditText mEditTextNewPassword;
    /**
     * 新密确认输入框
     */
    private EditText mEditTextConfirmNewPassword;
    /**
     * 确认修改密码按钮
     */
    private Button mButtonUpdata;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_password);

        initData();
        setTop();
        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog!=null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
        mBiz.cancelRequest();
        mBiz = null;
        mImageViewGoBack = null;
        mEditTextOldPassword = null;
        mEditTextNewPassword = null;
        mEditTextConfirmNewPassword = null;
        mButtonUpdata = null;
    }

    private void initData() {
        mBiz = new UpdataPasswordActivityBiz(this);
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
        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        mEditTextOldPassword = (EditText) this.findViewById(R.id.edittext_old_password);
        mEditTextOldPassword.requestFocus();
        mEditTextNewPassword = (EditText) this.findViewById(R.id.edittext_new_password);
        mEditTextConfirmNewPassword = (EditText) this.findViewById(R.id.edittext_confirm_new_password);
        mButtonUpdata = (Button) this.findViewById(R.id.button_updata);
    }

    private void setListener() {
        mImageViewGoBack.setOnClickListener(this);
        mButtonUpdata.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_goback://返回上一界面
                this.finish();
                break;
            case R.id.button_updata://确认提交修改密码
                updataPassWord();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void updataPassWord() {
        String oldPassword = mEditTextOldPassword.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtil.showToastBottom("原密码不能为空,请输入原密码！", Toast.LENGTH_SHORT);
            return;
        }
        String newPassword = mEditTextNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtil.showToastBottom("新密码不能为空,请输入新密码！", Toast.LENGTH_SHORT);
            return;
        }
        String confirmNewPassword = mEditTextConfirmNewPassword.getText().toString().trim();
        if (TextUtils.isEmpty(confirmNewPassword)) {
            ToastUtil.showToastBottom("再次输入新密码不能为空,请输入确认新密码！", Toast.LENGTH_SHORT);
            return;
        }
        if (!confirmNewPassword.equals(newPassword)) {
            ToastUtil.showToastBottom("确认密码与新密码不同，请从新输入确认密码！", Toast.LENGTH_SHORT);
            mEditTextConfirmNewPassword.setText("");
            return;
        }
        if (oldPassword.equals(newPassword)) {
            ToastUtil.showToastBottom("新密码与原密码相同，请从新输入新密码！", Toast.LENGTH_SHORT);
            return;
        }
        if (newPassword.length()<6) {
            ToastUtil.showToastBottom("密码不能少于6位数字或字母！", Toast.LENGTH_SHORT);
        }
        if (mBiz.updataPassword(oldPassword, newPassword)) {
            showLoadingDialog();
        } else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
        }
    }

    /**
     * 网络请求是显示的 Dialog
     */
    private void showLoadingDialog() {
        if (mLoadingDialog==null) {
            mLoadingDialog = new MyLoadingDialog(this);
        }
        mLoadingDialog.showDialog();
    }

    /**
     * 更新密码失败业务类调用的方法
     * @param message 显示的信息
     */
    public void updataPasswordFailed(String message) {
        mLoadingDialog.dismiss();
        ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
    }

    /**
     * 更新密码成功业务类调用的方法
     */
    public void updataPasswordSuccess() {
        mLoadingDialog.dismiss();
        ToastUtil.showToastBottom("更新密码成功！", Toast.LENGTH_SHORT);
        this.finish();
    }
}






















