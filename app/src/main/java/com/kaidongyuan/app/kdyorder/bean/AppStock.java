package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * 客户库存登记表中AppStock
 * Created by Administrator on 2017/6/7.
 */
public class AppStock implements Serializable{
   private String  IDX;//	ID号
   private String ENT_IDX;//	企业ID号
   private String STOCK_IDX;//	客户ID
   private String PRODUCT_IDX;//	产品IDX
   private String PRODUCT_NO;//	产品代码
   private String PRODUCT_NAME;//	产品名称
   private String PRODUCTION_DATE;//	生产日期
   private String DAOQI;//到期日期
   private String STOCK_QTY	;//	数量
   private String USER_NAME	;//	操作人
   private String ADD_DATE;//	创建时间
   private String EDIT_DATE;//	修改时间
   private String EXPIRATION_DAY;//	保质期
   private String HUO_LING;//	产品货龄
   private String A_ZHUO_LING;//货龄状态
   private String THUO_LING;//	填表时货龄
   private String A_ZTHUO_LING;//填表时货龄
    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getENT_IDX() {
        return ENT_IDX;
    }

    public void setENT_IDX(String ENT_IDX) {
        this.ENT_IDX = ENT_IDX;
    }

    public String getSTOCK_IDX() {
        return STOCK_IDX;
    }

    public void setSTOCK_IDX(String STOCK_IDX) {
        this.STOCK_IDX = STOCK_IDX;
    }

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

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getADD_DATE() {
        return ADD_DATE;
    }

    public void setADD_DATE(String ADD_DATE) {
        this.ADD_DATE = ADD_DATE;
    }

    public String getEDIT_DATE() {
        return EDIT_DATE;
    }

    public void setEDIT_DATE(String EDIT_DATE) {
        this.EDIT_DATE = EDIT_DATE;
    }

    public String getEXPIRATION_DAY() {
        return EXPIRATION_DAY;
    }

    public void setEXPIRATION_DAY(String EXPIRATION_DAY) {
        this.EXPIRATION_DAY = EXPIRATION_DAY;
    }

    public String getHUO_LING() {
        return HUO_LING;
    }

    public void setHUO_LING(String HUO_LING) {
        this.HUO_LING = HUO_LING;
    }

    public String getTHUO_LING() {
        return THUO_LING;
    }

    public void setTHUO_LING(String THUO_LING) {
        this.THUO_LING = THUO_LING;
    }

    public String getDAOQI() {
        return DAOQI;
    }

    public void setDAOQI(String DAOQI) {
        this.DAOQI = DAOQI;
    }

    public String getA_ZHUO_LING() {
        return A_ZHUO_LING;
    }

    public void setA_ZHUO_LING(String a_ZHUO_LING) {
        A_ZHUO_LING = a_ZHUO_LING;
    }

    public String getA_ZTHUO_LING() {
        return A_ZTHUO_LING;
    }

    public void setA_ZTHUO_LING(String a_ZTHUO_LING) {
        A_ZTHUO_LING = a_ZTHUO_LING;
    }

    @Override
    public String toString() {
        return "AppStock{" +
                "IDX='" + IDX + '\'' +
                ", ENT_IDX='" + ENT_IDX + '\'' +
                ", STOCK_IDX='" + STOCK_IDX + '\'' +
                ", PRODUCT_IDX='" + PRODUCT_IDX + '\'' +
                ", PRODUCT_NO='" + PRODUCT_NO + '\'' +
                ", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                ", PRODUCTION_DATE='" + PRODUCTION_DATE + '\'' +
                ", DAOQI='" + DAOQI + '\'' +
                ", STOCK_QTY='" + STOCK_QTY + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                ", ADD_DATE='" + ADD_DATE + '\'' +
                ", EDIT_DATE='" + EDIT_DATE + '\'' +
                ", EXPIRATION_DAY='" + EXPIRATION_DAY + '\'' +
                ", HUO_LING='" + HUO_LING + '\'' +
                ", A_ZHUO_LING='" + A_ZHUO_LING + '\'' +
                ", THUO_LING='" + THUO_LING + '\'' +
                ", A_ZTHUO_LING='" + A_ZTHUO_LING + '\'' +
                '}';
    }
}
