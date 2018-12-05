package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.PreOrderConfirmActivity;
import com.kaidongyuan.app.kdyorder.widget.ScanfProductNumberDialog;
import com.kaidongyuan.app.kdyorder.widget.ScanfProductPriceDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 * 确认订单产品列表适配器
 */
public class OrderProductDetailAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<PromotionDetail> mPromotionDetails;
    /**
     * 调价回调接口
     */
    private OrderProductDetailAdapterModifyPriceInterface mInterface;
    private int mInputCountToIndex;
    private ScanfProductPriceDialog mInputDialog;

    public OrderProductDetailAdapter(Context mContext, List<PromotionDetail> promotionDetails) {
        this.mContext = mContext;
        this.mPromotionDetails = promotionDetails == null ? new ArrayList<PromotionDetail>() : promotionDetails;
    }

    public void notifyChange(List<PromotionDetail> promotionDetails) {
        this.mPromotionDetails = promotionDetails == null ? new ArrayList<PromotionDetail>() : promotionDetails;
        this.notifyDataSetChanged();
    }

    public List<PromotionDetail> getmPromotionDetails() {
        return mPromotionDetails;
    }

    @Override
    public int getCount() {
        return mPromotionDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mPromotionDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final PromotionDetail promotionDetail = mPromotionDetails.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_productdetail, null);
            holder = new ViewHolder();
            holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tv_promotion_name = (TextView) convertView.findViewById(R.id.tv_promotion_name);
            holder.tv_promotion_count = (TextView) convertView.findViewById(R.id.tv_promotion_count);
            holder.tv_promotion_price = (TextView) convertView.findViewById(R.id.tv_promotion_price);
            holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
            holder.iv_deleteprice = (ImageView) convertView.findViewById(R.id.iv_deleteprice);
            holder.iv_addprice = (ImageView) convertView.findViewById(R.id.iv_addprice);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.ed_pricecount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_product_name.setText(promotionDetail.PRODUCT_NAME);
        if (promotionDetail.SALE_REMARK == null || promotionDetail.SALE_REMARK.equals("")) {
            holder.tv_promotion_name.setVisibility(View.GONE);
        } else {
            holder.tv_promotion_name.setVisibility(View.VISIBLE);
            holder.tv_promotion_name.setText(promotionDetail.SALE_REMARK);
        }
        holder.tv_promotion_count.setText(promotionDetail.PO_QTY + "");
        holder.tv_promotion_price.setText("原价:" + promotionDetail.ORG_PRICE);
        holder.tv_act_price.setText(promotionDetail.ACT_PRICE + "");
        if (mContext instanceof PreOrderConfirmActivity){
            holder.iv_addprice.setVisibility(View.GONE);
            holder.iv_deleteprice.setVisibility(View.GONE);
            holder.tv_act_price.setBackgroundColor(Color.TRANSPARENT);
        }else if (BusinessConstants.CAN_MODIFY_PRICE.equals(promotionDetail.LOTTABLE06)) {
            holder.iv_addprice.setVisibility(View.VISIBLE);
            holder.iv_deleteprice.setVisibility(View.VISIBLE);
            setOnclik(holder, position);
        } else {
            holder.iv_addprice.setVisibility(View.GONE);
            holder.iv_deleteprice.setVisibility(View.GONE);
            holder.tv_act_price.setBackgroundColor(Color.TRANSPARENT);
        }
        String ivUrl = promotionDetail.PRODUCT_URL;
        if (TextUtils.isEmpty(ivUrl)) {
            holder.iv_product.setImageResource(R.drawable.ic_gift);
        } else {
            Picasso.with(mContext).load(URLCostant.LOA_URL + ivUrl).error(R.drawable.ic_gift).into(holder.iv_product);
        }
        return convertView;
    }

    /**
     * 设置调价的监听方法
     *
     * @param holder   ViewHolder
     * @param position 当前 Item 在集合中的位置
     */
    private void setOnclik(ViewHolder holder, int position) {
        holder.iv_addprice.setTag(position);
        holder.iv_deleteprice.setTag(position);
        holder.tv_act_price.setTag(position);
        holder.iv_addprice.setOnClickListener(this);
        holder.iv_deleteprice.setOnClickListener(this);
        holder.tv_act_price.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView && mInterface != null) {
            int tag = (int) v.getTag();
            switch (v.getId()) {
                case R.id.iv_addprice:
                    mInterface.raisePrice(tag);
                    break;
                case R.id.iv_deleteprice:
                    mInterface.cutPrice(tag);
                    break;
            }
        } else if (v instanceof TextView) {
            switch (v.getId()) {
                case R.id.ed_pricecount:
                    mInputCountToIndex = (int) v.getTag();
                    createDialog();
                    mInputDialog.show();
                    break;
            }
        }
    }

    /**
     * 手动输入数量的 Dialog
     */
    private void createDialog() {
        PromotionDetail promotionDetail = mPromotionDetails.get(mInputCountToIndex);
        mInputDialog = new ScanfProductPriceDialog(mContext, String.valueOf(promotionDetail.LOTTABLE12),
                String.valueOf(promotionDetail.LOTTABLE13));
        mInputDialog.setInterface(new ScanfProductPriceDialog.ScanfProductPriceDialogInterface() {
            @Override
            public void pressConfrimButton(double inputPrice) {
                if (mInterface != null) {
                    mInterface.setProductPrice(mInputCountToIndex, inputPrice);
                }
            }
        });
        mInputDialog.show();
    }

    class ViewHolder {
        TextView tv_product_name, tv_promotion_name, tv_promotion_price;
        TextView tv_promotion_count;
        ImageView iv_product;
        ImageView iv_deleteprice;
        ImageView iv_addprice;
        TextView tv_act_price;
    }

    /**
     * Created by Administrator on 2016/4/21.
     * 产品调价的接口
     */
    public interface OrderProductDetailAdapterModifyPriceInterface {

        /**
         * 上调价格 0.1 元
         *
         * @param dataIndex 产品列表中的位置
         */
        void raisePrice(int dataIndex);

        /**
         * 下调价格 0.1 元
         *
         * @param dataIndex 产品列表中的位置
         */
        void cutPrice(int dataIndex);

        /**
         * 直接设置产品价格
         *
         * @param dataIndex 产品列表中的位置
         * @param pirce     输入的产品价格
         */
        void setProductPrice(int dataIndex, double pirce);
    }

    /**
     * 设置回调接口
     *
     * @param modifyPriceInterface 产品调价的接口
     */
    public void setInterface(OrderProductDetailAdapterModifyPriceInterface modifyPriceInterface) {
        this.mInterface = modifyPriceInterface;
    }

}
















