package com.kaidongyuan.app.kdyorder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kaidongyuan.app.kdyorder.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/11.
 */

public class YBMyAddrBlsItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<YBMyAddrBlsItemBean> list=new ArrayList<>();

    public YBMyAddrBlsItemAdapter(Context context) {
        this.context = context;

    }
    public void setDatas(ArrayList<YBMyAddrBlsItemBean> list){
        this.list=list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.yb_my_addr_bls_item_adapter, null);
            viewHold = new ViewHold();

            viewHold.addrDetail = (TextView) convertView.findViewById(R.id.yb_my_addr_item_addr_detail);
            viewHold.addrname = (TextView) convertView.findViewById(R.id.yb_my_addr_item_addr_name);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.addrDetail.setText(list.get(position).getAddrDetail());
        viewHold.addrname.setText(list.get(position).getAddrName());

        return convertView;
    }
    ViewHold viewHold ;

    private static class ViewHold {
        private TextView addrDetail;
        private TextView addrname;
    }
}
