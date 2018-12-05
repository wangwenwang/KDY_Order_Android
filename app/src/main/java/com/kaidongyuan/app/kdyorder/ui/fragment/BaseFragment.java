package com.kaidongyuan.app.kdyorder.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

/**
 * Created by Administrator on 2016/5/19.
 * BaseFragment 打印生命周期方法
 */
public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.w(this.getClass()+":onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.w(this.getClass() + ":onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.w(this.getClass() + ":onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Logger.w(this.getClass() + ":onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Logger.w(this.getClass() + ":onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.w(this.getClass() + ":onDestroy");
    }

    @Override
    public void startActivity(Intent intent) {//设置启动 Activity 动画
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_start_with_scal,
                R.anim.activity_startorexit_withnoanimation);
    }

}
