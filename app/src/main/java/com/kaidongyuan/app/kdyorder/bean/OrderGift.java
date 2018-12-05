package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/23.
 * 赠品列表实体类
 */
public class OrderGift implements Parcelable {

    private String TYPE_NAME;
    private double QTY;
    private double PRICE;
    private double choiceCount = 0;
    private boolean isChecked = false;

    public OrderGift(){}

    public OrderGift(String TYPE_NAME, double QTY, double PRICE) {
        this.TYPE_NAME = TYPE_NAME;
        this.QTY = QTY;
        this.PRICE = PRICE;
    }

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }

    public void setQTY(double QIT) {
        this.QTY = QIT;
    }

    public double getQTY() {
        return QTY;
    }

    public double getChoiceCount() {
        return choiceCount;
    }

    public void setChoiceCount(double choiceCount) {
        this.choiceCount = choiceCount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getTYPE_NAME() {
        return TYPE_NAME;
    }

    public void setTYPE_NAME(String TYPE_NAME) {
        this.TYPE_NAME = TYPE_NAME;
    }

    @Override
    public String toString() {
        return "OrderGift{" +
                "TYPE_NAME='" + TYPE_NAME + '\'' +
                ", QTY=" + QTY +
                ", PRICE=" + PRICE +
                ", choiceCount=" + choiceCount +
                ", isChecked=" + isChecked +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TYPE_NAME);
        dest.writeDouble(QTY);
        dest.writeDouble(PRICE);
        dest.writeDouble(choiceCount);
        dest.writeValue(isChecked);
    }

    public static final Creator<OrderGift> CREATOR = new Creator<OrderGift>() {
        @Override
        public OrderGift createFromParcel(Parcel source) {
            return new OrderGift(source);
        }

        @Override
        public OrderGift[] newArray(int size) {
            return new OrderGift[size];
        }
    };

    public OrderGift(Parcel source) {
        TYPE_NAME = source.readString();
        QTY = source.readDouble();
        PRICE = source.readDouble();
        choiceCount = source.readDouble();
        isChecked = (boolean)source.readValue(new ClassLoader() {
        });
    }

}

















