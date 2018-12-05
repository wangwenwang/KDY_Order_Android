package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Party;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户列表的listview适配器
 */
public class PartyListAdapter extends BaseAdapter {

	private Context mContext;
	List<Party> partyList;

	public PartyListAdapter(Context mContext, List<Party> partyList) {
		this.mContext = mContext;
		this.partyList = partyList==null? new ArrayList<Party>():partyList;
	}

	public void notifyChange(List<Party> partyList){
		this.partyList = partyList==null? new ArrayList<Party>():partyList;
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
		final Party party = partyList.get(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_party_list, null);
			holder = new ViewHolder();
			holder.tv_party_name = (TextView) convertView.findViewById(R.id.tv_party_name);
			holder.tv_party_code = (TextView) convertView.findViewById(R.id.tv_party_code);
			holder.tv_party_type = (TextView) convertView.findViewById(R.id.tv_party_type);
			holder.tv_party_city = (TextView) convertView.findViewById(R.id.tv_party_city);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		String partyName = party.getPARTY_NAME();
		if (partyName==null || partyName.length()<=0) {
			partyName = "暂无设置";
		}
		holder.tv_party_name.setText(String.valueOf(partyName));

		String partyCode = party.getPARTY_CODE();
		if (partyCode==null || partyCode.length()<=0){
			partyCode = "暂无设置";
		}
		holder.tv_party_code.setText(String.valueOf(partyCode));

		String partyType = party.getPARTY_TYPE();
		if (partyType==null || partyType.length()<=0) {
			partyType = "暂无设置";
		}
		holder.tv_party_type.setText(String.valueOf(partyType));

		String partyCity = party.getPARTY_CITY();
		if (partyCity==null || partyCity.length()<=0) {
			partyCity = "暂无设置";
		}
		holder.tv_party_city.setText(String.valueOf(partyCity));
		return convertView;
	}
	class ViewHolder{
		TextView tv_party_name, tv_party_code, tv_party_type, tv_party_city;
	}
}
