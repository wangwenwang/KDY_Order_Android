package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/28.
 * 客户拜访供货商列表 Adapter
 */
public class FirstPartyChoiceAdapter extends BaseAdapter {
    private List<CustomerMeeting> mData;
    private Context mContext;

    public FirstPartyChoiceAdapter(List<CustomerMeeting> data, Context context) {
        this.mData = data==null? new ArrayList<CustomerMeeting>():data;
        this.mContext = context;
    }

    /**
     * 刷新 ListView
     * @param data 数据
     */
    public void notifyChange(List<CustomerMeeting> data) {
        this.mData = data==null? new ArrayList<CustomerMeeting>():data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CustomerMeeting getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FirstPartyChoiceAdapter.ViewHolder holder;
        if (convertView==null) {
            holder = new FirstPartyChoiceAdapter.ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_business_listview, null);
            holder.tvChartName = (TextView) convertView.findViewById(R.id.textView_bussinessName);
            convertView.setTag(holder);
        } else {
            holder = (FirstPartyChoiceAdapter.ViewHolder) convertView.getTag();
        }

        String chartName = mData.get(position).getPARTY_NAME();
        if (TextUtils.isEmpty(chartName)) {
            chartName = MyApplication.getmRes().getString(R.string.no_set);
        }
        holder.tvChartName.setText(chartName);

        return convertView;
    }

    private class ViewHolder {
        TextView tvChartName;
    }
}
