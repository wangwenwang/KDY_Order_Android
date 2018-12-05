package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.OutPutToAddress;
import com.kaidongyuan.app.kdyorder.bean.Party;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户列表的listview适配器
 */
public class OutToAddressListAdapter extends BaseAdapter {

	private Context mContext;
	List<OutPutToAddress> partyList;

	public OutToAddressListAdapter(Context mContext, List<OutPutToAddress> partyList) {
		this.mContext = mContext;
		this.partyList = partyList==null? new ArrayList<OutPutToAddress>():partyList;
	}

	public void notifyChange(List<OutPutToAddress> partyList){
		this.partyList = partyList==null? new ArrayList<OutPutToAddress>():partyList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return partyList.size();
	}

	@Override
	public Object getItem(int position) {
		return partyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final OutPutToAddress party = partyList.get(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_toaddress_list, null);
			holder = new ViewHolder();
			holder.tv_party_name = (TextView) convertView.findViewById(R.id.tv_party_name);
			holder.tv_party_address = (TextView) convertView.findViewById(R.id.tv_party_address);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		String partyName = party.getPARTY_NAME();
		if (partyName==null || partyName.length()<=0) {
			partyName = "暂无设置";
		}
		holder.tv_party_name.setText(String.valueOf(partyName));

		String toaddress = party.getADDRESS_INFO();
		if (toaddress==null || toaddress.length()<=0){
			toaddress = "暂无设置";
		}
		holder.tv_party_address.setText(String.valueOf(toaddress));

		return convertView;
	}
	class ViewHolder{
		TextView tv_party_name, tv_party_address, tv_party_type, tv_party_city;
	}
}
