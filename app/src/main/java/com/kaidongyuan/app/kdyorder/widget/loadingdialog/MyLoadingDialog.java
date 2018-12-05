package com.kaidongyuan.app.kdyorder.widget.loadingdialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;


/**
 * Created by Administrator on 2015/8/29.
 * 网络请求进度 Dialog
 */
public class MyLoadingDialog extends Dialog {
    Window window;
    TextView tipTextView;
    NewtonCradleLoading loadingBalls;
    private Thread mCloseSelfThread;

    public MyLoadingDialog(Context context) {
        this(context, R.style.widgetDialog);
    }

    public MyLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    protected MyLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 显示 Dialog
     */
    public void showDialog() {
        if (window == null || window.getDecorView().getVisibility() == View.VISIBLE) {
            setContentView(R.layout.dialog_loading);
            windowDeploy();
            tipTextView = (TextView) findViewById(R.id.tipTextView);
            loadingBalls = (NewtonCradleLoading) findViewById(R.id.loading_balls);
        }
        tipTextView.setText("loading...");// 设置加载信息
        loadingBalls.start();
        try {
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCloseSelfThread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(15*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyLoadingDialog.this.dismiss();
            }
        };
        mCloseSelfThread.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCloseSelfThread!=null) {
            mCloseSelfThread.interrupt();
            mCloseSelfThread = null;
        }
    }


    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        // 设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(false);
        window = getWindow(); // 得到对话框
        // 设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.5f; //设置透明度
        wl.gravity = Gravity.CENTER; // 设置重力
        try {
            window.setAttributes(wl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
