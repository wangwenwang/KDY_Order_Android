package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.widget.ScanfProductNumberDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 * 已选赠品信息详情
 */
public class OrderGiftChoiceDetialAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<PromotionDetail> mData;
    private OrderGiftDetialAdapterInterface mDetialInterface;

    private int mInputCountToIndex;
    private ScanfProductNumberDialog mInputDialog;

    public OrderGiftChoiceDetialAdapter(Context context, List<PromotionDetail> data, OrderGiftDetialAdapterInterface detialInterface) {
        this.mContext = context;
        this.mData = data==null? new ArrayList<PromotionDetail>():data;
        if (detialInterface!=null) {
            this.mDetialInterface = detialInterface;
        }
    }

    public void notifyChange(List<PromotionDetail> data){
        this.mData = data==null? new ArrayList<PromotionDetail>():data;
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
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_order_giftchoice, null);
            holder.tvChoiceGiftName = (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tvChoiceGiftStyle = (TextView) convertView.findViewById(R.id.tv_productStyle);
            holder.tvChoiceGiftSize = (TextView) convertView.findViewById(R.id.tv_giftsize);
            holder.btAdd = (Button) convertView.findViewById(R.id.bt_add);
            holder.btDelete = (Button) convertView.findViewById(R.id.bt_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvChoiceGiftSize.setTag(position);
        holder.tvChoiceGiftSize.setOnClickListener(this);
        holder.btAdd.setTag(position);
        holder.btAdd.setOnClickListener(this);
        holder.btDelete.setTag(position);
        holder.btDelete.setOnClickListener(this);

        PromotionDetail promotionDetail = mData.get(position);
        String giftName = promotionDetail.PRODUCT_NAME;
        holder.tvChoiceGiftName.setText(String.valueOf(StringUtils.getProductName(giftName)));
        holder.tvChoiceGiftStyle.setText(String.valueOf(StringUtils.getProductStyle(giftName)));
        holder.tvChoiceGiftSize.setText(String.valueOf(promotionDetail.PO_QTY));

        return convertView;
    }

    private class ViewHolder {
        TextView tvChoiceGiftName;
        TextView tvChoiceGiftStyle;
        TextView tvChoiceGiftSize;
        Button btAdd;
        Button btDelete;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button && mDetialInterface!=null) {
            int tag = (int) v.getTag();
            switch (v.getId()) {
                case R.id.bt_add:
                    mDetialInterface.addGift(tag);
                    break;
                case R.id.bt_delete:
                    mDetialInterface.deleteGift(tag);
                    break;
            }
        } else if (v instanceof TextView){
            switch (v.getId()) {
                case R.id.tv_giftsize:
                    if (mInputDialog==null) {
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
    private void createDialog(){
        if (mInputDialog==null) {
            mInputDialog = new ScanfProductNumberDialog(mContext);
            mInputDialog.setInterface(new ScanfProductNumberDialog.ScanfProductNumberDialogInterface() {
                @Override
                public void pressConfrimButton(int inputNumber) {
                    if (mDetialInterface!=null) {
                        mDetialInterface.setGiftCount(mInputCountToIndex, inputNumber);
                    }
                }
            });
        }
        mInputDialog.show();
    }

    /**
     * Created by Administrator on 2016/4/22.
     * 已选赠品 Adapter 处理点击事件接口
     */
    public interface OrderGiftDetialAdapterInterface {

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
















