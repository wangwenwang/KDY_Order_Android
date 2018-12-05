package com.kaidongyuan.app.kdyorder.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kaidongyuan.app.kdyorder.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/5/7.
 * 滑动按钮，中间位置动画渲染移动
 */
public class MoveButton extends View implements View.OnTouchListener {

    /**
     * 控件的高度
     */
    private int mViewHeight;
    /**
     * 控件的宽度
     */
    private int mViewWidth;
    /**
     * 文字信息的高度
     */
    private int mSize;

    /**
     * 显示右侧的文字信息
     */
    private String mText = "->>>>>>>>>>";
    /**
     * 显示左侧的文字信息
     */
    private String mLeftText = "<<<<<<<<<<-";
    /**
     * 默认的文字大小
     */
    private int mTextSize = 100;
    /**
     * 文字开始绘制的位置
     */
    private int mTextStartDraw;
    /**
     * 渲染位置每次移动的间隔
     */
    private int mTranslate;
    /**
     * 圆点背景移动图片每次移动的距离 dp
     */
    private int mBitmapTransLength;
    /**
     * 渲染位置开始绘制的位置
     */
    private int mStart;
    /**
     * 文字信息的宽度
     */
    private int mTextWidth;
    /**
     * 移动圆形图片开始绘制的位置
     */
    private float mBitmapStartDraw;
    /**
     * 控制渲染位置是否移动
     */
    private boolean mNeedReDraw = true;
    /**
     * 圆形图片按钮是否处于用户按下的状态
     */
    private boolean mIsPressed = false;
    /**
     * 圆形移动图片是否要还原到起始位置
     */
    private boolean mNeedBack = false;
    /**
     * 控制控件是否处于能够被点击的状态
     */
    private boolean mCanMove = true;
    /**
     * 控件重新绘制的时间间隔
     */
    private long mReDrawTime = 50;
    /**
     * 整体的背景颜色默认为 0 不设置整体背景
     */
    private int mBackgroundTotalColor = 0;
    /**
     * 按钮的背景颜色
     */
    private int mBackgroundColor = Color.parseColor("#ABCDEF");
    /**
     * 记录用户点击按下 X 方向的位置
     */
    private float mDownX;
    /**
     * 记录用户移动过程中的 X 方向的位置
     */
    private float mMoveX;
    /**
     * 用户抬起动作是在空间左侧
     */
    private boolean mUpBitmapStartDrawNegative = false;
    /**
     * 用户抬起动作是在空间右侧
     */
    private boolean mUpBitmapStartDrawPositive = false;

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 线性渲染器
     */
    private LinearGradient mLinearGradient;
    /**
     * 左侧线性渲染器
     */
    private LinearGradient mLinearGradientLeft;
    /**
     * 图片处理工具
     */
    private Matrix mMatrix;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 移动图片
     */
    private Bitmap mCycleBitmap;
    /**
     * 用户移动到可执行位置时候的回调接口
     */
    private MoveToStartDoSomeThingInterface mStartDoSomeThingInterface;
    /**
     * 处理消息
     */
    private InnerHandler mHandler;


    public MoveButton(Context context) {
        this(context, null);
    }

    public MoveButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        initData();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return handleActionDown(event);
            case MotionEvent.ACTION_MOVE:
                return handleActionMove(event);
            case MotionEvent.ACTION_UP:
                return handleActionUp(event);
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawRightText(canvas);
        drawLeftText(canvas);
        drawBitmap(canvas);
        repeatDraw();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mPaint = new Paint();
        mMatrix = new Matrix();
        mHandler = new InnerHandler(this);
        mTextStartDraw = mViewHeight * 2 / 4;
        checkTextSize();
        mTranslate = mViewHeight / 20;
        mBitmapTransLength = 2;
        mSize = getTextHeight(mPaint, mTextSize);
        mTextWidth = getTextWidth(mPaint, mText, mTextSize);
        mLinearGradient = new LinearGradient(-mSize * 3, 0, 0, 0, new int[]{Color.GREEN, Color.YELLOW, Color.RED},
                new float[]{0, 0.7F, 1F}, Shader.TileMode.CLAMP);
        mLinearGradientLeft = new LinearGradient(-mSize * 3, 0, 0, 0, new int[]{Color.RED, Color.YELLOW, Color.GREEN},
                new float[]{0, 0.7F, 1F}, Shader.TileMode.CLAMP);

