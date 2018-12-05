package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${tom} on 2018/1/3.
 * 物流订单类
 */

public class TmsOrder implements Parcelable {
   private String IDX;
   private String ORD_No;//	订单编号
   private String ORD_TO_NAME;//	客户名称
   private String ORD_TO_ADDRESS;//	目的地地址
   private double ORD_QTY;//	订单总数量
   private double ORD_WEIGHT;//	下单总重量
   private double ORD_VOLUME;//	下单总体积
   private double TMS_QTY;//	物流总数量
   private double TMS_WEIGHT;//	物流总重量
   private double TMS_VOLUME;//	物流总体积
   private String ORD_DATE_ADD;//创建时间
    public TmsOrder() {
    }

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getORD_No() {
        return ORD_No;
    }

    public void setORD_No(String ORD_No) {
        this.ORD_No = ORD_No;
    }

    public String getORD_TO_NAME() {
        return ORD_TO_NAME;
    }

    public void setORD_TO_NAME(String ORD_TO_NAME) {
        this.ORD_TO_NAME = ORD_TO_NAME;
    }

    public String getORD_TO_ADDRESS() {
        return ORD_TO_ADDRESS;
    }

    public void setORD_TO_ADDRESS(String ORD_TO_ADDRESS) {
        this.ORD_TO_ADDRESS = ORD_TO_ADDRESS;
    }

    public double getORD_QTY() {
        return ORD_QTY;
    }

    public void setORD_QTY(double ORD_QTY) {
        this.ORD_QTY = ORD_QTY;
    }

    public double getORD_WEIGHT() {
        return ORD_WEIGHT;
    }

    public void setORD_WEIGHT(double ORD_WEIGHT) {
        this.ORD_WEIGHT = ORD_WEIGHT;
    }

    public double getORD_VOLUME() {
        return ORD_VOLUME;
    }

    public void setORD_VOLUME(double ORD_VOLUME) {
        this.ORD_VOLUME = ORD_VOLUME;
    }

    public double getTMS_QTY() {
        return TMS_QTY;
    }

    public void setTMS_QTY(double TMS_QTY) {
        this.TMS_QTY = TMS_QTY;
    }

    public double getTMS_WEIGHT() {
        return TMS_WEIGHT;
    }

    public void setTMS_WEIGHT(double TMS_WEIGHT) {
        this.TMS_WEIGHT = TMS_WEIGHT;
    }

    public double getTMS_VOLUME() {
        return TMS_VOLUME;
    }

    public void setTMS_VOLUME(double TMS_VOLUME) {
        this.TMS_VOLUME = TMS_VOLUME;
    }

    public String getORD_DATE_ADD() {
        return ORD_DATE_ADD;
    }

    public void setORD_DATE_ADD(String ORD_DATE_ADD) {
        this.ORD_DATE_ADD = ORD_DATE_ADD;
    }

    public static Creator<TmsOrder> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IDX);
        dest.writeString(ORD_No);
        dest.writeString(ORD_TO_NAME);
        dest.writeString(ORD_TO_ADDRESS);
        dest.writeDouble(ORD_QTY);
        dest.writeDouble(ORD_WEIGHT);
        dest.writeDouble(ORD_VOLUME);
        dest.writeDouble(TMS_QTY);
        dest.writeDouble(TMS_WEIGHT);
        dest.writeDouble(TMS_VOLUME);
        dest.writeString(ORD_DATE_ADD);
    }

    public TmsOrder(Parcel in) {
        IDX = in.readString();
        ORD_No = in.readString();
        ORD_TO_NAME = in.readString();
        ORD_TO_ADDRESS = in.readString();
        ORD_QTY = in.readDouble();
        ORD_WEIGHT = in.readDouble();
        ORD_VOLUME = in.readDouble();
        TMS_QTY = in.readDouble();
        TMS_WEIGHT = in.readDouble();
        TMS_VOLUME = in.readDouble();
        ORD_DATE_ADD=in.readString();
    }
    public static final Creator<TmsOrder> CREATOR = new Creator<TmsOrder>() {
        @Override
        public TmsOrder[] newArray(int size) {
            return new TmsOrder[size];
        }

        @Override
        public TmsOrder createFromParcel(Parcel in) {
            return new TmsOrder(in);
        }
    };

    @Override
    public String toString() {
        return "TmsOrder{" +
                "IDX='" + IDX + '\'' +
                ", ORD_No='" + ORD_No + '\'' +
                ", ORD_TO_NAME='" + ORD_TO_NAME + '\'' +
                ", ORD_TO_ADDRESS='" + ORD_TO_ADDRESS + '\'' +
                ", ORD_QTY=" + ORD_QTY +
                ", ORD_WEIGHT=" + ORD_WEIGHT +
                ", ORD_VOLUME=" + ORD_VOLUME +
                ", TMS_QTY=" + TMS_QTY +
                ", TMS_WEIGHT=" + TMS_WEIGHT +
                ", TMS_VOLUME=" + TMS_VOLUME +
                ", ORD_DATE_ADD='" + ORD_DATE_ADD + '\'' +
                '}';
    }
}
