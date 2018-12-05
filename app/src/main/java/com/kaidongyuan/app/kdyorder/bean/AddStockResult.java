package com.kaidongyuan.app.kdyorder.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/9.
 */
public class AddStockResult implements Serializable {
   private String  BUSINESS_IDX;
   private String  PARTY_IDX;
   private String  STOCK_DATE;
   private String  SUBMIT_DATE;
   private String  USER_NAME;
   private JSONObject AppStock;

    public AddStockResult() {
    }

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
    }

    public String getPARTY_IDX() {
        return PARTY_IDX;
    }

    public void setPARTY_IDX(String PARTY_IDX) {
        this.PARTY_IDX = PARTY_IDX;
    }

    public String getSTOCK_DATE() {
        return STOCK_DATE;
    }

    public void setSTOCK_DATE(String STOCK_DATE) {
        this.STOCK_DATE = STOCK_DATE;
    }

    public String getSUBMIT_DATE() {
        return SUBMIT_DATE;
    }

    public void setSUBMIT_DATE(String SUBMIT_DATE) {
        this.SUBMIT_DATE = SUBMIT_DATE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public JSONObject getAppStock() {
        return AppStock;
    }

    public void setAppStock(JSONObject appStock) {
        AppStock = appStock;
    }

    @Override
    public String toString() {
        return "AddStockResult{" +
                "BUSINESS_IDX='" + BUSINESS_IDX + '\'' +
                ", PARTY_IDX='" + PARTY_IDX + '\'' +
                ", STOCK_DATE='" + STOCK_DATE + '\'' +
                ", SUBMIT_DATE='" + SUBMIT_DATE + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                ", AppStock='" + AppStock + '\'' +
                '}';
    }
}
