package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.BillFee;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class BillFeeListAdapter extends BaseAdapter {
    private List<BillFee> billFees;
    private Context mContext;

    public BillFeeListAdapter(List<BillFee> billFees, Context mContext) {
        this.billFees= billFees==null?  new ArrayList<BillFee>() :billFees;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (billFees!=null){
            return billFees.size();
        }else return 0;
    }

    public void setData(List<BillFee> billFees){
        this.billFees= billFees==null?  new ArrayList<BillFee>() :billFees;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return billFees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BillFee billFee=billFees.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill_fee, null);
            holder = new Holder();
            holder.tv_ENT_IDX = (TextView) convertView.findViewById(R.id.tv_ENT_IDX);
            holder.tv_BILL_NAME = (TextView) convertView.findViewById(R.id.tv_BILL_NAME);
            holder.tv_BILL_DATE = (TextView) convertView.findViewById(R.id.tv_BILL_DATE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_ENT_IDX.setText(billFee.getENT_IDX());
        holder.tv_BILL_NAME.setText(billFee.getBILL_NAME());
        holder.tv_BILL_DATE.setText(billFee.getBILL_DATE());
        return convertView;
    }

    private class Holder {
        private TextView tv_ENT_IDX, tv_BILL_NAME, tv_BILL_DATE;
    }

}
