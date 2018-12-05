package com.kaidongyuan.app.kdyorder.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/8.
 * 柱状表格图控件
 */
public class ManyBarChatView extends View {

    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME = "name";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE = "value";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME1 = "name1";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE1 = "value1";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME2 = "name2";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE2 = "value2";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME3 = "name3";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE3 = "value3";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME4 = "name4";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE4 = "value4";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME5 = "name5";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE5 = "value5";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME6 = "name6";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE6 = "value6";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME7 = "name7";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE7 = "value7";
    /**
     * 数据集合中的 Map 集合存放信息的键
     */
    public static final String NAME8 = "name8";
    /**
     * 数据集合中的 Map 集合存放数据的键
     */
    public static final String VALUE8 = "value8";

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 控件的高度
     */
    private int mHeight;
    /**
     * 控件的宽度
     */
    private int mWidget;
    /**
     * 数据
     */
    private List<Map<String, String>> mData;
    /**
     * 柱状的颜色
     */
    private int[] mDataBarColors = {Color.RED, Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN, Color.DKGRAY, Color.MAGENTA, Color.parseColor("#ABCDEF")};
    /**
     * 数据名称
     */
    private Map<String, String> mDataNames;
    /**
     * 数据单位
     */
    private String mDataCompany = ".";
    /**
     * 底部表格名称
     */
    private String mDataTitle = null;
    /**
     * 底部信息栏文字的大小
     */
    private int mBottomTextSize;
    /**
     * 左侧等分信息栏文字的大小
     */
    private int mLeftTextSize;
    /**
     * 柱状图顶部文字的大小
     */
    private int mDataTopTextSize;
    /**
     * 表格标题文字大小
     */
    private int mTitleTextSize;
    /**
     * 左侧文字与数据区域的间隔
     */
    private int mSpanLeftText;
    /**
     * 柱状图顶部文字与柱状图的间隔
     */
    int mSpanDataTopText;
    /**
     * 底部信息字符串间隔
     */
    int mSpanBottomText;
    /**
     * 底部信息字符串与控件底部间隔
     */
    int mSpanBottom;
    /**
     * 数据部分柱状的间隔
     */
    private int mSpanChartBar;
    /**
     * 底部信息栏宽度最短的信息
     */
    private int mBottomMsgMinWidth;
    /**
     * 数据部分柱状的宽度
     */
    private int mCharBarWidth;
    /**
     * 绘制数据部分的背景颜色
     */
    private int mDataBackgroundColor = 0;
    /**
     * 底部信息字符串颜色
     */
    private int mBottomTextColor = Color.BLACK;
    /**
     * 左边信息栏文字颜色
     */
    private int mLeftTextColor = Color.BLACK;
    /**
     * 表格左侧和底部线的颜色
     */
    private int mLineColor = Color.BLACK;
    /**
     * 标题颜色
     */
    private int mTitleColor = Color.BLACK;
    /**
     * 表格移动的位置
     */
    private int mChartMovedSize = 0;
    /**
     * 用户按下时 X 方向位置
     */
    private int mDownX = 0;
    /**
     * 用户松手是 X 方向位置
     */
    private int mUpX = 0;
    /**
     * 表格 X 方向移动的最大距离
     */
    private int mChartMaxMovedLengthX;
    /**
     * 表的总长度
     */
    private int mChartTotalLength;


    public ManyBarChatView(Context context) {
        this(context, null);
    }

