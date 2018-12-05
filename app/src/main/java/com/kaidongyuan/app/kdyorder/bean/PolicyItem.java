package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 产品策略实体类
 * Created by Administrator on 2016/1/5.
 */
public class PolicyItem implements Parcelable {
    String Condition[];
    String Discount[];

    public PolicyItem() {
    }

    public PolicyItem(String Condition[], String Discount[]) {
        this.Condition = Condition;
        this.Discount = Discount;
    }

    public String[] getCondition() {
        return Condition;
    }

    public void setCondition(String[] condition) {
        Condition = condition;
    }

    public String[] getDiscount() {
        return Discount;
    }

    public void setDiscount(String[] discount) {
        Discount = discount;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(Condition);
        dest.writeArray(Discount);
    }

    public static final Creator<PolicyItem> CREATOR = new Creator<PolicyItem>() {
        @Override
        public PolicyItem[] newArray(int size) {
            return new PolicyItem[size];
        }

        @Override
        public PolicyItem createFromParcel(Parcel in) {
            return new PolicyItem(in);
        }
    };

    public PolicyItem(Parcel in) {
        in.readStringArray(Condition);
        in.readStringArray(Discount);
    }

}
