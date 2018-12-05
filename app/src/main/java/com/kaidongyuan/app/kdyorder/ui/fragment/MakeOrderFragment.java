package com.kaidongyuan.app.kdyorder.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.AddressListAdapter;
import com.kaidongyuan.app.kdyorder.adapter.PartyListAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.MakeOrderFragmentBiz;
import com.kaidongyuan.app.kdyorder.ui.activity.MakeOrderActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.kaidongyuan.app.kdyorder.widget.xlistview.XListView;

import java.util.List;


/**
 * Created by Administrator on 2016/4/1.
 * 主页 Fragment
 */
public class MakeOrderFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View mParentView;

    /**
     * 下单业务类
     */
    private MakeOrderFragmentBiz mBiz;
    /**
     * 客户列表 ListView
     */
    private XListView mCustomerListview;
    /**
     * 客户列表
     */
    private PartyListAdapter mPartyListAdapter;
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
    /**
     * 显示客户地址的 Dialog
     */
    private Dialog mCustomerAddressDialog;
    /**
     * 显示客户地址的 ListView
     */
    private ListView mListviewCustomerAddress;
    /**
     * 取消选择客户地址
     */
    private Button mButtonCancelChoiceAddress;
    /**
     * 显示客户地址的 Adapter
     */
    private AddressListAdapter mCustomerAddressAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mParentView = inflater.inflate(R.layout.fragment_makeorder, null);
            initData();
            initView();
            initListener();
            return mParentView;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return mParentView;
        }
    }

    @Override
    public void onStart() {
        getCustomerData();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            mBiz.cancelRequest();
            mBiz = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            if (mCustomerAddressDialog != null && mCustomerAddressDialog.isShowing()) {
                mCustomerAddressDialog.dismiss();
            }
            mLoadingDialog = null;
            mCustomerListview = null;
            mCustomerListview = null;
            mPartyListAdapter = null;
            mParentView = null;
            mEdittextSearch = null;
            mTextviewNodata = null;
            mListviewCustomerAddress = null;
            mButtonCancelChoiceAddress = null;
            mCustomerAddressAdapter = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络获取数据
     */
    private void getCustomerData() {
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

    private void initData() {
        try {
            mBiz = new MakeOrderFragmentBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initView() {
        try {
            mCustomerListview = (XListView) mParentView.findViewById(R.id.lv_select_customer);
            mPartyListAdapter = new PartyListAdapter(this.getActivity(), null);
            mCustomerListview.setAdapter(mPartyListAdapter);
            mCustomerListview.setPullLoadEnable(false);
            mCustomerListview.setPullRefreshEnable(true);
            mEdittextSearch = (EditText) mParentView.findViewById(R.id.edittext_headview_content);
            mTextviewNodata = (TextView) mParentView.findViewById(R.id.textview_nodata);
            mTextviewNodata.setVisibility(View.GONE);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initListener() {
        try {
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
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//选择了客户，进入选择地址
        try {
            if (mBiz.getPartygetAddressInfo(position - 1)) {//发送请求成功,集合中数据的 Index 比 ListView 中的Index 小 1，考虑 headerView
                showDwonLoadingDialog();
            } else {//发送请求失败
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
            mLoadingDialog = new MyLoadingDialog(this.getActivity());
        }
        mLoadingDialog.showDialog();
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
     * 处理获取客户地址请求失败
     *
     * @param msg 要显示的信息
     */
    public void getCustomerAdressDataError(String msg) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 处理获取客户地址成功
     */
    public void getCustomerAdressDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            List<Address> customerAddress = mBiz.getmCustomerAddress();
            int customerAddressSize = customerAddress.size();
            if (customerAddressSize == 1) {
                jumpToMakeOrderActivity(customerAddress.get(0));
            } else if (customerAddressSize > 1) {
                showAddressDialog(customerAddress);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示选择客户地址 Dialog
     *
     * @param customerAddress 客户地址集合
     */
    private void showAddressDialog(List<Address> customerAddress) {
        try {
            if (customerAddress.size() <= 0) {
                ToastUtil.showToastBottom("没有有效的客户地址！", Toast.LENGTH_SHORT);
            }
            if (mCustomerAddressDialog == null) {
                createAddressDialog();
            }
            mCustomerAddressDialog.show();
            mCustomerAddressAdapter.notifyChange(customerAddress);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void createAddressDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            mCustomerAddressDialog = builder.create();
            mCustomerAddressDialog.setCanceledOnTouchOutside(false);
            mCustomerAddressDialog.show();
            Window window = mCustomerAddressDialog.getWindow();
            window.setContentView(R.layout.dialog_customeraddress_choice);
            mListviewCustomerAddress = (ListView) window.findViewById(R.id.listView_customeraddress);
            mListviewCustomerAddress.setOnItemClickListener(new InnerOnItemClickListener());
            mButtonCancelChoiceAddress = (Button) window.findViewById(R.id.button_cancelchoiceaddress);
            mButtonCancelChoiceAddress.setOnClickListener(this);
            mCustomerAddressAdapter = new AddressListAdapter(this.getActivity(), null);
            mListviewCustomerAddress.setAdapter(mCustomerAddressAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 选择用户地址 Dialog 中 ListView 的监听
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mCustomerAddressDialog.dismiss();
                Address address = mBiz.getmCustomerAddress().get(position);
                jumpToMakeOrderActivity(address);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_cancelchoiceaddress://取消选择用户地址下单,Dialog中按钮
                    mCustomerAddressDialog.dismiss();
                    break;
                case R.id.textview_nodata://重新加载数据
                    getCustomerData();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 跳转到下单界面
     *
     * @param address 下单客户地址
     */
    private void jumpToMakeOrderActivity(Address address) {
        try {
            Intent makeOrderIntent = new Intent(getActivity(), MakeOrderActivity.class);
            Party party = mBiz.getmSelectedParty();
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_PARTY_ID, party.getIDX());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_PARTY_NAME, party.getPARTY_NAME());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_CODE, address.getADDRESS_CODE());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX, address.getIDX());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_INFORMATION, address.getADDRESS_INFO());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactPerson,address.getCONTACT_PERSON());
            makeOrderIntent.putExtra(EXTRAConstants.ORDER_ADDRESS_ContactTel,address.getCONTACT_TEL());
            startActivity(makeOrderIntent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

}























