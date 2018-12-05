package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/7/14.
 */
public class NormalAddress implements Parcelable {
    private String ITEM_IDX;
    private String ITEM_NAME;//	名字

    public String getITEM_IDX() {
        return ITEM_IDX;
    }

    public void setITEM_IDX(String ITEM_IDX) {
        this.ITEM_IDX = ITEM_IDX;
    }

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public NormalAddress(Parcel in) {
        ITEM_IDX=in.readString();
        ITEM_NAME=in.readString();
    }

    public NormalAddress() {
    }

    public NormalAddress(String ITEM_IDX, String ITEM_NAME) {
        this.ITEM_IDX = ITEM_IDX;
        this.ITEM_NAME = ITEM_NAME;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ITEM_IDX);
        dest.writeString(ITEM_NAME);
    }

    public static final Creator<NormalAddress> CREATOR = new Creator<NormalAddress>() {

        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public NormalAddress[] newArray(int size) {
            return new NormalAddress[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public NormalAddress createFromParcel(Parcel source) {
            return new NormalAddress(source);
        }
    };


}
