package com.kaidongyuan.app.kdyorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.NormalAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/1.
 */
public class ProvincesAdapter extends BaseAdapter {
    List<NormalAddress> addresses;
    @Override
    public int getCount() {
        if (addresses!=null){
            return addresses.size();
        }else {
            return 0;
        }
    }
    public void setData(List<NormalAddress> addresses){
        this.addresses=addresses==null?new ArrayList<NormalAddress>():addresses;
        notifyDataSetChanged();
    }
    @Override
    public Object getItem(int i) {
        return addresses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder holder=null;
        if (view==null){
            view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_simple_str,viewGroup,false);
            holder=new MyViewHolder();
            holder.tv_simple_str= (TextView) view.findViewById(R.id.tv_simple_str);
            view.setTag(holder);
        }else {
            holder= (MyViewHolder) view.getTag();
        }
        holder.tv_simple_str.setText(addresses.get(i).getITEM_NAME());
        return view;
    }

    class MyViewHolder{
        public TextView tv_simple_str;
    }

}
