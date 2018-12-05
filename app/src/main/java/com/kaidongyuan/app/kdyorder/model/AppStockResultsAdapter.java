package com.kaidongyuan.app.kdyorder.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.AppStock;
import com.kaidongyuan.app.kdyorder.bean.AppStockResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/9.
 */
public class AppStockResultsAdapter extends BaseAdapter {
    private List<AppStockResult> appStockResults;
    private Context mContext;

    public AppStockResultsAdapter(Context mContext, List<AppStockResult> appStockResults) {
        this.mContext = mContext;
        this.appStockResults = appStockResults==null?new ArrayList<AppStockResult>():appStockResults;
    }

    @Override
    public int getCount() {
        return appStockResults.size();
    }

    @Override
    public Object getItem(int position) {
        return appStockResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppStockResultHolder holder;
        AppStockResult appStockResult=appStockResults.get(position);
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_appstockresult_list,null);
            holder=new AppStockResultHolder();
            holder.tv_PRODUCT_NAME= (TextView) convertView.findViewById(R.id.tv_PRODUCT_NAME);
            holder.tv_PRODUCT_NO= (TextView) convertView.findViewById(R.id.tv_PRODUCT_NO);
            holder.tv_PRODUCTION_DATE= (TextView) convertView.findViewById(R.id.tv_PRODUCTION_DATE);
            holder.tv_STOCK_QTY= (TextView) convertView.findViewById(R.id.tv_STOCK_QTY);
            convertView.setTag(holder);
        }else {
            holder= (AppStockResultHolder) convertView.getTag();
        }
        holder.tv_PRODUCT_NAME.setText(appStockResult.getPRODUCT_NAME());
        holder.tv_STOCK_QTY.setText(appStockResult.getSTOCK_QTY());
        holder.tv_PRODUCT_NO.setText(appStockResult.getPRODUCT_NO());
        holder.tv_PRODUCTION_DATE.setText(appStockResult.getPRODUCTION_DATE());
        return convertView;
    }

    private class AppStockResultHolder{
        private TextView tv_PRODUCT_NO, tv_PRODUCT_NAME, tv_PRODUCTION_DATE, tv_STOCK_QTY;
    }

}
