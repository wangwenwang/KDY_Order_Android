package com.kaidongyuan.app.kdyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;

/**
 * Created by Administrator on 2016/6/1.
 * 提示用户下载的 Dialog 重写返回键按下方法
 */
public class ScanfProductNumberDialog extends Dialog implements View.OnClickListener {

    private Button mButtonCancel;
    private Button mButtonConfirm;
    private EditText mEditTextInputNumber;
    private ScanfProductNumberDialogInterface mInterface;

    public ScanfProductNumberDialog(Context context) {
        this(context, R.style.widgetDialog);
    }

    public ScanfProductNumberDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ScanfProductNumberDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_scanf_product_number);
        mButtonCancel = (Button) window.findViewById(R.id.button_cancel);
        mButtonConfirm = (Button) window.findViewById(R.id.button_confirm);
        mEditTextInputNumber = (EditText) window.findViewById(R.id.edittext_inputnumber);
        mButtonCancel.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        mEditTextInputNumber.setOnClickListener(this);
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
                    int inputNumber;
                    try {
                        inputNumber = Integer.parseInt(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "请输入数字!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mInterface.pressConfrimButton(inputNumber);
                }
                break;
        }
    }


    public interface ScanfProductNumberDialogInterface {
        void pressConfrimButton(int inputNumber);
    }

    public void setInterface(ScanfProductNumberDialogInterface scanfProductNumberDialogInterface) {
        this.mInterface = scanfProductNumberDialogInterface;
    }

}







