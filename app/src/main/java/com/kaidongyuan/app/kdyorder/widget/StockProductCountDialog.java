package com.kaidongyuan.app.kdyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.SlideDateTimeListener;
import com.example.mylibrary.SlideDateTimePicker;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.ui.activity.MakeAppStockActivity;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/1.
 * 提示用户下载的 Dialog 重写返回键按下方法
 */
public class StockProductCountDialog extends Dialog implements View.OnClickListener {

    private Button mButtonCancel;
    private Button mButtonConfirm;
    private EditText mEditTextInputNumber;
    private TextView mTextViewInputTime;
    private ScanfProductNumberDialogInterface mInterface;
    private MakeAppStockActivity mActivity;
    /**
     * 用户选择的生产日期
     */
    private Date mDate;

    public StockProductCountDialog(Context context,MakeAppStockActivity mActivity) {
        this(context, R.style.widgetDialog);
        this.mActivity=mActivity;
        mDate=new Date();
    }

    public StockProductCountDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected StockProductCountDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_stock_product_count);
        mButtonCancel = (Button) window.findViewById(R.id.button_cancel);
        mButtonConfirm = (Button) window.findViewById(R.id.button_confirm);
        mEditTextInputNumber = (EditText) window.findViewById(R.id.edittext_inputnumber);
        mTextViewInputTime= (TextView) window.findViewById(R.id.textview_inputtiem);
        mTextViewInputTime.setHint(DateUtil.formateWithTime(mDate));
        mButtonCancel.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        mEditTextInputNumber.setOnClickListener(this);
        mTextViewInputTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.button_confirm:
                dismiss();
                if (mInterface!=null) {
                    String str = mEditTextInputNumber.getText().toString().trim();
                    if (TextUtils.isEmpty(str)) {
                        Toast.makeText(getContext(), "请输入库存数量！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int inputNumber;
                    try {
                        inputNumber = Integer.parseInt(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "请输入数字!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mInterface.pressConfrimButton(inputNumber,mDate);
                }
                break;
            case R.id.textview_inputtiem:
                new SlideDateTimePicker.Builder((mActivity.getSupportFragmentManager()))
                        .setListener(new DateHandler(0))
                        .setInitialDate(new Date())
                        .setMaxDate(new Date(System.currentTimeMillis()+30*24*60*60*1000L))
                        .build()
                        .show();
                break;
        }
    }
    /**
     * 选择送货时间的监听
     */
    class DateHandler extends SlideDateTimeListener {
        int which;

        DateHandler(int which) {
            this.which = which;
        }

        @Override
        public void onDateTimeCancel() {//不设置送货时间
            try {
                super.onDateTimeCancel();
                mDate =new Date();
                mTextViewInputTime.setText(DateUtil.formateWithoutTime(mDate));
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void onDateTimeSet(Date date) {//设置送货时间
            try {
                mDate = date;
                if (date != null) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    mTextViewInputTime.setText(df.format(date));
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    public interface ScanfProductNumberDialogInterface {
        void pressConfrimButton(int inputNumber,Date mDate);
    }

    public void setInterface(ScanfProductNumberDialogInterface scanfProductNumberDialogInterface) {
        this.mInterface = scanfProductNumberDialogInterface;
    }

}







