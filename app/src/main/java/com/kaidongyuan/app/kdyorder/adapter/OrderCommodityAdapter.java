package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.AddGiftActivity;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.widget.ScanfProductNumberDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 * 赠品产品 Adapter
 */
public class OrderCommodityAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<PromotionDetail> mData;
    private List<PromotionDetail> mGiftData;
    private ScanfProductNumberDialog mInputDialog;
    private int mInputCountToIndex;
    private CommodityAdapterInterface mCommodityInterface;

    public OrderCommodityAdapter(Context context, List<PromotionDetail> data, List<PromotionDetail> giftData, CommodityAdapterInterface commodityInterface) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<PromotionDetail>() : data;
        this.mGiftData = giftData == null ? new ArrayList<PromotionDetail>() : giftData;
        if (commodityInterface != null) {
            this.mCommodityInterface = commodityInterface;
        }
    }

    public void notifyChange(List<PromotionDetail> data, List<PromotionDetail> giftData) {
        this.mData = data == null ? new ArrayList<PromotionDetail>() : data;
        this.mGiftData = giftData == null ? new ArrayList<PromotionDetail>() : giftData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_order_commodity, null);
            holder.btAdd = (Button) convertView.findViewById(R.id.bt_add);
            holder.btDelete = (Button) convertView.findViewById(R.id.bt_delete);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tvProductStyle = (TextView) convertView.findViewById(R.id.tv_product_style);
            holder.tvProductStock = (TextView) convertView.findViewById(R.id.tv_product_stock);
            holder.tvShouldCareStock = (TextView) convertView.findViewById(R.id.tv_should_care_stock);
            holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_commodity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btAdd.setTag(position);
        holder.btAdd.setOnClickListener(this);
        holder.btDelete.setTag(position);
        holder.btDelete.setOnClickListener(this);
        holder.tvCount.setTag(position);
        holder.tvCount.setOnClickListener(this);

        PromotionDetail product = mData.get(position);
        String giftName = product.PRODUCT_NAME;

        holder.tvProductName.setText(String.valueOf(StringUtils.getProductName(giftName)));
        holder.tvProductStyle.setText(String.valueOf(StringUtils.getProductStyle(giftName)));
        String ivheadUrl = product.PRODUCT_URL;
        if (ivheadUrl==null) {
            holder.ivHead.setImageResource(R.drawable.ic_gift);
        } else {
            Picasso.with(mContext).load(URLCostant.LOA_URL + ivheadUrl).error(R.drawable.ic_gift).into(holder.ivHead);
        }

        if (AddGiftActivity.NEED_CARE_STOCK.equals(product.LOTTABLE09)) {//true 为需要考虑库存
            holder.tvProductStock.setText(String.valueOf(product.LOTTABLE11));
            holder.tvProductStock.setVisibility(View.VISIBLE);
            holder.tvShouldCareStock.setVisibility(View.VISIBLE);
        }

        int giftIndex = mGiftData.indexOf(product);
        if (giftIndex != -1) {
            holder.tvCount.setText(String.valueOf(mGiftData.get(giftIndex).PO_QTY));
        } else {
            holder.tvCount.setText("0");
        }

        return convertView;
    }

    private class ViewHolder {
        Button btAdd;
        Button btDelete;
        TextView tvCount;
        TextView tvProductName;
        TextView tvProductStyle;
        TextView tvProductStock;
        TextView tvShouldCareStock;
        ImageView ivHead;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button && mCommodityInterface != null) {
            int tag = (int) v.getTag();
            switch (v.getId()) {
                case R.id.bt_add:
                    mCommodityInterface.addGift(tag);
                    break;
                case R.id.bt_delete:
                    mCommodityInterface.deleteGift(tag);
                    break;
            }
        } else if (v instanceof TextView) {
            switch (v.getId()) {
                case R.id.tv_count:
                    if (mInputDialog == null) {
                        createDialog();
                    }
                    mInputCountToIndex = (int) v.getTag();
                    mInputDialog.show();
                    break;
            }
        }
    }

    /**
     * 手动输入数量的 Dialog
     */
    private void createDialog() {
        if (mInputDialog==null) {
            mInputDialog = new ScanfProductNumberDialog(mContext);
            mInputDialog.setInterface(new ScanfProductNumberDialog.ScanfProductNumberDialogInterface() {
                @Override
                public void pressConfrimButton(int inputNumber) {
                    if (mCommodityInterface != null) {
                        mCommodityInterface.setGiftCount(mInputCountToIndex, inputNumber);
                    }
                }
            });
        }
        mInputDialog.show();
    }

    /**
     * Created by Administrator on 2016/4/21.
     * 添加赠品 Adapter 的回调接口
     */
    public interface CommodityAdapterInterface {

        /**
         * 单个增加赠品
         * @param dataIndex 产品列表中的位置
         */
        void addGift(int dataIndex);

        /**
         * 单个减少赠品
         * @param dataIndex 产品列表中的位置
         */
        void deleteGift(int dataIndex);

        /**
         * 直接设置赠品的数量
         * @param dataIndex 产品列表中的位置
         * @param giftCount 赠品数量
         */
        void setGiftCount(int dataIndex, int giftCount);

    }
}














