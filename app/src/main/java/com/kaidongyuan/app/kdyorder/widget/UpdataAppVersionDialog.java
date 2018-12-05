package com.kaidongyuan.app.kdyorder.widget;

import android.app.Dialog;
import android.content.Context;

import com.kaidongyuan.app.kdyorder.R;

/**
 * Created by Administrator on 2016/6/1.
 * 提示用户下载的 Dialog 重写返回键按下方法
 */
public class UpdataAppVersionDialog extends Dialog {
    public UpdataAppVersionDialog(Context context) {
        this(context, R.style.widgetDialog);
    }

    public UpdataAppVersionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    protected UpdataAppVersionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onBackPressed() {//返回键按下的时候不消除 Dialog
    }
}







