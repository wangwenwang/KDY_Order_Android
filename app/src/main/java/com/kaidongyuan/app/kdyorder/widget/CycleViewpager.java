package com.kaidongyuan.app.kdyorder.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kaidongyuan.app.kdyorder.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/7.
 * 广告轮播自定义控件，使用时需调用 setImagesData 方法，设置显示的图片和圆点的背景
 * 默认最多设置 10 张图片，如果超过 10 张，修改布局文件中增加 Point 的 ImageView
 * 最小设置 4 张图片，否则报错 IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
 */
public class CycleViewpager extends FrameLayout {

    /**
     * 选中PointView 背景颜色的键
     */
    public static final String SELECTED = "selected";
    /**
     * 未选中PointView 背景颜色的键
     */
    public static final String UNSELECTED = "unSelected";

    /**
     * CycleViewpager 回调接口
     */
    private ICycleViewpager mICycleViewpager;
    /**
     * 轮播图片集合
     */
    private List<Integer> mImageResourcesId;
    /**
     * 轮播图片下的店的背景图片集合
     */
    private Map<String, Integer> mPointResourcesId;
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 显示轮播图片的 Viewpager
     */
    private ViewPager mViewpager;
    /**
     * 存放显示 Point 的 ImageView 集合
     */
    private List<View> mPointViewList;
    /**
     * 存放显示 Image 的集合
     */
    private List<ImageView> mImageViewList;
    /**
     * 进行轮循播放图片，默认为 True
     */
    private boolean mIsAutoPlay = false;
    /**
     * 当前显示图片的角标
     */
    private int mCurrentIndex = 0;
    /**
     * 处理消息的 Handler
     */
    private Handler mHandler;
    /**
     * 发送执行切换的 what 值
     */
    private final int mWhatChangeImage = 0;
    /**
     * 执行轮播的线程
     */
    private Thread mPlayThread;
    /**
     * 控制广告轮播，销毁时改为 false
     */
    private boolean mShouldPlay = true;
    /**
     * 轮播时间间隔
     */
    private long mPlaySpanTime = 3600;


    public CycleViewpager(Context context) {
        this(context, null);
    }

