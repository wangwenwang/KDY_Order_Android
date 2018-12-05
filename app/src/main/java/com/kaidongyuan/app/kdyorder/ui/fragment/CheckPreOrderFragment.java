package com.kaidongyuan.app.kdyorder.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.CheckOrderFragmentViewpagerAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.interfaces.JumpFragmentInterface;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.CheckPreOrderFragmentAuditOrderPage;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.CheckPreOrderFragmentCancelOrderPager;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.CheckPreOrderFragmentUnAuditOrderPage;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.PagerNecessaryInterface;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/1.
 * 主页 Fragment
 */
public class CheckPreOrderFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mParentView;

    /**
     * ViewPager 显示订单集合
     */
    private ViewPager mViewpager;
    /**
     * 显示订单集合 Viewpager 适配器
     */
    private CheckOrderFragmentViewpagerAdapter mViewPagerAdapter;
    /**
     * 新建计划单
     */
    private TextView mTextViewMakePreOrder;
    /**
     * 待审核订单按钮
     */
    private Button mButtonUnAuditOrder;
    /**
     * 已审核订单按钮
     */
    private Button mButtonAuditOrder;
    /**
     * 已取消订单按钮
     */
    private Button mButtonCancelOrder;

    /**
     * 点击切换按钮前现实的 Pager Index
     */
    private int mPreviousIndex;
    /**
     * 当前需要显示的 Pager Index
     */
    private int mCurrentIndex;
    /**
     * 存放切换按钮的数组
     */
    private Button[] mButtonsArr;
    /**
     * 存放 Pager 的数组
     */
    private PagerNecessaryInterface[] mPagersArr;

    /**
     * 在途订单界面角标
     */
    private final int mOrderUnAuditOrdersPagerIndex = 0;
    /**
     * 在途订单界面角标
     */
    private final int mOrderAuditOrdersPagerIndex = 1;
    /**
     * 在途订单界面角标
     */
    private final int mOrderCancelOrdersPagerIndex = 2;

    private JumpFragmentInterface mCallback;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mParentView = inflater.inflate(R.layout.fragment_checkpreorder, null);
            initData();
            initView();
            setListener();

        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
        return mParentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 这个方法是用来确认当前的Activity容器是否已经继承了该接口，如果没有将抛出错误提示
        try {
            mCallback = (JumpFragmentInterface) activity;
        } catch (ClassCastException e) {
            Logger.w(activity.toString()+"must implement JumpFragmentInterface");
        }
    }

    @Override
    public void onDestroyView() {//执行每个 Pager 的销毁方法，取消请求，取消 显示的 Dialog
        super.onDestroyView();
        PagerNecessaryInterface pager;
        for (int i=mPagersArr.length-1; i>=0; i--) {
            pager = mPagersArr[i];
            if (pager!=null) {
                pager.onDestroy();
            }
            mViewpager = null;
            mViewPagerAdapter = null;
            mContext = null;
            mParentView = null;
            mButtonsArr = null;
        }
    }

    private void initData() {
        try {
            this.mContext = this.getActivity();
            this.mPreviousIndex = -1;
            this.mCurrentIndex = mOrderUnAuditOrdersPagerIndex;
            this.mButtonsArr = new Button[5];
            this.mPagersArr = new PagerNecessaryInterface[5];
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initView() {
        try {
            this.mViewpager = (ViewPager) mParentView.findViewById(R.id.viewpager_order);
            this.mViewPagerAdapter = new CheckOrderFragmentViewpagerAdapter(getViews());
            this.mViewpager.setAdapter(mViewPagerAdapter);
            this.mViewpager.setOnPageChangeListener(new InnerPagerChangeListener());
            this.mTextViewMakePreOrder= (TextView) mParentView.findViewById(R.id.tv_makePreOrder);
            this.mButtonUnAuditOrder = (Button) mParentView.findViewById(R.id.button_unAuditOrder);
            this.mButtonsArr[mOrderUnAuditOrdersPagerIndex] = mButtonUnAuditOrder;
            this.mButtonAuditOrder = (Button) mParentView.findViewById(R.id.button_AuditOrder);
            this.mButtonsArr[mOrderAuditOrdersPagerIndex] = mButtonAuditOrder;
            this.mButtonCancelOrder = (Button) mParentView.findViewById(R.id.button_cancelOrder);
            this.mButtonsArr[mOrderCancelOrdersPagerIndex] = mButtonCancelOrder;
            mViewpager.setCurrentItem(mOrderUnAuditOrdersPagerIndex);
            setCurrentPager(mOrderUnAuditOrdersPagerIndex);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            this.mTextViewMakePreOrder.setOnClickListener(this);
            this.mButtonUnAuditOrder.setOnClickListener(this);
            this.mButtonAuditOrder.setOnClickListener(this);
            this.mButtonCancelOrder.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取 ViewPager 的各个页面
     * @return 页面集合
     */
    private List<View> getViews() {
        try {
            ArrayList<View> viewList = new ArrayList<>();
            CheckPreOrderFragmentUnAuditOrderPage pagerOne = new CheckPreOrderFragmentUnAuditOrderPage(mContext);
            viewList.add(mOrderUnAuditOrdersPagerIndex, pagerOne.getSelf());
            mPagersArr[mOrderUnAuditOrdersPagerIndex] = pagerOne;
            CheckPreOrderFragmentAuditOrderPage pagerTwo = new CheckPreOrderFragmentAuditOrderPage(mContext);
            viewList.add(mOrderAuditOrdersPagerIndex, pagerTwo.getSelf());
            mPagersArr[mOrderAuditOrdersPagerIndex] = pagerTwo;
            CheckPreOrderFragmentCancelOrderPager pageThree = new CheckPreOrderFragmentCancelOrderPager(mContext);
            viewList.add(mOrderCancelOrdersPagerIndex, pageThree.getSelf());
            mPagersArr[mOrderCancelOrdersPagerIndex] = pageThree;
            return viewList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_unAuditOrder://显示待审核订单
                    setCurrentPager(mOrderUnAuditOrdersPagerIndex);
                    break;
                case R.id.button_AuditOrder://显示已审核订单
                    setCurrentPager(mOrderAuditOrdersPagerIndex);
                    break;
                case R.id.button_cancelOrder://显示已取消订单
                    setCurrentPager(mOrderCancelOrdersPagerIndex);
                    break;
                case R.id.tv_makePreOrder:
                    int mMakeOrderFragmentIndex = 1;//跳转到下计划单的角标
                   if (!mCallback.jumpFragment(mMakeOrderFragmentIndex)){
                       ToastUtil.showToastMid("页面跳转失败，请退出重进~", Toast.LENGTH_SHORT);
                   }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 切换显示的 Pager
     *
     * @param pagerIndex 需要显示的 Pager 的 Index
     */
    private void setCurrentPager(int pagerIndex) {
        try {
            mCurrentIndex = pagerIndex;
            setButtonBackground();
            mViewpager.setCurrentItem(mCurrentIndex);
            mPagersArr[mCurrentIndex].initData();//让选中的 Pager 初始化数据，在 Pager 中判断，仅初始化一次
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置切换按钮的背景颜色
     */
    private void setButtonBackground() {
        try {
            if (mPreviousIndex == mCurrentIndex) {
                return;
            }
            int unSelectedColor = MyApplication.getmRes().getColor(R.color.checkorder_topbutton_background_unSelected);
            int unSelectedTextColor = MyApplication.getmRes().getColor(R.color.checkorder_topbutton_textcolor_unSelected);
            for (Button button : mButtonsArr) {//初始化选择按钮
                if (button != null) {
                    button.setBackgroundColor(unSelectedColor);
                    button.setTextColor(unSelectedTextColor);
                }
            }
            //设置选择按钮
            Button selectedButton = mButtonsArr[mCurrentIndex];
            selectedButton.setBackgroundColor(MyApplication.getmRes().getColor(R.color.checkorder_topbutton_background_Selected));
            selectedButton.setTextColor(MyApplication.getmRes().getColor(R.color.checkorder_topbutton_textcolor_Selected));
            mPreviousIndex = mCurrentIndex;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * ViewPager 切换界面时的监听器
     */
    private class InnerPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            try {
                setCurrentPager(i);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }

    }

}























