package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.ArrivedStoreActivityBiz;
import com.kaidongyuan.app.kdyorder.util.BitmapUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.io.File;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2016/10/19.
 */
public class ArrivedStoreActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应业务类
     */
    private ArrivedStoreActivityBiz mBiz;

    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    private String strVisitIdx;
    private final String Tag_Visit = "Tag_Visit";//客户拜访TAG
    private ImageView imageView1, imageView2;
    private Button button1, button2, cancelbutton, submitbutton;
    /**
     * 保存图片和签名的文件夹
     */
    private final String mCacheFileName = "kaidongyuan";
    /**
     * 照片1保存的文件名
     */
    private final String mPictureFileName = "stopImage.jpg";
    /**
     * 　照片1调用系统相机是的请求码
     */
    private final int SystemCapture = 10;
    /**
     * 　照片保存的绝对路径
     */
    private String mPictureFilePath;
    /**
     * 照片2保存的文件名
     */
    private final String mPictureFileName2 = "stopImage2.jpg";
    /**
     * 照片2调用系统相机是的请求码2
     */
    private final int SystemCapture2 = 11;
    /**
     * 　照片2保存的绝对路径
     */
    private String mPictureFilePath2;
    /**
     * 储存添加照片是的临时文件名
     */
    private String mTempPictureFileName;
    /**
     * 储存添加照片时的临时请求码
     */
    private int mTempRequestCode;
    /**
     * 储存添加照片时的临时文件路径
     */
    private String mTempPictureFilePath;
    /**
     * 　签名和照片文件宽度 单位（px）
     */
    private final int mBitmapWidth = 400;
    /**
     * 签名和照片的质量
     */
    private final int mPictureQuity = 60;
    private Snackbar choicsSnackbar;
    private boolean onlyphotograph = true;
    /*** 20180122 交付时提交当前定位坐标**/
    private RequestQueue mRequestQueue;
    private final static String Tag_Upload_Position = "Tag_Upload_Position";
    private double currentLocationLat, currntLocationLng;
    private String currentLocationAds;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private TextView currAddress;
    private String strAddress;
    private CustomerMeeting customerM;

    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // 经度
            double longitude = location.getLongitude();
            // 纬度
            double latitude = location.getLatitude();
            // 地址
            strAddress = location.getAddrStr();
            // 精度
            float radius = location.getRadius();
            // 定位码
            int errorCode = location.getLocType();

            currAddress.setText("当前位置：" + strAddress);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrived_store);
        initData();
        initview();
        setListener();

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initLocationClient();
        //注册监听函数
        mLocationClient.start();
        /*取出Intent中附加的数据*/
        customerM = (CustomerMeeting) getIntent().getParcelableExtra("CustomerMeeting");
    }

    /**
     * 初始化定位客户端
     */
    public void initLocationClient() {
        LocationClientOption option = new LocationClientOption();
        option.setProdName(this.getPackageName());
        option.setCoorType("bd09ll");//百度地图的编码模式 // 返回的定位结果是百度经纬度，默认值gcj02
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIgnoreKillProcess(false);
        option.setTimeOut(10 * 1000); // 网络定位的超时时间
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initview() {

        mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        Intent intent = getIntent();

        imageView1 = (ImageView) findViewById(R.id.imageView_picture);
        imageView1.setOnClickListener(this);
        imageView2 = (ImageView) findViewById(R.id.imageView_picture2);
        imageView2.setOnClickListener(this);
        button1 = (Button) findViewById(R.id.button_addPicture);
        button1.setOnClickListener(this);
        button2 = (Button) findViewById(R.id.button_addPicture2);
        button2.setOnClickListener(this);
        cancelbutton = (Button) findViewById(R.id.button_cancel);
        cancelbutton.setOnClickListener(this);
        submitbutton = (Button) findViewById(R.id.button_submit);
        submitbutton.setOnClickListener(this);
        currAddress = (TextView) findViewById(R.id.tv_currAddress);
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
            e.printStackTrace();
        }

        super.onDestroy();
    }

    private void initData() {
        mBiz = new ArrivedStoreActivityBiz(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_goback:
                this.finish();
                break;
            case R.id.imageView_picture:
                if (choicsSnackbar != null && choicsSnackbar.isShown()) {
                    choicsSnackbar.dismiss();
                    return;
                }
                if (mPictureFilePath == null || mPictureFilePath.length() <= 0) {
                    addPicture(mPictureFileName, SystemCapture);
                } else {
                    Intent intent = new Intent(this, ZoomImageviewActivity.class);
                    intent.putExtra(EXTRAConstants.EXTRA_IMAGE_PATH, mPictureFilePath);
                    startActivity(intent);
                }
                break;
            case R.id.imageView_picture2:
                if (choicsSnackbar != null && choicsSnackbar.isShown()) {
                    choicsSnackbar.dismiss();
                    return;
                }
                if (mPictureFilePath2 == null || mPictureFilePath2.length() <= 0) {
                    addPicture(mPictureFileName2, SystemCapture2);
                } else {
                    Intent intent = new Intent(this, ZoomImageviewActivity.class);
                    intent.putExtra(EXTRAConstants.EXTRA_IMAGE_PATH, mPictureFilePath2);
                    startActivity(intent);
                }
                break;
            case R.id.button_addPicture:
                if (choicsSnackbar != null && choicsSnackbar.isShown()) {
                    choicsSnackbar.dismiss();
                    return;
                }
                addPicture(mPictureFileName, SystemCapture);
                break;
            case R.id.button_addPicture2:
                if (choicsSnackbar != null && choicsSnackbar.isShown()) {
                    choicsSnackbar.dismiss();
                    return;
                }
                addPicture(mPictureFileName2, SystemCapture2);
                break;
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_submit:
                nextOnclick();
                break;
        }
    }


    private void nextOnclick() {
        if ((mPictureFilePath != null && mPictureFilePath.length() > 0)
                ) {
            Bitmap pictureBitmap = BitmapUtil.resizeImage(mPictureFilePath, mBitmapWidth);
            Bitmap pictureBitmap2 = null;
            if (mPictureFilePath2 != null && mPictureFilePath2.length() > 0) {
                pictureBitmap2 = BitmapUtil.resizeImage(mPictureFilePath2, mBitmapWidth);
            }
            if (pictureBitmap != null) {
                try {
                    String strpicture = BitmapUtil.changeBitmapToString(pictureBitmap);
                    String strpicture2 = (pictureBitmap2 != null) ? BitmapUtil.changeBitmapToString(pictureBitmap2) : "";
                    showLoadingDialog();
                    strAddress = (strAddress == null) ? "" : strAddress;
                    mBiz.GetVisitEnterShop(customerM.getVISIT_IDX(), strpicture, strpicture2, strAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showToastBottom("照片处理异常", Toast.LENGTH_SHORT);
            }
        } else {
            ToastUtil.showToastBottom("请将门店照片上传!", Toast.LENGTH_SHORT);
        }
    }


    private void addPicture(String mPictureFileName, int systemCapture) {
        mTempPictureFileName = mPictureFileName;
        mTempRequestCode = systemCapture;
        showUpdataChoice();
    }

    private void showUpdataChoice() {
        // tv_snackbar.setPadding(50,50,50,50);
        if (onlyphotograph) {
            choicsSnackbar = Snackbar.make(findViewById(R.id.activity_arrived_store), "", Snackbar.LENGTH_INDEFINITE);
            View v = choicsSnackbar.getView();
            v.setBackgroundColor(getResources().getColor(R.color.details_text));
        } else {
            choicsSnackbar = Snackbar.make(findViewById(R.id.activity_arrived_store), "相册选取", Snackbar.LENGTH_INDEFINITE);
            View v = choicsSnackbar.getView();
            v.setBackgroundColor(getResources().getColor(R.color.details_text));
            final TextView tv_snackbar = (TextView) v.findViewById(R.id.snackbar_text);
            tv_snackbar.setGravity(Gravity.CENTER);
            tv_snackbar.setTextColor(getResources().getColor(R.color.white));
            tv_snackbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //从相册获取照片上传
                    if (choicsSnackbar != null) {
                        choicsSnackbar.dismiss();
                    }
                    mTempRequestCode *= 2;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, mTempRequestCode);
                }
            });
        }

        choicsSnackbar.setAction("拍照上传", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dirFile = BitmapUtil.getCacheDirFile(mCacheFileName);
                if (dirFile == null) {
                    ToastUtil.showToastBottom("请先授权读写sd卡权限~", Toast.LENGTH_SHORT);
                    return;
                }
                File pictureFile = new File(dirFile, mTempPictureFileName);
                mTempPictureFilePath = pictureFile.getAbsolutePath();
                Uri pictureUri = Uri.fromFile(pictureFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(intent, mTempRequestCode);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SystemCapture:
                    Bitmap bitmap0 = BitmapUtil.resizeImage(mTempPictureFilePath, mBitmapWidth);
                    getPictureResultHandle(imageView1, button1, bitmap0, mPictureFileName);
                    break;
                case SystemCapture * 2:
                    Uri uri = data.getData();
                    Bitmap bitmap1 = BitmapUtil.getBitmap(getApplicationContext(), uri, mBitmapWidth);
                    getPictureResultHandle(imageView1, button1, bitmap1, mPictureFileName);
                    break;
                case SystemCapture2:
                    Bitmap bitmap2 = BitmapUtil.resizeImage(mTempPictureFilePath, mBitmapWidth);
                    getPictureResultHandle(imageView2, button2, bitmap2, mPictureFileName2);
                    break;
                case SystemCapture2 * 2:
                    Uri uri1 = data.getData();
                    Bitmap bitmap3 = BitmapUtil.getBitmap(getApplicationContext(), uri1, mBitmapWidth);
                    getPictureResultHandle(imageView2, button2, bitmap3, mPictureFileName2);
                    break;
                default:
                    ToastUtil.showToastBottom("图片传输失败，请重新取图", Toast.LENGTH_LONG);
                    break;
            }

        }

    }


    @Override
    public void onBackPressed() {
        if (choicsSnackbar != null && choicsSnackbar.isShown()) {
            choicsSnackbar.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    private void getPictureResultHandle(ImageView imageView1, Button button1, Bitmap bitmap, String mPictureFileName) {
        if (bitmap != null) {
            imageView1.setImageBitmap(bitmap);
            button1.setText("重新上传");
            String picturePath = BitmapUtil.writeBimapToFile(bitmap, mPictureFileName, mCacheFileName, mPictureQuity);
            if (mTempRequestCode == SystemCapture || mTempRequestCode == SystemCapture * 2) {
                mPictureFilePath = picturePath;
            } else if (mTempRequestCode == SystemCapture2 || mTempRequestCode == SystemCapture2 * 2) {
                mPictureFilePath2 = picturePath;
            }
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 进店成功
     *
     * @param message 要显示的消息
     */
    public void EnterShopSuccess(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
            Intent intent = new Intent(this, CustomerMeetingCheckInventoryActivity.class);
            intent.putExtra("CustomerMeeting", customerM);
            startActivity(intent);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 进店失败
     *
     * @param message 要显示的消息
     */
    public void EnterShopError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
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
}
