package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知列表adapter
 */
public class InformationAdapter extends BaseAdapter {

	private Context mContext;
	List<Information> mInformations;

	public InformationAdapter(Context mContext, List<Information> informations) {
		this.mContext = mContext;
		this.mInformations = informations==null? new ArrayList<Information>():informations;
	}

	public void notifyChange(List<Information> informations){
		this.mInformations = informations==null? new ArrayList<Information>():informations;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mInformations.size();
	}

	@Override
	public Object getItem(int position) {
		return mInformations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Information information = mInformations.get(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_newest_information, null);
			holder = new ViewHolder();
			holder.iv_notify = (ImageView) convertView.findViewById(R.id.iv_notify);
			holder.tv_notify_title = (TextView) convertView.findViewById(R.id.tv_notify_title);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_notify_title.setText(information.getITitle());
		Picasso.with(mContext).load(URLCostant.INFORMATION_PICTURE_URL+information.getIImage())
				.fit().error(R.drawable.ic_information_picture).into(holder.iv_notify);
		return convertView;
	}
	class ViewHolder{
		TextView tv_notify_title;
		ImageView iv_notify;
	}
}
