package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.AppStock;
import com.kaidongyuan.app.kdyorder.util.DateUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/7.
 */
public class AppStocksAdapter extends BaseAdapter {
    private List<AppStock> appStocks;
    private Context mContext;

    public AppStocksAdapter(List<AppStock> appStocks, Context mContext) {
        this.appStocks = appStocks==null?new ArrayList<AppStock>():appStocks;
        this.mContext = mContext;
    }
    public void setData(List<AppStock> appStocks){
        this.appStocks=appStocks==null?new ArrayList<AppStock>():appStocks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return appStocks.size();
    }

    @Override
    public Object getItem(int position) {
        return appStocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppStock appStock=appStocks.get(position);
        AppStockHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_appstock_list,null);
            holder=new AppStockHolder();
            holder.tv_PRODUCT_NO= (TextView) convertView.findViewById(R.id.tv_PRODUCT_NO);
            holder.tv_PRODUCT_NAME= (TextView) convertView.findViewById(R.id.tv_PRODUCT_NAME);
            holder.tv_PRODUCTION_DATE= (TextView) convertView.findViewById(R.id.tv_PRODUCTION_DATE);
            holder.tv_PRODUCTION_LASTDATE= (TextView) convertView.findViewById(R.id.tv_PRODUCTION_LASTDATE);
            holder.tv_STOCK_QTY= (TextView) convertView.findViewById(R.id.tv_STOCK_QTY);
            holder.tv_EXPIRATION_DAY= (TextView) convertView.findViewById(R.id.tv_EXPIRATION_DAY);
            holder.tv_HUO_LING= (TextView) convertView.findViewById(R.id.tv_HUO_LING);
            holder.tv_THUO_LING= (TextView) convertView.findViewById(R.id.tv_THUO_LING);
            holder.tv_ZHUO_LING= (TextView) convertView.findViewById(R.id.tv_ZHUO_LING);
            holder.tv_ZTHUO_LING= (TextView) convertView.findViewById(R.id.tv_ZTHUO_LING);
            convertView.setTag(holder);
        }else {
            holder= (AppStockHolder) convertView.getTag();
        }
        holder.tv_PRODUCT_NO.setText(appStock.getPRODUCT_NO());
        holder.tv_PRODUCT_NAME.setText(appStock.getPRODUCT_NAME());
        holder.tv_PRODUCTION_DATE.setText(StringUtils.subBySpace(appStock.getPRODUCTION_DATE(),true));
        holder.tv_STOCK_QTY.setText(appStock.getSTOCK_QTY());
        holder.tv_EXPIRATION_DAY.setText(appStock.getEXPIRATION_DAY()+"个月");
        holder.tv_HUO_LING.setText(appStock.getHUO_LING());
        holder.tv_THUO_LING.setText(appStock.getTHUO_LING());
        holder.tv_PRODUCTION_LASTDATE.setText(StringUtils.subBySpace(appStock.getDAOQI(),true));
        holder.tv_ZHUO_LING.setText(appStock.getA_ZHUO_LING());
        holder.tv_ZTHUO_LING.setText(appStock.getA_ZTHUO_LING());
        return convertView;
    }

    private CharSequence setlastdate(String production_date, String expiration_day) {
        Date stdate=StringUtils.str2date(production_date);
        if (stdate==null){
            return "暂无数据";
        }else {
            try {
                int day=Integer.parseInt(expiration_day.trim());
                Date eddate=new Date(stdate.getTime()+day*24*60*60*1000);
                return DateUtil.formateWithTime(eddate);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }


    private class AppStockHolder {
        private TextView tv_PRODUCT_NO, tv_PRODUCT_NAME, tv_PRODUCTION_DATE,tv_PRODUCTION_LASTDATE, tv_STOCK_QTY,
                tv_EXPIRATION_DAY,tv_HUO_LING,tv_THUO_LING,tv_ZHUO_LING,tv_ZTHUO_LING;
    }
}
