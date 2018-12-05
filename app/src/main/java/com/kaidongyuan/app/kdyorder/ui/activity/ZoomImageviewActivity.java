package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/5/10.
 * 图片缩放的 Activity，开启时将图片文件的绝对路径传递
 */
public class ZoomImageviewActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 显示图片的 ImageView
     */
    private ImageView mImageViewZoom;
    /**
     * 返回按钮
     */
    private Button mButtonBack;
    /**
     * 图片缩放三方开源工具
     */
    private PhotoViewAttacher mPhotoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        try {
            setTop();
            initView();
            setData();
            setListener();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mImageViewZoom = null;
            mButtonBack = null;
            mPhotoViewAttacher = null;
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
            topParams.height = DensityUtil.getStatusHeight();
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            mImageViewZoom = (ImageView) this.findViewById(R.id.imageview_zoom);
            mPhotoViewAttacher = new PhotoViewAttacher(mImageViewZoom);
            mButtonBack = (Button) this.findViewById(R.id.button_back);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setData() {
        try {
            Intent intent = this.getIntent();
            if (intent.hasExtra(EXTRAConstants.EXTRA_IMAGE_URL)) {
                String url = intent.getStringExtra(EXTRAConstants.EXTRA_IMAGE_URL);
                Picasso.with(this).load(url).error(R.drawable.ic_imageview_default_bg)
                        .transform(new CropSquareTransformation()).into(mImageViewZoom);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mButtonBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_back:
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 图片处理的类，Picasso 使用,图片放大大适应屏幕大小
     */
    private class CropSquareTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {

            int bitmapX = source.getWidth();
            int bitmapY = source.getHeight();
            float sizeX = ((float)(DensityUtil.getWidth())) / bitmapX;
            float sizeY = ((float)(DensityUtil.getHeight())) / bitmapY;
            float scaleSize = Math.min(sizeX, sizeY);

            Matrix matrix = new Matrix();
            matrix.setScale(scaleSize, scaleSize);
            Bitmap squaredBitmap = Bitmap.createBitmap(source, 0, 0, bitmapX, bitmapY, matrix, true);
            if (squaredBitmap != source) {
                source.recycle();
            }

            int newBitmapWidth = squaredBitmap.getWidth();
            int newBitmapHeight = squaredBitmap.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(newBitmapWidth, newBitmapHeight, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            canvas.drawRect(0, 0, newBitmapWidth, newBitmapHeight, paint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "square";
        }
    }

}
































