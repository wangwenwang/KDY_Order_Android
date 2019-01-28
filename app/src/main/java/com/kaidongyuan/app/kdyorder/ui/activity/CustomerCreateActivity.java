package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ChannelChoiceAdapter;
import com.kaidongyuan.app.kdyorder.adapter.LineChoiceAdapter;
import com.kaidongyuan.app.kdyorder.adapter.PartyList1Adapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.CustomerChannel;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeetingLine;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.CustomerCreateActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 * 新建客户界面
 */
public class CustomerCreateActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应业务类
     */
    private CustomerCreateActivityBiz mBiz;


    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;


    private AlertDialog linesAlertDialog;    // 拜访线路多选框
    private ArrayList<Boolean> linesCheckedList;                    // 记住多选的item
    public String linesParmas;

    private LinearLayout llMeetingType, llPartyChannel, llSendAddress;
    private TextView tvMeetingType, tvPartyChannel, tvFatherAddressName;
    public EditText edPartyName, edPartyCode;
    public String strChannel = "";
    public String strLine = "";
    public String fatherPartyAddressID;

    /**
     * 选择客户渠道的 Dialog
     */
    private Dialog mChoiceChannelDialog;
    /**
     * 显示客户渠道的 ListView
     */
    private ListView mListViewChoiceChannels;
    /**
     * 选择客户渠道的 Adapter
     */
    private ChannelChoiceAdapter mChannelsChoiceAdapter;
    /**
     * 记录当前选中的报表在 ListView 中的位置
     */
    private int mCurrentChannelIndex = 0;

    /**
     * 选择拜访线路的 Dialog
     */
    private Dialog mChoiceLineDialog;
    /**
     * 显示拜访线路的 ListView
     */
    private ListView mListViewChoiceLines;
    /**
     * 选择拜访线路的 Adapter
     */
    private LineChoiceAdapter mLinesChoiceAdapter;
    /**
     * 记录当前选中的报表在 ListView 中的位置
     */
    private int mCurrentLineIndex = 0;
    public EditText edPartyRemark;
    public EditText edContactPerson;
    public EditText edContactTel;
    public EditText edAddressDetail;
    private TextView tvAddContact;
    private TextView tvAddressBelong;
    private TextView tvAddressGps;
    private Button buttonUpdata;
    private final int RequestAddContact = 1001;
    private final int RequestAddressBelong = 1008;
    /**
     *
     */
    public NormalAddress pv, ct, ar, ru;
    public String latitude = "";
    public String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_create_party);
        try {
            initData();
            setTop();
            initView();
            setListener();
            mBiz.ObtainPartyCode(MyApplication.getInstance().getBusiness().getBUSINESS_CODE(), MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
            fatherPartyAddressID = getIntent().getStringExtra("fatherPartyAddressID");
            tvFatherAddressName.setText(getIntent().getStringExtra("fatherPartyAddressName"));
            getMeetingLineDatas();
            getPartyVisitChannel();
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
        mBiz = new CustomerCreateActivityBiz(this);
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
            llMeetingType = (LinearLayout) findViewById(R.id.ll_meeting_type);
            tvMeetingType = (TextView) findViewById(R.id.tv_meeting_type);
            llPartyChannel = (LinearLayout) findViewById(R.id.ll_party_channel);
            tvPartyChannel = (TextView) findViewById(R.id.tv_party_channel);
            tvFatherAddressName = (TextView) findViewById(R.id.tv_father_address_name);
            edPartyCode = (EditText) findViewById(R.id.ed_party_code);
            edPartyName = (EditText) findViewById(R.id.ed_party_name);
            edPartyRemark = (EditText) findViewById(R.id.ed_party_remark);
            edContactPerson = (EditText) findViewById(R.id.ed_contact_person);
            edContactTel = (EditText) findViewById(R.id.ed_contact_tel);
            edAddressDetail = (EditText) findViewById(R.id.ed_address_detail);
            tvAddContact = (TextView) findViewById(R.id.tv_add_contact);
            tvAddressBelong = (TextView) findViewById(R.id.tv_address_belong);
            tvAddressGps = (TextView) findViewById(R.id.tv_address_gps);
            buttonUpdata = (Button) findViewById(R.id.button_updata);
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            llSendAddress = (LinearLayout) findViewById(R.id.ll_send_address);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            llMeetingType.setOnClickListener(this);
            llPartyChannel.setOnClickListener(this);
            tvAddContact.setOnClickListener(this);
            tvAddressBelong.setOnClickListener(this);
            buttonUpdata.setOnClickListener(this);
            llSendAddress.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取客服拜访线路数据集
     */
    private void getPartyVisitChannel() {
        try {
            if (mBiz.GetPartyVisitChannel()) {
                // showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取客服拜访线路数据集
     */
    private void getMeetingLineDatas() {
        try {
            if (mBiz.GetPartyVisitLines()) {
                // showLoadingDialog();
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
     * 获取数据失败
     *
     * @param msg 失败的原因
     */
    public void getCustomerDataError(String msg) {
        try {
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
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
     * 获取客户拜访线路类型失败
     *
     * @param message 要显示的消息
     */
    public void getChannelsError(String message) {
        try {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    public void getChannelsSuccess(List<CustomerChannel> channels) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        // 不给默认
//        if (channels != null && channels.size() > 0) {
//            strChannel = channels.get(0).getITEM_NAME();
//            tvPartyChannel.setText(strChannel);
//
//        }
    }

    /**
     * 获取客户拜访线路类型失败
     *
     * @param message 要显示的消息
     */
    public void getMeetingLinesError(String message) {
        try {
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 保存客户成功
     *
     * @param
     */
    public void updataPartySuccess() {
        try {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("新增客户成功", Toast.LENGTH_SHORT);
            finish();
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
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void getMeetingLinesSuccess(List<CustomerMeetingLine> customerMeetingLines) {
//        if (mLoadingDialog != null) mLoadingDialog.dismiss();
//        if (customerMeetingLines != null && customerMeetingLines.size() > 0) {
//            // 拜访线路定位到当天
//            for (int i = 0; i < customerMeetingLines.size(); i++) {
//                String line = customerMeetingLines.get(i).getITEM_NAME();
//                if (line.equals(DateUtil.GetCurrWeek())) {
//                    strLine = line;
//                    break;
//                }
//            }
//            strLine = strLine.equals("") ? customerMeetingLines.get(0).getITEM_NAME() : strLine;
//            tvMeetingType.setText(strLine);
//        }
    }

    /**
     * 显示选择线路 Dialog
     */
    private void showChoiceChannelDialog() {
        try {
            if (mBiz.getChannels() == null || mBiz.getChannels().size() <= 0) {
                getMeetingLineDatas();
                return;
            }
            if (mChoiceChannelDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceChannelDialog = builder.show();
                mChoiceChannelDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceChannelDialog.getWindow();
                window.setContentView(R.layout.dialog_channel_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceChannelDialog.dismiss();
                    }
                });
                mListViewChoiceChannels = (ListView) window.findViewById(R.id.listView_chart_choice);
                mChannelsChoiceAdapter = new ChannelChoiceAdapter(null, this);
                mListViewChoiceChannels.setAdapter(mChannelsChoiceAdapter);
                mListViewChoiceChannels.setOnItemClickListener(new CustomerCreateActivity.InnerOnItemClickListener2());
            }
            mChoiceChannelDialog.show();
            mChannelsChoiceAdapter.notifyChange(mBiz.getChannels());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示选择线路 Dialog
     */
    private void showChoiceLineDialog() {
        Context mContext = this;
        try {
            if (mBiz.getMeetingLines() == null || mBiz.getMeetingLines().size() <= 0) {
                getMeetingLineDatas();
                return;
            }
            final String[] items = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
            // 让当天默认选中
            if (linesCheckedList == null) {
                linesCheckedList = new ArrayList();
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[0]));
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[1]));
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[2]));
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[3]));
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[4]));
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[5]));
                linesCheckedList.add(DateUtil.GetCurrWeek().equals(items[6]));
            }

            boolean[] array = new boolean[linesCheckedList.size()];
            for (int i = 0; i < linesCheckedList.size(); i++) {
                array[i] = linesCheckedList.get(i);
            }
            if (linesAlertDialog == null) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                alertBuilder.setTitle("请选择拜访线路");
                alertBuilder.setMultiChoiceItems(items, array, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        linesCheckedList.set(i, isChecked);
                    }
                });
                alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<String> selectedString = new ArrayList();
                        for (int u = 0; u < linesCheckedList.size(); u++) {

                            if (linesCheckedList.get(u)) {
                                selectedString.add(items[u]);
                            }
                        }
                        if (selectedString.size() == 0) {
                            return;
                        }
                        linesAlertDialog.dismiss();
                        if (selectedString.size() == 1) {
                            tvMeetingType.setText(selectedString.get(0));
                        } else {
                            String weeksString = "";
                            for (int j = 0; j < selectedString.size(); j++) {
                                weeksString = weeksString + selectedString.get(j) + "  ";
                            }
                            weeksString = weeksString.replace("星期", "");
                            tvMeetingType.setText(weeksString);
                        }
                        for (int j = 0; j < selectedString.size(); j++) {
                            if (j == 0) {
                                linesParmas = selectedString.get(j);
                            } else {
                                linesParmas = linesParmas + "," + selectedString.get(j);
                            }
                        }
                        Log.d("LM", "onClick: ");
                    }
                });
                alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        linesAlertDialog.dismiss();
                    }
                });
                linesAlertDialog = alertBuilder.create();
                Log.d("LM", "onClick: ");
            }
            linesAlertDialog.show();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * Dialog 中选择客户渠道的监听
     */
    private class InnerOnItemClickListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceChannelDialog.dismiss();
