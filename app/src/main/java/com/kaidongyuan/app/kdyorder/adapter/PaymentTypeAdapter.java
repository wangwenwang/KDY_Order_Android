package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PayType;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 * 支付类型 ListView 的适配器
 */
public class PaymentTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PayType> mData;

    public PaymentTypeAdapter(Context context, List<PayType> data) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<PayType>() : data;
    }

    public void notifyChange(List<PayType> data) {
        this.mData = data == null ? new ArrayList<PayType>() : data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_payment_type, null);
            holder.textViewPaymentTyep = (TextView) convertView.findViewById(R.id.textView_paymentType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String paymentType = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mData.get(position).getText());
        holder.textViewPaymentTyep.setText(paymentType);

        return convertView;
    }

    private class ViewHolder {
        TextView textViewPaymentTyep;
    }

}
