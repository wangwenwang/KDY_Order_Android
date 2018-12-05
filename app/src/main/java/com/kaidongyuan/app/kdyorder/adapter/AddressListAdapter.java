package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户列表的listview适配器
 * 
 */
public class AddressListAdapter extends BaseAdapter {

	private Context mContext;
	List<Address> addressList;

	public AddressListAdapter(Context mContext, List<Address> addressList) {
		this.mContext = mContext;
		this.addressList = addressList==null? new ArrayList<Address>():addressList;
	}

	public void notifyChange(List<Address> addressList){
		this.addressList = addressList==null? new ArrayList<Address>():addressList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return addressList.size();
	}

	@Override
	public Object getItem(int position) {
		return addressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Address address = addressList.get(position);
		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_customeraddress_list, null);
			holder = new ViewHolder();
			holder.tv_contact_name = (TextView) convertView.findViewById(R.id.tv_contact_name);
			holder.tv_contact_phone = (TextView) convertView.findViewById(R.id.tv_contact_phone);
			holder.tv_address_code = (TextView) convertView.findViewById(R.id.tv_address_code);
			holder.tv_address_detail = (TextView) convertView.findViewById(R.id.tv_address_detail);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		String noSet = MyApplication.getmRes().getString(R.string.no_set);
		String contactPerson = address.getCONTACT_PERSON();
		if (TextUtils.isEmpty(contactPerson)) {
			contactPerson = noSet;
		}
		holder.tv_contact_name.setText(contactPerson);

		String contactTel = address.getCONTACT_TEL();
		if (TextUtils.isEmpty(contactTel)) {
			contactTel = noSet;
		}
		holder.tv_contact_phone.setText(contactTel);

		String addressCode = address.getADDRESS_CODE();
		if (TextUtils.isEmpty(addressCode)) {
			addressCode = noSet;
		}
		holder.tv_address_code.setText(addressCode);

		String addressInfo = address.getADDRESS_INFO();
		if (TextUtils.isEmpty(addressInfo)) {
			addressInfo = noSet;
		}
		holder.tv_address_detail.setText(addressInfo);

		return convertView;
	}

	private class ViewHolder{
		TextView tv_contact_name, tv_contact_phone, tv_address_code, tv_address_detail;
	}
}
