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

import com.kaidongyuan.app.kdyorder.R;

/**
 * Created by Administrator on 2016/6/1.
 * 提示用户下载的 Dialog 重写返回键按下方法
 */
public class ScanfProductPriceDialog extends Dialog implements View.OnClickListener {

    private Button mButtonCancel;
    private Button mButtonConfirm;
    private EditText mEditTextInputNumber;
    private ScanfProductPriceDialogInterface mInterface;
    private String mUpperLimitPrice;
    private String mLowerLimitPrice;

    /**
     *
     * @param context 上下文
     * @param upperLimitPrice 产品调价下限
     * @param lowerLimitPrice 产品调价上限
     */
    public ScanfProductPriceDialog(Context context, String upperLimitPrice, String lowerLimitPrice) {
        this(context, R.style.widgetDialog);
        this.mUpperLimitPrice = upperLimitPrice;
        this.mLowerLimitPrice = lowerLimitPrice;
    }

    public ScanfProductPriceDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ScanfProductPriceDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_scanf_product_price);
        mButtonCancel = (Button) window.findViewById(R.id.button_cancel);
        mButtonConfirm = (Button) window.findViewById(R.id.button_confirm);
        mEditTextInputNumber = (EditText) window.findViewById(R.id.edittext_inputnumber);
        mButtonCancel.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        mEditTextInputNumber.setOnClickListener(this);
        TextView textViewUpperLimitPrice = (TextView) getWindow().findViewById(R.id.textview_upperlimit_price);
        textViewUpperLimitPrice.setText(mUpperLimitPrice);
        TextView textViewLowerLimitPrice = (TextView) getWindow().findViewById(R.id.textview_lowerlimit_price);
        textViewLowerLimitPrice.setText(mLowerLimitPrice);
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
                        Toast.makeText(getContext(), "请输入下单数量！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double inputPrice;
                    try {
                        inputPrice = Double.parseDouble(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "请输入数字!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mInterface.pressConfrimButton(inputPrice);
                }
                break;
        }
    }


    public interface ScanfProductPriceDialogInterface {
        void pressConfrimButton(double inputPrice);
    }

    public void setInterface(ScanfProductPriceDialogInterface scanfProductNumberDialogInterface) {
        this.mInterface = scanfProductNumberDialogInterface;
    }

}







