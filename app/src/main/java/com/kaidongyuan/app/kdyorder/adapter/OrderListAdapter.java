package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户搜索订单获取的列表数据
 * Created by Administrator on 2015/9/10.
 */
public class OrderListAdapter extends BaseAdapter {
    private List<Order> orderList;
    private Context mContext;

    public OrderListAdapter(Context context, List<Order> orderList) {
        this.mContext = context;
        this.orderList = orderList==null? new ArrayList<Order>():orderList;
    }

    public void notifyChange(List<Order> orderList) {
        this.orderList = orderList==null? new ArrayList<Order>():orderList;
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
        final Order order = orderList.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_order_result, null);
            holder = new Holder();
            holder.tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
            holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_driver_info = (TextView) convertView.findViewById(R.id.tv_driver_info);
            holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
            holder.tv_order_creat_time = (TextView) convertView.findViewById(R.id.tv_order_creat_time);
            holder.tv_evaluate_info = (TextView) convertView.findViewById(R.id.tv_evaluate_info);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_order_creat_time.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_DATE_ADD()));
        holder.tv_order_id.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_NO()));
        holder.tv_order_status.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderStatus(order.getORD_STATE())));
        holder.tv_customer_name.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getORD_TO_NAME()));

        if ("CLOSE".equals(order.getORD_STATE())){
            holder.tv_evaluate_info.setText("未评价");
        }
        if ("".equals(order.getTMS_DRIVER_NAME()) && "".equals(order.getTMS_DRIVER_TEL()) && "".equals(order.getTMS_PLATE_NUMBER())){
            holder.tv_driver_info.setText("无");
        }else {
            holder.tv_driver_info.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_DRIVER_NAME()) + " "
                    + CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_DRIVER_TEL()) + " "
                    + CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(order.getTMS_PLATE_NUMBER()));
        }

        return convertView;
    }

    private class Holder {
        private TextView tv_order_id, tv_customer_name, tv_driver_info, tv_order_status, tv_order_creat_time, tv_evaluate_info;
    }


}
