package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.ProductPolicy;
import com.kaidongyuan.app.kdyorder.bean.StockProduct;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.MakeAppStockActivity;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.widget.MyListView;
import com.kaidongyuan.app.kdyorder.widget.ScanfProductNumberDialog;
import com.kaidongyuan.app.kdyorder.widget.StockProductCountDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 * 产品列表适配器
 */
public class StockProductAdapter extends BaseAdapter implements View.OnClickListener {

    private MakeAppStockActivity mActivity;
    private List<StockProduct> mProducts;
    private List<StockProduct> mChoicedProducts;

    /**
     * 回调接口
     */
    private StockProductAdapterInterface mInterfae;
    /**
     * 用户输入的数量
     */
    private StockProduct mInputStockProduct;
    /**
     * 用户输入下单数量的 Dialog
     */
    private StockProductCountDialog mInputDialog;

    public StockProductAdapter(MakeAppStockActivity mActivity, List<StockProduct> products, List<StockProduct> choicedProduct) {
        this.mActivity = mActivity;
        this.mProducts = products == null ? new ArrayList<StockProduct>() : products;
        this.mChoicedProducts = choicedProduct == null ? new ArrayList<StockProduct>() : choicedProduct;
    }

    public void notifyChange(List<StockProduct> products, List<StockProduct> choicedProduct) {
        this.mProducts = products == null ? new ArrayList<StockProduct>() : products;
        this.mChoicedProducts = choicedProduct == null ? new ArrayList<StockProduct>() : choicedProduct;
        this.notifyDataSetChanged();
    }




    @Override
    public void onClick(View v) {
        if (v instanceof ImageView && mInterfae != null) {

            switch (v.getId()) {
                case R.id.iv_add:
                    showInputDialog();
                    mInputStockProduct= (StockProduct) v.getTag();
                    break;
                case R.id.iv_delete:
                    mInputStockProduct= (StockProduct) v.getTag();
                    mInterfae.deleteProduct(mInputStockProduct);
                    break;
            }
        }
    }

    /**
     * 显示输入 Dialog
     */
    private void showInputDialog() {
        if (mInputDialog == null) {
            mInputDialog = new StockProductCountDialog(mActivity,mActivity);
        }
        mInputDialog.setInterface(new StockProductCountDialog.ScanfProductNumberDialogInterface() {
            @Override
            public void pressConfrimButton(int inputNumber,Date mdate) {
                mInterfae.setProductCount(mInputStockProduct, inputNumber,mdate);
            }
        });
        mInputDialog.show();
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView==null){
            groupViewHolder = new GroupViewHolder();
            convertView = View.inflate(mActivity, R.layout.item_stockproduct_list, null);
            groupViewHolder.textViewProductName = (TextView) convertView.findViewById(R.id.tv_productName);
            groupViewHolder.textViewProductStyle = (TextView) convertView.findViewById(R.id.tv_product_style);
            groupViewHolder.ll_stockproduct_count= (MyListView) convertView.findViewById(R.id.ll_stockproduct_count);
            groupViewHolder.imageViewAdd= (ImageView) convertView.findViewById(R.id.iv_add);
            groupViewHolder.imageViewDelete= (ImageView) convertView.findViewById(R.id.iv_delete);
            groupViewHolder.imageViewProduct= (ImageView) convertView.findViewById(R.id.iv_product);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder= (GroupViewHolder) convertView.getTag();
        }
         StockProduct product=mProducts.get(position);
          String name=product.getPRODUCT_NAME();
          groupViewHolder.textViewProductName.setText(StringUtils.getProductName(name));
          groupViewHolder.textViewProductStyle.setText(StringUtils.getProductStyle(name));
        //显示图片
        String productImgUrl = product.getPRODUCT_URL();
        if (TextUtils.isEmpty(productImgUrl)) {
            groupViewHolder.imageViewProduct.setImageResource(R.drawable.ic_gift);
        } else {
            productImgUrl = URLCostant.LOA_URL + productImgUrl;
            Picasso.with(mActivity).load(productImgUrl).error(R.drawable.ic_gift).fit().into(groupViewHolder.imageViewProduct);
        }

        StockProductCountAdapter productCountAdapter=new StockProductCountAdapter(null,mActivity);
        groupViewHolder.ll_stockproduct_count.setAdapter(productCountAdapter);
        int productIndexInChoicedProductList = getChoiceProductIndexInChoicedProductList(product);
        if (productIndexInChoicedProductList != -1) {
            product = mChoicedProducts.get(productIndexInChoicedProductList);
        }
        //设置监听
        groupViewHolder.imageViewAdd.setTag(product);
        groupViewHolder.imageViewAdd.setOnClickListener(this);
        groupViewHolder.imageViewDelete.setTag(product);
        groupViewHolder.imageViewDelete.setOnClickListener(this);
        productCountAdapter.setStockProductCounts(product.getPRODUCT_POLICY());
        return convertView;
    }

    private class GroupViewHolder {
        TextView textViewProductName;
        TextView textViewProductStyle;
        ImageView imageViewProduct;
        ImageView imageViewDelete;
        ImageView imageViewAdd;
        ListView  ll_stockproduct_count;

    }

    private class ChildViewHolder {
        TextView textViewChild;//库存数量
        TextView textViewTime;//生产日期
    }

    /**
     * 获取产品在已选产品集合中的位置
     *
     * @param choiceProduct 需要查询的商品
     * @return 在选择集合中的位置，没有则返回 -1
     */
    private int getChoiceProductIndexInChoicedProductList(StockProduct choiceProduct) {
        try {
            int index = -1;
            int size = mChoicedProducts.size();
            StockProduct productInChoicedList;
            for (int i = 0; i < size; i++) {//根据产品的 IDX 去查找产品在集合中的位置
                productInChoicedList = mChoicedProducts.get(i);
                if (choiceProduct.getIDX() == productInChoicedList.getIDX()) {
                    index = i;
                }
            }
            return index;
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * Created by Administrator on 2016/4/21.
     * 添加赠品 Adapter 的回调接口
     */
    public interface StockProductAdapterInterface {

        /**
         * 单个增加商品
         *
         * @param dataIndex 产品列表中的位置
         */
        void addProduct(int dataIndex);

        /**
         * 单个减少商品
         *
         * @param product 产品
         */
        void deleteProduct(StockProduct product);

        /**
         * 直接设置赠品的商品
         *
         * @param product 产品
         * @param Count 数量
         */
        void setProductCount(StockProduct product, int Count,Date date);

    }

    /**
     * 设置接口
     * @param stockProductAdapterInterface 回调接口
     */
    public void setInterface(StockProductAdapterInterface stockProductAdapterInterface) {
        this.mInterfae = stockProductAdapterInterface;
    }

}










