package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.BillFee;
import com.kaidongyuan.app.kdyorder.bean.BusinessFeeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 账单费用条目适配器
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class BusinessFeeListAdapter extends BaseAdapter {
    private List<BusinessFeeItem> businessFeeItems;
    private Context mContext;

    public BusinessFeeListAdapter(List<BusinessFeeItem> businessFeeItems, Context mContext) {
        this.businessFeeItems= businessFeeItems==null?  new ArrayList<BusinessFeeItem>() :businessFeeItems;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (businessFeeItems!=null){
            return businessFeeItems.size();
        }else return 0;
    }

    public void setData(List<BusinessFeeItem> billFees){
        this.businessFeeItems= billFees==null?  new ArrayList<BusinessFeeItem>() :billFees;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return businessFeeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusinessFeeItem businessFeeItem=businessFeeItems.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_business_fee, null);
            holder = new Holder();
            holder.tv_BUSINESSFEE_COST = (TextView) convertView.findViewById(R.id.tv_BUSINESSFEE_COST);
            holder.tv_BUSINESSFEE_NAME = (TextView) convertView.findViewById(R.id.tv_BUSINESSFEE_NAME);
            holder.tv_BUSINESSFEE_TIME = (TextView) convertView.findViewById(R.id.tv_BUSINESSFEE_TIME);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_BUSINESSFEE_COST.setText(businessFeeItem.getFEE_AMOUNT());
        holder.tv_BUSINESSFEE_NAME.setText(businessFeeItem.getFEE_NAME());
        holder.tv_BUSINESSFEE_TIME.setText(businessFeeItem.getFEE_DATE());
        return convertView;
    }

    private class Holder {
        private TextView tv_BUSINESSFEE_COST, tv_BUSINESSFEE_NAME, tv_BUSINESSFEE_TIME;
    }

}
