package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.kaidongyuan.app.kdyorder.adapter.GridImageAdapter;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.ArrivedStoreActivityBiz;
import com.kaidongyuan.app.kdyorder.util.BitmapUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private final String Tag_Visit = "Tag_Visit";//客户拜访TAG
    private Button submitbutton;
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
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    // 位置
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private TextView currAddress;
    private String strAddress;
    private CustomerMeeting customerM;

    // 照片
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;

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

            currAddress.setText(strAddress);
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
        submitbutton = (Button) findViewById(R.id.button_submit);
        submitbutton.setOnClickListener(this);
        currAddress = (TextView) findViewById(R.id.tv_currAddress);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ArrivedStoreActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(ArrivedStoreActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(2);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            int themeId = R.style.picture_default_style;
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(ArrivedStoreActivity.this).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 单独拍照
            PictureSelector.create(ArrivedStoreActivity.this)
                    .openCamera(PictureMimeType.ofImage())
                    .selectionMedia(selectList)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }

    };

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
            case R.id.button_submit:
                nextOnclick();
                break;
        }
    }

    private void nextOnclick() {

        if (selectList.size() <= 0) {
            ToastUtil.showToastBottom("请将门店照片上传!", Toast.LENGTH_SHORT);
            return;
        }

        LocalMedia LM = selectList.get(0);
        Bitmap pictureBitmap1 = BitmapUtil.resizeImage(LM.getPath(), mBitmapWidth);
        Log.d("LM", "进店图片1大小|" + BitmapUtil.getBitmapSize(pictureBitmap1));



        Bitmap pictureBitmap2 = null;
        if (selectList.size() > 1) {
            LocalMedia LM2 = selectList.get(1);
            pictureBitmap2 = BitmapUtil.resizeImage(LM2.getPath(), mBitmapWidth);
            Log.d("LM", "进店图片2大小|" + BitmapUtil.getBitmapSize(pictureBitmap2));
        }
        if (pictureBitmap1 != null) {
            try {
                String strpicture = BitmapUtil.changeBitmapToString(pictureBitmap1);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
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
