package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.ScanfProductNumberDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * popupwindow 产品列表的listview适配器
 * 逻辑写的比较复杂
 */
public class ChoicedProductAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<Product> mData;
    private choicedProductAdapterInterface mInterfae;
    private ScanfProductNumberDialog mInputDialog;
    /**
     * 用户输入的下单数量
     */
    private int mInputCountToIndex = 0;

    public ChoicedProductAdapter(Context context, ArrayList<Product> selectedProductList) {
        this.mContext = context;
        this.mData = selectedProductList==null? new ArrayList<Product>():selectedProductList;
    }

    public void notifyChange(List<Product> selectedProductList) {
        this.mData = selectedProductList==null? new ArrayList<Product>():selectedProductList;
        Logger.w(selectedProductList.toString());
        notifyDataSetChanged();
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
        Viewholder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_choiced_product, null);
            holder = new Viewholder();
            holder.textViewProductName = (TextView) convertView.findViewById(R.id.tv_productName_choiced);
            holder.imageViewDelete = (ImageView) convertView.findViewById(R.id.iv_delete_choiced);
            holder.imageViewAdd = (ImageView) convertView.findViewById(R.id.iv_add_choiced);
            holder.textViewProductCount = (TextView) convertView.findViewById(R.id.tv_count_choiced);
            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        final Product product = mData.get(position);

        //产品名称和规格用一个 TextView 显示
        String productName = StringUtils.getProductName(product.getPRODUCT_NAME()) + "    " + StringUtils.getProductStyle(product.getPRODUCT_TYPE());
        holder.textViewProductName.setText(CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(productName));
        holder.textViewProductCount.setText(String.valueOf(product.getCHOICED_SIZE()));
        holder.textViewProductCount.setTag(position);
        holder.textViewProductCount.setOnClickListener(this);
        holder.imageViewAdd.setTag(position);
        holder.imageViewAdd.setOnClickListener(this);
        holder.imageViewDelete.setTag(position);
        holder.imageViewDelete.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView && mInterfae != null) {
            int tag = (int) v.getTag();
            Logger.w(tag+"");
            switch (v.getId()) {
                case R.id.iv_add_choiced:
                    mInterfae.addProduct(tag);
                    break;
                case R.id.iv_delete_choiced:
                    mInterfae.deleteProduct(tag);
                    break;
            }
        } else if (v instanceof TextView && mInterfae != null) {
            switch (v.getId()) {
                case R.id.tv_count_choiced:
                    if (mInputDialog == null) {
                        showInputDialog();
                    }
                    mInputCountToIndex = (int) v.getTag();
                    mInputDialog.show();
                    break;
            }
        }
    }

    /**
     * 显示输入 Dialog
     */
    private void showInputDialog() {
        if (mInputDialog == null) {
            mInputDialog = new ScanfProductNumberDialog(mContext);
        }
        mInputDialog.setInterface(new ScanfProductNumberDialog.ScanfProductNumberDialogInterface() {
            @Override
            public void pressConfrimButton(int inputNumber) {
                mInterfae.setProductCount(mInputCountToIndex, inputNumber);
            }
        });
        mInputDialog.show();
    }

    class Viewholder {
        TextView textViewProductName;
//        TextView textViewProductStyle;
        TextView textViewProductCount;
        ImageView imageViewAdd;
        ImageView imageViewDelete;
    }

    /**
     * Created by Administrator on 2016/4/21.
     * 添加赠品 Adapter 的回调接口
     */
    public interface choicedProductAdapterInterface {

        /**
         * 单个增加商品
         *
         * @param dataIndex 产品列表中的位置
         */
        void addProduct(int dataIndex);

        /**
         * 单个减少商品
         *
         * @param dataIndex 产品列表中的位置
         */
        void deleteProduct(int dataIndex);

        /**
         * 直接设置赠品的商品
         *
         * @param dataIndex 产品列表中的位置
         * @param giftCount 赠品数量
         */
        void setProductCount(int dataIndex, int giftCount);

    }

    /**
     * 设置接口
     *
     * @param orderProductAdapterInterface 回调接口
     */
    public void setInterface(choicedProductAdapterInterface orderProductAdapterInterface) {
        this.mInterfae = orderProductAdapterInterface;
    }

}
