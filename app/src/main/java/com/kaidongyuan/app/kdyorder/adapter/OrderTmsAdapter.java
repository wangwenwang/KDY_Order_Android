package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户搜索订单获取的列表数据
 * Created by Administrator on 2015/9/10.
 */
public class OrderTmsAdapter extends BaseAdapter {

    private List<Order> mTmsDetails;
    private Context mContext;
    private OrderTmsAdapterOnclickInterface mInterface;
    private InnerOnCheckOndeClickListener mNodeClickListener;
    private InnerOnCheckPathClickListener mPathClickListener;
    private InnerCheckPictureClickListener mPictureClickListener;

    public OrderTmsAdapter(Context context, List<Order> orders) {
        this.mTmsDetails = orders==null? new ArrayList<Order>():orders;
        this.mContext = context;
        mNodeClickListener = new InnerOnCheckOndeClickListener();
        mPathClickListener = new InnerOnCheckPathClickListener();
        mPictureClickListener = new InnerCheckPictureClickListener();
    }

    public void notifyChange(List<Order> orderDetails) {
        this.mTmsDetails = orderDetails==null? new ArrayList<Order>():orderDetails;
        notifyDataSetChanged();
    }

    public void setInterface(OrderTmsAdapterOnclickInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public int getCount() {
        return mTmsDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mTmsDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_tms_detail, null);
            holder = new Holder();
            holder.tv_tms_order_no = (TextView) convertView.findViewById(R.id.tv_tms_order_no);
            holder.tv_tms_shipment_no = (TextView) convertView.findViewById(R.id.tv_tms_shipment_no);
            holder.tv_tms_date_load = (TextView) convertView.findViewById(R.id.tv_tms_date_load);
            holder.tv_tms_date_issue = (TextView) convertView.findViewById(R.id.tv_tms_date_issue);
            holder.tv_ord_issue_qty = (TextView) convertView.findViewById(R.id.tv_ord_issue_qty);
            holder.tv_ord_issue_weight = (TextView) convertView.findViewById(R.id.tv_ord_issue_weight);
            holder.tv_ord_issue_volume = (TextView) convertView.findViewById(R.id.tv_ord_issue_volume);
            holder.tv_ord_workflow = (TextView) convertView.findViewById(R.id.tv_ord_workflow);
            holder.tv_driver_pay = (TextView) convertView.findViewById(R.id.tv_driver_pay);

            holder.tv_check_time_node = (Button) convertView.findViewById(R.id.tv_check_time_node);
            holder.tv_check_path = (Button) convertView.findViewById(R.id.tv_check_path);
            holder.tv_check_picture = (Button) convertView.findViewById(R.id.tv_check_picture);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Order tmsDetail = this.mTmsDetails.get(position);

        String orderNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getORD_NO());
        holder.tv_tms_order_no.setText(orderNumber);

        String tmsShipmentNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getTMS_SHIPMENT_NO());
        holder.tv_tms_shipment_no.setText(tmsShipmentNumber);

        String tmsDataLoad = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getTMS_DATE_LOAD());
        holder.tv_tms_date_load.setText(tmsDataLoad);

        String tmsDateIssue = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getTMS_DATE_ISSUE());
        holder.tv_tms_date_issue.setText(tmsDateIssue);

        String orderIssueQty = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getORD_ISSUE_QTY());
        int index = orderIssueQty.indexOf(".");
        if (index!=-1) {
            orderIssueQty = orderIssueQty.substring(0, index+2);
        }
        holder.tv_ord_issue_qty.setText(orderIssueQty + "件");

        String orderIssueWeight = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getORD_ISSUE_WEIGHT());
        holder.tv_ord_issue_weight.setText(orderIssueWeight + "吨");

        String orderIssueVolume = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(tmsDetail.getORD_ISSUE_VOLUME());
        holder.tv_ord_issue_volume.setText(orderIssueVolume + "m³");

        String orderWorkFlow = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderState(tmsDetail.getORD_WORKFLOW()));
        holder.tv_ord_workflow.setText(orderWorkFlow);

        if (BusinessConstants.ORDER_WORKFLOW_COMPLETE.equals(orderWorkFlow)) {
            holder.tv_check_picture.setVisibility(View.VISIBLE);
        } else {
            holder.tv_check_picture.setVisibility(View.GONE);
        }

        String driverPay = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderState(tmsDetail.getDRIVER_PAY()));
        holder.tv_driver_pay.setText(driverPay);

        holder.tv_check_time_node.setTag(position);
        holder.tv_check_time_node.setOnClickListener(mNodeClickListener);
        holder.tv_check_path.setTag(position);
        holder.tv_check_path.setOnClickListener(mPathClickListener);
        holder.tv_check_picture.setTag(position);
        holder.tv_check_picture.setOnClickListener(mPictureClickListener);

        return convertView;
    }

    class Holder {
        TextView tv_tms_order_no, tv_tms_shipment_no, tv_tms_date_load, tv_tms_date_issue,
                tv_ord_issue_qty, tv_ord_issue_weight, tv_ord_issue_volume,
                tv_ord_workflow, tv_driver_pay;
        Button tv_check_time_node, tv_check_path, tv_check_picture;
    }

    /**
     * 查看进度点击监听
     */
    private class InnerOnCheckOndeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (mInterface!=null) {
                mInterface.doCheckNodeClick(position);
            }
        }
    }

    /**
     * 查看线路点击监听
     */
    private class InnerOnCheckPathClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (mInterface!=null) {
                mInterface.doCheckPathClick(position);
            }
        }
    }

    /**
     * 查看电子签名图片和交货现场图片
     */
    private class InnerCheckPictureClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (mInterface!=null) {
                mInterface.doCheckPictureClick(position);
            }
        }
    }

    public interface OrderTmsAdapterOnclickInterface {
        /**
         * 点击查看订单进度是的回调方法
         * @param position 数据在ListView中的位置
         */
        void doCheckNodeClick(int position);

        /**
         * 点击查看订单线路是的回调方法
         * @param position 数据在ListView中的位置
         */
        void doCheckPathClick(int position);

        /**
         * 点击查看订单签名和交货现场图片的回调方法
         * @param position 数据在ListView中的位置
         */
        void doCheckPictureClick(int position);
    }

}







