package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.OrderDetails;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.DoubleArithUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户搜索订单获取的列表数据
 * Created by Administrator on 2015/9/10.
 */
public class PreOrderDetailsAdapter extends BaseAdapter {

    private List<OrderDetails> orderDetails;
    private Context mContext;

    public PreOrderDetailsAdapter(Context context, List<OrderDetails> detailses) {
        this.orderDetails = detailses==null? new ArrayList<OrderDetails>():detailses;
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
            convertView = View.inflate(mContext, R.layout.item_order_detail, null);
            holder = new Holder();
            holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_product_no = (TextView) convertView.findViewById(R.id.tv_product_no);
            holder.tv_order_qty = (TextView) convertView.findViewById(R.id.tv_order_qty);
            holder.tv_order_weight = (TextView) convertView.findViewById(R.id.tv_order_weight);
            holder.tv_org_price = (TextView) convertView.findViewById(R.id.tv_org_price);
            //设置产品付款价
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.tv_order_volume = (TextView) convertView.findViewById(R.id.tv_order_volume);
            holder.tv_total_price = (TextView) convertView.findViewById(R.id.tv_total_price);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String productName = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPRODUCT_NAME());
        holder.tv_productName.setText(productName);

        String productNumber = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPRODUCT_NO());
        holder.tv_product_no.setText(productNumber);

        String orderQTY = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPO_QTY() + orderDetail.getPO_UOM());
        holder.tv_order_qty.setText(orderQTY);

        String orderWeight = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPO_WEIGHT());
        holder.tv_order_weight.setText(orderWeight + "吨");

        String oryPrice = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(String.valueOf(orderDetail.getORG_PRICE()));
        holder.tv_org_price.setText("￥" + oryPrice);

        String actPrice = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(String.valueOf(orderDetail.getACT_PRICE()));
        holder.tv_act_price.setText("￥" + actPrice);

        String orderVolume = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(orderDetail.getPO_VOLUME());
        holder.tv_order_volume.setText(orderVolume + "m³");

        String totalPrice = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(String.valueOf(DoubleArithUtil.mul(orderDetail.getPO_QTY() , orderDetail.getACT_PRICE())));
        holder.tv_total_price.setText("￥" + totalPrice); // 总价

        Picasso.with(mContext).load(URLCostant.LOA_URL+orderDetail.getPRODUCT_URL())
                .error(R.drawable.ic_gift).into(holder.iv_product);

        return convertView;
    }

    class Holder {
        TextView tv_productName, tv_order_qty, tv_order_weight, tv_order_volume,
                tv_org_price, tv_act_price, tv_total_price, tv_product_no;

        ImageView iv_product;
    }


}
