package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/9.
 */
public class AppStockResult implements Serializable{
    private String PRODUCT_IDX;//   产品IDX
    private String PRODUCT_NO;//	产品代码
    private String PRODUCT_NAME;//	产品名称
    private String PRODUCTION_DATE;//	生产日期
    private String STOCK_QTY;//	  数量

    public String getPRODUCT_IDX() {
        return PRODUCT_IDX;
    }

    public void setPRODUCT_IDX(String PRODUCT_IDX) {
        this.PRODUCT_IDX = PRODUCT_IDX;
    }

    public String getPRODUCT_NO() {
        return PRODUCT_NO;
    }

    public void setPRODUCT_NO(String PRODUCT_NO) {
        this.PRODUCT_NO = PRODUCT_NO;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getPRODUCTION_DATE() {
        return PRODUCTION_DATE;
    }

    public void setPRODUCTION_DATE(String PRODUCTION_DATE) {
        this.PRODUCTION_DATE = PRODUCTION_DATE;
    }

    public String getSTOCK_QTY() {
        return STOCK_QTY;
    }

    public void setSTOCK_QTY(String STOCK_QTY) {
        this.STOCK_QTY = STOCK_QTY;
    }

    @Override
    public String toString() {
        return "AppStockResult{" +
                "PRODUCT_IDX='" + PRODUCT_IDX + '\'' +
                ", PRODUCT_NO='" + PRODUCT_NO + '\'' +
                ", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                ", PRODUCTION_DATE='" + PRODUCTION_DATE + '\'' +
                ", STOCK_QTY='" + STOCK_QTY + '\'' +
                '}';
    }
}
