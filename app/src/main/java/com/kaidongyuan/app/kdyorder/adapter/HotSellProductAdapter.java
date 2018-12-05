package com.kaidongyuan.app.kdyorder.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品列表的listview适配器
 * 
 */
public class HotSellProductAdapter extends BaseAdapter {

	private Context mContext;
	List<Product> mProductList;


	public HotSellProductAdapter(Context mContext, List<Product> productList) {
		this.mContext = mContext;
		this.mProductList = productList==null? new ArrayList<Product>():productList;
	}


	public void notifyChange(List<Product> productList){
		this.mProductList = productList==null? new ArrayList<Product>():productList;
		this.notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return mProductList.size();
	}

	@Override
	public Object getItem(int position) {
		return mProductList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Product product = mProductList.get(position);
		if (convertView == null){
			convertView = View.inflate(mContext, R.layout.item_hot_sell_product_list, null);
			holder = new ViewHolder();
			holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
			holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
			holder.tv_product_style = (TextView) convertView.findViewById(R.id.tv_product_style);
			holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_origin_price);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		String name = product.getPRODUCT_NAME();

		String productName = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getProductName(name));
		holder.tv_productName.setText(productName);

		String productStyle = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getProductStyle(name));
		holder.tv_product_style.setText(productStyle);

		String originPrice = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(String.valueOf(product.getPRODUCT_PRICE()));
		holder.tv_origin_price.setText("￥" + originPrice);

		Picasso.with(mContext).load(URLCostant.LOA_URL + product.getPRODUCT_URL()).fit()
				.error(R.drawable.ic_gift).into(holder.iv_product);

		return convertView;
	}

	class ViewHolder{
		TextView tv_productName, tv_product_style;
		TextView tv_origin_price;
		ImageView iv_product;
	}
}