    public ManyBarChatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ManyBarChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mBottomTextSize = dpToPx(context, 15);
        this.mLeftTextSize = dpToPx(context, 10);
        this.mDataTopTextSize = dpToPx(context, 10);
        this.mSpanLeftText = dpToPx(context, 2);
        this.mSpanDataTopText = dpToPx(context, 3);
        this.mSpanBottomText = dpToPx(context, 10);
        this.mSpanBottom = dpToPx(context, 5);
        this.mTitleTextSize = dpToPx(context, 20);
        this.mCharBarWidth = dpToPx(context, 15);
        this.mSpanChartBar = mCharBarWidth / 4;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mWidget = w;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mUpX = (int) event.getX();
                shouldMoveChart();
                break;
            case MotionEvent.ACTION_UP:
                mUpX = (int) event.getX();
                shouldMoveChart();
                break;
        }
        return true;
    }

    /**
     * 判断移动的距离大于规定距离就移动表格
     */
    private void shouldMoveChart() {
        if (mData != null && mData.size() > 0 && mChartTotalLength > mWidget) {
            if (mChartMaxMovedLengthX < mWidget) {
                this.mChartMaxMovedLengthX = (mChartTotalLength - mWidget * 2 / 3);
            }
            int size = dpToPx(mContext, 2);
            if ((mUpX - mDownX) >= size || (mDownX - mUpX) >= size) {
                mChartMovedSize += (mUpX - mDownX);
                mDownX = mUpX;
                if (mChartMovedSize >= 0) {
                    mChartMovedSize = 0;
                }
                if (mChartMovedSize < -mChartMaxMovedLengthX) {
                    mChartMovedSize = -mChartMaxMovedLengthX;
                }
                this.invalidate();
            }
        }
    }

    private InnerDraw innerDraw;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData != null && mData.size() > 0) {
            if (innerDraw == null) {
                innerDraw = new InnerDraw(canvas);
            } else {
                innerDraw.initData(canvas);
            }

            checkBarWidth();
            this.mChartTotalLength = getXTotalLength();

            innerDraw.drawLeftMenue();
            innerDraw.drawDataBackground();
            innerDraw.drawDataTitle();
            innerDraw.drawCompany();

            int dataSize = mData.size();
            for (int i = 0; i < dataSize; i++) {
                //设置数据，必须在第一行
                int state = innerDraw.startDrawBody(mData.get(i), i);
                if (state == 0) {
                    continue;
                } else if (state == -1) {
                    break;
                }
                innerDraw.drawDataTopText();
                innerDraw.drawDataBar();
                innerDraw.drawBottomMessage();
                innerDraw.endDrawBody();
            }
        } else {
            drawDataIsNull(canvas);
        }
    }

    /**
     * 数据为空时绘制提示信息
     *
     * @param canvas 画布
     */
    private void drawDataIsNull(Canvas canvas) {
        String dataNull = "等待数据";
        Paint paint = new Paint();
        int dataLeft = (mWidget - getTextWidth(paint, dataNull, mTitleTextSize)) / 2;
        int dataBottom = (mHeight + getTextHeight(paint, mTitleTextSize)) / 2;
        paint.setTextSize(mTitleTextSize);
        paint.setColor(Color.RED);
        canvas.drawText(dataNull, dataLeft, dataBottom, paint);
    }

    /**
     * 根据文字大小获取文字高度
     *
     * @param paint    画笔
     * @param textSize 要绘制的文字的大小
     * @return 要绘制文字的高度
     */
    private int getTextHeight(Paint paint, int textSize) {
        String text = "测试";
        Rect rect = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 根据文字大小获取文字宽度
     *
     * @param paint    画笔
     * @param text     要绘制的文字
     * @param textSize 要绘制的文字的大小
     * @return 要绘制文字的宽度
     */
    private int getTextWidth(Paint paint, String text, int textSize) {
        if (text != null && text.length() > 0) {
            Rect rect = new Rect();
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), rect);
            return rect.width();
        }
        return 0;
    }

    /**
     * 获取左侧等分等分信息中文字最长的字符串
     *
     * @return 字符串，抹去数字后面小数点
     */
    private String getLeftValueMaxString() {
        int innerSize;
        String maxLengthString = "0";
        String tempString;
        Float maxString = 0F;
        Float tempF;
        for (Map<String, String> map : mData) {
            innerSize = map.size() - 1;
            for (int i = 1; i <= innerSize; i++) {
                tempString = map.get(VALUE + i);
                tempF = Float.parseFloat(tempString);
                if (tempF > maxString && tempString.length() > maxLengthString.length()) {
                    maxLengthString = tempString;
                    maxString = tempF;
                }
                if (tempString.length() > maxLengthString.length()) {
                    maxLengthString = tempString;
                }
            }
        }
        if (maxLengthString.contains(".")) {
            maxLengthString = maxLengthString.substring(0, maxLengthString.indexOf('.'));
        }
        maxLengthString = maxLengthString + ".0";
        return maxLengthString;
    }

    /**
     * 获取最短信息字符串
     *
     * @return 底部信息栏中最短字符串
     */
    private String getBottomMinLengthString() {
        String minString = "你好我好大家好才是真的好你说好不好";
        String tempString;
        for (Map<String, String> map : mData) {
            tempString = map.get(NAME);
            if (tempString != null && minString.length() > tempString.length()) {
                minString = tempString;
            }
        }
        if ("你好我好大家好才是真的好你说好不好".equals(minString)) {
            return "0";
        }
        return minString;
    }

    /**
     * 获取数据中的最大值
     *
     * @return 数据中的最大值 int float 类型
     */
    private int getMaxValue() {
        int maxValue = 0;
        int tempValue;
        int innerSize;
        String value;
        for (Map<String, String> map : mData) {
            innerSize = map.size() - 1;
            for (int i = 1; i <= innerSize; i++) {
                if (map.get(VALUE + i) != null) {
                    value = map.get(VALUE + i);
                    value = value.length() > 0 ? value : "0";
                    tempValue = Float.valueOf(value).intValue();
                    if (maxValue < tempValue) {
                        maxValue = tempValue + tempValue / 10;
                    }
                }
            }
        }
        return maxValue;
    }

    /**
     * 获取数据中的最长字符串
     *
     * @return 数据中的最长值字符串
     */
    private String getMaxValueText() {
        String maxValue = "0";
        int innerSize;
        String value;
        for (Map<String, String> map : mData) {
            innerSize = map.size() - 1;
            for (int i = 1; i <= innerSize; i++) {
                if (map.get(VALUE + i) != null) {
                    value = map.get(VALUE + i);
                    if (value.length() > maxValue.length()) {
                        maxValue = value;
                    }
                }
            }
        }
        return maxValue;
    }

    /**
     * 检查柱状条的宽度是否小于顶部文字宽度，小于的话就增加柱状条的宽度
     */
    private void checkBarWidth() {
        String maxValueText = getMaxValueText();
        Paint paint = new Paint();
        while (getTextWidth(paint, maxValueText, mDataTopTextSize) > (mCharBarWidth + mSpanChartBar)) {
            mCharBarWidth = mCharBarWidth * 10 / 9;
            mSpanChartBar = mCharBarWidth * 4 / 5;
        }
    }

    /**
     * 获取表格 X 方向的总长
     *
     * @return X 方向总长 int
     */
    private int getXTotalLength() {
        int xTotalLength = 0;
        int addLength;
        int msgWidth;
        int barWidth;
        int size;
        String msg;
        Paint paint = new Paint();
        for (Map<String, String> map : mData) {
            msg = map.get(NAME);
            if (msg == null || msg.length() <= 0) {
                msg = NAME;
            }
            msgWidth = getTextWidth(paint, msg, mBottomTextSize);
            size = (map.size() - 1) > mDataNames.size() ? (map.size() - 1) : mDataNames.size();
            barWidth = size * (mCharBarWidth + mSpanChartBar) + mSpanChartBar;
            addLength = msgWidth > barWidth ? msgWidth : barWidth;
            xTotalLength = xTotalLength + addLength + mSpanBottomText;
        }
        return xTotalLength;
    }

    /**
     * 根据数据中的最大值将数据分成10等分，每等分为10的倍数
     *
     * @param maxValue 数据中的最大值
     * @return 左侧等分栏的每一等分的数值 int 类型
     */
    private int getPiceValue(int maxValue) {
        float value = maxValue;
        if (value <= 10) {
            return 1;
        }
        int piceValue = 1;
        while (value > 10) {
            value = value / 10;
            piceValue = piceValue * 10;
        }
        if (value <= 5) {
            piceValue = piceValue / 2;
        }
        if (maxValue > 1000 && value < 2.5F) {
            piceValue = piceValue / 2;
        }
        if (maxValue > 10000 && value < 1.25F) {
            piceValue = piceValue / 2;
        }
        return piceValue;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue 要转换的 dp 值
     * @return 转换后的 px 值
     */
    private int dpToPx(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置数据
     *
     * @param data 表中的数据
     *             data集合中的Map
     *             "name"存放名称，使用 ManyBarChatView 中的常量
     *             "value"存放数据，使用 ManyBarChatView 中的常量
     */
    public ManyBarChatView setData(List<Map<String, String>> data) {
        try {
            this.mData = data == null ? new ArrayList<Map<String, String>>() : data;
            this.mChartMovedSize = 0;
            this.mChartMaxMovedLengthX = 0;
            this.mBottomMsgMinWidth = getTextWidth(new Paint(), getBottomMinLengthString(), mBottomTextSize);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置数据部分柱状条的颜色,默认为红色
     *
     * @param dataBarColors 16进制 int 类型颜色
     * @return 对象本身
     */
    public ManyBarChatView setDataBarColors(int[] dataBarColors) {
        try {
            int size = dataBarColors.length;
            for (int i = 0; i < size; i++) {
                mDataBarColors[i] = dataBarColors[i];
            }
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置数据名称
     *
     * @param dataNames 数据名称
     * @return 对象本身
     */
    public ManyBarChatView setDataNames(Map<String, String> dataNames) {
        try {
            this.mDataNames = dataNames.size() > 0 ? dataNames : new HashMap<String, String>();
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置绘制数据部分背景颜色
     *
     * @param dataBackgroundColor 16进制 int 类型颜色
     * @return 对象本身
     */
    public ManyBarChatView setDataBackgroundColor(int dataBackgroundColor) {
        try {
            this.mDataBackgroundColor = dataBackgroundColor;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置左边信息栏文字颜色
     *
     * @param leftTextColor 16进制 int 类型颜色
     * @return 对象本身
     */
    public ManyBarChatView setLeftTextColor(int leftTextColor) {
        try {
            this.mLeftTextColor = leftTextColor;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置底部信息文字颜色
     *
     * @param bottomTextColor 16进制 int 类型颜色
     * @return 对象本身
     */
    public ManyBarChatView setBottomTextColor(int bottomTextColor) {
        try {
            this.mBottomTextColor = bottomTextColor;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param titleColor 颜色16进制的 int 类型
     * @return 对象本身
     */
    public ManyBarChatView setTitleColor(int titleColor) {
        try {
            this.mTitleColor = titleColor;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置表格中线的颜色
     *
     * @param lineColor 颜色16进制的 int 类型
     * @return 对象本身
     */
    public ManyBarChatView setLineColor(int lineColor) {
        try {
            this.mLineColor = lineColor;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置底部信息文字大小
     *
     * @param bottomTextSize int 类型 dp
     * @return 对象本身
     */
    public ManyBarChatView setBottomTextSize(int bottomTextSize) {
        try {
            this.mBottomTextSize = dpToPx(mContext, bottomTextSize);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置左侧信息文字大小
     *
     * @param leftTextSize int 类型 dp
     * @return 对象本身
     */
    public ManyBarChatView setLeftTextSize(int leftTextSize) {
        try {
            this.mLeftTextSize = dpToPx(mContext, leftTextSize);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置柱状条顶部文字大小
     *
     * @param dataTopTextSize int 类型 dp
     * @return 对象本身
     */
    public ManyBarChatView setDataTopTextSize(int dataTopTextSize) {
        try {
            this.mDataTopTextSize = dpToPx(mContext, dataTopTextSize);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置底部表格标题文字大小
     *
     * @param titleTextSize 标题文字大小
     * @return 对象本身
     */
    public ManyBarChatView setTitleTextSize(int titleTextSize) {
        try {
            this.mTitleTextSize = dpToPx(mContext, titleTextSize);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置表格数据单位
     *
     * @param dataCompany 数据单位
     * @return 对象本身
     */
    public ManyBarChatView setDataCompany(String dataCompany) {
        try {
            if (dataCompany == null) {
                return this;
            }
            this.mDataCompany = dataCompany.length() > 0 ? dataCompany : ".";
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置表格标题
     *
     * @param dataTitle 表格标题
     * @return 对象本身
     */
    public ManyBarChatView setDataTitle(String dataTitle) {
        try {
            mDataTitle = dataTitle;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置底部信息栏文字的间隔 默认为 10dp
     *
     * @param spanBottomText 间隔距离 dp
     * @return 对象本身
     */
    public ManyBarChatView setSpanBottomText(int spanBottomText) {
        try {
            this.mSpanBottomText = dpToPx(mContext, spanBottomText);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置绘制信息部分文字竖直方向间距
     *
     * @param spanDataTopText 间隔距离 dp
     * @return 对象本身
     */
    public ManyBarChatView setSpanDataTopText(int spanDataTopText) {
        try {
            this.mSpanDataTopText = dpToPx(mContext, spanDataTopText);
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置柱状条的宽度
     *
     * @param charBarWidth 间隔距离 dp
     * @return 对象本身
     */
    public ManyBarChatView setCharBarWidth(int charBarWidth) {
        try {
            this.mCharBarWidth = dpToPx(mContext, charBarWidth);
            this.mSpanChartBar = mCharBarWidth / 4;
            this.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    //***************************************************** 绘制表格的类 ******************************************************

    /**
     * 绘制控件界面的类
     */
    private class InnerDraw {

        private Canvas canvas;
        private Paint paint;

        /**
         * 记录绘制到的位置
         */
        private int bottomTextPainted;
        /**
         * 底部标题高度
         */
        private int bottomTitleHeight;
        /**
         * 底部信息文字高度
         */
        private int bottomTextHeight;
        /**
         * 左侧文字高度
         */
        private int leftTextHeight;
        /**
         * 底部信息的高度
         */
        private int bottomMessageHeight;
        /**
         * 左侧信息栏最长文字
         */
        private String leftValueMaxString;
        /**
         * 表格中柱状图上文字高度
         */
        private int dataTopTextHeight;
        /**
         * 左侧信息栏宽度
         */
        private int leftValueWidth;
        /**
         * 绘制数据部分的高度
         */
        private int chartDataHeight;
        /**
         * 左侧等分栏的每等分高度
         */
        private int piceValueHeight;
        /**
         * 左侧等分栏每等分的数值
         */
        private int piceValue;
        /**
         * 底部信息
         */
        String bottomMessage;
        /**
         * 底部文字宽度
         */
        int bottomTextWidth;
        /**
         * 绘制柱状区域的宽度
         */
        int barsWidth;
        /**
         * 底部信息的宽度
         */
        private int bottomMessageWidth;
        /**
         * 数据信息
         */
        Map<String, String> data;
        /**
         * 要绘制信息的位置
         */
        int index;
        /**
         * 数据的个数
         */
        int dataSize;

        public InnerDraw(Canvas canvas) {
            initData(canvas);
        }

        /**
         * 初始化数据
         */
        public void initData(Canvas canvas) {
            this.canvas = canvas;
            this.paint = new Paint();
            this.bottomTitleHeight = mDataTitle == null ? 0 : getTextHeight(paint, mTitleTextSize);
            this.bottomTextPainted = 0;
            this.leftTextHeight = getTextHeight(paint, mLeftTextSize);
            this.bottomTextHeight = getTextHeight(paint, mBottomTextSize);
            this.bottomMessageHeight = bottomTextHeight + bottomTitleHeight + mSpanBottom + leftTextHeight + mSpanDataTopText;
            this.dataTopTextHeight = getTextHeight(paint, mDataTopTextSize) + mSpanDataTopText;
            this.chartDataHeight = mHeight - bottomMessageHeight - mSpanBottom;
            this.piceValueHeight = (chartDataHeight - dataTopTextHeight - getTextHeight(paint, mSpanDataTopText)) / 10;
            this.piceValue = getPiceValue(getMaxValue());
            this.leftValueMaxString = String.valueOf(piceValue * 10);
            this.leftValueWidth = getTextWidth(paint, leftValueMaxString, mLeftTextSize) + mSpanLeftText;
            this.bottomMessage = "";
            this.bottomTextWidth = 0;
            this.barsWidth = 0;
            this.bottomMessageWidth = 0;
            this.data = new HashMap<>();
            this.index = 0;
            this.dataSize = mDataNames.size();
        }

        /**
         * 绘制左侧等分栏
         */
        public void drawLeftMenue() {
            int textLeft;
            int textTop;
            String valueStr;
            int strLength;
            String maxValueStr = String.valueOf(piceValue * 10);
            int textMaxLength = maxValueStr.length();
            int leftTextHeight = getTextHeight(paint, mLeftTextSize);
            paint.setTextSize(mLeftTextSize);
            for (int i = 0; i <= 10; i++) {
                textLeft = 0;
                valueStr = String.valueOf(i * piceValue);
                strLength = valueStr.length();
                if (strLength < textMaxLength) {
                    textLeft = getTextWidth(paint, maxValueStr.substring(strLength), mLeftTextSize);
                }
                textTop = chartDataHeight - i * piceValueHeight;
                paint.setColor(mLineColor);
                canvas.drawLine(leftValueWidth - mSpanLeftText, textTop, leftValueWidth, textTop, paint);
                textTop = textTop + leftTextHeight / 3;
                paint.setColor(mLeftTextColor);
                canvas.drawText(valueStr + "", textLeft, textTop, paint);
            }
        }

        /**
         * 绘制表格中数据部分背景
         */
        public void drawDataBackground() {
            if (mDataBackgroundColor != 0) {
                paint.setColor(mDataBackgroundColor);
                Rect rect = new Rect(leftValueWidth, 0, mWidget, chartDataHeight);
                canvas.drawRect(rect, paint);
            }
            paint.setColor(mLineColor);
            canvas.drawLine(leftValueWidth, dataTopTextHeight - mLeftTextSize * 2 / 5, leftValueWidth, chartDataHeight, paint);
            canvas.drawLine(leftValueWidth, chartDataHeight, mWidget, chartDataHeight, paint);
        }

        /**
         * 绘制底部标题
         */
        public void drawDataTitle() {
            if (mDataTitle != null) {
                paint.setColor(mTitleColor);
                paint.setTextSize(mTitleTextSize);
                int titleWidget = getTextWidth(paint, mDataTitle, mTitleTextSize);
                float titleLeft = (mWidget - titleWidget) / 2;
                float titleBottom = mHeight - mSpanBottom;
                canvas.drawText(mDataTitle, titleLeft, titleBottom, paint);
            }
        }

        /**
         * 绘制数据单位
         */
        public void drawCompany() {
            int companyBottom = chartDataHeight + bottomTextHeight + leftTextHeight + mSpanDataTopText * 5 / 4;
            paint.setColor(mLeftTextColor);
            paint.setTextSize(mLeftTextSize);
            canvas.drawText(mDataCompany, leftValueWidth, companyBottom, paint);
            //绘制数据信息名称
            int dataTopTextHeight = getTextHeight(paint, mDataTopTextSize);
            int companyLeft = leftValueWidth + getTextWidth(paint, mDataCompany, mDataTopTextSize) + mSpanBottomText / 2;
            int size = mDataNames.size();
            String msg;
            Rect rect;
            for (int i = 1; i <= size; i++) {
                msg = mDataNames.get(NAME + i);
                if (msg == null || msg.length() <= 0) {
                    msg = "没设置";
                }
                paint.setColor(mDataBarColors[i - 1]);
                rect = new Rect(companyLeft, companyBottom - dataTopTextHeight * 5 / 6, companyLeft + dataTopTextHeight * 2 / 3, companyBottom - dataTopTextHeight / 6);
                canvas.drawRect(rect, paint);
                companyLeft = companyLeft + dataTopTextHeight;
                paint.setTextSize(mLeftTextSize);
                canvas.drawText(msg, companyLeft, companyBottom, paint);
                companyLeft = companyLeft + getTextWidth(paint, msg, mDataTopTextSize) + mSpanBottomText / 2;
            }
        }

        /**
         * 开始绘制，设置数据，同事设置底部信息宽度
         *
         * @param data  数据信息
         * @param index 绘制到的位置
         * @return 状态码： -1 为结束循环，0 为继续下一个循环，1 为正常绘制
         */
        public int startDrawBody(Map<String, String> data, int index) {
            this.data = data;
            this.index = index;
            this.dataSize = dataSize > (data.size() - 1) ? dataSize : (data.size() - 1);
            this.bottomMessage = data.get(NAME);
            this.bottomMessage = bottomMessage == null ? "无" : bottomMessage;
            this.bottomTextWidth = getTextWidth(paint, bottomMessage, mBottomTextSize);
            this.barsWidth = (mCharBarWidth + mSpanChartBar) * (dataSize) + mSpanChartBar;
            this.bottomMessageWidth = bottomTextWidth > barsWidth ? bottomTextWidth : barsWidth;
            int bottomMsgBegainDrawX = (index + 1) * mSpanBottomText + mChartMovedSize + leftValueWidth + bottomTextPainted;
            if ((bottomMsgBegainDrawX + bottomMessageWidth) < leftValueWidth) {//需要绘制的区域在绘制区域的左侧
                bottomTextPainted += bottomMessageWidth;
                return 0;
            }
            if (bottomMsgBegainDrawX > mWidget) {//需要绘制的区域超出了控件的右边,结束绘制
                return -1;
            }
            return 1;
        }

        /**
         * 结束绘制
         */
        public void endDrawBody() {
            bottomTextPainted += bottomMessageWidth;
        }

        /**
         * 绘制底部信息栏
         */
        public void drawBottomMessage() {
            paint.setColor(mBottomTextColor);
            paint.setTextSize(mBottomTextSize);
            int bottomLeft = (index + 1) * mSpanBottomText + mChartMovedSize + leftValueWidth + bottomTextPainted;
            if (bottomTextWidth < bottomMessageWidth) {
                bottomLeft = bottomLeft + (bottomMessageWidth - bottomTextWidth) / 2;
            }
            int bottomTop = chartDataHeight + bottomTextHeight + mSpanDataTopText / 2;
            if (bottomLeft >= leftValueWidth && bottomLeft < mWidget) {
                canvas.drawText(bottomMessage, bottomLeft, bottomTop, paint);
            } else if ((bottomLeft + bottomMessageWidth) > leftValueWidth) {
                int index = (leftValueWidth - bottomLeft) * bottomMessage.length() / bottomMessageWidth + 1;
                if (index >= 0 && index < bottomMessage.length()) {
                    canvas.drawText(bottomMessage.substring(index), leftValueWidth, bottomTop, paint);
                }
            }
        }

        /**
         * 绘制条形数据
         */
        public void drawDataBar() {
            int bottomLeft = (index + 1) * mSpanBottomText + mChartMovedSize + leftValueWidth + bottomTextPainted;
            if (barsWidth < bottomMessageWidth) {
                bottomLeft = bottomLeft + (bottomMessageWidth - barsWidth) / 2;
            }
            int dataValue;
            int dataLeft = bottomLeft + mSpanChartBar;
            int dataBottom;
            int dataTop;
            int dataRight;
            String value;
            Rect dataRect;
            for (int i = 1; i <= dataSize; i++) {
                value = data.get(VALUE + i);
                value = value == null ? "0" : value;
                value = value.length() > 0 ? value : "0";
                dataValue = Float.valueOf(value).intValue();
                dataBottom = chartDataHeight;
                dataTop = chartDataHeight;
                if (dataValue != 0) {
                    dataTop = dataTop - (dataValue * piceValueHeight / piceValue);
                }
                dataRight = dataLeft + mCharBarWidth;
                if (dataLeft < leftValueWidth && dataRight > leftValueWidth) {
                    dataLeft = leftValueWidth;
                }
                dataRect = new Rect(dataLeft, dataTop, dataRight, dataBottom);
                if (dataRight > leftValueWidth) {
                    paint.setColor(mDataBarColors[i - 1]);
                    canvas.drawRect(dataRect, paint);
                }
                dataLeft = dataRight + mSpanChartBar;
            }
        }

        /**
         * 绘制条形数据顶部文字
         */
        public void drawDataTopText() {
            int bottomLeft = (index + 1) * mSpanBottomText + mChartMovedSize + leftValueWidth + bottomTextPainted;
            if (barsWidth < bottomMessageWidth) {
                bottomLeft = bottomLeft + (bottomMessageWidth - barsWidth) / 2;
            }
            String topTextMessage;
            int topTextWidth;
            int topTextLeft = bottomLeft + mSpanChartBar;
            int tempTextLeft;
            int dataValue;
            int topTextTop;
            for (int i = 1; i <= dataSize; i++) {
                topTextMessage = data.get(VALUE + i);
                topTextMessage = topTextMessage == null ? "0" : topTextMessage;
                topTextMessage = topTextMessage.length() > 0 ? topTextMessage : "0";
                topTextWidth = getTextWidth(paint, topTextMessage, mDataTopTextSize);
                paint.setColor(mDataBarColors[i - 1]);
                paint.setTextSize(mDataTopTextSize);
                dataValue = Float.valueOf(topTextMessage).intValue();
                tempTextLeft = topTextLeft - (topTextWidth - mCharBarWidth) / 2;
                topTextTop = chartDataHeight;
                if (dataValue != 0) {
                    topTextTop = topTextTop - (dataValue * (chartDataHeight - dataTopTextHeight - mSpanDataTopText) / piceValue) / 10;
                }
                topTextTop = topTextTop - mSpanDataTopText * 2 / 3;
                if (tempTextLeft >= leftValueWidth && bottomLeft < mWidget) {
                    canvas.drawText(topTextMessage, tempTextLeft, topTextTop, paint);
                } else if ((tempTextLeft + topTextWidth) > leftValueWidth) {
                    int index = (leftValueWidth - tempTextLeft) * topTextMessage.length() / topTextWidth + 1;
                    if (index >= 0 && index < topTextMessage.length()) {
                        canvas.drawText(topTextMessage.substring(index), leftValueWidth, topTextTop, paint);
                    }
                }
                topTextLeft = topTextLeft + mCharBarWidth + mSpanChartBar;
            }
        }
    }
}





















