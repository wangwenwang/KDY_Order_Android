package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.kaidongyuan.app.kdyorder.adapter.PartyManageListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.model.PartyManageActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/12.
 */
public class PartyManageActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    /**
     * 标题栏返回按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 下单业务类
     */
    private PartyManageActivityBiz mBiz;
    /**
     * 客户列表 ListView
     */
    private XListView mCustomerListview;
    /**
     * 客户列表
     */
    private PartyManageListAdapter mPartyListAdapter;
    /**
     * 搜索客户编辑框
     */
    private EditText mEdittextSearch;
    /**
     * 没有数据时显示的控件，默认为不显示
     */
    private TextView mTextviewNodata;
    /**
     * 网络请求是的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    private Button mButtonAddParty;

    private Dialog mDialog;
    private Button bt_dialog_comfit,bt_dialog_cancel;
    private EditText ed_party_name;
    private EditText ed_party_remark;
    private EditText ed_address_province,ed_address_city,ed_address_area,ed_address_rural;
    private TextView ed_address_belong;
    private EditText ed_address_address;
    private EditText ed_address_person;
    private EditText ed_address_tel;
    private final int mRequestcode=1004;
    private NormalAddress pv,ct,ar,ru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minefragment_partymanage);
        initData();
        initView();
        initListener();
        getCustomerData();
    }

    private void initData() {
        try {
            mBiz = new PartyManageActivityBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mCustomerListview = (XListView) this.findViewById(R.id.lv_select_customer);
            mPartyListAdapter = new PartyManageListAdapter(PartyManageActivity.this, null);
            mCustomerListview.setAdapter(mPartyListAdapter);
            mCustomerListview.setPullLoadEnable(false);
            mCustomerListview.setPullRefreshEnable(true);
            mEdittextSearch = (EditText) this.findViewById(R.id.edittext_headview_content);
            mTextviewNodata = (TextView) this.findViewById(R.id.textview_nodata);
            mTextviewNodata.setVisibility(View.GONE);
            mButtonAddParty= (Button) this.findViewById(R.id.bt_addparty);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mCustomerListview.setXListViewListener(new XListView.IXListViewListener() {
                @Override
                public void onRefresh() {//上拉刷新数据
                    mEdittextSearch.setText("");
                    getCustomerData();
                }

                @Override
                public void onLoadMore() {
                }
            });
            mCustomerListview.setOnItemClickListener(this);
            mEdittextSearch.addTextChangedListener(new InnerTextWatcher());
            mTextviewNodata.setOnClickListener(this);
            mButtonAddParty.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    private void showmDialog() {

        if (mDialog==null){
            mDialog=new Dialog(PartyManageActivity.this);
        }
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_party_add);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = DensityUtil.dip2px(DensityUtil.getWidth_dp());//宽高可设置具体大小
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        ed_party_name = (EditText) window.findViewById(R.id.ed_Party_NAME);
        ed_party_remark = (EditText) window.findViewById(R.id.ed_Party_Remark);
//        ed_address_province = (EditText) window.findViewById(R.id.ed_ADDRESS_PROVINCE);
//        ed_address_province.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b){
//                    Intent intent=new Intent(PartyManageActivity.this, ChooseProvinceActivity.class);
//                    startActivityForResult(intent,mRequestcode);
//                }
//                return;
//            }
//        });
//
//        ed_address_city = (EditText) window.findViewById(R.id.ed_ADDRESS_CITY);
//        ed_address_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b){
//                    Intent intent=new Intent(PartyManageActivity.this,ChooseProvinceActivity.class);
//                    startActivityForResult(intent,mRequestcode);
//                }
//                return;
//            }
//        });
//        ed_address_area = (EditText) window.findViewById(R.id.ed_ADDRESS_AREA);
//        ed_address_rural = (EditText) window.findViewById(R.id.ed_ADDRESS_RURAL);
        ed_address_belong =(TextView)window.findViewById(R.id.ed_ADDRESS_BELONG);
        ed_address_belong.setOnClickListener(this);
        ed_address_address = (EditText) window.findViewById(R.id.ed_ADDRESS_ADDRESS);
        ed_address_person = (EditText) window.findViewById(R.id.ed_ADDRESS_PERSON);
        ed_address_tel = (EditText) window.findViewById(R.id.ed_ADDRESS_TEL);
        bt_dialog_comfit= (Button) window.findViewById(R.id.bt_dialog_comfit);
        bt_dialog_comfit.setOnClickListener(this);
        bt_dialog_cancel= (Button) window.findViewById(R.id.bt_dialog_cancel);
        bt_dialog_cancel.setOnClickListener(this);
    }
    /**
     * 网络获取数据
     */
    public void getCustomerData() {
        try {
            boolean isSuccess = mBiz.getCustomerData();
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

    public void addPartySuccess(){
        try {
            ToastUtil.showToastBottom("添加客户成功！",Toast.LENGTH_SHORT);
            getCustomerData();
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据是显示的 Dialog
     */
    private void showDwonLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MyLoadingDialog(PartyManageActivity.this);
        }
        mLoadingDialog.showDialog();
    }

    public void deleteParty(Party party) {
        try {
            boolean isSuccess = mBiz.deleteParty(party.getIDX());
            if (isSuccess) {//发送请求成功
                showDwonLoadingDialog();
            } else {//发送请求失败
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }

    }

    public void deletePartyError(String msg) {
        try {
            if (mLoadingDialog!=null){
                mLoadingDialog.dismiss();
            }
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 内部监听编辑框变化的类
     */
    private class InnerTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {//用户输入的文字变化开始搜索，显示结果
            try {
                String msg = s.toString();
                mBiz.searchParty(msg);//搜索客户
                //显示搜索后的客户列表
                List<Party> parties = mBiz.getmCustomerList();
                mPartyListAdapter.notifyChange(parties);
                if (parties.size() <= 0) {
                    mTextviewNodata.setVisibility(View.VISIBLE);
                } else {
                    mTextviewNodata.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
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
            List<Party> parties = mBiz.getmCustomerList();
            mCustomerListview.stopRefresh();
            mPartyListAdapter.notifyChange(parties);
            if (parties == null || parties.size() <= 0) {
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
    public void getCustomerDataError(String msg) {
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
    public void getCustomerDataSuccess() {
        try {
            handleGetCustomerData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 处理获取客户地址请求失败
     *
     * @param msg 要显示的信息
     */
    public void getCustomerAdressDataError(String msg) {
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
                    getCustomerData();
                    break;
                case R.id.button_goback:
                    this.finish();
                    break;
                case R.id.bt_addparty:
                    showmDialog();
                    break;
                case R.id.ed_ADDRESS_BELONG:
                    Intent intent0=new Intent(PartyManageActivity.this, ChooseProvinceActivity.class);
                    startActivityForResult(intent0,mRequestcode);
                    break;
                case R.id.bt_dialog_cancel:
                    mDialog.dismiss();
                    break;
                case R.id.bt_dialog_comfit:
                    if (ed_address_address.getText().toString().isEmpty()||ed_address_person.getText().toString().isEmpty()
                            ||ed_address_tel.getText().toString().isEmpty() ||ed_party_name.getText().toString().isEmpty()
                            ||ed_address_belong.getText().toString().isEmpty()){
                        ToastUtil.showToastBottom("添加客户信息填写不完整",Toast.LENGTH_SHORT);
                    }else {
                        mDialog.dismiss();
                        if (mBiz.addParty(ed_party_name.getText().toString(),ed_party_remark.getText().toString()+"",ct.getITEM_NAME()
                                ,pv.getITEM_IDX(),ct.getITEM_IDX(),ar.getITEM_IDX(),ru.getITEM_IDX(),ed_address_address.getText().toString(),
                                pv.getITEM_NAME()+ct.getITEM_NAME()+ar.getITEM_NAME()+ru.getITEM_NAME()+ed_address_address.getText().toString(),
                                ed_address_person.getText().toString(),ed_address_tel.getText().toString())){
                            showDwonLoadingDialog();
                        } else {//发送请求失败
                            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                        }
                    }
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
            Party mSelecteParty=mBiz.getmCustomerList().get(position-1);
            Intent partyaddressmanageintent=new Intent(PartyManageActivity.this,PartyAddressManageActivity.class);
            partyaddressmanageintent.putExtra("partyidx",mSelecteParty.getIDX());
            startActivity(partyaddressmanageintent);
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
                        ed_address_belong.setText(pv.getITEM_NAME().trim()+ ct.getITEM_NAME().trim()+ ar.getITEM_NAME().trim()+ru.getITEM_NAME().trim());
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
