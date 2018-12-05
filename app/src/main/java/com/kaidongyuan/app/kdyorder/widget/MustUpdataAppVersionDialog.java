package com.kaidongyuan.app.kdyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;

/**
 * Created by Administrator on 2016/6/1.
 * 版本更新是下载升级包时显示的 Dialog
 */
public class MustUpdataAppVersionDialog extends Dialog {

    /**
     * 显示下载进度的文本框
     */
    private TextView mTextViewDownloadProgress;

    public MustUpdataAppVersionDialog(Context context) {
        this(context, R.style.widgetDialog);
    }

    public MustUpdataAppVersionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MustUpdataAppVersionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 设置下载进度
     */
    public void showDialog() {
        show();
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_must_updata_appversion);
        mTextViewDownloadProgress = (TextView) findViewById(R.id.textview_download_progress);
    }

    /**
     * 设置下载的进度
     * @param progress 下载的进度
     */
    public void setDownloadProgress(int progress) {
        mTextViewDownloadProgress.setText(String.valueOf(progress)+"%");
    }
}















