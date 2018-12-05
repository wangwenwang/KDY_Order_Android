package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.InPutSimpleOrder;
import com.kaidongyuan.app.kdyorder.bean.OutPutSimpleOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class InputSimpleOrderListAdapter extends BaseAdapter {
    private List<InPutSimpleOrder> outPutSimpleOrders;
    private Context mContext;

    public InputSimpleOrderListAdapter(List<InPutSimpleOrder> outPutSimpleOrders, Context mContext) {
        this.outPutSimpleOrders= outPutSimpleOrders==null?  new ArrayList<InPutSimpleOrder>() :outPutSimpleOrders;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (outPutSimpleOrders!=null){
            return outPutSimpleOrders.size();
        }else return 0;
    }

    public void setData(List<InPutSimpleOrder> outPutSimpleOrders){
        this.outPutSimpleOrders= outPutSimpleOrders==null?  new ArrayList<InPutSimpleOrder>() :outPutSimpleOrders;
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
        InPutSimpleOrder outPutSimpleOrder=outPutSimpleOrders.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_input_simpleorder, null);
            holder = new Holder();
            holder.tv_OUTPUT_NO = (TextView) convertView.findViewById(R.id.tv_OUTPUT_NO);
            holder.tv_ORGOrder_NO = (TextView) convertView.findViewById(R.id.tv_ORGOrder_NO);
            holder.tv_ADD_DATE = (TextView) convertView.findViewById(R.id.tv_ADD_DATE);
            holder.tv_OUTPUT_TYPE = (TextView) convertView.findViewById(R.id.tv_OUTPUT_TYPE);
            holder.tv_OUTPUT_QTY = (TextView) convertView.findViewById(R.id.tv_OUTPUT_QTY);
            holder.tv_OUTPUT_WORKFLOW = (TextView) convertView.findViewById(R.id.tv_OUTPUT_WORKFLOW);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_OUTPUT_NO.setText(outPutSimpleOrder.getINPUT_NO());
        holder.tv_ORGOrder_NO.setText(outPutSimpleOrder.getOUTPUT_NO());
        holder.tv_ADD_DATE.setText(outPutSimpleOrder.getADD_DATE());
        if (outPutSimpleOrder.getINPUT_TYPE()!=null&&outPutSimpleOrder.getINPUT_TYPE().equals("采购入库")){
            holder.tv_OUTPUT_TYPE.setText("采购入库");
            holder.tv_OUTPUT_TYPE.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        }else if (outPutSimpleOrder.getINPUT_TYPE()!=null&&outPutSimpleOrder.getINPUT_TYPE().equals("其它入库")){
            holder.tv_OUTPUT_TYPE.setText("其它入库");
            holder.tv_OUTPUT_TYPE.setTextColor(mContext.getResources().getColor(R.color.red));
        }else if (outPutSimpleOrder.getINPUT_TYPE()!=null&&outPutSimpleOrder.getINPUT_TYPE().equals("采购退库")){
            holder.tv_OUTPUT_TYPE.setText("采购退库");
            holder.tv_OUTPUT_TYPE.setTextColor(mContext.getResources().getColor(R.color.indexmenue_text_unselected));
        }
        if (outPutSimpleOrder.getINPUT_STATE()!=null&&outPutSimpleOrder.getINPUT_STATE().equals("CANCEL")){
            holder.tv_OUTPUT_WORKFLOW.setText("此单已经被撤销");
            holder.tv_OUTPUT_WORKFLOW.setTextColor(mContext.getResources().getColor(R.color.indexmenue_text_unselected));
        }else if (outPutSimpleOrder.getINPUT_WORKFLOW()!=null&&outPutSimpleOrder.getINPUT_WORKFLOW().equals("新建")){
            holder.tv_OUTPUT_WORKFLOW.setText("未确认");
            holder.tv_OUTPUT_WORKFLOW.setTextColor(mContext.getResources().getColor(R.color.red));
        }else if (outPutSimpleOrder.getINPUT_WORKFLOW()!=null){
            holder.tv_OUTPUT_WORKFLOW.setText("已确认");
            holder.tv_OUTPUT_WORKFLOW.setTextColor(mContext.getResources().getColor(R.color.dark_green));
        }
        holder.tv_OUTPUT_QTY.setText(outPutSimpleOrder.getINPUT_QTY());
        return convertView;
    }

    private class Holder {
        private TextView tv_OUTPUT_NO, tv_ORGOrder_NO, tv_ADD_DATE, tv_OUTPUT_TYPE, tv_OUTPUT_QTY,tv_OUTPUT_WORKFLOW;
    }

}
