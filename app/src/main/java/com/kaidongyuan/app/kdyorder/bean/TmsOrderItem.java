package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ${tom} on 2018/1/3.
 * 物流订单类
 */

public class TmsOrderItem implements Serializable {
   private String ORD_IDX;
   private String ORD_NO;//	订单编号
   private String TMS_SHIPMENT_NO;//	装运编号
   private String TMS_DATE_LOAD;//	装运时间
   private String TMS_DATE_ISSUE;//	出库时间
   private double ORD_ISSUE_QTY;//	发货数量
   private double ORD_ISSUE_WEIGHT;//	发货重量
   private double ORD_ISSUE_VOLUME;//	发货体积
   private String ORD_WORKFLOW;//	订单流程
   private String DRIVER_PAY;//	交付状态

    public String getORD_IDX() {
        return ORD_IDX;
    }

    public void setORD_IDX(String ORD_IDX) {
        this.ORD_IDX = ORD_IDX;
    }

    public String getORD_NO() {
        return ORD_NO;
    }

    public void setORD_NO(String ORD_NO) {
        this.ORD_NO = ORD_NO;
    }

    public String getTMS_SHIPMENT_NO() {
        return TMS_SHIPMENT_NO;
    }

    public void setTMS_SHIPMENT_NO(String TMS_SHIPMENT_NO) {
        this.TMS_SHIPMENT_NO = TMS_SHIPMENT_NO;
    }

    public String getTMS_DATE_LOAD() {
        return TMS_DATE_LOAD;
    }

    public void setTMS_DATE_LOAD(String TMS_DATE_LOAD) {
        this.TMS_DATE_LOAD = TMS_DATE_LOAD;
    }

    public String getTMS_DATE_ISSUE() {
        return TMS_DATE_ISSUE;
    }

    public void setTMS_DATE_ISSUE(String TMS_DATE_ISSUE) {
        this.TMS_DATE_ISSUE = TMS_DATE_ISSUE;
    }

    public double getORD_ISSUE_QTY() {
        return ORD_ISSUE_QTY;
    }

    public void setORD_ISSUE_QTY(double ORD_ISSUE_QTY) {
        this.ORD_ISSUE_QTY = ORD_ISSUE_QTY;
    }

    public double getORD_ISSUE_WEIGHT() {
        return ORD_ISSUE_WEIGHT;
    }

    public void setORD_ISSUE_WEIGHT(double ORD_ISSUE_WEIGHT) {
        this.ORD_ISSUE_WEIGHT = ORD_ISSUE_WEIGHT;
    }

    public double getORD_ISSUE_VOLUME() {
        return ORD_ISSUE_VOLUME;
    }

    public void setORD_ISSUE_VOLUME(double ORD_ISSUE_VOLUME) {
        this.ORD_ISSUE_VOLUME = ORD_ISSUE_VOLUME;
    }

    public String getORD_WORKFLOW() {
        return ORD_WORKFLOW;
    }

    public void setORD_WORKFLOW(String ORD_WORKFLOW) {
        this.ORD_WORKFLOW = ORD_WORKFLOW;
    }

    public String getDRIVER_PAY() {
        return DRIVER_PAY;
    }

    public void setDRIVER_PAY(String DRIVER_PAY) {
        this.DRIVER_PAY = DRIVER_PAY;
    }

    @Override
    public String toString() {
        return "TmsOrderItem{" +
                "ORD_IDX='" + ORD_IDX + '\'' +
                ", ORD_NO='" + ORD_NO + '\'' +
                ", TMS_SHIPMENT_NO='" + TMS_SHIPMENT_NO + '\'' +
                ", TMS_DATE_LOAD='" + TMS_DATE_LOAD + '\'' +
                ", TMS_DATE_ISSUE='" + TMS_DATE_ISSUE + '\'' +
                ", ORD_ISSUE_QTY=" + ORD_ISSUE_QTY +
                ", ORD_ISSUE_WEIGHT=" + ORD_ISSUE_WEIGHT +
                ", ORD_ISSUE_VOLUME=" + ORD_ISSUE_VOLUME +
                ", ORD_WORKFLOW='" + ORD_WORKFLOW + '\'' +
                ", DRIVER_PAY='" + DRIVER_PAY + '\'' +
                '}';
    }
}
