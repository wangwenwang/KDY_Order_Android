package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.kaidongyuan.app.kdyorder.model.CustomerCreateActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

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

    private LinearLayout llMeetingType, llPartyChannel;
    private TextView tvMeetingType, tvPartyChannel;
    public EditText edPartyName, edPartyCode;
    public String strSearch = "";
    public String strChannel = "";
    public String strLine = "";
    public Address fatherPartyAddress;
    /**
     * 选择上级客户的 Dialog
     */
    private Dialog mChoiceFatherPartyDialog;
    /**
     * 显示上级客户的 ListView
     */
    private ListView mListViewChoiceFatherPartys;
    /**
     * 选择上级客户的 Adapter
     */
    private PartyList1Adapter mPartyListAdapter;
    /**
     * 记录当前选中的报表在 ListView 中的位置
     */
    private int mCurrentFatherPartyIndex = 0;

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
    private final int RequestAddressBelong=1008;
    /**
     *
     */
    public NormalAddress pv,ct,ar,ru;
    public double lat=1,lng=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getCustomerData();
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
            edPartyCode = (EditText) findViewById(R.id.ed_party_code);
            edPartyName = (EditText) findViewById(R.id.ed_party_name);
            edPartyRemark = (EditText) findViewById(R.id.ed_party_remark);
            edContactPerson = (EditText) findViewById(R.id.ed_contact_person);
            edContactTel = (EditText) findViewById(R.id.ed_contact_tel);
            edAddressDetail = (EditText) findViewById(R.id.ed_address_detail);
            tvAddContact = (TextView) findViewById(R.id.tv_add_contact);
            tvAddressBelong = (TextView) findViewById(R.id.tv_address_belong);
            tvAddressGps = (TextView) findViewById(R.id.tv_address_gps);
            buttonUpdata= (Button) findViewById(R.id.button_updata);
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
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
                showLoadingDialog();
            } else {//发送请求失败
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
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
     * 处理获取客户地址成功
     */
    public void getCustomerAdressDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            List<Address> customerAddress = mBiz.getmCustomerAddress();

            if (customerAddress != null && customerAddress.size() > 0) {
                fatherPartyAddress = customerAddress.get(0);
            } else {
                ToastUtil.showToastBottom("未获取到上级客户地址信息", Toast.LENGTH_SHORT);
            }
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
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    public void getChannelsSuccess(List<CustomerChannel> channels) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        if (channels != null && channels.size() > 0) {
            strChannel = channels.get(0).getITEM_NAME();
            tvPartyChannel.setText(strChannel);

        }
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
     * @param
     */
    public void updataPartySuccess() {
        try {
            if (mLoadingDialog!=null)mLoadingDialog.dismiss();
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
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void getMeetingLinesSuccess(List<CustomerMeetingLine> customerMeetingLines) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        if (customerMeetingLines != null && customerMeetingLines.size() > 0) {
            strLine = customerMeetingLines.get(0).getITEM_NAME();
            tvMeetingType.setText(strLine);
        }
    }

    /**
     * 获取数据成功
     */
    public void getCustomerDataSuccess() {
        try {
            showChoiceFatherPartyDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示选择上级客户 Dialog
     */
    private void showChoiceFatherPartyDialog() {
        try {
            if (mChoiceFatherPartyDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceFatherPartyDialog = builder.show();
                mChoiceFatherPartyDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceFatherPartyDialog.getWindow();
                window.setContentView(R.layout.dialog_fatherparty_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceFatherPartyDialog.dismiss();
                    }
                });
                mListViewChoiceFatherPartys = (ListView) window.findViewById(R.id.listView_chart_choice);
                mPartyListAdapter = new PartyList1Adapter(this, null);
                mListViewChoiceFatherPartys.setAdapter(mPartyListAdapter);
                mListViewChoiceFatherPartys.setOnItemClickListener(new CustomerCreateActivity.InnerOnItemClickListener1());
            }
            mChoiceFatherPartyDialog.show();
            mPartyListAdapter.notifyChange(mBiz.getmCustomerList());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 显示选择线路 Dialog
     */
    private void showChoiceChannelDialog() {
        try {
            if (mBiz.getChannels()==null||mBiz.getChannels().size()<=0){
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
        try {
            if (mBiz.getMeetingLines()==null||mBiz.getMeetingLines().size()<=0){
               getMeetingLineDatas();
                return;
            }
            if (mChoiceLineDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceLineDialog = builder.show();
                mChoiceLineDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceLineDialog.getWindow();
                window.setContentView(R.layout.dialog_line_choice);
                window.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mChoiceLineDialog.dismiss();
                    }
                });
                mListViewChoiceLines = (ListView) window.findViewById(R.id.listView_chart_choice);
                mLinesChoiceAdapter = new LineChoiceAdapter(null, this);
                mListViewChoiceLines.setAdapter(mLinesChoiceAdapter);
                mListViewChoiceLines.setOnItemClickListener(new CustomerCreateActivity.InnerOnItemClickListener());
            }
            mChoiceLineDialog.show();
            mLinesChoiceAdapter.notifyChange(mBiz.getMeetingLines());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * Dialog 中选择拜访线路的监听
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceLineDialog.dismiss();
                if (mCurrentLineIndex == position) {
                    return;
                }
                mCurrentLineIndex = position;
                List<CustomerMeetingLine> meetingLines = mBiz.getMeetingLines();
                if (meetingLines.size() <= 0) {//集合中没有数据网络请求数据
                    mBiz.GetPartyVisitLines();
                    showLoadingDialog();
                } else {//集合中已有数据，直接显示
                    strLine = mBiz.getMeetingLines().get(mCurrentLineIndex).getITEM_NAME();
                    tvMeetingType.setText(strLine);

                }

            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * Dialog 中选择上级客户的监听
     */
    private class InnerOnItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceFatherPartyDialog.dismiss();
                mCurrentFatherPartyIndex = position;
                mBiz.getPartygetAddressInfo(mCurrentFatherPartyIndex);
                showLoadingDialog();
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
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
                if (mCurrentChannelIndex == position) {
                    return;
                }
                mCurrentChannelIndex = position;
                strChannel=mBiz.getChannels().get(mCurrentChannelIndex).getITEM_NAME();
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
                    intent=new Intent(CustomerCreateActivity.this, ChooseProvinceActivity.class);
                    startActivityForResult(intent,RequestAddressBelong);
                    break;
                case R.id.button_updata:
                    if (StringUtils.isBlank(edPartyName.getText().toString())||StringUtils.isBlank(edPartyCode.getText().toString())||StringUtils.isBlank(strChannel)
                            ||StringUtils.isBlank(strLine)||fatherPartyAddress==null||StringUtils.isBlank(edContactPerson.getText().toString())
                            ||StringUtils.isBlank(edContactTel.getText().toString()) ||pv==null||StringUtils.isBlank(edAddressDetail.getText().toString())
                            ||lat<=0||lng<=0){
                            updataPartyError("请填写完整新建客户资料必填项");
                    }else {
                        showLoadingDialog();
                        mBiz.AddParty();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                pv=data.getParcelableExtra("province");
                ct=data.getParcelableExtra("city");
                ar=data.getParcelableExtra("area");
                ru=data.getParcelableExtra("rural");
                tvAddressBelong.setText(pv.getITEM_NAME().trim()+ct.getITEM_NAME().trim()
                        +ar.getITEM_NAME().trim()+ru.getITEM_NAME().trim());
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
}














