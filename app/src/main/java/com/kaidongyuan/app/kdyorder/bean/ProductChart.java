package com.kaidongyuan.app.kdyorder.bean;

/**
 * 产品报表实体类
 */
public class ProductChart {
    private String PRODUCT_NAME;
    private String PRODUCT_TYPE;
    private String ACT_PRICE;

    private float PO_QTY;

    public String getACT_PRICE() {
        return ACT_PRICE;
    }

    public void setACT_PRICE(String ACT_PRICE) {
        this.ACT_PRICE = ACT_PRICE;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getPRODUCT_TYPE() {
        return PRODUCT_TYPE;
    }

    public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
        this.PRODUCT_TYPE = PRODUCT_TYPE;
    }

    public float getPO_QTY() {
        return PO_QTY;
    }

    public void setPO_QTY(float PO_QTY) {
        this.PO_QTY = PO_QTY;
    }

    @Override
    public String toString() {
        return "ProductChart{" +
                "PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                ", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
                ", PO_QTY=" + PO_QTY +
                '}';
    }
}
