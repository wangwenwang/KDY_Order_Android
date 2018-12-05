package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.interfaces.JumpFragmentInterface;
import com.kaidongyuan.app.kdyorder.ui.fragment.CheckOrderFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.CheckPreOrderFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.MakeOrderFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.MakePreOrderFragment;
import com.kaidongyuan.app.kdyorder.ui.fragment.MineFragment;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;

/**
 * Created by ${tom} on 2017/12/4.
 */
public class PreOrderManageActivity extends BaseFragmentActivity implements View.OnClickListener,JumpFragmentInterface {
    /**
     * Fragment 管理
     */
    private FragmentManager mFragmentManager;
    /**
     * 存放 Fragment数组
     */
    private Fragment[] mFragmentArr;
    /**
     * 记录上次点击的是哪个 Fragment
     */
    private int mPerviousFragment;
    /**
     * 目前显示的 Fragment
     */
    private int mCurrentFragment;

    /**
     * CheckOrder 的角标
     */
    private final int mCheckOrderFragmentIndex = 0;

    /**
     * MakeOrder 的角标
     */
    private final int mMakeOrderFragmentIndex = 1;

    /**
     * MineFragment 的角标
     */
    private final int mMineFragmentFragmentIndex = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preorder_manage);
        initData();
        setTop();
        initView();
    }

    private void initView() {
        transFragment();
    }



    /**
     * 初始化数据
     */
    private void initData() {
        try {
            mFragmentManager = this.getFragmentManager();
            mFragmentArr = new Fragment[4];
            mPerviousFragment = -1;
            mCurrentFragment = mCheckOrderFragmentIndex;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 切换界面中的 Fragment
     */
    private void transFragment() {
        try {
            if (mCurrentFragment == mPerviousFragment) {
                return;
            }
            FragmentTransaction trans = mFragmentManager.beginTransaction();
            Fragment currentFragment = mFragmentArr[mCurrentFragment];
            if (currentFragment == null) {
                switch (mCurrentFragment) {
                    case mMakeOrderFragmentIndex:
                        currentFragment = new MakePreOrderFragment();
                        mFragmentArr[mMakeOrderFragmentIndex] = currentFragment;
                        break;
                    case mCheckOrderFragmentIndex:
                        currentFragment = new CheckPreOrderFragment();
                        mFragmentArr[mCheckOrderFragmentIndex] = currentFragment;
                        break;
                    case mMineFragmentFragmentIndex:
                        currentFragment = new MineFragment();
                        mFragmentArr[mMineFragmentFragmentIndex] = currentFragment;
                        break;
                }
                trans.add(R.id.framelayout_fragment, currentFragment);
            }
            if (mPerviousFragment != -1) {
                Fragment hideFragment = mFragmentArr[mPerviousFragment];
                trans.hide(hideFragment);
            }
            trans.show(currentFragment).commit();
            mPerviousFragment = mCurrentFragment;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 跳转到指定的 Fragment
     *
     * @param fragmentIndex 要跳转到的 Fragment 角标
     */
    private void setCurrentShowFragment(int fragmentIndex) {
        try {
            mCurrentFragment = fragmentIndex;
            transFragment();
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
    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean jumpFragment(int currentFragmentIndex, String... tags) {
       try {
           setCurrentShowFragment(currentFragmentIndex);
           return true;
       }catch (Exception e){
           ExceptionUtil.handlerException(e);
           return false;
       }
    }
}
