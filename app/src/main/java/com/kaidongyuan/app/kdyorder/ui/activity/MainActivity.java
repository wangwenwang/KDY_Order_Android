package com.kaidongyuan.app.kdyorder.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.constants.BroadcastConstants;
import com.kaidongyuan.app.kdyorder.constants.FileConstants;
import com.kaidongyuan.app.kdyorder.model.CheckAppVersionBiz;
import com.kaidongyuan.app.kdyorder.model.DownloadNewVersionAppBiz;
import com.kaidongyuan.app.kdyorder.ui.fragment.CheckOrderFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.HomeFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.MakeOrderFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.MineFragment;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.MPermissionsUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.MustUpdataAppVersionDialog;
import com.kaidongyuan.app.kdyorder.widget.UpdataAppVersionDialog;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 下载新版本 app 安装包是 Notification 标记
     */
    private static final int DOWNLOAD_NEWVRESIONAPP_PROGRESS_NOTIFICATION_TAG = 0;
    /**
     * 检查版本信息和下载更新业务类
     */
    private CheckAppVersionBiz mCheckVersionBiz;

    /**
     * 主页按钮容器
     */
    private PercentLinearLayout mPercentllHome;
    /**
     * 下单按钮容器
     */
    private PercentLinearLayout mPercentllMakeOrder;
    /**
     * 查单按钮容器
     */
    private PercentLinearLayout mPercentllCheckOrder;
    /**
     * 我的按钮容器
     */
    private PercentLinearLayout mPercentllMine;
    /**
     * 主页图片控件
     */
    private ImageView mImageviewHome;
    /**
     * 下单图片控件
     */
    private ImageView mImageviewMakeOrder;
    /**
     * 查单图片控件
     */
    private ImageView mImageviewCheckOrder;
    /**
     * 我的图片控件
     */
    private ImageView mImageviewMine;
    /**
     * 主页文本控件
     */
    private TextView mTextviewHome;
    /**
     * 下单文本控件
     */
    private TextView mTextviewMakeOrder;
    /**
     * 查单文本控件
     */
    private TextView mTextviewCheckOrder;
    /**
     * 我的文本控件
     */
    private TextView mTextviewMine;

    /**
     * Resources 项目资源
     */
    private Resources mResources;
    /**
     * Fragment 管理
     */
    private FragmentManager mFragmentManager;
    /**
     * 存放 Fragment数组
     */
    private Fragment[] mFragmentArr;
    /**
     * 记录上次点击的是哪个 Fragment
     */
    private int mPerviousFragment;
    /**
     * 目前显示的 Fragment
     */
    private int mCurrentFragment;
    /**
     * 广播接收
     */
    private BroadcastReceiver mReceiver;
    /**
     * MainFragment 的角标
     */
    private final int mHomeFragmentIndex = 0;
    /**
     * MakeOrder 的角标
     */
    private final int mMakeOrderFragmentIndex = 1;
    /**
     * CheckOrder 的角标
     */
    private final int mCheckOrderFragmentIndex = 2;
    /**
     * MineFragment 的角标
     */
    private final int mMineFragmentFragmentIndex = 3;
    /**
     * 记录用户第一次按下返回键的时间
     */
    private long mBackKeyFirstPressedTime = 0;
    /**
     * 两次按下返回键间隔小于此值返回主界面
     */
    private final long mShouldGoHomeActivityTime = 2000;
    /**
     * 提示用户升级的 Dialog
     */
    private Dialog mUpdataAppVersionDialog;
    /**
     * 取消更新 app
     */
    private Button mButtonCancelUpdata;
    /**
     * 更新 app
     */
    private Button mButtonUpdata;
    /**
     * app 当前版本
     */
    private TextView mTextViewCurrentVersion;
    /**
     * app 最新版本
     */
    private TextView mTextViewNewestVersion;
    /**
     * Hanlder
     */
    private InnerHandler mHandler;
    /**
     * 管理 Notification Manager
     */
    private NotificationManager mNotificationManager;
    /**
     * 显示下载进度的 Notification
     */
    private Notification mUpdataNotification;
    /**
     * 下载新版本 app 升级包线程
     */
    private Thread mDownLoadAppPackageThread;
    /**
     * 网络请求时的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 需强制升级时显示的 Dialog，防止用户进行操作
     */
    private MustUpdataAppVersionDialog mMustUpdataAppVersionDialog;
    private final int RequestPermission_STATUS_CODE0=8800;
    private Snackbar pmSnackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PushManager.getInstance().initialize(this.getApplicationContext());//初始化个推
            initData();
            setTop();
            initView();
            setListener();
            //checkVersion(true);
            initPermission();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    private void initPermission() {
        if (Build.VERSION.SDK_INT>=23){
            if (MPermissionsUtil.checkAndRequestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                       Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,RequestPermission_STATUS_CODE0)){

                checkVersion(true);
            }
        }else {
            checkVersion(true);
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {//如果用户按下的是返回键，判断是否要返回系统桌面
                long currentTime = System.currentTimeMillis();
                if ((currentTime - mBackKeyFirstPressedTime) < mShouldGoHomeActivityTime) {//返回系统桌面
                    mCurrentFragment = mHomeFragmentIndex;
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                } else {
                    ToastUtil.showToastBottom("再次点击返回键退出程序！", Toast.LENGTH_SHORT);
                    mBackKeyFirstPressedTime = currentTime;
                }
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return true;
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            initMenue();
            transFragment();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            this.unregisterReceiver(mReceiver);
            this.mNotificationManager.cancel(DOWNLOAD_NEWVRESIONAPP_PROGRESS_NOTIFICATION_TAG);
            mFragmentManager = null;
            for (int i = mFragmentArr.length - 1; i >= 0; i--) {
                mFragmentArr[i] = null;
            }
            mReceiver = null;
            if (mUpdataAppVersionDialog != null && mUpdataAppVersionDialog.isShowing()) {
                mUpdataAppVersionDialog.dismiss();
            }
            mUpdataAppVersionDialog = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            if (mDownLoadAppPackageThread != null) {
                mDownLoadAppPackageThread.interrupt();
                mDownLoadAppPackageThread = null;
            }
            if (mMustUpdataAppVersionDialog != null && mMustUpdataAppVersionDialog.isShowing()) {
                mMustUpdataAppVersionDialog.dismiss();
            }
            mMustUpdataAppVersionDialog = null;
            mCheckVersionBiz = null;
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            mResources = MyApplication.getmRes();
            mFragmentManager = this.getFragmentManager();
            mFragmentArr = new Fragment[4];
            mPerviousFragment = -1;
            mCurrentFragment = mHomeFragmentIndex;
            mHandler = new InnerHandler(this);

            mReceiver = new InnerBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.JUMPTO_MAKEORDER_FRAGMENT);
            filter.addAction(BroadcastConstants.JUMPTO_CHECKORDER_FRAGMENT);
            filter.addAction(BroadcastConstants.JUMPTO_HOME_FRAGMENT);
            filter.addAction("com.igexin.sdk.action.bZmU5pX5hT7ZijVqzsWmm4");//添加个推权限
            this.registerReceiver(mReceiver, filter);
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
            topView.setLayoutParams(topParams);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        try {
            mPercentllHome = (PercentLinearLayout) this.findViewById(R.id.percentll_home);
            mPercentllMakeOrder = (PercentLinearLayout) this.findViewById(R.id.percentll_makekOrder);
            mPercentllCheckOrder = (PercentLinearLayout) this.findViewById(R.id.percentll_checkOrder);
            mPercentllMine = (PercentLinearLayout) this.findViewById(R.id.percentll_mine);
            mImageviewHome = (ImageView) this.findViewById(R.id.imageview_home);
            mImageviewMakeOrder = (ImageView) this.findViewById(R.id.imageview_makeOrder);
            mImageviewCheckOrder = (ImageView) this.findViewById(R.id.imageview_checkOrder);
            mImageviewMine = (ImageView) this.findViewById(R.id.imageview_mine);
            mTextviewHome = (TextView) this.findViewById(R.id.textview_home);
            mTextviewMakeOrder = (TextView) this.findViewById(R.id.textview_makeOrder);
            mTextviewCheckOrder = (TextView) this.findViewById(R.id.textview_checkOrder);
            mTextviewMine = (TextView) this.findViewById(R.id.textview_mine);

            initMenue();
            transFragment();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        try {
            mPercentllHome.setOnClickListener(this);
            mPercentllMakeOrder.setOnClickListener(this);
            mPercentllCheckOrder.setOnClickListener(this);
            mPercentllMine.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.percentll_home:
                    setCurrentShowFragment(mHomeFragmentIndex);
                    break;
                case R.id.percentll_makekOrder:
                    setCurrentShowFragment(mMakeOrderFragmentIndex);
                    break;
                case R.id.percentll_checkOrder:
                    setCurrentShowFragment(mCheckOrderFragmentIndex);
                    break;
                case R.id.percentll_mine:
                    setCurrentShowFragment(mMineFragmentFragmentIndex);
                    break;
                case R.id.button_canceupdate_version://取消更新
                    mUpdataAppVersionDialog.dismiss();
                    break;
                case R.id.button_down_new_version://更新 app
                    updataAppVersion();
                    mUpdataAppVersionDialog.dismiss();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 跳转到指定的 Fragment
     *
     * @param fragmentIndex 要跳转到的 Fragment 角标
     */
    private void setCurrentShowFragment(int fragmentIndex) {
        try {
            mCurrentFragment = fragmentIndex;
            initMenue();
            transFragment();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 改变主界面 Menue 菜单的选中状态
     */
    private void initMenue() {
        try {
            if (mCurrentFragment == mPerviousFragment) {
                return;
            }
            String menueTextUnselectedColor = mResources.getString(R.string.menue_text_unselectedcolor);
            String menueTextSelectedColor = mResources.getString(R.string.menue_text_selectedcolor);
            mImageviewHome.setImageResource(R.drawable.menu_index_unselected);
            mImageviewCheckOrder.setImageResource(R.drawable.menu_order_unselected);
            mImageviewMakeOrder.setImageResource(R.drawable.menu_makeorder_unselected);
            mImageviewMine.setImageResource(R.drawable.menu_me_unselected);
            mTextviewHome.setTextColor(Color.parseColor(menueTextUnselectedColor));
            mTextviewMakeOrder.setTextColor(Color.parseColor(menueTextUnselectedColor));
            mTextviewCheckOrder.setTextColor(Color.parseColor(menueTextUnselectedColor));
            mTextviewMine.setTextColor(Color.parseColor(menueTextUnselectedColor));
            switch (mCurrentFragment) {
                case mHomeFragmentIndex:
                    mImageviewHome.setImageResource(R.drawable.menu_index_selected);
                    mTextviewHome.setTextColor(Color.parseColor(menueTextSelectedColor));
                    break;
                case mMakeOrderFragmentIndex:
                    mImageviewMakeOrder.setImageResource(R.drawable.menu_makeorder_selected);
                    mTextviewMakeOrder.setTextColor(Color.parseColor(menueTextSelectedColor));
                    break;
                case mCheckOrderFragmentIndex:
                    mImageviewCheckOrder.setImageResource(R.drawable.menu_order_selected);
                    mTextviewCheckOrder.setTextColor(Color.parseColor(menueTextSelectedColor));
                    break;
                case mMineFragmentFragmentIndex:
                    mImageviewMine.setImageResource(R.drawable.menu_me_selected);
                    mTextviewMine.setTextColor(Color.parseColor(menueTextSelectedColor));
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 切换界面中的 Fragment
     */
    private void transFragment() {
        try {
            if (mCurrentFragment == mPerviousFragment) {
                return;
            }
            FragmentTransaction trans = mFragmentManager.beginTransaction();
            Fragment currentFragment = mFragmentArr[mCurrentFragment];
            if (currentFragment == null) {
                switch (mCurrentFragment) {
                    case mHomeFragmentIndex:
                        currentFragment = new HomeFragment();
                        mFragmentArr[mHomeFragmentIndex] = currentFragment;
                        break;
                    case mMakeOrderFragmentIndex:
                        currentFragment = new MakeOrderFragment();
                        mFragmentArr[mMakeOrderFragmentIndex] = currentFragment;
                        break;
                    case mCheckOrderFragmentIndex:
                        currentFragment = new CheckOrderFragment();
                        mFragmentArr[mCheckOrderFragmentIndex] = currentFragment;
                        break;
                    case mMineFragmentFragmentIndex:
                        currentFragment = new MineFragment();
                        mFragmentArr[mMineFragmentFragmentIndex] = currentFragment;
                        break;
                }
                trans.add(R.id.framelayout_fragment, currentFragment);
            }
            if (mPerviousFragment != -1) {
                Fragment hideFragment = mFragmentArr[mPerviousFragment];
                trans.hide(hideFragment);
            }
            trans.show(currentFragment).commit();
            mPerviousFragment = mCurrentFragment;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==RequestPermission_STATUS_CODE0){
            for (int i=0;i<permissions.length;i++){
                if (grantResults[i]== PackageManager.PERMISSION_DENIED) {
                    switch (permissions[i]){
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            ToastUtil.showToastBottom("请允许应用使用SD卡存储", Toast.LENGTH_SHORT);
                           // showToastMsg("请允许应用使用SD卡存储",3000);
                            showSnackbar("请允许应用使用SD卡存储~", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent3);
                                    MyApplication.isLogin = false;
                                    finish();
                                }
                            },Snackbar.LENGTH_INDEFINITE);
                            break;

                        case Manifest.permission.ACCESS_COARSE_LOCATION:
                            ToastUtil.showToastBottom("请授权应用网络定位和GPS定位权限,01", Toast.LENGTH_SHORT);
                            break;

                        case Manifest.permission.ACCESS_FINE_LOCATION:
                            ToastUtil.showToastBottom("请授权应用网络定位和GPS定位权限,02", Toast.LENGTH_SHORT);
                            break;

                        default:
                            showSnackbar("请授权应用网络定位和GPS定位权限,00", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent1=new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent1);
                                }
                            },Snackbar.LENGTH_INDEFINITE);
                            break;
                    }
                    return;
                }
            }

        }


    }

    private void showSnackbar(String strSnackbar, View.OnClickListener listener,int duration) {
        pmSnackbar = Snackbar.make(findViewById(R.id.acitvity_mainAcitivity),strSnackbar,duration);
        View v= pmSnackbar.getView();
        v.setBackgroundColor(getResources().getColor(R.color.details_text));
//        LinearLayout.LayoutParams vp= (LinearLayout.LayoutParams) v.getLayoutParams();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            vp.setMarginEnd(10);
//            vp.setMarginStart(10);
//        }
        final TextView tv_snackbar= (TextView) v.findViewById(R.id.snackbar_text);
        // tv_snackbar.setPadding(50,50,50,50);
        tv_snackbar.setGravity(Gravity.CENTER);
        tv_snackbar.setTextColor(getResources().getColor(R.color.white));
        // v.setAlpha(0.8f);
        // pmSnackbar.setActionTextColor(getResources().getColor(R.color.white));
        pmSnackbar.setAction("确认",listener).show();
    }

    /**
     * 内部处理广播接收类
     */
    private class InnerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (BroadcastConstants.JUMPTO_MAKEORDER_FRAGMENT.equals(action)) {//跳转到下单界面
                    setCurrentShowFragment(mMakeOrderFragmentIndex);
                } else if (BroadcastConstants.JUMPTO_CHECKORDER_FRAGMENT.equals(action)) {//跳转到查单界面
                    setCurrentShowFragment(mCheckOrderFragmentIndex);
                } else if (BroadcastConstants.JUMPTO_HOME_FRAGMENT.equals(action)) {//跳转到 HomeFragment
                    mCurrentFragment = mHomeFragmentIndex;//等 MainActivity 下次显示的时候再进行跳转
                }

                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    switch (bundle.getInt(PushConsts.CMD_ACTION)) {
                        case PushConsts.GET_CLIENTID:
                            // 获取ClientID(CID)
                            // 第三方应用通常需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送。
                            // 部分特殊情况下CID可能会发生变化，为确保应用服务端保存的最新的CID，应用程序在每次获取CID广播后，如果发现CID出现变化，需要重新进行一次关联绑定
                            String cid = bundle.getString("clientid");
                         //  Toast.makeText(MainActivity.this,cid,Toast.LENGTH_LONG).show();
                         //   mTextviewMakeOrder.setText(cid);
                            Logger.w("MainActivity.InnerBroadcastReceiver.Got CID:" + cid);
                            break;
//                        case PushConsts.GET_MSG_DATA:
//                            // 获取透传（payload）数据
//                            byte[] payload = bundle.getByteArray("payload");
//                            if (payload != null) {
//                                String data = new String(payload);
//                                Logger.w("PushTestBroadcastReceiver.data:" + data);
//                            }
//                            break;
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }


    //********************************************************* 以下为版本更新 *********************************************************

    /**
     * 检查版本
     *
     * @param isInMainActivity 是否是在 mainActivity 中调用了此方法
     */
    public void checkVersion(final boolean isInMainActivity) {
        try {
            if (mDownLoadAppPackageThread != null && mDownLoadAppPackageThread.isAlive()) {//正在下载新版本app时
                ToastUtil.showToastBottom("新版本 app 正在下载中！", Toast.LENGTH_SHORT);
                return;
            }
            mCheckVersionBiz = new CheckAppVersionBiz();
            if (!isInMainActivity) {
                showLoadingDialog();
            }
            mCheckVersionBiz.checkVersion(new CheckAppVersionBiz.CheckVersionInterface() {
                @Override
                public void currentVersionIsNewestVersion() {
                    dismisssLoadingDialog();
                    if (!isInMainActivity) {
                        ToastUtil.showToastBottom("当前版本是最新版本！", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void canUpdataVersion(String newestVersion) {
                    dismisssLoadingDialog();
                    showUpdataAppVersionDialog(true, newestVersion);
                }

                @Override
                public void mustUpdataVersion(String newestVersion) {
                    dismisssLoadingDialog();
                    showUpdataAppVersionDialog(false, newestVersion);
                }

                @Override
                public void checkVersionError(String message) {
                    dismisssLoadingDialog();
                    if (!isInMainActivity) {
                        ToastUtil.showToastBottom("获取最新版本信息失败！", Toast.LENGTH_SHORT);
                    }
                }
            });
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
     * 隐藏网络请求时的 Dialog
     */
    private void dismisssLoadingDialog() {
        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 检查版本后显示的 Dialog
     *
     * @param shouldShowCancelButton 是否需要显示取消升级的按钮
     * @param newestVersion          网络 app 最新版本
     */
    private void showUpdataAppVersionDialog(boolean shouldShowCancelButton, String newestVersion) {
        try {
            String currentVersion = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (mUpdataAppVersionDialog == null) {
                mUpdataAppVersionDialog = new UpdataAppVersionDialog(this);
                mUpdataAppVersionDialog.setCanceledOnTouchOutside(false);
                mUpdataAppVersionDialog.show();
                Window window = mUpdataAppVersionDialog.getWindow();
                window.setContentView(R.layout.dialog_download_newapp_version);
                mButtonCancelUpdata = (Button) window.findViewById(R.id.button_canceupdate_version);
                mButtonUpdata = (Button) window.findViewById(R.id.button_down_new_version);
                mButtonCancelUpdata.setOnClickListener(this);
                mButtonUpdata.setOnClickListener(this);
                if (!shouldShowCancelButton) {
                    mButtonCancelUpdata.setVisibility(View.GONE);
                }
                mTextViewCurrentVersion = (TextView) window.findViewById(R.id.textview_currentversion);
                mTextViewNewestVersion = (TextView) window.findViewById(R.id.textview_newestversion);
            } else {
                mUpdataAppVersionDialog.show();
            }
            mTextViewCurrentVersion.setText(String.valueOf(currentVersion));
            mTextViewNewestVersion.setText(String.valueOf(newestVersion));
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 下载新版本的 app
     */
    private void updataAppVersion() {
        try {
            showNotifaction(1);//立即显示下载了百分之 1 ，让用户感觉已在下载
            showDownAppPackageDialog();
            mDownLoadAppPackageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //另起线程执行下载，安卓最新sdk规范，网络操作不能再主线程。
                    final DownloadNewVersionAppBiz l = new DownloadNewVersionAppBiz(mCheckVersionBiz.getAppDownAddress());
                    int status = l.down2sd(FileConstants.DOWN_DIR_NAME, FileConstants.DOWN_NEWVERSION_APP_FILE_NAME, new DownloadNewVersionAppBiz.downhandler() {
                        public void setSize(int size) {
                            try {
                                Message msg = mHandler.obtainMessage();
                                msg.arg1 = size;
                                msg.sendToTarget();
                            } catch (Exception e) {
                                ExceptionUtil.handlerException(e);
                            }
                        }
                    });
                    if (status != 1) {//下载新版本安装包失败
                        ToastUtil.showToastBottom("连接网络下载安装包失败！", Toast.LENGTH_SHORT);
                    }
                }
            });
            mDownLoadAppPackageThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            mDownLoadAppPackageThread = null;
        }
    }

    /**
     * 创建下载进度的 notification
     *
     * @param percent 下载进度
     */
    private void showNotifaction(int percent) {
        try {
            //自定义 Notification 布局
            if (mUpdataNotification == null) {
                mUpdataNotification = new Notification();
                mUpdataNotification.icon = R.drawable.icon;
            }
            RemoteViews remoteView = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.notifaction_download_app);
            remoteView.setTextViewText(R.id.textView_dialog_download, percent + "%");
            remoteView.setProgressBar(R.id.progressBar_dialog_download, 100, percent, false);
            mUpdataNotification.contentView = remoteView;
            mNotificationManager.notify(DOWNLOAD_NEWVRESIONAPP_PROGRESS_NOTIFICATION_TAG, mUpdataNotification);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 在 Activity 中显示下载进度的 Dialog
     */
    private void showDownAppPackageDialog() {
        if (mMustUpdataAppVersionDialog == null) {
            mMustUpdataAppVersionDialog = new MustUpdataAppVersionDialog(this);
        }
        mMustUpdataAppVersionDialog.show();
        mMustUpdataAppVersionDialog.showDialog();
    }

    /**
     * 内部 Handler ，处理下载进度,使用软引用防止内存泄漏
     */
    private class InnerHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        public InnerHandler(MainActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                super.handleMessage(msg);
                MainActivity activity = weakReference.get();
                if (activity != null) {
                    int percent = msg.arg1;
                    if (percent == 100) {//下载成功
                        showNotifaction(percent);
                        mMustUpdataAppVersionDialog.setDownloadProgress(percent);
                        installNewVersionApp();
                        activity.mNotificationManager.cancel(DOWNLOAD_NEWVRESIONAPP_PROGRESS_NOTIFICATION_TAG);
                        mMustUpdataAppVersionDialog.dismiss();
                    } else if (percent == -1) {//下载新版本安装包出现异常
                        Toast.makeText(MainActivity.this, "下载更新文件失败", Toast.LENGTH_LONG).show();
                        activity.mNotificationManager.cancel(DOWNLOAD_NEWVRESIONAPP_PROGRESS_NOTIFICATION_TAG);
                        mMustUpdataAppVersionDialog.dismiss();
                    } else {//更新下载进度
                        showNotifaction(percent);
                        mMustUpdataAppVersionDialog.setDownloadProgress(percent);
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 启动安装 app 界面
     */
    private void installNewVersionApp() {
        try {
            String fileName = Environment.getExternalStorageDirectory() + FileConstants.DOWN_NEWVERSION_APP_FILE_PATH;
            File file = new File(fileName);
            if (!file.exists()) {
                ToastUtil.showToastBottom("升级包不存在", Toast.LENGTH_SHORT);
            } else {
                Uri uri = Uri.fromFile(file);
                String type = "application/vnd.android.package-archive";//.apk 的 mime 名
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, type);
                startActivity(intent);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}













