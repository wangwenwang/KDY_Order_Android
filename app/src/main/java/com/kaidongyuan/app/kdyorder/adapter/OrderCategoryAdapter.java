package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.OrderGift;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 * 赠品类别 Adapter
 */
public class OrderCategoryAdapter extends BaseAdapter {

    private List<OrderGift> mData;
    private Context mContext;

    public OrderCategoryAdapter (Context context, List<OrderGift> data) {
        this.mContext = context;
        this.mData = data==null? new ArrayList<OrderGift>():data;
    }

    public void notifyChange(List<OrderGift> data) {
        this.mData = data==null? new ArrayList<OrderGift>():data;
        this.notifyDataSetChanged();
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
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_order_category, null);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.textview);
            holder.tvGiftChoiceSize = (TextView) convertView.findViewById(R.id.tv_gifgchoicesize);
            holder.tvGfitTotalChoiceSize = (TextView) convertView.findViewById(R.id.tv_gifgttotalchoicesize);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderGift orderGift = mData.get(position);
        holder.tvCategory.setText(String.valueOf(orderGift.getTYPE_NAME()));
        holder.tvGfitTotalChoiceSize.setText(String.valueOf(orderGift.getQTY()));
        holder.tvGiftChoiceSize.setText(String.valueOf(orderGift.getQTY()-orderGift.getChoiceCount()));
        if (orderGift.isChecked()) {
            convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#00000000"));
        }

        return convertView;
    }

    private class ViewHolder{
        TextView tvCategory;
        TextView tvGiftChoiceSize;
        TextView tvGfitTotalChoiceSize;
    }
}














