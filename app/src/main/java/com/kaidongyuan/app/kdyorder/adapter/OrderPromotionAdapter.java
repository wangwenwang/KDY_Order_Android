package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 促销产品列表 adapter
 */
public class OrderPromotionAdapter extends BaseAdapter {

	private Context mContext;
	List<PromotionDetail> mPromotionDetails;


	public OrderPromotionAdapter(Context mContext, List<PromotionDetail> promotionDetails) {
		this.mContext = mContext;
		this.mPromotionDetails = promotionDetails==null? new ArrayList<PromotionDetail>():promotionDetails;
	}

	public void notifyChange(List<PromotionDetail> promotionDetails) {
		this.mPromotionDetails = promotionDetails==null? new ArrayList<PromotionDetail>():promotionDetails;
		this.notifyDataSetChanged();
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
		ViewHolder holder;
		final PromotionDetail promotionDetail = mPromotionDetails.get(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_promotion, null);
			holder = new ViewHolder();
			holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
			holder.tv_promotion_name = (TextView) convertView.findViewById(R.id.tv_promotion_name);
			holder.tv_promotion_count = (TextView) convertView.findViewById(R.id.tv_promotion_count);
			holder.tv_promotion_price = (TextView) convertView.findViewById(R.id.tv_promotion_price);
			holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_product_name.setText(promotionDetail.PRODUCT_NAME);
		if (promotionDetail.SALE_REMARK == null || promotionDetail.SALE_REMARK.equals("")){
			holder.tv_promotion_name.setVisibility(View.GONE);
		}else {
			holder.tv_promotion_name.setVisibility(View.VISIBLE);
			holder.tv_promotion_name.setText(promotionDetail.SALE_REMARK);
		}
		holder.tv_promotion_count.setText(promotionDetail.PO_QTY+"");
		holder.tv_promotion_price.setText("原价:" + promotionDetail.ORG_PRICE + "\t\t现价:" + promotionDetail.ACT_PRICE);
		Picasso.with(mContext).load(URLCostant.LOA_URL+promotionDetail.PRODUCT_URL).error(R.drawable.ic_gift).into(holder.iv_product);
		return convertView;
	}
	class ViewHolder{
		TextView tv_product_name, tv_promotion_name, tv_promotion_price;
		TextView tv_promotion_count;
		ImageView iv_product;
	}

}
