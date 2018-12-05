package com.kaidongyuan.app.kdyorder.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/16.
 * 查单界面 Viewpager 适配器
 */
public class CheckOrderFragmentViewpagerAdapter extends PagerAdapter {

    private List<View> mData;

    public CheckOrderFragmentViewpagerAdapter(List<View> data){
        this.mData = data==null? new ArrayList<View>():data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mData.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mData.get(position);
        container.addView(view);
        return view;
    }

}



















