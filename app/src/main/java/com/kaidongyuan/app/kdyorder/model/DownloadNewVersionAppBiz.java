package com.kaidongyuan.app.kdyorder.model;

import android.os.Environment;
import android.util.Log;

import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/22.
 * 用于下载文件的类，在版本更新中用到
 */
public class DownloadNewVersionAppBiz {

    /**
     * 连接url
     */
    private String urlstr;
    /**
     * sd卡目录路径
     */
    private String sdcard;
    /**
     * http连接管理类
     */
    private HttpURLConnection urlcon;
    /**
     * 记录已上次显示的下载进度
     */
    private int mPreviousDownloadPercent;
    /**
     * 当前下载的进度
     */
    private long mCurrentDownloadSize;


    public DownloadNewVersionAppBiz(String url) {
        try {
            this.urlstr = url;
            //获取设备sd卡目录
            this.sdcard = Environment.getExternalStorageDirectory() + "/";
            urlcon = getConnection();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 获取http连接处理类HttpURLConnection
     */
    private HttpURLConnection getConnection() {
        URL url;
        HttpURLConnection urlcon = null;
        try {
            url = new URL(urlstr);
            urlcon = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlcon;
    }

    /**
     * 获取连接文件长度。
     * @return 文件长度
     */
    public int getLength() {
        return urlcon.getContentLength();
    }

    /**
     * 写文件到sd卡 demo
     * 前提需要设置模拟器sd卡容量，否则会引发EACCES异常
     * 先创建文件夹，在创建文件
     *
     * @param dir      下载的文件夹
     * @param filename 保存文件的文件名
     * @param handler  处理下载进度和异常的回调接口
     * @return 下载的状态码，1 为下载成功
     */
    public int down2sd(String dir, String filename, downhandler handler) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            //初始化进度条，累计下载长度
            mPreviousDownloadPercent = 0;
            mCurrentDownloadSize = 0L;
            int responseCode = urlcon.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                handler.setSize(-1);
                Logger.w(DownloadNewVersionAppBiz.this.getClass() + "下载新版本app升级包失败！返回码：" + responseCode);
                return -1;
            }

            StringBuilder sb = new StringBuilder(sdcard).append(dir);
            File file = new File(sb.toString());
            if (!file.exists()) {
                //创建文件夹
                file.mkdirs();
            }

            //获取文件全名
            sb.append(filename);
            file = new File(sb.toString());
            //创建文件
            file.createNewFile();

            is = urlcon.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[1024 * 10];
            int length;
            while ((length = is.read(buf)) != -1) {
                fos.write(buf, 0, length);
                mCurrentDownloadSize += length;
                int currentDownloadPercent = (int) (mCurrentDownloadSize * 100 / getLength());
                if ((currentDownloadPercent-mPreviousDownloadPercent)>=1 || currentDownloadPercent>= 99) {
                    //同步更新数据让 Activity 响应
                    handler.setSize(currentDownloadPercent);
                    mPreviousDownloadPercent = currentDownloadPercent;
                }
            }
            return 1;

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return -1;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 内部回调接口类
     */
    public interface downhandler {
        /**
         * 下载时的进度回调接口
         *
         * @param size 每次写入文件数据的大小 如果是-1则是出现了异常
         */
        void setSize(int size);
    }


}






