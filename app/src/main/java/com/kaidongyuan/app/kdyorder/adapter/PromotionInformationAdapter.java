package com.kaidongyuan.app.kdyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.bean.ProductPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略列表adapter
 */
public class PromotionInformationAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    List<ProductPolicy> mProductPolicyList;

    public PromotionInformationAdapter(Context mContext) {
        this.mContext = mContext;
        this.mProductPolicyList = new ArrayList<>();
    }

    public void setData(List<ProductPolicy> productPolicyList) {
        this.mProductPolicyList = productPolicyList;
        this.notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return mProductPolicyList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mProductPolicyList.get(groupPosition).getPolicyItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mProductPolicyList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mProductPolicyList.get(groupPosition).getPolicyItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ProductPolicy policy = mProductPolicyList.get(groupPosition);
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_promotion_information, null);
            holder = new GroupHolder();
            holder.tv_policy_name = (TextView) convertView.findViewById(R.id.tv_policy_name);
            holder.ll_detail = (LinearLayout) convertView.findViewById(R.id.ll_detail);
            holder.iv_arrow_expand = (ImageView) convertView.findViewById(R.id.iv_arrow_expand);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded) {
            holder.iv_arrow_expand.setImageResource(R.drawable.button_drop_down);
        } else {
            holder.iv_arrow_expand.setImageResource(R.drawable.button_drop_up);
        }
        if (policy.getPolicyItems().size() == 0) {
            holder.ll_detail.setVisibility(View.GONE);
        } else {
            holder.ll_detail.setVisibility(View.VISIBLE);
        }

        holder.tv_policy_name.setText(policy.getPOLICY_NAME());

        return convertView;
    }

    class GroupHolder {
        TextView tv_policy_name;
        LinearLayout ll_detail;
        ImageView iv_arrow_expand;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_promotion_information_child, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_condition = (TextView) convertView.findViewById(R.id.tv_condition);
            childViewHolder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }


        if (mProductPolicyList.get(groupPosition).getPolicyItems().size() != 0) {
            if (mProductPolicyList.get(groupPosition).getPolicyItems().get(childPosition).getCondition().length != 0) {
                childViewHolder.tv_condition.setText("满:\t"+getStringByArray(mProductPolicyList.get(groupPosition).getPolicyItems().get(childPosition).getCondition()));
            }
            if (mProductPolicyList.get(groupPosition).getPolicyItems().get(childPosition).getDiscount().length != 0) {
                childViewHolder.tv_discount.setText("赠:\t"+getStringByArray(mProductPolicyList.get(groupPosition).getPolicyItems().get(childPosition).getDiscount()));
            }
        }
        return convertView;
    }

    class ChildViewHolder {
        TextView tv_condition, tv_discount;
    }

    private String getStringByArray(String[] conditions) {
        String condition = "";
        for (String s : conditions) {
            condition += s + "\n\t\t";
        }
        return condition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
