package com.kaidongyuan.app.kdyorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyAddressManageActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyManageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户列表的listview适配器
 */
public class PartyAddressManageListAdapter extends BaseAdapter {

	private PartyAddressManageActivity mActivity;
	List<Address> addresses;

	public PartyAddressManageListAdapter(PartyAddressManageActivity mActivity, List<Address> addresses) {
		this.mActivity = mActivity;
		this.addresses = addresses==null? new ArrayList<Address>():addresses;
	}

	public void notifyChange(List<Address> addresses){
		this.addresses = addresses==null? new ArrayList<Address>():addresses;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return addresses.size();
	}

	@Override
	public Object getItem(int position) {
		return addresses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Address address = addresses.get(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_party_addressmanage_list, null);
			holder = new ViewHolder();
			holder.tv_address_belong = (TextView) convertView.findViewById(R.id.tv_party_name);
			holder.tv_address_addressinfo = (TextView) convertView.findViewById(R.id.tv_address_addressinfo);
			holder.tv_address_person = (TextView) convertView.findViewById(R.id.tv_address_person);
			holder.tv_address_tel = (TextView) convertView.findViewById(R.id.tv_address_tel);
			holder.tv_address_del= (TextView) convertView.findViewById(R.id.tv_address_del);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

//		String partyName = address.get();
//		if (partyName==null || partyName.length()<=0) {
//			partyName = "暂无设置";
//		}
//		holder.tv_party_name.setText(String.valueOf(partyName));

		String addressinfo = address.getADDRESS_INFO();
		if (addressinfo==null || addressinfo.length()<=0){
			addressinfo = "暂无设置";
		}
		holder.tv_address_addressinfo.setText(String.valueOf(addressinfo));

		String addressperson = address.getCONTACT_PERSON();
		if (addressperson==null || addressperson.length()<=0) {
			addressperson = "暂无设置";
		}
		holder.tv_address_person.setText(String.valueOf(addressperson));

		String addresstel = address.getCONTACT_TEL();
		if (addresstel==null || addresstel.length()<=0) {
			addresstel = "暂无设置";
		}
		holder.tv_address_tel.setText(addresstel);


		holder.tv_address_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (addresses==null||addresses.size()<=1){
					mActivity.deletePartyAddressError("须至少保留一个客户地址！");
					return;
				}
				mActivity.deletePartyAddress(address);
			}
		});
		return convertView;
	}
	class ViewHolder{
		TextView tv_address_belong, tv_address_addressinfo, tv_address_person, tv_address_tel,tv_address_del;
	}
}
