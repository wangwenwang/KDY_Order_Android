package com.kaidongyuan.app.kdyorder.ui.activity;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.adapter.ChartChoiceAdapter;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerChart;
import com.kaidongyuan.app.kdyorder.bean.ProductChart;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.model.ChartCheckActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.widget.ManyBarChatView;
import com.kaidongyuan.app.kdyorder.widget.ManyBarRotateChatView;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 * 查看订单 Activity
 */
public class ChartCheckActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private ChartCheckActivityBiz mBiz;
    /**
     * 标题返回上一个界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 切换报表
     */
    private PercentLinearLayout mPercentllChangeType;
    /**
     * 显示当前报表名称
     */
    private TextView mTextViewChartType;
    /**
     * 饼状图报表控件
     */
    private PieChart mPieChart;
    /**
     * 条形报表图控件
     */
    private ManyBarChatView mManyBarChartView;
    /**
     * 全屏查看是使用的控件
     */
    private View mBlankView;
    /**
     * 存放条形报表控件和按钮布局
     */
    private RelativeLayout mRelativeLayout;
    /**
     * 全屏查看的条形报表控件
     */
    private ManyBarRotateChatView mManyBarRotateChatView;
    /**
     * 存放条形报表全屏查看的布局
     */
    private RelativeLayout mRelativeLayoutBarChart;
    /**
     * 饼状表的字体风格
     */
    private Typeface mPieChartTextStyle;
    /**
     * 选择报表的 Dialog
     */
    private Dialog mChoiceChartDialog;
    /**
     * 显示报表名的 ListView
     */
    private ListView mListViewChoiceChart;
    /**
     * 选择报表名的 Adapter
     */
    private ChartChoiceAdapter mChartChoiceAdapter;
    /**
     * 记录当前选中的报表在 ListView 中的位置
     */
    private int mCurrentChartIndex = 0;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_check);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getChartDataList();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mBiz.cancelRequest();
            mBiz = null;
            if (mLoadingDialog!=null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mImageViewGoBack = null;
            mPercentllChangeType = null;
            mTextViewChartType = null;
            mPieChart = null;
            mManyBarChartView = null;
            mBlankView = null;
            mRelativeLayout = null;
            mManyBarRotateChatView = null;
            mRelativeLayoutBarChart = null;
            mPieChartTextStyle = null;
            mChoiceChartDialog = null;
            mListViewChoiceChart = null;
            mChartChoiceAdapter = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new ChartCheckActivityBiz(this);
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

    private void initView() {
        try {
            this.mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            this.mPercentllChangeType = (PercentLinearLayout) this.findViewById(R.id.rl_change_type);
            this.mTextViewChartType = (TextView) this.findViewById(R.id.tv_type);
            this.mPieChart = (PieChart) this.findViewById(R.id.piechart_chart);
            initPieChart();
            this.mManyBarChartView = (ManyBarChatView) this.findViewById(R.id.manybarChartview);
            initManyBarChart();
            this.mBlankView = this.findViewById(R.id.blankView);
            this.mRelativeLayout = (RelativeLayout) this.findViewById(R.id.relativeLayout);
            this.mManyBarRotateChatView = (ManyBarRotateChatView) this.findViewById(R.id.manyBarRotateChartView);
            initRotateChart();
            this.mRelativeLayoutBarChart = (RelativeLayout) this.findViewById(R.id.relativeLayoutBars);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            mPercentllChangeType.setOnClickListener(this);
            this.findViewById(R.id.buttonFullScreen).setOnClickListener(this);
            this.findViewById(R.id.buttonBack).setOnClickListener(this);
            this.findViewById(R.id.buttonPieChart).setOnClickListener(this);
            this.findViewById(R.id.buttonBarChart).setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取报表数据
     */
    private void getChartDataList() {
        try {
            if (mBiz.getChartDataList(mCurrentChartIndex)) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示 Dialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback://返回上一个界面
                    this.finish();
                    break;
                case R.id.rl_change_type://切换报表
                    showChoiceChartDialog();
                    break;
                case R.id.buttonFullScreen://全屏查看条形图
                    mBlankView.setVisibility(View.VISIBLE);
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    startAnimationPaoWuXianIn(mRelativeLayout, 1.0F);
                    break;
                case R.id.buttonBack://退出全屏查看条形图
                    mBlankView.setVisibility(View.GONE);
                    startAnimationPaoWuXianOut(mRelativeLayout, 0.2F);
                    break;
                case R.id.buttonPieChart://以饼状图查看
                    mPieChart.setVisibility(View.VISIBLE);
                    mRelativeLayoutBarChart.setVisibility(View.GONE);
                    break;
                case R.id.buttonBarChart://以条形图形式查看
                    mPieChart.setVisibility(View.GONE);
                    mRelativeLayoutBarChart.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_cancelLogin://取消选择报表的 Dialog
                    mChoiceChartDialog.dismiss();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示选择报表 Dialog
     */
    private void showChoiceChartDialog() {
        try {
            if (mChoiceChartDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                mChoiceChartDialog = builder.show();
                mChoiceChartDialog.setCanceledOnTouchOutside(false);
                Window window = mChoiceChartDialog.getWindow();
                window.setContentView(R.layout.dialog_chart_choice);
                window.findViewById(R.id.button_cancelLogin).setOnClickListener(this);
                mListViewChoiceChart = (ListView) window.findViewById(R.id.listView_chart_choice);
                mChartChoiceAdapter = new ChartChoiceAdapter(null, this);
                mListViewChoiceChart.setAdapter(mChartChoiceAdapter);
                mListViewChoiceChart.setOnItemClickListener(new InnerOnItemClickListener());
            } else {
                mChoiceChartDialog.show();
            }
            mChartChoiceAdapter.notifyChange(mBiz.getChartNames());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 监听返回键按下
     */
    @Override
    public void onBackPressed() {
        try {
            //如果全屏查看报表的状态则先退出全屏查看
            if (mRelativeLayout.getVisibility() == View.VISIBLE) {
                mBlankView.setVisibility(View.GONE);
                startAnimationPaoWuXianOut(mRelativeLayout, 0.2F);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * Dialog 中选择报表名的监听
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mChoiceChartDialog.dismiss();
                if (mCurrentChartIndex == position) {
                    return;
                }
                mCurrentChartIndex = position;
                List chartDataList = mBiz.getCurrentChartDataList(mCurrentChartIndex);
                if (chartDataList.size() <= 0) {//集合中没有数据网络请求数据
                    mBiz.getChartDataList(mCurrentChartIndex);
                    showLoadingDialog();
                } else {//集合中已有数据，直接显示
                    String chartName = mBiz.getChartNames().get(mCurrentChartIndex);
                    if (BusinessConstants.CUSTOMER_CHART_NAME.equals(chartName)) {
                        getCustomerChartDataListSuccess();
                    } else if (BusinessConstants.PRODUCT_CHART_NAME.equals(chartName)) {
                        getProductChartDataListSuccess();
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 网络获取客户报表数据集合成功
     */
    public void getCustomerChartDataListSuccess() {
        try {
            mPieChart.setCenterText("客户销量统计");
            mManyBarChartView.setDataTitle("客户销量统计");
            mManyBarChartView.setDataCompany("竖向金额，横向客户名称");
            mManyBarRotateChatView.setDataTitle("客户销量统计");
            mManyBarRotateChatView.setDataCompany("竖向金额，横向客户名称");
            mTextViewChartType.setText(BusinessConstants.CUSTOMER_CHART_NAME);
            getChartDataSuccess();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络获取产品报表数据集合成功
     */
    public void getProductChartDataListSuccess() {
        try {
            mPieChart.setCenterText("产品销量统计");
            mManyBarChartView.setDataTitle("产品销量统计");
            mManyBarChartView.setDataCompany("竖向金额，横向产品名称");
            mManyBarRotateChatView.setDataTitle("产品销量统计");
            mManyBarRotateChatView.setDataCompany("竖向金额，横向产品名称");
            mTextViewChartType.setText(BusinessConstants.PRODUCT_CHART_NAME);
            getChartDataSuccess();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络获取报表数据成功时Biz调用
     */
    private void getChartDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            List chartDataList = mBiz.getCurrentChartDataList(mCurrentChartIndex);
            if (chartDataList==null || chartDataList.size()<=0) {
                ToastUtil.showToastBottom("报表数据为空！", Toast.LENGTH_SHORT);
                return;
            }
            setPieChartData(chartDataList);
            setManyBarChartData(chartDataList);
            setRotateChartData(chartDataList);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求报表数据失败是Biz调用
     *
     * @param message 显示的信息
     */
    public void getChartDataError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初始化 饼图报表
     */
    private void initPieChart() {
        try {
            mPieChart = (PieChart) findViewById(R.id.piechart_chart);
            mPieChart.setUsePercentValues(true);
            mPieChart.setDescription("");
            mPieChart.setExtraOffsets(5, 10, 5, 5);
            mPieChart.setDragDecelerationFrictionCoef(0.95f);
            mPieChartTextStyle = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
            mPieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
            mPieChart.setDescription("");
            mPieChart.setCenterText("客户销量统计"); // 中间的描述
            mPieChart.setDrawHoleEnabled(true);
            mPieChart.setTransparentCircleColor(Color.WHITE);
            mPieChart.setTransparentCircleAlpha(110);
            mPieChart.setHoleRadius(58f);
            mPieChart.setTransparentCircleRadius(61f);
            mPieChart.setDrawCenterText(true);
            mPieChart.setRotationAngle(0);
            mPieChart.setRotationEnabled(true);
            mPieChart.setHighlightPerTapEnabled(true);
            mPieChart.setDescriptionTypeface(mPieChartTextStyle);
            mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            Legend l = mPieChart.getLegend();
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初始条形报表界面外观
     */
    private void initManyBarChart() {
        try {
            int[] productBarColos = {Color.parseColor("#F18D00"), Color.RED};
            mManyBarChartView.setDataBarColors(productBarColos)
                    .setLeftTextColor(Color.BLUE)
                    .setBottomTextColor(Color.BLUE)
                    .setDataBackgroundColor(Color.WHITE)
                    .setTitleColor(Color.RED)
                    .setLineColor(Color.BLACK)
                    .setLeftTextSize(8)
                    .setBottomTextSize(9)
                    .setDataTopTextSize(8)
                    .setTitleTextSize(20)
                    .setSpanDataTopText(5)
                    .setSpanBottomText(10);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初始化全屏旋转报表界面外观
     */
    private void initRotateChart() {
        try {
            int[] rotateColor = {Color.parseColor("#F18D00"), Color.RED};
            mManyBarRotateChatView.setDataBarColors(rotateColor)
                    .setLeftTextColor(Color.BLUE)
                    .setBottomTextColor(Color.BLUE)
                    .setDataBackgroundColor(Color.WHITE)
                    .setTitleColor(Color.RED)
                    .setLineColor(Color.BLACK)
                    .setLeftTextSize(8)
                    .setBottomTextSize(10)
                    .setDataTopTextSize(8)
                    .setTitleTextSize(20)
                    .setSpanDataTopText(8)
                    .setSpanBottomText(10)
                    .setCharBarWidth(20)
                    .setChartRotationAngle(90F);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 为饼状报表 添加数据
     *
     * @param chartList 数据源
     */
    private void setPieChartData(List chartList) {
        try {
            if (chartList == null || chartList.size() == 0) return;
            int count = chartList.size();
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();
            ArrayList<String> xVals = new ArrayList<String>();
            if (chartList.get(0) instanceof CustomerChart) {
                for (int i = 0; i < count; i++) {
                    yVals1.add(new Entry(((CustomerChart) chartList.get(i)).getORD_QTY(), i));
                    xVals.add(((CustomerChart) chartList.get(i)).getTO_CITY());
                }
            } else if (chartList.get(0) instanceof ProductChart) {
                for (int i = 0; i < count; i++) {
                    yVals1.add(new Entry(((ProductChart) chartList.get(i)).getPO_QTY(), i));
                    xVals.add(((ProductChart) chartList.get(i)).getPRODUCT_NAME());
                }
            }
            PieDataSet dataSet = new PieDataSet(yVals1, ""); // 各列名称
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);
            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.DKGRAY); // 设置单项文字颜色
            mPieChart.setData(data);
            mPieChart.highlightValues(null);
            mPieChart.invalidate();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置条形报表数据
     *
     * @param chartList 数据源
     */
    private void setManyBarChartData(List chartList) {
        try {
            if (chartList == null || chartList.size() == 0) {
                return;
            }

            mManyBarChartView.setDataNames(getNames(chartList))
                    .setData(getTypeData(chartList));
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置全屏浏览是旋转表格的数据为客户报表数据
     *
     * @param chartList 数据源
     */
    private void setRotateChartData(List chartList) {
        try {
            if (chartList == null || chartList.size() == 0) {
                return;
            }

            mManyBarRotateChatView.setDataNames(getNames(chartList))
                    .setData(getTypeData(chartList));
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取数据名称
     *
     * @param chartList 表格中的数据名称
     * @return 数据名称集合
     */
    private Map<String, String> getNames(List chartList) {
        try {
            Map<String, String> names = new HashMap<>();
            int count = chartList.size();
            if (chartList.get(0) instanceof CustomerChart) {
                for (int i = 0; i < count; i++) {
                    names.put(ManyBarChatView.NAME1, "销售数量(件)");
                    names.put(ManyBarChatView.NAME2, "销售总额(元)");
                }
            } else if (chartList.get(0) instanceof ProductChart) {
                for (int i = 0; i < count; i++) {
                    names.put(ManyBarChatView.NAME1, "销售数量(件)");
                    names.put(ManyBarChatView.NAME2, "销售总额(元)");
                }
            }
            return names;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return new HashMap<>();
        }
    }

    /**
     * 获取对应的数据集合
     *
     * @param chartList 传入的集合
     * @return 返回集合
     */
    private List getTypeData(List chartList) {
        try {
            List<Map<String, String>> data = new ArrayList<>();
            int count = chartList.size();
            if (chartList.get(0) instanceof CustomerChart) {
                Map<String, String> customerMap;
                CustomerChart customerNumber;
                for (int i = 0; i < count; i++) {
                    customerMap = new HashMap<>();
                    customerNumber = (CustomerChart) chartList.get(i);
                    customerMap.put(ManyBarChatView.NAME, customerNumber.getTO_CITY());
                    customerMap.put(ManyBarChatView.VALUE1, customerNumber.getORD_QTY() + "");
                    customerMap.put(ManyBarChatView.VALUE2, customerNumber.getORG_PRICE() + "");
                    data.add(customerMap);
                }
            } else if (chartList.get(0) instanceof ProductChart) {
                Map<String, String> productMap;
                ProductChart productChart;
                for (int i = 0; i < count; i++) {
                    productMap = new HashMap<>();
                    productChart = (ProductChart) chartList.get(i);
                    productMap.put(ManyBarChatView.NAME, productChart.getPRODUCT_NAME());
                    productMap.put(ManyBarChatView.VALUE1, productChart.getPO_QTY() + "");
                    productMap.put(ManyBarChatView.VALUE2, productChart.getACT_PRICE() + "");
                    data.add(productMap);
                }
            }
            return data;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return new ArrayList();
        }
    }

    /**
     * 抛物线动画
     *
     * @param view           显示动画的控件
     * @param durationSecond 动画时间
     */
    private void startAnimationPaoWuXianIn(final View view, final Float durationSecond) {
        try {
            final int screenWidth = DensityUtil.getWidth();
            final int screenHeight = DensityUtil.getHeight();
            final Float transX = screenWidth / (60 * durationSecond);
            final Float transY = screenHeight / (60 * durationSecond);

            ValueAnimator animator = new ValueAnimator();
            int duration = (int) (durationSecond * 1000);
            animator.setDuration(duration);
            animator.setObjectValues(new PointF(-screenWidth, -screenHeight));
            animator.setInterpolator(new LinearInterpolator());
            animator.setEvaluator(new TypeEvaluator() {
                @Override
                public Object evaluate(float fraction, Object startValue, Object endValue) {
                    PointF pointF = new PointF();
                    pointF.x = -screenWidth + transX * fraction * durationSecond * 60;
                    pointF.y = -screenHeight + 0.5f * transY * durationSecond * 60 * 2 * fraction * fraction;
                    if (fraction == 1F) {
                        pointF.x = 0;
                        pointF.y = 0;
                    }
                    return pointF;
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    PointF pointF = (PointF) animation.getAnimatedValue();
                    view.setX(pointF.x);
                    view.setY(pointF.y);
                }
            });
            animator.start();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 抛物线动画
     *
     * @param view           要消失动画的控件
     * @param durationSecond 动画时间
     */
    private void startAnimationPaoWuXianOut(final View view, final Float durationSecond) {
        try {
            final int screenWidth = DensityUtil.getWidth();
            final int screenHeight = DensityUtil.getHeight();
            final Float transX = screenWidth / (durationSecond * 60);
            final Float transY = screenHeight / (durationSecond * 60);

            ValueAnimator animator = new ValueAnimator();
            int duration = (int) (durationSecond * 1000);
            animator.setDuration(duration);
            animator.setObjectValues(new PointF(0, 0));
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setEvaluator(new TypeEvaluator() {
                @Override
                public Object evaluate(float fraction, Object startValue, Object endValue) {
                    PointF pointF = new PointF();
                    pointF.x = durationSecond * 60 * transX * fraction;
                    pointF.y = durationSecond * 60 * 2 * 0.5F * transY * fraction * fraction;
                    if (fraction == 1F) {
                        view.setVisibility(View.GONE);
                        pointF.x = screenWidth;
                        pointF.y = screenHeight;
                    }
                    return pointF;
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    PointF pointF = (PointF) animation.getAnimatedValue();
                    view.setY(pointF.y);
                }
            });
            animator.start();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}














