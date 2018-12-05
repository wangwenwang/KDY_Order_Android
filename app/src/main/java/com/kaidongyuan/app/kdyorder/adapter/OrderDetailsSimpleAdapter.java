package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.OrderDetails;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 物流信息中的Adapter，内容比较简单
 */
public class OrderDetailsSimpleAdapter extends BaseAdapter {
    private List<OrderDetails> orderDetails;
    private Context mContext;

    public OrderDetailsSimpleAdapter(Context context, List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails==null? new ArrayList<OrderDetails>():orderDetails;
        this.mContext = context;
    }

    public void notifyChange(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails==null? new ArrayList<OrderDetails>():orderDetails;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orderDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderDetails orderDetail = this.orderDetails.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_simple, null);
            holder = new Holder();
            holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_productNumber = (TextView) convertView.findViewById(R.id.tv_productNumber);
            holder.tv_order_qty = (TextView) convertView.findViewById(R.id.tv_order_qty);
            holder.tv_order_weight = (TextView) convertView.findViewById(R.id.tv_order_weight);
            holder.tv_order_volume = (TextView) convertView.findViewById(R.id.tv_order_volume);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String productName = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPRODUCT_NAME());
        holder.tv_productName.setText(productName);

        String productNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPRODUCT_NO());
        holder.tv_productNumber.setText(productNumber);

        String totalSize = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getISSUE_QTY() + orderDetail.getORDER_UOM());
        int index = totalSize.indexOf(".");
        if (index!=-1) {
            totalSize = totalSize.substring(0, index+2);
        }
        if (!totalSize.contains("箱")) {
            totalSize += "箱";
        }
        holder.tv_order_qty.setText(totalSize);

        String issueWeight = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getISSUE_WEIGHT());
        holder.tv_order_weight.setText(issueWeight + "吨");

        String issueVolume = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getISSUE_VOLUME());
        holder.tv_order_volume.setText(issueVolume + "吨");
        return convertView;
    }

    class Holder {
        TextView tv_productName, tv_productNumber, tv_order_qty, tv_order_weight, tv_order_volume;
    }


}
