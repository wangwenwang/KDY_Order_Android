package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.StockProduct;
import com.kaidongyuan.app.kdyorder.bean.StockProductCount;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/8.
 */
public class StockProductCountAdapter extends BaseAdapter {
    private List<StockProductCount> stockProductCounts;
    private Context mcontext;


    public StockProductCountAdapter(List<StockProductCount> stockProductCounts, Context mcontext) {
        this.stockProductCounts = stockProductCounts== null ? new ArrayList<StockProductCount>() : stockProductCounts;
        this.mcontext = mcontext;
    }

    public void setStockProductCounts(List<StockProductCount> stockProductCounts){
        this.stockProductCounts = stockProductCounts== null ? new ArrayList<StockProductCount>() : stockProductCounts;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stockProductCounts.size();
    }

    @Override
    public Object getItem(int position) {
        return stockProductCounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView==null){
            holder=new ChildViewHolder();
            convertView = View.inflate(mcontext, R.layout.item_stockproduct_count, null);
            holder.textViewChild = (TextView) convertView.findViewById(R.id.tv_state);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder= (ChildViewHolder) convertView.getTag();
        }
        StockProductCount stockProductCount=stockProductCounts.get(position);
        holder.textViewChild.setText(stockProductCount.getSTOCK_QTY()+"");
        holder.textViewTime.setText(stockProductCount.getPRODUCTION_DATE());
        return convertView;
    }

    private class ChildViewHolder {
        TextView textViewChild;//库存数量
        TextView textViewTime;//生产日期
    }

}
