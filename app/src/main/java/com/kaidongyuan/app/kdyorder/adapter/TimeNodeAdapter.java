package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.StateTack;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间节点的listView适配器
 * 
 * @author ke
 * 
 */
public class TimeNodeAdapter extends BaseAdapter {

	private Context mContext;
	private List<StateTack> stateTackList;

	public TimeNodeAdapter(Context mContext, List<StateTack> stateTackList) {
		this.stateTackList = stateTackList==null? new ArrayList<StateTack>():stateTackList;
		this.mContext = mContext;
	}
	public void notifyChange(List<StateTack> stateTackList) {
		this.stateTackList = stateTackList==null? new ArrayList<StateTack>():stateTackList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return stateTackList.size();
	}
	@Override
	public Object getItem(int position) {
		return stateTackList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_time_node, null);
			holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		StateTack bean = stateTackList.get(position);

		String stateTime = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(bean.getSTATE_TIME());
		holder.tv_time.setText(stateTime);

		String orderState = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(StringUtils.getOrderState(bean.getORDER_STATE()));
		holder.tv_state.setText(orderState);

		return convertView;
	}

	static class Holder {
		private TextView tv_state;
		private TextView tv_time;
	}
}
