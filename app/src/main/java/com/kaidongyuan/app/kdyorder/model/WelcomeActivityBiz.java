package com.kaidongyuan.app.kdyorder.model;

import android.content.Context;
import android.widget.ImageView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.ui.activity.WelcomeActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/5/16.
 * WelcomeActivity 的业务类
 */
public class WelcomeActivityBiz {

    private Context mContext;
    private WelcomeActivity mWelcomeActivity;

    public WelcomeActivityBiz(WelcomeActivity activity) {
        this.mContext = activity;
        this.mWelcomeActivity = activity;
    }


    /**
     * 根据业务类型设置欢迎界面的图片
     * @param mImageViewPicture 显示图片的控件
     */
    public void setImageViewBackground(ImageView mImageViewPicture) {
        try {
            String businessType = SharedPreferencesUtil.getValueByName(SharedPreferenceConstants.BUSSINESS_CODE,
                    SharedPreferenceConstants.NAME, SharedPreferencesUtil.STRING);
            //根据业务类型不同，打开app显示不同的图片
            if (businessType.contains(BusinessConstants.TYPE_YIBAO)) {
                mImageViewPicture.setBackgroundResource(R.drawable.bg_splash);
            } else if (businessType.contains(BusinessConstants.TYPE_DIKUI)) {
                mImageViewPicture.setBackgroundResource(R.drawable.bg_splash2);
            }else if (businessType.contains(BusinessConstants.TYPE_KANGSHIFU)) {
                mImageViewPicture.setBackgroundResource(R.drawable.bg_splash);
            }else if (businessType.contains(BusinessConstants.TYPE_KDYMY)){
                mImageViewPicture.setBackgroundResource(R.drawable.login_background);
            }else {
                mImageViewPicture.setImageResource(R.drawable.login_background);
            }
        } catch (Exception e) {
            mImageViewPicture.setImageResource(R.drawable.login_background);
            ExceptionUtil.handlerException(e);
        }
    }
}











