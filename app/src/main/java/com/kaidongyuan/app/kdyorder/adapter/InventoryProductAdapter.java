package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PartyProductStock;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/8/22.
 */
public class InventoryProductAdapter extends BaseAdapter {
    private List<PartyProductStock> products;
    private Context context;

    public InventoryProductAdapter(List<PartyProductStock> products, Context context) {
        this.products = products==null?new ArrayList<PartyProductStock>():products;
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


    public void setData(List<PartyProductStock> products) {
        this.products=products==null?new ArrayList<PartyProductStock>():products;
        notifyDataSetChanged();
    }

    public List<PartyProductStock> getData(){
        return this.products==null?new ArrayList<PartyProductStock>():products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PartyProductStock product=products.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_inventory_product,null);
            holder=new ViewHolder();
            holder.tv_productName= (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_product_style= (TextView) convertView.findViewById(R.id.tv_product_style);
            holder.tv_product_price= (TextView) convertView.findViewById(R.id.tv_product_price);
            holder.tv_productInventory= (TextView) convertView.findViewById(R.id.tv_productInventory);
            holder.iv_product= (ImageView) convertView.findViewById(R.id.iv_product);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_productName.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(product.getPRODUCT_NAME()));
 //       holder.tv_productName.setText(StringUtils.getProductName(product.getPRODUCT_NAME()).trim());
//        holder.tv_product_style.setText(StringUtils.getProductStyle(product.getPRODUCT_NAME()).trim());
//        holder.tv_product_price.setText("￥" +product.getPRODUCT_PRICE());
//        holder.tv_productInventory.setText(String.valueOf(product.getPRODUCT_INVENTORY()));
//        //显示图片
//        String productImgUrl = product.getPRODUCT_URL();
//        if (TextUtils.isEmpty(productImgUrl)) {
//            holder.iv_product.setImageResource(R.drawable.ic_gift);
//        } else {
//            productImgUrl = URLCostant.LOA_URL + productImgUrl;
//            Picasso.with(context).load(productImgUrl).error(R.drawable.ic_gift).fit().into(holder.iv_product);
//        }
        return convertView;
    }


    private class ViewHolder {
        TextView tv_productName,tv_product_style,tv_product_price,tv_productInventory;
        ImageView iv_product;
    }

}
