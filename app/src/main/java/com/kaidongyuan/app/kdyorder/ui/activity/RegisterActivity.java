package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.StockProductAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.bean.StockProduct;
import com.kaidongyuan.app.kdyorder.model.MakeAppStockActivityBiz;
import com.kaidongyuan.app.kdyorder.model.RegisterActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.Date;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/12.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private RegisterActivityBiz mBiz;
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;

    private EditText ed_user_code,ed_user_pwd,ed_user_pwd_again,ed_user_name,ed_party_name,
            ed_party_contact_address, ed_party_contact_name,ed_party_contact_tel;
    private TextView ed_party_contact_belong;
    /**
     * 申请注册按钮
     */
    private Button bt_register;
    /**
     *
     */
    private NormalAddress pv,ct,ar,ru;
    /**
     * 网络加载时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    private final int mRequestcode=1004;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setTop();
        initView();
        setListener();
    }

    private void init() {
        try {
            mBiz =new RegisterActivityBiz(this);
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
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
        mImageViewGoBack= (ImageView) this.findViewById(R.id.button_goback);
        ed_user_code= (EditText) this.findViewById(R.id.ed_user_code);
        ed_user_name= (EditText) this.findViewById(R.id.ed_user_name);
        ed_user_pwd= (EditText) this.findViewById(R.id.ed_user_pwd);
        ed_user_pwd_again= (EditText) this.findViewById(R.id.ed_user_pwd_again);
        ed_party_name= (EditText) this.findViewById(R.id.ed_party_name);
        ed_party_contact_belong= (TextView) this.findViewById(R.id.ed_ADDRESS_BELONG);
        ed_party_contact_address= (EditText) this.findViewById(R.id.ed_party_contact_address);
        ed_party_contact_name= (EditText) this.findViewById(R.id.ed_party_contact_name);
        ed_party_contact_tel= (EditText) this.findViewById(R.id.ed_party_contact_tel);
        bt_register= (Button) this.findViewById(R.id.bt_register);
    }


    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            bt_register.setOnClickListener(this);
            ed_party_contact_belong.setOnClickListener(this);

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 网络请求是显示的 Dialog
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
     * 登录失败
     * @param message 显示的信息
     */
    public void loginError(String message) {
        try {
            mLoadingDialog.dismiss();
            if (message != null) {
                ToastUtil.showToastBottom(message, Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 登录
     */
    private void register() {
        try {
            String userName = ed_user_name.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToastBottom("用户名为空！", Toast.LENGTH_SHORT);
                return;
            }
            String userCode = ed_user_code.getText().toString();
            if (TextUtils.isEmpty(userCode)||userCode.length()!=11) {
                ToastUtil.showToastBottom("请输入正确帐号（手机号）！", Toast.LENGTH_SHORT);
                return;
            }
            String password = ed_user_pwd.getText().toString();
            if (TextUtils.isEmpty(password)) {
                ToastUtil.showToastBottom("密码为空！", Toast.LENGTH_SHORT);
                return;
            }
            String passwordAgain = ed_user_pwd_again.getText().toString();
            if (TextUtils.isEmpty(passwordAgain)||!password.equals(passwordAgain)) {
                ToastUtil.showToastBottom("两次输入的密码不一致！", Toast.LENGTH_SHORT);
                return;
            }
            String partyName=ed_party_name.getText().toString();
            if (TextUtils.isEmpty(partyName)){
                ToastUtil.showToastBottom("客户名称为空！", Toast.LENGTH_SHORT);
                return;
            }
            if (pv==null||ct==null||ar==null||ru==null){
                ToastUtil.showToastBottom("请先填写送货所属地区！", Toast.LENGTH_SHORT);
                return;
            }
            String partyProvinceId=pv.getITEM_IDX();
            if (TextUtils.isEmpty(partyProvinceId)){
                ToastUtil.showToastBottom("请填写完整收货地址！", Toast.LENGTH_SHORT);
                return;
            }
            String partyCity=ct.getITEM_NAME();
            String partyCityId=ct.getITEM_IDX();
            if (TextUtils.isEmpty(partyCityId)){
                ToastUtil.showToastBottom("请填写完整收货地址！", Toast.LENGTH_SHORT);
                return;
            }
            String partyDistrictId=ar.getITEM_IDX();
            if (TextUtils.isEmpty(partyDistrictId)){
                ToastUtil.showToastBottom("请填写完整收货地址！", Toast.LENGTH_SHORT);
                return;
            }
            String partyStreetId=ru.getITEM_IDX();
            if (TextUtils.isEmpty(partyStreetId)){
                ToastUtil.showToastBottom("请填写完整收货地址！", Toast.LENGTH_SHORT);
                return;
            }
            String partyAddress=ed_party_contact_address.getText().toString().trim();
            if (TextUtils.isEmpty(partyAddress)){
                ToastUtil.showToastBottom("请填写完整收货地址！", Toast.LENGTH_SHORT);
                return;
            }
            String contactName=ed_party_contact_name.getText().toString().trim();
            if (TextUtils.isEmpty(contactName)){
                ToastUtil.showToastBottom("收货联系人姓名为空！", Toast.LENGTH_SHORT);
                return;
            }
            String contactTel=ed_party_contact_tel.getText().toString().trim();
            if (TextUtils.isEmpty(contactTel)){
                ToastUtil.showToastBottom("收货联系电话为空！", Toast.LENGTH_SHORT);
                return;
            }
            if (mBiz.register(userName,userCode, password,partyName,partyCity,contactName,contactTel,
                    partyProvinceId,partyCityId,partyDistrictId,partyStreetId,partyAddress,
                    pv.getITEM_NAME()+ct.getITEM_NAME()+ar.getITEM_NAME()+ru.getITEM_NAME()+partyAddress.trim())) {//发送请求成功
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog!=null)mLoadingDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_register:register();
                break;
            case R.id.button_goback:this.finish();
                break;
            case R.id.ed_ADDRESS_BELONG:
                intent=new Intent(RegisterActivity.this, ChooseProvinceActivity.class);
                startActivityForResult(intent,mRequestcode);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case mRequestcode:
                     pv=data.getParcelableExtra("province");
                     ct=data.getParcelableExtra("city");
                     ar=data.getParcelableExtra("area");
                     ru=data.getParcelableExtra("rural");
                     ed_party_contact_belong.setText(pv.getITEM_NAME().trim()+ct.getITEM_NAME().trim()
                             +ar.getITEM_NAME().trim()+ru.getITEM_NAME().trim());
                    break;
                default:
                    break;

            }
        }
    }

}
