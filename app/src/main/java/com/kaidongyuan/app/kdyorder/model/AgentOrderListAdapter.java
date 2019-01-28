package com.kaidongyuan.app.kdyorder.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AgentOrderListAdapter extends BaseAdapter {
    private List<Order> agentOrders;
    private Context mContext;

    public AgentOrderListAdapter(List<OutPutSimpleOrder> outPutSimpleOrders, Context mContext) {
        this.agentOrders= agentOrders==null?  new ArrayList<Order>() :agentOrders;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (agentOrders!=null){
            return agentOrders.size();
        }else return 0;
    }

    public void setData(List<Order> agentOrders){
        this.agentOrders= agentOrders==null?  new ArrayList<Order>() :agentOrders;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return agentOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order agentOrder=agentOrders.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_input_agentorder, null);
            holder = new AgentOrderListAdapter.Holder();
            holder.tv_ORD_NO = (TextView) convertView.findViewById(R.id.tv_ORD_NO);
            holder.tv_TO_NAME = (TextView) convertView.findViewById(R.id.tv_TO_NAME);
            holder.tv_ORD_DATE_ADD = (TextView) convertView.findViewById(R.id.tv_ORD_DATE_ADD);
            holder.tv_OUTPUTWORKFLOW = (TextView) convertView.findViewById(R.id.tv_OUTPUTWORKFLOW);
            holder.tv_ORD_QTY = (TextView) convertView.findViewById(R.id.tv_ORD_QTY);
            convertView.setTag(holder);
        } else {
            holder = (AgentOrderListAdapter.Holder) convertView.getTag();
        }
        holder.tv_ORD_NO.setText(agentOrder.getORD_NO());
        holder.tv_TO_NAME.setText(agentOrder.getORD_TO_NAME());
        holder.tv_ORD_DATE_ADD.setText(agentOrder.getORD_DATE_ADD());
        holder.tv_OUTPUTWORKFLOW.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderStatus(agentOrder.getORD_STATE())));
        holder.tv_ORD_QTY.setText(agentOrder.getORD_QTY());
        return convertView;
    }

    private class Holder {
        private TextView tv_ORD_NO, tv_TO_NAME, tv_ORD_DATE_ADD, tv_OUTPUTWORKFLOW, tv_ORD_QTY;
    }
}