    public CycleViewpager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CycleViewpager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mHandler = new InnerHandler(this);
    }

    /**
     * 设置回调接口
     *
     * @param iCycleViewpager 回调接口
     */
    public void setImagesData(ICycleViewpager iCycleViewpager) {
        this.mICycleViewpager = iCycleViewpager;
        initData();
        initUi();
        startPlay();
    }

    /**
     * 初始化图片资源
     */
    private void initData() {
        this.mImageResourcesId = mICycleViewpager.setImageResourcesId();
        this.mPointResourcesId = mICycleViewpager.setPointResourcesId();
    }

    /**
     * 内部 Handler
     */
    private class InnerHandler extends Handler {
        private WeakReference<CycleViewpager> reCycleViewpager;

        public InnerHandler(CycleViewpager cycleViewpager) {
            this.reCycleViewpager = new WeakReference<>(cycleViewpager);
        }

        @Override
        public void handleMessage(Message msg) {
            CycleViewpager cycleViewpager = reCycleViewpager.get();
            if (cycleViewpager != null) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case mWhatChangeImage:
                        setPointBackground(mCurrentIndex % mImageViewList.size());
                        cycleViewpager.mViewpager.setCurrentItem(mCurrentIndex);
                        break;
                }
            }
        }
    }

    /**
     * 初始化轮播控件的界面
     */
    private void initUi() {
        LayoutInflater.from(mContext).inflate(R.layout.widget_cycleviewpapger, this, true);

        //设置轮播图片的背景图片
        mImageViewList = new ArrayList<>();
        ImageView imageView;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        for (int id : mImageResourcesId) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(id);
            mImageViewList.add(imageView);
        }

        mPointViewList = new ArrayList<>();
        mPointViewList.add(this.findViewById(R.id.imageView_point1));
        mPointViewList.add(this.findViewById(R.id.imageView_point2));
        mPointViewList.add(this.findViewById(R.id.imageView_point3));
        mPointViewList.add(this.findViewById(R.id.imageView_point4));
        mPointViewList.add(this.findViewById(R.id.imageView_point5));
        mPointViewList.add(this.findViewById(R.id.imageView_point6));
        mPointViewList.add(this.findViewById(R.id.imageView_point7));
        mPointViewList.add(this.findViewById(R.id.imageView_point8));
        mPointViewList.add(this.findViewById(R.id.imageView_point9));
        mPointViewList.add(this.findViewById(R.id.imageView_point10));
        setPointViewVisiable();

        mViewpager = (ViewPager) this.findViewById(R.id.viewpager_cycleviewpager);
        mViewpager.setAdapter(new InnerViewpagerAdapter());
        mViewpager.setOnPageChangeListener(new InnerOnPagerChangerListener());

        //设置默认的播放图片和选中的点
        setPointBackground(0);
        mViewpager.setCurrentItem(mImageViewList.size() * 100);
    }

    /**
     * 根据用户传入的轮播图片的数量设置 PointView 的显示的数量，最大为10个
     */
    private void setPointViewVisiable() {
        int pointViewSize = mPointViewList.size();
        int imageresSize = mImageResourcesId.size();
        for (int i = 0; i < pointViewSize; i++) {
            if (i >= imageresSize) {
                mPointViewList.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 创建定时线程轮播背景图片
     */
    private void startPlay() {
        mPlayThread = new Thread(new playRunnable());
        mPlayThread.start();
    }

    /**
     * 停止轮播背景图片
     */
    public void stopPlay() {
        mIsAutoPlay = false;
        mShouldPlay = false;
        mPlayThread.interrupt();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 执行轮播图片切换
     */
    private class playRunnable implements Runnable {
        @Override
        public void run() {
            while (mShouldPlay) {
                try {
                    Thread.sleep(mPlaySpanTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mIsAutoPlay) {
                    mCurrentIndex += 1;
                    Message message = mHandler.obtainMessage();
                    message.what = mWhatChangeImage;
                    message.sendToTarget();
                }
            }
        }
    }

    /**
     * 这只 PointView 中的选中的点
     *
     * @param index 设置选中点的角标
     */
    private void setPointBackground(int index) {
        //显示的店为轮播图片的数量
        int pointViewSize = mImageResourcesId.size();
        ImageView pointView;
        for (int i = 0; i < pointViewSize; i++) {
            pointView = (ImageView) mPointViewList.get(i);
            if (i == index) {
                pointView.setImageResource(mPointResourcesId.get(SELECTED));
            } else {
                pointView.setImageResource(mPointResourcesId.get(UNSELECTED));
            }
        }
    }

    /**
     * Viewpager 适配器
     */
    private class InnerViewpagerAdapter extends PagerAdapter {
        int imageSize = mImageViewList.size();

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViewList.get(position % imageSize));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position % imageSize));
            return mImageViewList.get(position % imageSize);
        }
    }

    /**
     * Viewpager 页面滑动监听器
     */
    private class InnerOnPagerChangerListener implements ViewPager.OnPageChangeListener {
        int imageSize = mImageViewList.size();

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setPointBackground(position % imageSize);
            mCurrentIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 0:
                    break;
                case 1://正在滑动
                    mIsAutoPlay = false;
                    break;
                case 2://滑动结束
                    mIsAutoPlay = true;
                    break;
            }
        }
    }

    /**
     * Created by Administrator on 2016/4/7.
     * 给 CycleViewpager 设置轮播图片和圆点图片
     */
    public interface ICycleViewpager {

        /**
         * 设置 CycleViewpager 中轮播图片资源的id
         *
         * @return 图片 id 集合
         */
        List<Integer> setImageResourcesId();

        /**
         * 设置 CycleViewpager 中圆点的背景图片（选中和未选中）
         *
         * @return 圆点图片 id 集合，选中和未选中两个图片,
         * 选中图片： ("selected"，"图片id")
         * 未选中图片： ("unSelected", "图片id")
         */
        Map<String, Integer> setPointResourcesId();
    }

    /**
     * 获取轮播的时间间隔
     * @return 时间间隔
     */
    public long getmPlaySpanTime() {
        return mPlaySpanTime;
    }

    /**
     * 设置轮播时间间隔
     * @param mPlaySpanTime 时间间隔
     */
    public void setmPlaySpanTime(long mPlaySpanTime) {
        this.mPlaySpanTime = mPlaySpanTime;
    }

}

























