package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.TmsOrder;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.DoubleArithUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户搜索订单获取的列表数据
 * Created by Administrator on 2015/9/10.
 */
public class TmsOrderListAdapter extends BaseAdapter {
    private List<TmsOrder> orderList;
    private Context mContext;

    public TmsOrderListAdapter(Context context, List<TmsOrder> orderList) {
        this.mContext = context;
        this.orderList = orderList==null? new ArrayList<TmsOrder>():orderList;
    }

    public void notifyChange(List<TmsOrder> orderList) {
        this.orderList = orderList==null? new ArrayList<TmsOrder>():orderList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orderList != null ? orderList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TmsOrder order = orderList.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tms_order_info, null);
            holder = new Holder();
            holder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
            holder.tv_order_addDate= (TextView) convertView.findViewById(R.id.tv_order_addDate);
            holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_order_toaddress = (TextView) convertView.findViewById(R.id.tv_order_toaddress);
            holder.tv_order_qty = (TextView) convertView.findViewById(R.id.tv_order_qty);
            holder.tv_order_weight = (TextView) convertView.findViewById(R.id.tv_order_weight);
            holder.tv_order_volume = (TextView) convertView.findViewById(R.id.tv_order_volume);
            holder.tv_tms_qty = (TextView) convertView.findViewById(R.id.tv_tms_qty);
            holder.tv_tms_weight = (TextView) convertView.findViewById(R.id.tv_tms_weight);
            holder.tv_tms_volume = (TextView) convertView.findViewById(R.id.tv_tms_volume);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_order_no.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_No()));
        holder.tv_order_addDate.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_DATE_ADD()));
        holder.tv_customer_name.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_TO_NAME()));
        holder.tv_order_toaddress.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderStatus(order.getORD_TO_ADDRESS())));
        holder.tv_order_qty.setText(DoubleArithUtil.round(order.getORD_QTY(),3)+"件");
        holder.tv_order_weight.setText(DoubleArithUtil.round(order.getORD_QTY(),3)+"吨");
        holder.tv_order_volume.setText(DoubleArithUtil.round(order.getORD_QTY(),3)+"m³");
        holder.tv_tms_qty.setText(DoubleArithUtil.round(order.getTMS_QTY(),3)+"件");
        holder.tv_tms_weight.setText(DoubleArithUtil.round(order.getTMS_WEIGHT(),3)+"吨");
        holder.tv_tms_volume.setText(DoubleArithUtil.round(order.getTMS_VOLUME(),3)+"m³");
        return convertView;
    }

    private class Holder {
        private TextView tv_order_no,tv_order_addDate, tv_customer_name, tv_order_toaddress, tv_order_qty, tv_order_weight, tv_order_volume,
                tv_tms_qty, tv_tms_weight, tv_tms_volume;
    }

}
