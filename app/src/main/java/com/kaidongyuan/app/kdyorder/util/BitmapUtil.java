package com.kaidongyuan.app.kdyorder.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;

import com.github.mikephil.charting.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/3.
 * 图片处理工具类
 */
public class BitmapUtil {

    /**
     * @param bitmapPath 要压缩的图片路径
     * @param width 压缩的宽度
     * @return 压缩后的图片
     */
    public static Bitmap resizeImage(String bitmapPath, int width){
        try {
            //防止内存溢出
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(bitmapPath, options);
            int pictureWidth = options.outWidth;
            options.inSampleSize = pictureWidth / width;
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, options);
            return resizeImage(bitmap, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 Bitmap 压缩到指定的宽度
     * @param bitmap 要压缩的 Bitmap
     * @param width 要压缩到的宽度
     * @return 压缩后的 Bitmap
     */
    public static Bitmap resizeImage(Bitmap bitmap, int width) {
        if (bitmap==null) {
            return null;
        }
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        float scaleWidth = ((float) width) / originWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        return Bitmap.createBitmap(bitmap, 0, 0, originWidth, originHeight, matrix, true);
    }

    /**
     * 强图片转换成字符串
     * @param bitmap 图片
     * @return 图片对应的 Base64字符串
     */
    public static String changeBitmapToString(Bitmap bitmap) {
        if (bitmap==null){
            return "";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] pictureBytes = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(pictureBytes, 0);
    }

    /**
     * 将字符串转换成 Bitmap
     * @param body Bitmap 对应的字符串
     * @return Bitmap 对象
     */
    public static Bitmap changeStringToBitmap(String body) {
        byte[] btye = Base64.decode(body, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(btye, 0, btye.length);
        return bitmap;
    }

    /**
     * 将 Bitmap 旋转一定的角度
     * @param bitmap 要旋转的对象
     * @param angle 旋转角度
     * @return 处理完的 Bitmap
     */
    public static Bitmap rotateBitmapAngle (Bitmap bitmap, float angle) {
        if (bitmap==null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    /**
     * * 将 Bitmap 写入已文件形式写入到指定的文件
     * @param bitmap  Bitmap
     * @param fileName 文件名，文件夹为 app 缓存文件夹
     * @param cacheFileName 文件夹名
     * @param pictureQuity 保存图片质量
     * @return 保存文件的绝对路径
     */
    public static String writeBimapToFile(Bitmap bitmap, String fileName, String cacheFileName, int pictureQuity) {
        FileOutputStream outputStream = null;
        String str = null;
        try {
            File dirFile = getCacheDirFile(cacheFileName);
            if (!dirFile.exists()) {
                return null;
            }
            File autographFile = new File(dirFile, fileName);
            outputStream = new FileOutputStream(autographFile);
            boolean bol = bitmap.compress(Bitmap.CompressFormat.JPEG, pictureQuity, outputStream);
            if (bol) {
                str = autographFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * 根据图片的 Uri 路径生成尺寸为 横向最大为 1200 px 图片
     * @param context 上下文，用 Aplication 的上下文
     * @param uri 图片路径
     * @param widthPx 图片横向最大宽度 Px
     * @return Bitmap 对象
     */
    public static Bitmap getBitmap (Context context, Uri uri, int widthPx) {
        InputStream in = null;
        InputStream i = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            Rect outPadding = new Rect(0, 0, 0, 0);
            in = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(in, outPadding, opts);

            int width = opts.outWidth;
            int size = 1;

            if (width > widthPx) {
                size = width / widthPx;
            }

            opts.inJustDecodeBounds = false;
            opts.inSampleSize = size;

            //输入流必须在这里再创建一次，不然得到的 Bitmap 为空
            i = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(i, outPadding, opts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (i!=null) {
                try {
                    i.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 获取缓存文件夹
     * @param fileName 缓存文件名
     * @return 缓存文件夹
     */
    public static File getCacheDirFile(String fileName) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return null;
        }
        File dirFile = new File(Environment.getExternalStorageDirectory(), fileName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dirFile;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}






