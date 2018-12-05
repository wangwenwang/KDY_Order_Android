package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.ProductStock;
import com.kaidongyuan.app.kdyorder.bean.ProductStock2B;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/20.
 */
public class ProductStock2BListAdapter extends BaseAdapter{
    private List<ProductStock2B> productStocks;
    private Context mContext;

    public ProductStock2BListAdapter(Context mContext) {
        this.productStocks = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setData(List<ProductStock2B> productStocks){
        this.productStocks=productStocks==null?new ArrayList<ProductStock2B>():productStocks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return productStocks.size();
    }

    @Override
    public Object getItem(int position) {
        return productStocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_productstock2b_detail,parent,false);
            holder=new ViewHolder();
            holder.tv_stock_no= (TextView) convertView.findViewById(R.id.tv_stock_no);
            holder.tv_product_date= (TextView) convertView.findViewById(R.id.tv_product_date);
            holder.tv_product_qty= (TextView) convertView.findViewById(R.id.tv_product_qty);
            holder.tv_product_kesum= (TextView) convertView.findViewById(R.id.tv_product_kesum);
            holder.tv_product_QTYALLOCATED= (TextView) convertView.findViewById(R.id.tv_product_QTYALLOCATED);
            holder.tv_product_WeiQTYALLOCATED= (TextView) convertView.findViewById(R.id.tv_product_WeiQTYALLOCATED);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        ProductStock2B productStock=productStocks.get(position);
        holder.tv_stock_no.setText(productStock.getLoc());
        holder.tv_product_date.setText(productStock.getLottable04());
        holder.tv_product_qty.setText(productStock.getQTY()+productStock.getSusr2());
        holder.tv_product_kesum.setText(productStock.getKesum()+productStock.getSusr2());
        holder.tv_product_QTYALLOCATED.setText(productStock.getQTYALLOCATED()+productStock.getSusr2());
        holder.tv_product_WeiQTYALLOCATED.setText(productStock.getWeiQTYALLOCATED()+productStock.getSusr2());
        return convertView;
    }

    private class ViewHolder{
        TextView tv_stock_no,tv_product_date,tv_product_qty,
                tv_product_kesum,tv_product_QTYALLOCATED,tv_product_WeiQTYALLOCATED;
    }
}
