package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Fleet;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/7.
 */
public class FleetListAdapter extends BaseAdapter {
    private List<Fleet> fleets;
    private Context mContext;

    public FleetListAdapter(List<Fleet> fleets, Context mContext) {
        this.fleets = fleets==null?new ArrayList<Fleet>():fleets;
        this.mContext = mContext;
    }
    public void setData(List<Fleet> fleets){
        this.fleets=fleets==null?new ArrayList<Fleet>():fleets;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fleets.size();
    }

    @Override
    public Object getItem(int position) {
        return fleets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fleet fleet=fleets.get(position);
        FleetHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_fleet_list,null);
            holder=new FleetHolder();
            holder.tv_BUSINESS_NAME= (TextView) convertView.findViewById(R.id.tv_BUSINESS_NAME);
            holder.tv_PARTY_NAME= (TextView) convertView.findViewById(R.id.tv_PARTY_NAME);
            holder.tv_STOCK_DATE= (TextView) convertView.findViewById(R.id.tv_STOCK_DATE);
            holder.tv_SUBMIT_DATE= (TextView) convertView.findViewById(R.id.tv_SUBMIT_DATE);
            holder.tv_USER_NAME= (TextView) convertView.findViewById(R.id.tv_USER_NAME);
            holder.tv_ADD_DATE= (TextView) convertView.findViewById(R.id.tv_ADD_DATE);
            holder.tv_EDIT_DATE= (TextView) convertView.findViewById(R.id.tv_EDIT_DATE);
            convertView.setTag(holder);
        }else {
            holder= (FleetHolder) convertView.getTag();
        }
        holder.tv_BUSINESS_NAME.setText(fleet.getBUSINESS_NAME());
        holder.tv_EDIT_DATE.setText(fleet.getEDIT_DATE());
        holder.tv_ADD_DATE.setText(fleet.getADD_DATE());
        holder.tv_PARTY_NAME.setText(fleet.getPARTY_NAME());
        holder.tv_USER_NAME.setText(fleet.getUSER_NAME());
        holder.tv_STOCK_DATE.setText(fleet.getSTOCK_DATE());
        holder.tv_SUBMIT_DATE.setText(fleet.getSUBMIT_DATE());
        return convertView;
    }


    private class FleetHolder {
        private TextView tv_BUSINESS_NAME, tv_PARTY_NAME, tv_STOCK_DATE, tv_SUBMIT_DATE, tv_USER_NAME, tv_ADD_DATE,tv_EDIT_DATE;
    }
}