//                if (mCurrentChannelIndex == position) {
//                    return;
//                }
                mCurrentChannelIndex = position;
                strChannel = mBiz.getChannels().get(mCurrentChannelIndex).getITEM_NAME();
                tvPartyChannel.setText(strChannel);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback:
                    this.finish();
                    break;
                case R.id.ll_meeting_type:

                    showChoiceLineDialog();
                    break;
                case R.id.ll_party_channel:
                    showChoiceChannelDialog();
                    break;
                case R.id.tv_add_contact:
                    Uri uri = Uri.parse("content://contacts/people");
                    Intent intent = new Intent(Intent.ACTION_PICK, uri);
                    startActivityForResult(intent, RequestAddContact);
                    break;
                case R.id.tv_address_belong:
                    intent = new Intent(CustomerCreateActivity.this, ChooseProvinceActivity.class);
                    startActivityForResult(intent, RequestAddressBelong);
                    break;
                case R.id.button_updata:

                    if(StringUtils.isBlank(edPartyName.getText().toString())) {
                        updataPartyError("客户名称不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(edPartyCode.getText().toString())) {
                        updataPartyError("客户代码不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(strChannel)) {
                        updataPartyError("渠道不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(linesParmas)) {
                        updataPartyError("拜访线路不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(edContactPerson.getText().toString())) {
                        updataPartyError("收货人名称不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(edContactTel.getText().toString())) {
                        updataPartyError("收货人电话不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(tvAddressBelong.getText().toString())) {
                        updataPartyError("所在地区不能为空");
                        return;
                    }
                    if(StringUtils.isBlank(edAddressDetail.getText().toString())) {
                        updataPartyError("详细地址不能为空");
                        return;
                    }
                    if(latitude.length() <= 0 || latitude.length() <= 0) {
                        updataPartyError("经纬坐标不能为空");
                        return;
                    }
                    if(fatherPartyAddressID == null) {
                        updataPartyError("供货商不能为空");
                        return;
                    }
                    showLoadingDialog();
                    mBiz.AddParty();
                    break;
                case R.id.ll_send_address:
                    Intent bls = new Intent(CustomerCreateActivity.this, YBMyAddressAdd2.class);
                    startActivityForResult(bls, 110);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == 110) && (resultCode == 120)) {
//            String aa=data.getExtras().getString("address");
            String addressDetail = data.getExtras().getString("addressDetail");
            latitude = data.getExtras().getString("latitude");
            longitude = data.getExtras().getString("longitude");
            float latitudeF = Float.parseFloat(latitude);
            float longitudeF = Float.parseFloat(longitude);
            String latitudeS = String.format("%.6f", latitudeF);
            String longitudeS = String.format("%.6f", longitudeF);
            tvAddressGps.setText(latitudeS + "，" + longitudeS + "（" + addressDetail + "附近）");//得到新Activity 关闭后返回的数据
//            addrEdt.setText(bb);//得到新Activity 关闭后返回的数据
        }

        switch (requestCode) {
            case RequestAddContact:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息     
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                edContactPerson.setText(contacts[0].trim());
                edContactTel.setText(contacts[1].trim());

                break;
            case RequestAddressBelong:
                try {
                    pv = data.getParcelableExtra("province");
                    ct = data.getParcelableExtra("city");
                    ar = data.getParcelableExtra("area");
                    ru = data.getParcelableExtra("rural");
                    tvAddressBelong.setText(pv.getITEM_NAME().trim() + ct.getITEM_NAME().trim()
                            + ar.getITEM_NAME().trim() + ru.getITEM_NAME().trim());
                } catch (Exception e) {
                    ExceptionUtil.handlerException(e);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    /**
     * 推荐客户代码
     *
     * @param
     */
    public void ObtainPartyCodeSuccess(String partyCode) {
        try {
            edPartyCode.setText(partyCode);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}

