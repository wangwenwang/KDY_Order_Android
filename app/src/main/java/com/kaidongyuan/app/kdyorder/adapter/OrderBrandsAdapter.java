package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.PayType;
import com.kaidongyuan.app.kdyorder.util.CheckStringEmptyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 * 品牌类型 ListView 的适配器
 */
public class OrderBrandsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;
    /**
     * 当前选择的 item 的位置
     */
    private int mSelectedIndex;

    public OrderBrandsAdapter(Context context, List<String> data, int selectedIndex) {
        this.mContext = context;
        this.mSelectedIndex = selectedIndex;
        this.mData = data == null ? new ArrayList<String>() : data;
    }

    public void notifyChange(List<String> data, int selectedIndex) {
        this.mSelectedIndex = selectedIndex;
        this.mData = data == null ? new ArrayList<String>() : data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_order_brands, null);
            holder.textViewOrderBrand = (TextView) convertView.findViewById(R.id.textView_order_brand);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position==mSelectedIndex) {
            holder.textViewOrderBrand.setBackgroundColor(Color.parseColor("#EEEEEE"));
        } else {
            holder.textViewOrderBrand.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }

        String brand = CheckStringEmptyUtil.checkStringIsEmptyWithNoSet(mData.get(position));
        holder.textViewOrderBrand.setText(brand);

        return convertView;
    }

    private class ViewHolder {
        TextView textViewOrderBrand;
    }

}
