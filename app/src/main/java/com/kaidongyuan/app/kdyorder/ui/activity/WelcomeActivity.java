package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.model.WelcomeActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

/**
 * app 启动界面
 * 欢迎界面
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 跳转到 LoginActivity 按钮
     */
    private Button mButtonSkip;
    /**
     * 显示欢迎图片的控件
     */
    private ImageView mImageViewPicture;

    /**
     * 欢迎界面停留时间 5 秒
     */
    private final long mStayTime = 5000;
    /**
     * 是否是默认的线程跳转
     */
    private boolean mJumpWithTread = true;
    /**
     * 是否是通过按钮跳转到 AoginActiviy
     */
    private boolean mJumpWithButton = true;
    /**
     * 欢迎界面
     */
    private WelcomeActivityBiz mBiz;
    /**
     * 跳转到 LoginActivity 线程
     */
    private Thread mJumpThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        try {
            setTop();
            initView();
            initData();
            setListener();
            if (MyApplication.isLogin && MyApplication.getInstance().getActivitysList().size()>1) {
                this.finish();
            } else {
                startThread();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onBackPressed() {//用户点击返回键退出时销毁线程
        super.onBackPressed();
        try {
            mJumpWithTread = false;
            mJumpThread.interrupt();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            mButtonSkip = null;
            mImageViewPicture = null;
            mJumpWithTread = false;
            mJumpWithButton = false;
            mBiz = null;
            if (mJumpThread!=null) {
                mJumpThread.interrupt();
            }
            mJumpThread = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setTop () {
        //版本4.4以上设置状态栏透明，界面布满整个界面
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initView() {
        try {
            mButtonSkip = (Button) this.findViewById(R.id.button_skip_welcomeactivity);
            mImageViewPicture = (ImageView) this.findViewById(R.id.imageView_welcome);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 根据业务类型设置不同的欢迎图片
     */
    private void initData() {
        try {
            mBiz = new WelcomeActivityBiz(this);
            mBiz.setImageViewBackground(mImageViewPicture);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mButtonSkip.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_skip_welcomeactivity:
                    if (mJumpWithButton) {
                        mJumpWithTread = false;
                        mJumpThread.interrupt();
                        jumpToLoginActivity();
                        Logger.w(WelcomeActivity.this.getClass() + "用户点击跳转按钮跳转");
                    }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 开启线程定时跳转
     */
    private void startThread() {
        try {
            mJumpThread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(mStayTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mJumpWithTread) {
                        mJumpWithButton = false;
                        jumpToLoginActivity();
                        Logger.w(WelcomeActivity.this.getClass() + "停留后线程跳转");
                    }
                }
            };
            mJumpThread.start();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 跳转到登录界面
     */
    private void jumpToLoginActivity() {
        try {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
