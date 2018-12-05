package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.CheckOrderPictureActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/6/8.
 * 查看已完成订单的电子签名和图片
 */
public class CheckOrderPictureActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private CheckOrderPictureActivityBiz mBiz;

    /**
     * 客户签名图片空间
     */
    private ImageView mImageViewCustomerAutograph;
    /**
     * 交货现场图片1
     */
    private ImageView mImageViewPicture1;
    /**
     * 交货现场图片2
     */
    private ImageView mImageViewPicture2;
    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;

    /**
     * 获取签名和交货现场图片订单的 idx
     */
    private String mOrderIdx;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorderpicture);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getAutographAndPictureData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (mLoadingDialog!=null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mBiz.cancelRequest();
            mBiz = null;
            mImageViewCustomerAutograph = null;
            mImageViewPicture1 = null;
            mImageViewPicture2 = null;
            mImageViewGoBack = null;
            mLoadingDialog = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new CheckOrderPictureActivityBiz(this);
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.EXTRA_ORDER_IDX)) {
                mOrderIdx = intent.getStringExtra(EXTRAConstants.EXTRA_ORDER_IDX);
            } else {
                Toast.makeText(this, "没有订单号，请重新进入该界面！", Toast.LENGTH_SHORT).show();
                finish();
            }
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

    private void initView() {
        try {
            mImageViewCustomerAutograph = (ImageView) this.findViewById(R.id.imageview_customer_autograph);
            mImageViewPicture1 = (ImageView) this.findViewById(R.id.imageview_picture1);
            mImageViewPicture2 = (ImageView) this.findViewById(R.id.imageView_picture2);
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewCustomerAutograph.setOnClickListener(this);
            mImageViewPicture1.setOnClickListener(this);
            mImageViewPicture2.setOnClickListener(this);
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单签名和交货现场图片
     */
    private void getAutographAndPictureData() {
        try {
            if (mBiz.getAutographAndPictureData(mOrderIdx)) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);

        }
    }

    /**
     * 网络获取电子签名和现场图片失败时回调方法
     *
     * @param message 显示的信息
     */
    public void getOrderPathDataError(String message) {
        try {
            cancelLoadingDialog();
            Toast.makeText(this, String.valueOf(message), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求成功时回调方法
     */
    public void getAutographAndPictureDataSuccess() {
        try {
            cancelLoadingDialog();
            setImageViewSrc();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示网络请求是的 Dialog
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
     * 取消网络请求时显示的 Dialog
     */
    private void cancelLoadingDialog() {
        try {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置图片显示控件的图片
     */
    private void setImageViewSrc() {
        try {
            Picasso.with(this).load(mBiz.getPictureUrl(0)).fit().error(R.drawable.ic_imageview_default_bg).into(mImageViewCustomerAutograph);
            Picasso.with(this).load(mBiz.getPictureUrl(1)).fit().error(R.drawable.ic_imageview_default_bg).into(mImageViewPicture1);
            Picasso.with(this).load(mBiz.getPictureUrl(2)).fit().error(R.drawable.ic_imageview_default_bg).into(mImageViewPicture2);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback://返回上一界面
                    finish();
                    break;
                case R.id.imageview_customer_autograph://查看签名图片
                    jumpToZoomActivity(mBiz.getPictureUrl(0));
                    break;
                case R.id.imageview_picture1://查看交货现场图片1
                    jumpToZoomActivity(mBiz.getPictureUrl(1));
                    break;
                case R.id.imageView_picture2://查看交货现场图片2
                    jumpToZoomActivity(mBiz.getPictureUrl(2));
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 跳转到查看图片界面
     *
     * @param pictureUrl 图片 URL 路径
     */
    private void jumpToZoomActivity(String pictureUrl) {
        Intent zoomImageViewIntent = new Intent(this, ZoomImageviewActivity.class);
        if (!TextUtils.isEmpty(pictureUrl)) {
            zoomImageViewIntent.putExtra(EXTRAConstants.EXTRA_IMAGE_URL, pictureUrl);
            startActivity(zoomImageViewIntent);
        }
    }


}






















