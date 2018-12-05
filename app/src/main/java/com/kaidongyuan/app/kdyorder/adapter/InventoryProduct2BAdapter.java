package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.BusinessProductStock;
import com.kaidongyuan.app.kdyorder.bean.PartyProductStock;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * 业务代码仓库的产品库存简要
 * Created by Administrator on 2017/8/22.
 */
public class InventoryProduct2BAdapter extends BaseAdapter {
    private List<BusinessProductStock> products;
    private Context context;

    public InventoryProduct2BAdapter(List<BusinessProductStock> products, Context context) {
        this.products = products==null?new ArrayList<BusinessProductStock>():products;
        this.context = context;
    }

    @Override
    public int getCount() {
        return products != null ? products.size() : 0;
    }

    @Override
    public Object getItem(int position) {

        return products.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    public void setData(List<BusinessProductStock> products) {
        this.products=products==null?new ArrayList<BusinessProductStock>():products;
        notifyDataSetChanged();
    }

    public List<BusinessProductStock> getData(){
        return this.products==null?new ArrayList<BusinessProductStock>():products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusinessProductStock product=products.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_inventory2b_product,null);
            holder=new ViewHolder();
            holder.tv_productName= (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_productNo= (TextView) convertView.findViewById(R.id.tv_productNo);
            holder.tv_product_QTY= (TextView) convertView.findViewById(R.id.tv_product_QTY);
            holder.tv_product_kesum= (TextView) convertView.findViewById(R.id.tv_product_kesum);
            holder.tv_product_WeiQTYALLOCATED= (TextView) convertView.findViewById(R.id.tv_product_WeiQTYALLOCATED);
            holder.tv_product_QTYALLOCATED= (TextView) convertView.findViewById(R.id.tv_product_QTYALLOCATED);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_productName.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getDescr()));
        holder.tv_productNo.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getSku()));
        holder.tv_product_QTY.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getQTY()+product.getSusr2()));
        holder.tv_product_kesum.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getKesum()+product.getSusr2()));
        holder.tv_product_QTYALLOCATED.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getQTYALLOCATED()+product.getSusr2()));
        holder.tv_product_WeiQTYALLOCATED.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getWeiQTYALLOCATED()+product.getSusr2()));
        return convertView;
    }


    private class ViewHolder {
        TextView tv_productNo,tv_productName,tv_product_QTY,tv_product_kesum,tv_product_WeiQTYALLOCATED,tv_product_QTYALLOCATED;

    }

}