        if (mCycleBitmap == null) {//判断用户有没有设置按钮圆形背景图片，没有就设置默认的图片
            mCycleBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_movebutton_point);
            if (mCycleBitmap.getWidth() != mViewHeight) {
                mCycleBitmap = resizeImage(mCycleBitmap);
            }
        }
        this.setOnTouchListener(this);
    }

    /**
     * 处理用户按下动作
     *
     * @param event Event
     * @return 是否处理了该动作
     */
    private boolean handleActionDown(MotionEvent event) {
        if (mCanMove) {//用户按下，检查是否移动按钮
            mDownX = event.getX();
            float left = (mViewWidth - mViewHeight) / 2;
            float right = (mViewWidth + mViewHeight) / 2;
            if (mDownX > left && mDownX < right) {
                mNeedReDraw = false;
                mNeedBack = false;
                mCanMove = false;
                mIsPressed = true;
                return true;
            }
        }
        return false;
    }

    /**
     * 处理用户移动的动作
     *
     * @param event Event
     * @return 是否处理了该动作
     */
    private boolean handleActionMove(MotionEvent event) {
        if (!mCanMove) {//按钮移动
            mMoveX = event.getX();
            mBitmapStartDraw += (mMoveX - mDownX);
            float moveLength = (mViewWidth - mViewHeight) / 2;
            if (mBitmapStartDraw < -moveLength) {
                mBitmapStartDraw = -moveLength;
            }
            if (mBitmapStartDraw > moveLength) {
                mBitmapStartDraw = moveLength;
            }
            if (mBitmapStartDraw < (mViewWidth - mCycleBitmap.getWidth())) {
                mStart = (int) Math.abs(mBitmapStartDraw) + mSize / 2;
                mDownX = mMoveX;
                invalidate();
            } else {
                mBitmapStartDraw = mViewWidth - mCycleBitmap.getWidth();
            }
            if (mBitmapStartDraw > 0) {
                mUpBitmapStartDrawNegative = false;
                mUpBitmapStartDrawPositive = true;
            } else {
                mUpBitmapStartDrawNegative = true;
                mUpBitmapStartDrawPositive = false;
            }
            return true;
        }
        return false;
    }

    /**
     * 处理用户点击完成事件
     *
     * @param event Event
     * @return 是否处理了该事件
     */
    private boolean handleActionUp(MotionEvent event) {
        mIsPressed = false;
        if (!mCanMove) {//检查按钮是否到了执行的位置
            mStart = 0;
            float doInterfaceLegth = mViewWidth / 2 - mViewHeight * 2 / 3;
            if (Math.abs(mBitmapStartDraw) > doInterfaceLegth) {
                mNeedReDraw = true;
                mCanMove = true;
                if (mStartDoSomeThingInterface != null && mBitmapStartDraw > 0) {//执行滑动到右侧可执行位置接口
                    mStartDoSomeThingInterface.doRightSuccess();
                } else if (mStartDoSomeThingInterface != null && mBitmapStartDraw < 0) {//执行滑动到左侧可执行位置接口
                    mStartDoSomeThingInterface.doLeftSuccess();
                }
                mBitmapStartDraw = 0;
            } else {
                mReDrawTime = 10;
                mNeedBack = true;
            }
            if (mBitmapStartDraw > 0) {
                mUpBitmapStartDrawNegative = false;
                mUpBitmapStartDrawPositive = true;
            } else {
                mUpBitmapStartDrawNegative = true;
                mUpBitmapStartDrawPositive = false;
            }
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * 绘制背景
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        Drawable drawable = getBackground();
        if (drawable == null) {//如果布局文件设置了背景就不再设置背景
            //绘制整体背景
            mPaint.reset();
            if (mBackgroundTotalColor != 0) {
                mPaint.setColor(mBackgroundTotalColor);
                canvas.drawRect(0, 0, mViewWidth, mViewHeight, mPaint);
            }

            //绘制按钮背景
            if (mBackgroundColor != 0) {
                mPaint.setColor(mBackgroundColor);
                int radious = mViewHeight / 2;
                canvas.drawCircle(radious, radious, radious, mPaint);
                canvas.drawCircle(mViewWidth - radious, radious, radious, mPaint);
                canvas.drawRect(radious, 0, mViewWidth - radious, mViewHeight, mPaint);
            }
        }
    }

    /**
     * 绘制右侧文字
     *
     * @param canvas 画布
     */
    private void drawRightText(Canvas canvas) {
        mMatrix.setTranslate(mViewWidth/2 + mStart + mSize, 0);
        mLinearGradient.setLocalMatrix(mMatrix);
        mPaint.setShader(mLinearGradient);

        int textHeight = getTextHeight(mPaint, mTextSize);
        canvas.drawText(mText, mViewWidth / 2, (mViewHeight - textHeight) / 2 + textHeight, mPaint);

        if (mNeedBack) {//按钮移动到初始位置
            if (mUpBitmapStartDrawPositive) {
                mBitmapStartDraw -= dip2px(mBitmapTransLength);
                if (mBitmapStartDraw < 0) {
                    initBitmapStartDraw();
                }
            } else if (mUpBitmapStartDrawNegative) {
                mBitmapStartDraw += dip2px(mBitmapTransLength);
                if (mBitmapStartDraw > 0) {
                    initBitmapStartDraw();
                }
            } else {
                initBitmapStartDraw();
            }
        }
        if (mNeedReDraw) {//移动渲染的位置
            mStart += mTranslate;
            if (mStart > mViewWidth/2-mTextStartDraw) {
                mStart = 0;
            }
        }
    }

    /**
     * 绘制左侧文字
     *
     * @param canvas 画布
     */
    private void drawLeftText(Canvas canvas) {
        mMatrix.setTranslate(mViewWidth/2 - mStart + mSize, 0);
        mLinearGradientLeft.setLocalMatrix(mMatrix);
        mPaint.setShader(mLinearGradientLeft);

        int textHeight = getTextHeight(mPaint, mTextSize);
        canvas.drawText(mLeftText, mTextStartDraw, (mViewHeight - textHeight) / 2 + textHeight, mPaint);

        if (mNeedBack) {//按钮移动到初始位置
            if (mUpBitmapStartDrawPositive) {
                mBitmapStartDraw -= dip2px(mBitmapTransLength);
                if (mBitmapStartDraw < 0) {
                    initBitmapStartDraw();
                }
            } else if (mUpBitmapStartDrawNegative) {
                mBitmapStartDraw += dip2px(mBitmapTransLength);
                if (mBitmapStartDraw > 0) {
                    initBitmapStartDraw();
                }
            } else {
                initBitmapStartDraw();
            }
        }
        if (mNeedReDraw) {//移动渲染的位置
            mStart += mTranslate;
            if (mStart > mViewWidth/2-mTextStartDraw) {
                mStart = 0;
            }
        }
    }

    /**
     * 初始化圆形图片到中间位置时各个状态
     */
    private void initBitmapStartDraw() {
        mBitmapStartDraw = 0;
        mReDrawTime = 50;
        mNeedReDraw = true;
        mNeedBack = false;
        mCanMove = true;
        mUpBitmapStartDrawNegative = false;
        mUpBitmapStartDrawPositive = false;
    }

    /**
     * 绘制按钮的圆形图片
     *
     * @param canvas 画布
     */
    private void drawBitmap(Canvas canvas) {
        mPaint.reset();
        Bitmap bitmap = mCycleBitmap;
        if ((mUpBitmapStartDrawNegative && mBitmapStartDraw<0 && mIsPressed)
                || (mUpBitmapStartDrawPositive && mBitmapStartDraw>0 && !mIsPressed)) {
            Matrix m = new Matrix();
            m.postScale(-1, 1);
            bitmap = Bitmap.createBitmap(mCycleBitmap, 0, 0, mCycleBitmap.getWidth(), mCycleBitmap.getHeight(), m, true);
        }
        canvas.drawBitmap(bitmap, mBitmapStartDraw + (mViewWidth - mViewHeight) / 2, 0, mPaint);
    }

    /**
     * 重新绘制控件
     */
    private void repeatDraw() {
        //处理消息，重新绘制控件，将之前消息移除
        mHandler.removeCallbacksAndMessages(null);
        Message msg = mHandler.obtainMessage();
        msg.what = InnerHandler.REFRESH;
        mHandler.sendMessageDelayed(msg, mReDrawTime);
    }

    /**
     * 调整字体大小,让子充满显示的区域
     */
    private void checkTextSize() {
        int maxWidth = mViewWidth / 2 - mTextStartDraw;
        while (getTextWidth(mPaint, mText, mTextSize) > maxWidth) {
            mTextSize -= 2;
        }
        while (getTextWidth(mPaint, mText, mTextSize) < maxWidth) {
            mTextSize += 2;
        }
    }

    /**
     * 处理消息 Handler
     */
    private static class InnerHandler extends Handler {

        public static final int REFRESH = 1;
        private WeakReference<MoveButton> buttonWeakReference;

        public InnerHandler(MoveButton button) {
            this.buttonWeakReference = new WeakReference<>(button);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH) {
                MoveButton moveButton = buttonWeakReference.get();
                if (moveButton != null) {
                    moveButton.invalidate();
                }
            }
        }
    }

    /**
     * 设置按钮滑动到执行位置是的回调
     *
     * @param startDoSomeThingInterface moveToStartDoSomeThing 接口
     */
    public void setOnMoveToDoSomeThingListener(MoveToStartDoSomeThingInterface startDoSomeThingInterface) {
        this.mStartDoSomeThingInterface = startDoSomeThingInterface;
    }

    /**
     * 设置整体背景颜色
     *
     * @param color 颜色字符串 "#abcdef", "#00abcdef"
     */
    public void setBackgroundTotalColor(String color) {
        color = color.trim();
        if (color.length() == 4 || color.length() == 7 || color.length() == 9) {
            this.mBackgroundTotalColor = Color.parseColor(color);
            this.invalidate();
        }
    }

    /**
     * 设置背景颜色
     *
     * @param color 颜色字符串 "#abcdef", "#00abcdef"
     */
    public void setShaderBackgroundColor(String color) {
        color = color.trim();
        if (color.length() == 4 || color.length() == 7 || color.length() == 9) {
            this.mBackgroundColor = Color.parseColor(color);
            this.invalidate();
        }
    }

    /**
     * 设置显示的文字
     *
     * @param text 文字标识 如：">>>>>>"
     */
    public void setText(String text) {
        if (text == null || text.length() < 1) {
            return;
        }
        this.mText = text;
        checkTextSize();
        this.mTextWidth = getTextWidth(mPaint, mText, mTextSize);
    }

    /**
     * 设置圆形图片
     *
     * @param bitmap Bitmap
     */
    public void setCircleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mCycleBitmap = bitmap;
        }
    }

    /**
     * 按钮滑动到执行位置时调用的接口
     */
    public interface MoveToStartDoSomeThingInterface {
        /**
         * 滑动到右侧可执行位置时调用的接口
         */
        void doRightSuccess();

        /**
         * 滑动到左侧可执行位置时调用的接口
         */
        void doLeftSuccess();
    }

    /**
     * 根据文字大小获取文字高度
     *
     * @param paint    画笔
     * @param textSize 要绘制的文字的大小
     * @return 要绘制文字的高度
     */
    private int getTextHeight(Paint paint, int textSize) {
        if (paint == null) {
            paint = new Paint();
        }
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
            if (paint == null) {
                paint = new Paint();
            }
            Rect rect = new Rect();
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), rect);
            return rect.width();
        }
        return 0;
    }

    /**
     * 将 Bitmap 压缩到指定的宽度
     *
     * @param bitmap 要压缩的 Bitmap
     * @return 压缩后的 Bitmap
     */
    private Bitmap resizeImage(Bitmap bitmap) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        float scaleWidth = ((float) mViewHeight) / originWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        return Bitmap.createBitmap(bitmap, 0, 0, originWidth, originHeight, matrix, true);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}

















