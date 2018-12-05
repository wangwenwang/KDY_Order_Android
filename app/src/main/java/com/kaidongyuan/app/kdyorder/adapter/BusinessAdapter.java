package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 * 用户业务类型 Adapter
 */
public class BusinessAdapter extends BaseAdapter {

    private List<Business> mData;
    private Context mContext;

    public BusinessAdapter(List<Business> data, Context context) {
        this.mData = data==null? new ArrayList<Business>():data;
        this.mContext = context;
    }

    /**
     * 刷新 ListView
     * @param data 数据
     */
    public void notifyChange(List<Business> data) {
        this.mData = data==null? new ArrayList<Business>():data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Business getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_business_listview, null);
            holder.tvBusinessType = (TextView) convertView.findViewById(R.id.textView_bussinessName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String businessName = mData.get(position).getBUSINESS_NAME();
        if (TextUtils.isEmpty(businessName)) {
            businessName = MyApplication.getmRes().getString(R.string.no_set);
        }
        holder.tvBusinessType.setText(businessName);

        return convertView;
    }

    private class ViewHolder {
        TextView tvBusinessType;
    }
}



















