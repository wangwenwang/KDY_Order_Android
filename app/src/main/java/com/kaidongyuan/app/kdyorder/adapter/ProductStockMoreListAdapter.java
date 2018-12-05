package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.ProductStock;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/20.
 */
public class ProductStockMoreListAdapter extends BaseAdapter{
    private List<ProductStock> productStocks;
    private Context mContext;

    public ProductStockMoreListAdapter(Context mContext) {
        this.productStocks = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setData(List<ProductStock> productStocks){
        this.productStocks=productStocks==null?new ArrayList<ProductStock>():productStocks;
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_productstock_detail_more,parent,false);
            holder=new ViewHolder();
            holder.tv_stock_no= (TextView) convertView.findViewById(R.id.tv_stock_no);
            holder.tv_product_date= (TextView) convertView.findViewById(R.id.tv_product_date);
            holder.tv_action_date= (TextView) convertView.findViewById(R.id.tv_action_date);
            holder.tv_stock_qty= (TextView) convertView.findViewById(R.id.tv_stock_qty);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        ProductStock productStock=productStocks.get(position);
        holder.tv_stock_no.setText(productStock.getSTOCK_NO());
        holder.tv_action_date.setText(productStock.getCHANGE_DATE());
        holder.tv_product_date.setText(productStock.getPRODUCT_STATE());
        if (productStock.getSTOCK_QTY()!=null&&StringUtils.isDouble(productStock.getSTOCK_QTY())){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");//将double类型保留两位小数，不四舍五入
            holder.tv_stock_qty.setText(decimalFormat.format(Double.parseDouble(productStock.getSTOCK_QTY()))+productStock.getSTOCK_UOM());
        }else {
            holder.tv_stock_qty.setText(productStock.getSTOCK_QTY()+productStock.getSTOCK_UOM());
        }
        return convertView;
    }

    private class ViewHolder{
        TextView tv_stock_no,tv_stock_qty,tv_action_date,tv_product_date;
    }
}
