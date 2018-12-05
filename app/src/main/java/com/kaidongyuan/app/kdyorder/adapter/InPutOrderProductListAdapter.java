package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.InPutOrderProduct;
import com.kaidongyuan.app.kdyorder.bean.OutPutOrderProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * 账单费用条目适配器
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class InPutOrderProductListAdapter extends BaseAdapter {
    private List<InPutOrderProduct> moutPutOrderProducts;
    private Context mContext;

    public InPutOrderProductListAdapter(List<InPutOrderProduct> moutPutOrderProducts, Context mContext) {
        this.moutPutOrderProducts= moutPutOrderProducts==null?  new ArrayList<InPutOrderProduct>() :moutPutOrderProducts;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (moutPutOrderProducts!=null){
            return moutPutOrderProducts.size();
        }else return 0;
    }

    public void setData(List<InPutOrderProduct> billFees){
        this.moutPutOrderProducts= billFees==null?  new ArrayList<InPutOrderProduct>() :billFees;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return moutPutOrderProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InPutOrderProduct mProduct=moutPutOrderProducts.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_outputorder_product, null);
            holder = new Holder();
            holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tv_product_qty = (TextView) convertView.findViewById(R.id.tv_product_qty);
            holder.tv_ORG_PRICE = (TextView) convertView.findViewById(R.id.tv_ORG_PRICE);
            holder.tv_OUTPUT_WEIGHT = (TextView) convertView.findViewById(R.id.tv_OUTPUT_WEIGHT);
            holder.tv_ACT_PRICE = (TextView) convertView.findViewById(R.id.tv_ACT_PRICE);
            holder.tv_OUTPUT_VOLUME = (TextView) convertView.findViewById(R.id.tv_OUTPUT_VOLUME);
            holder.tv_SUM = (TextView) convertView.findViewById(R.id.tv_SUM);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_product_name.setText(mProduct.getPRODUCT_NAME());
        holder.tv_product_qty.setText((int) mProduct.getINPUT_QTY()+mProduct.getINPUT_UOM());
        holder.tv_ORG_PRICE.setText(mProduct.getPRICE()+"");
        holder.tv_OUTPUT_WEIGHT.setText(mProduct.getPRODUCT_WEIGHT()+"");
        holder.tv_ACT_PRICE.setText(mProduct.getPRICE()+"");
        holder.tv_OUTPUT_VOLUME.setText(mProduct.getPRODUCT_VOLUME()+"");
        holder.tv_SUM.setText(mProduct.getSUM());
        return convertView;
    }

    private class Holder {
        private TextView tv_product_name, tv_product_qty, tv_ORG_PRICE,
                tv_OUTPUT_WEIGHT,tv_ACT_PRICE,tv_OUTPUT_VOLUME,tv_SUM;
    }

}
