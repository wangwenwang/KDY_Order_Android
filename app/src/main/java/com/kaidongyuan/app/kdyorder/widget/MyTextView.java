package com.kaidongyuan.app.kdyorder.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 重写考虑文字换行后，计算高度情况的TextView
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/7.
 */
public class MyTextView extends TextView {
    private Context context;
    public MyTextView(Context context) {
        super(context);
        this.context = context;
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) Math.ceil(getMaxLineHeight(this.getText().toString(), mode))
                    + getCompoundPaddingTop() + getCompoundPaddingBottom();
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    private float getMaxLineHeight(String str, int mode) {
        float height = 0.0f;
        float width = getMeasuredWidth();
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        //这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，
        // 这个是拿TextView父控件的Padding的，为了更准确的算出换行
        float pLeft = ((LinearLayout) getParent()).getPaddingLeft();
        float pRight = ((LinearLayout) getParent()).getPaddingRight();
        //检测字符串中是否包含换行符,获得换行的次数，在之后计算高度时加上
        int br = 0;
        if (str.contains("\n"))
            br = str.split("\n").length - 1;
        /**
         *  wrap_content/未指定宽度(MeasureSpec.UNSPECIFIED)，则用屏幕宽度计算
         *  否则就使用View自身宽度计算,并且无需计算Parent的Padding
         */
        int line;
        if (mode == MeasureSpec.UNSPECIFIED)
            line = (int)
                    Math.ceil((this.getPaint().measureText(str) /
                            (widthPixels - getPaddingLeft() - pLeft - pRight - getPaddingRight())));
        else {
            line = (int)
                    Math.ceil((this.getPaint().measureText(str) /
                            (width - getPaddingLeft() - getPaddingRight())));
        }
        height = (this.getPaint().getFontMetrics().descent -
                this.getPaint().getFontMetrics().ascent) * (line + br);
        return height;
    }

}
