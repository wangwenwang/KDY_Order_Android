package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.PartyAddressManageListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.model.PartyAddressManageActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/17.
 */
public class PartyAddressManageActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener  {
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 下单业务类
     */
    private PartyAddressManageActivityBiz mBiz;
    /**
     * 客户列表 ListView
     */
    private XListView mPartyAddressListview;
    /**
     * 客户地址列表
     */
    private PartyAddressManageListAdapter mPartyAddressListAdapter;

    /**
     * 没有数据时显示的控件，默认为不显示
     */
    private TextView mTextviewNodata;
    /**
     * 网络请求是的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    private Button mButtonAddAddress;

    private Dialog mDialog;
    private Button bt_dialog_comfit,bt_dialog_cancel;
    private TextView ed_address_belong;
    private EditText ed_address_address;
    private EditText ed_address_person;
    private EditText ed_address_tel;
    private final int mRequestcode=1004;
    private NormalAddress pv,ct,ar,ru;
    private String partyidx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partyaddress_manage);
        initData();
        initView();
        initListener();
        getCustomerAddressData();
    }

    private void initData() {
        try {
            if (getIntent().hasExtra("partyidx")){
                partyidx=getIntent().getStringExtra("partyidx");
            }else {
                ToastUtil.showToastBottom("获取客户信息失败！",Toast.LENGTH_SHORT);
                finish();
            }
            mBiz = new PartyAddressManageActivityBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mPartyAddressListview = (XListView) this.findViewById(R.id.lv_party_addresses);
            mPartyAddressListAdapter = new PartyAddressManageListAdapter(PartyAddressManageActivity.this, null);
            mPartyAddressListview.setAdapter(mPartyAddressListAdapter);
            mPartyAddressListview.setPullLoadEnable(false);
            mPartyAddressListview.setPullRefreshEnable(true);
            mTextviewNodata = (TextView) this.findViewById(R.id.textview_nodata);
            mTextviewNodata.setVisibility(View.GONE);
            mButtonAddAddress= (Button) this.findViewById(R.id.bt_addaddress);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mPartyAddressListview.setXListViewListener(new XListView.IXListViewListener() {
                @Override
                public void onRefresh() {//上拉刷新数据
                    getCustomerAddressData();
                }

                @Override
                public void onLoadMore() {
                }
            });
         //  mPartyAddressListview.setOnItemClickListener(this);

            mTextviewNodata.setOnClickListener(this);
            mButtonAddAddress.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    private void showmAddDialog() {

        if (mDialog==null){
            mDialog=new Dialog(PartyAddressManageActivity.this);
        }
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_address_update);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = DensityUtil.dip2px(DensityUtil.getWidth_dp());//宽高可设置具体大小
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        ed_address_belong =(TextView)window.findViewById(R.id.ed_ADDRESS_BELONG);
        ed_address_belong.setOnClickListener(this);
        ed_address_address = (EditText) window.findViewById(R.id.ed_ADDRESS_ADDRESS);
        ed_address_person = (EditText) window.findViewById(R.id.ed_ADDRESS_PERSON);
        ed_address_tel = (EditText) window.findViewById(R.id.ed_ADDRESS_TEL);
        bt_dialog_comfit= (Button) window.findViewById(R.id.bt_dialog_update);
        bt_dialog_comfit.setOnClickListener(this);
        bt_dialog_comfit.setText("添加");
        bt_dialog_cancel= (Button) window.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_cancel.setOnClickListener(this);
    }

    private void showmUpdateDialog(Address address) {

        if (mDialog==null){
            mDialog=new Dialog(PartyAddressManageActivity.this);
        }
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_address_update);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = DensityUtil.dip2px(DensityUtil.getWidth_dp());//宽高可设置具体大小
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        ed_address_belong =(TextView)window.findViewById(R.id.ed_ADDRESS_BELONG);
        ed_address_belong.setOnClickListener(this);
        ed_address_address = (EditText) window.findViewById(R.id.ed_ADDRESS_ADDRESS);
        ed_address_person = (EditText) window.findViewById(R.id.ed_ADDRESS_PERSON);
        ed_address_tel = (EditText) window.findViewById(R.id.ed_ADDRESS_TEL);
        bt_dialog_comfit= (Button) window.findViewById(R.id.bt_dialog_update);
        bt_dialog_comfit.setText("修改");
        bt_dialog_comfit.setTag("updata");
        bt_dialog_comfit.getTag();
        bt_dialog_comfit.setOnClickListener(this);
        bt_dialog_cancel= (Button) window.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_cancel.setOnClickListener(this);
    }
    /**
     * 网络获取数据
     */
    public void getCustomerAddressData() {
        try {
            boolean isSuccess = mBiz.getPartyAddressInfo(partyidx);
            if (isSuccess) {//发送请求成功
                showDwonLoadingDialog();
            } else {//发送请求失败
                mTextviewNodata.setVisibility(View.VISIBLE);
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据是显示的 Dialog
     */
    private void showDwonLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MyLoadingDialog(PartyAddressManageActivity.this);
        }
        mLoadingDialog.showDialog();
    }

    public void deletePartyAddress(Address address) {
        try {
            boolean isSuccess = mBiz.deleteAddress(address.getIDX());
            if (isSuccess) {//发送请求成功
                showDwonLoadingDialog();
            } else {//发送请求失败
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }

    }
   public void deletePartyAddressSuccess(String msg){
       try {
           ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);

           mLoadingDialog.dismiss();
           getCustomerAddressData();
       } catch (Exception e) {
           ExceptionUtil.handlerException(e);
       }
   }



    public void deletePartyAddressError(String msg) {
        try {
            if (mLoadingDialog!=null){
                mLoadingDialog.dismiss();
            }
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 处理获取客户列表数据结果
     */
    private void handleGetCustomerData() {
        try {
            mLoadingDialog.dismiss();
            List<Address> addresses = mBiz.getmCustomerAddressList();
            mPartyAddressListview.stopRefresh();
            mPartyAddressListAdapter.notifyChange(addresses);
            if (addresses == null || addresses.size() <= 0) {
                mTextviewNodata.setVisibility(View.VISIBLE);
            } else {
                mTextviewNodata.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据失败
     *
     * @param msg 失败的原因
     */
    public void getCustomerAddressDataError(String msg) {
        try {
            handleGetCustomerData();
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据成功
     */
    public void getCustomerAddressDataSuccess() {
        try {
            handleGetCustomerData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }




    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mBiz.cancelRequest();
        mBiz = null;
        mImageViewGoBack = null;

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.textview_nodata://重新加载数据
                    getCustomerAddressData();
                    break;
                case R.id.button_goback:
                    this.finish();
                    break;
                case R.id.bt_addaddress:
                    showmAddDialog();
                    break;
                case R.id.ed_ADDRESS_BELONG:
                    Intent intent0=new Intent(PartyAddressManageActivity.this, ChooseProvinceActivity.class);
                    startActivityForResult(intent0,mRequestcode);
                    break;
                case R.id.bt_dialog_cancel:
                    mDialog.dismiss();
                    break;
                case R.id.bt_dialog_update:
                    if (ed_address_address.getText().toString().isEmpty()||ed_address_person.getText().toString().isEmpty()
                            ||ed_address_tel.getText().toString().isEmpty()||ed_address_belong.getText().toString().isEmpty())
                    {
                        ToastUtil.showToastBottom("添加客户信息填写不完整",Toast.LENGTH_SHORT);
                    }else {
                        if (mDialog!=null){ mDialog.dismiss();}
                        if (mBiz.addPartyAddress(partyidx,pv.getITEM_IDX(),ct.getITEM_IDX(),ar.getITEM_IDX(),
                                ru.getITEM_IDX(), ed_address_address.getText().toString(),
                                pv.getITEM_NAME()+ct.getITEM_NAME()+ar.getITEM_NAME()+ru.getITEM_NAME()+ed_address_address.getText().toString(),
                                ed_address_person.getText().toString(), ed_address_tel.getText().toString())){
                            showDwonLoadingDialog();
                        } else {//发送请求失败
                            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                        }
                    }
                    break;
//                case R.id.bt_dialog_update:
//                    if (ed_address_address.getText().toString().isEmpty()||ed_address_person.getText().toString().isEmpty()
//                            ||ed_address_tel.getText().toString().isEmpty() ||ed_party_name.getText().toString().isEmpty()
//                            ||ed_address_belong.getText().toString().isEmpty()){
//                        ToastUtil.showToastBottom("客户信息填写不完整",Toast.LENGTH_SHORT);
//                    }else {
//                        mDialog.dismiss();
//                        if (mBiz.updataAddress(ed_party_name.getText().toString(),ed_party_remark.getText().toString()+""
//                                ,pv,ct,ar,ru,ed_address_address.getText().toString(),ed_address_person.getText().toString(),
//                                ed_address_tel.getText().toString())){
//                            showDwonLoadingDialog();
//                        } else {//发送请求失败
//                            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
//                        }
//                    }
//                    break;
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
            Address mSelecterAddress=mBiz.getmCustomerAddressList().get(position-1);
            PartyAddressManageActivity.this.showmAddDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case mRequestcode:
                    try {
                        pv = data.getParcelableExtra("province");
                        ct = data.getParcelableExtra("city");
                        ar = data.getParcelableExtra("area");
                        ru = data.getParcelableExtra("rural");
                        ed_address_belong.setText(pv.getITEM_NAME().trim()+ ct.getITEM_NAME().trim()+
                                ar.getITEM_NAME().trim()+ru.getITEM_NAME().trim());
                    }catch (Exception e){
                        ToastUtil.showToastBottom("所属地区加载失败，请重新选择！",Toast.LENGTH_SHORT);
                    }
                    break;
                default:
                    break;

            }
        }
    }


}
