package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.BillFee;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class OutputSimpleOrderListAdapter extends BaseAdapter {
    private List<OutPutSimpleOrder> outPutSimpleOrders;
    private Context mContext;

    public OutputSimpleOrderListAdapter(List<OutPutSimpleOrder> outPutSimpleOrders, Context mContext) {
        this.outPutSimpleOrders= outPutSimpleOrders==null?  new ArrayList<OutPutSimpleOrder>() :outPutSimpleOrders;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (outPutSimpleOrders!=null){
            return outPutSimpleOrders.size();
        }else return 0;
    }

    public void setData(List<OutPutSimpleOrder> outPutSimpleOrders){
        this.outPutSimpleOrders= outPutSimpleOrders==null?  new ArrayList<OutPutSimpleOrder>() :outPutSimpleOrders;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return outPutSimpleOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OutPutSimpleOrder outPutSimpleOrder=outPutSimpleOrders.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_output_simpleorder, null);
            holder = new Holder();
            holder.tv_OUTPUT_NO = (TextView) convertView.findViewById(R.id.tv_OUTPUT_NO);
            holder.tv_PARTY_NAME = (TextView) convertView.findViewById(R.id.tv_PARTY_NAME);
            holder.tv_ADD_DATE = (TextView) convertView.findViewById(R.id.tv_ADD_DATE);
            holder.tv_OUTPUT_TYPE = (TextView) convertView.findViewById(R.id.tv_OUTPUT_TYPE);
            holder.tv_OUTPUT_QTY = (TextView) convertView.findViewById(R.id.tv_OUTPUT_QTY);
            holder.tv_OUTPUT_WORKFLOW = (TextView) convertView.findViewById(R.id.tv_OUTPUT_WORKFLOW);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_OUTPUT_NO.setText(outPutSimpleOrder.getOUTPUT_NO());
        holder.tv_PARTY_NAME.setText(outPutSimpleOrder.getPARTY_NAME());
        holder.tv_ADD_DATE.setText(outPutSimpleOrder.getADD_DATE());
        if (outPutSimpleOrder.getOUTPUT_TYPE()!=null&&outPutSimpleOrder.getOUTPUT_TYPE().equals("销售出库")){
            holder.tv_OUTPUT_TYPE.setText("销售出库");
            holder.tv_OUTPUT_TYPE.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        }else if (outPutSimpleOrder.getOUTPUT_TYPE()!=null&&outPutSimpleOrder.getOUTPUT_TYPE().equals("其它出库")){
            holder.tv_OUTPUT_TYPE.setText("其它出库");
            holder.tv_OUTPUT_TYPE.setTextColor(mContext.getResources().getColor(R.color.red));
        }else if (outPutSimpleOrder.getOUTPUT_TYPE()!=null&&outPutSimpleOrder.getOUTPUT_TYPE().equals("出库退库")){
            holder.tv_OUTPUT_TYPE.setText("出库退库");
            holder.tv_OUTPUT_TYPE.setTextColor(mContext.getResources().getColor(R.color.indexmenue_text_unselected));
        }
        if (outPutSimpleOrder.getOUTPUT_STATE()!=null&&outPutSimpleOrder.getOUTPUT_STATE().equals("CANCEL")){
            holder.tv_OUTPUT_WORKFLOW.setText("此单已经被撤销");
            holder.tv_OUTPUT_WORKFLOW.setTextColor(mContext.getResources().getColor(R.color.indexmenue_text_unselected));
        }else if (outPutSimpleOrder.getOUTPUT_WORKFLOW()!=null&&outPutSimpleOrder.getOUTPUT_WORKFLOW().equals("新建")){
            holder.tv_OUTPUT_WORKFLOW.setText("未确认");
            holder.tv_OUTPUT_WORKFLOW.setTextColor(mContext.getResources().getColor(R.color.red));
        }else if (outPutSimpleOrder.getOUTPUT_WORKFLOW()!=null){
            holder.tv_OUTPUT_WORKFLOW.setText("已确认");
            holder.tv_OUTPUT_WORKFLOW.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        }
        try {
            String OUTPUT_QTYS = outPutSimpleOrder.getOUTPUT_QTY();
            float OUTPUT_QTYF = Float.parseFloat(OUTPUT_QTYS);
            OUTPUT_QTYS = String.format("%.1f", OUTPUT_QTYF);
            holder.tv_OUTPUT_QTY.setText(OUTPUT_QTYS);
        }catch (Exception e){
            holder.tv_OUTPUT_QTY.setText(outPutSimpleOrder.getOUTPUT_QTY());
        }
        return convertView;
    }

    private class Holder {
        private TextView tv_OUTPUT_NO, tv_PARTY_NAME, tv_ADD_DATE, tv_OUTPUT_TYPE, tv_OUTPUT_QTY,tv_OUTPUT_WORKFLOW;
    }

}
