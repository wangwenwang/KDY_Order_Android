package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.CustomerMeeting;
import com.kaidongyuan.app.kdyorder.bean.Information;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.interfaces.OnClickListenerStrInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户拜访列表adapter
 */
public class CustomerMeetingAdapter extends BaseAdapter {

    private Context mContext;
    List<CustomerMeeting> mMeetings;
    OnClickListenerStrInterface onClickListenerStr;

    public CustomerMeetingAdapter(Context mContext, List<CustomerMeeting> mMeetings) {
        this.mContext = mContext;
        this.mMeetings = mMeetings == null ? new ArrayList<CustomerMeeting>() : mMeetings;
    }

    public void setOnClickListenerStr(OnClickListenerStrInterface onClickListenerStr) {
        this.onClickListenerStr = onClickListenerStr;
    }

    public void notifyChange(List<CustomerMeeting> mMeetings) {
        this.mMeetings = mMeetings == null ? new ArrayList<CustomerMeeting>() : mMeetings;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMeetings.size();
    }

    @Override
    public Object getItem(int position) {
        return mMeetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final CustomerMeeting customerMeeting = mMeetings.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_customer_meeting, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_customer_address = (TextView) convertView.findViewById(R.id.tv_customer_address);
            holder.tv_create = (TextView) convertView.findViewById(R.id.tv_create);
            holder.tv_read = (TextView) convertView.findViewById(R.id.tv_read);
            holder.tv_write = (TextView) convertView.findViewById(R.id.tv_write);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(customerMeeting.getCONTACTS());
        holder.tv_phone.setText(customerMeeting.getCONTACTS_TEL());
        holder.tv_time.setText(customerMeeting.getVISIT_DATE());
        holder.tv_customer_name.setText(customerMeeting.getPARTY_NAME());
        switch (customerMeeting.getVISIT_STATES()) {
            case "":
                holder.tv_state.setText("未拜访");
                holder.tv_state.setTextColor(mContext.getResources().getColor(R.color.text_color_heavy));
                holder.tv_read.setVisibility(View.GONE);
                holder.tv_write.setVisibility(View.GONE);
                holder.tv_create.setVisibility(View.VISIBLE);
                break;
            case "离店":
                holder.tv_state.setText("已拜访");
                holder.tv_state.setTextColor(mContext.getResources().getColor(R.color.dark_green));
                holder.tv_read.setVisibility(View.VISIBLE);
                holder.tv_write.setVisibility(View.GONE);
                holder.tv_create.setVisibility(View.GONE);
                break;
            default:
                holder.tv_state.setText("拜访中");
                holder.tv_state.setTextColor(mContext.getResources().getColor(R.color.text_color_normal));
                holder.tv_read.setVisibility(View.GONE);
                holder.tv_write.setVisibility(View.VISIBLE);
                holder.tv_create.setVisibility(View.GONE);
                break;
        }
        holder.tv_customer_address.setText(customerMeeting.getPARTY_ADDRESS());
        holder.tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListenerStr.onClick(position, "tv_read");
            }
        });
        holder.tv_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListenerStr.onClick(position, "tv_write");
            }
        });
        holder.tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListenerStr.onClick(position, "tv_create");
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_phone, tv_time, tv_customer_name, tv_state, tv_customer_address, tv_create, tv_write, tv_read;
    }
}
