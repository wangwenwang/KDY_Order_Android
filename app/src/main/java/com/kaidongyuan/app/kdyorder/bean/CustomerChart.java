package com.kaidongyuan.app.kdyorder.bean;

/**
 * 客户报表数据实体类
 */
public class CustomerChart {

    private String TO_CITY;
    private float ORD_QTY;
    private float ORG_PRICE;

    public String getTO_CITY() {
        return TO_CITY;
    }

    public void setTO_CITY(String TO_CITY) {
        this.TO_CITY = TO_CITY;
    }

    public float getORD_QTY() {
        return ORD_QTY;
    }

    public void setORD_QTY(float ORD_QTY) {
        this.ORD_QTY = ORD_QTY;
    }

    public float getORG_PRICE() {
        return ORG_PRICE;
    }

    public void setORG_PRICE(float ORG_PRICE) {
        this.ORG_PRICE = ORG_PRICE;
    }

    @Override
    public String toString() {
        return "CustomerChart{" +
                "TO_CITY='" + TO_CITY + '\'' +
                ", ORD_QTY=" + ORD_QTY +
                ", ORG_PRICE=" + ORG_PRICE +
                '}';
    }
}
