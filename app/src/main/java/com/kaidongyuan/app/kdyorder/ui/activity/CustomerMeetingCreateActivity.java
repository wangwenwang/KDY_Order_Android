package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.model.CustomerMeetingCreateActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

/**
 * Created by Administrator on 2016/5/24.
 * 新建客户拜访界面
 */
public class CustomerMeetingCreateActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应业务类
     */
    private CustomerMeetingCreateActivityBiz mBiz;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    private Dialog mDialog;
    private Button bt_dialog_update,bt_dialog_cancel;
    private EditText ed_customer_name;
    private TextView ed_customer_address;
    private EditText ed_address_person;
    private EditText ed_address_tel;

    private final int RequestAddContact = 1001;
    private final int RequestAddressBelong=1008;
    /**
     *
     */
    public NormalAddress pv,ct,ar,ru;
    public double lat=1,lng=1;
    private TextView tvPartyCode;
    private TextView tvPartyName;
    private TextView tvPartyClass;
    private TextView tvPartyMark;
    private TextView tvPartyChannel;
    private TextView tvPartyLine;
    private TextView tvWeeklyVisitFrequency;
    private TextView tvSingleStoreSales;
    private TextView tvContactName;
    private TextView tvContactTel;
    private TextView tvPartyAddress;
    private EditText edMeetingRemark;
    private Button buttonEdit;
    private Button buttonUpdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        try {
            initView();
            setTop();
            initData();
            setListener();
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
            mImageViewGoBack = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        mBiz = new CustomerMeetingCreateActivityBiz(this);
        if (getIntent().hasExtra("CustomerMeeting")){
            mBiz.setCustomerMeeting((CustomerMeeting) getIntent().getParcelableExtra("CustomerMeeting"));
            setData();
        }else {
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.no_data), Toast.LENGTH_SHORT);
            finish();
        }

    }

    private void setData() {
        CustomerMeeting customerMeeting=mBiz.getCustomerMeeting();
        tvPartyCode.setText(customerMeeting.getPARTY_NO());
        tvPartyName .setText(customerMeeting.getPARTY_NAME());
        tvPartyClass .setText(customerMeeting.getPARTY_LEVEL());
        tvPartyMark .setText(customerMeeting.getPARTY_STATES());
        tvPartyChannel .setText(customerMeeting.getCHANNEL());
        tvPartyLine .setText(customerMeeting.getLINE());
        tvWeeklyVisitFrequency .setText(customerMeeting.getWEEKLY_VISIT_FREQUENCY());
        tvSingleStoreSales .setText(customerMeeting.getSINGLE_STORE_SALES());
        tvContactName.setText(customerMeeting.getCONTACTS());
        tvContactTel.setText(customerMeeting.getCONTACTS_TEL());
        tvPartyAddress.setText(customerMeeting.getPARTY_ADDRESS());
    }

    /**
     * 保存失败
     *
     * @param message 要显示的消息
     */
    public void updataError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
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
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            tvPartyCode = (TextView) this.findViewById(R.id.tv_party_code);
            tvPartyName = (TextView) this.findViewById(R.id.tv_party_name);
            tvPartyClass = (TextView) this.findViewById(R.id.tv_party_class);
            tvPartyMark = (TextView) this.findViewById(R.id.tv_party_mark);
            tvPartyChannel = (TextView) this.findViewById(R.id.tv_party_channel);
            tvPartyLine = (TextView) this.findViewById(R.id.tv_party_line);
            tvWeeklyVisitFrequency = (TextView) this.findViewById(R.id.tv_weekly_visit_frequency);
            tvSingleStoreSales = (TextView) this.findViewById(R.id.tv_single_store_sales);
            tvContactName = (TextView) this.findViewById(R.id.tv_contact_name);
            tvContactTel = (TextView) this.findViewById(R.id.tv_contact_tel);
            tvPartyAddress = (TextView) this.findViewById(R.id.tv_party_address);
            edMeetingRemark = (EditText) this.findViewById(R.id.ed_meeting_remark);
            buttonEdit = (Button) this.findViewById(R.id.button_edit);
            buttonUpdata = (Button) this.findViewById(R.id.button_updata);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            buttonEdit.setOnClickListener(this);
            buttonUpdata.setOnClickListener(this);
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
     * 保存客户成功
     * @param
     */
    public void getPartyVisitInsertSuccess() {
        try {
            if (mLoadingDialog!=null)mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("新增客户拜访成功", Toast.LENGTH_SHORT);
            startActivity(new Intent(this,ArrivedStoreActivity.class));
            finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 保存客户成功
     * @param
     */
    public void getVisitConfirmCustomerSuccess() {
        try {
            if (mLoadingDialog!=null)mLoadingDialog.dismiss();

            ToastUtil.showToastBottom("修改客户拜访成功", Toast.LENGTH_SHORT);
            setData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 保存客户失败
     *
     * @param message 要显示的消息
     */
    public void updataPartyError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void showmEditDialog() {

        if (mDialog==null){
            mDialog=new Dialog(CustomerMeetingCreateActivity.this);
        }
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_edit_customerinfo);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.width = DensityUtil.dip2px(DensityUtil.getWidth_dp()-20);//宽高可设置具体大小
//        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        mDialog.getWindow().setAttributes(lp);
        ed_customer_name =(EditText) window.findViewById(R.id.ed_customer_name);
        ed_customer_address = (EditText) window.findViewById(R.id.ed_customer_address);
        ed_address_person = (EditText) window.findViewById(R.id.ed_ADDRESS_PERSON);
        ed_address_tel = (EditText) window.findViewById(R.id.ed_ADDRESS_TEL);
        ed_customer_name.setText(mBiz.getCustomerMeeting().getPARTY_NAME());
        ed_customer_address.setText(mBiz.getCustomerMeeting().getPARTY_ADDRESS());
        ed_address_person.setText(mBiz.getCustomerMeeting().getCONTACTS());
        ed_address_tel.setText(mBiz.getCustomerMeeting().getCONTACTS_TEL());
        bt_dialog_update= (Button) window.findViewById(R.id.bt_dialog_update);
        bt_dialog_update.setOnClickListener(this);
        bt_dialog_cancel= (Button) window.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback:
                    this.finish();
                    break;
                case R.id.button_edit:
                    if (mBiz.getCustomerMeeting()!=null){
                        showmEditDialog();
                    }
                    break;
                case R.id.button_updata:
                   if (mBiz.GetPartyVisitInsert()){
                       showLoadingDialog();
                   }
                    break;
                case R.id.bt_dialog_update:
                    if (mDialog!=null)mDialog.dismiss();

                   if (mBiz.GetVisitConfirmCustomer(ed_customer_name.getText().toString(),ed_customer_address.getText().toString(),
                           ed_address_person.getText().toString(),ed_address_tel.getText().toString())){
                       showLoadingDialog();
                   }
                    break;
                case R.id.bt_dialog_cancel:
                    if (mDialog!=null)mDialog.dismiss();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}














