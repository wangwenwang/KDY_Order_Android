package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * 客户库存登记表
 * Created by Administrator on 2017/6/7.
 */
public class Fleet implements Serializable{
    private String IDX;//库存Id
    private String BUSINESS_CODE;//	业务名
    private String BUSINESS_NAME;//	业务名
    private String PARTY_CODE;//	客户代码
    private String PARTY_NAME;//	客户名称
    private String STOCK_DATE;//	库存月份
    private String SUBMIT_DATE;//	填表日期
    private String USER_NAME;//	操作人名
    private String ENT_IDX;//	企业ID号
    private String ADD_DATE;//	创建时间
    private String EDIT_DATE;//	修改时间
    private String STOCK_STATE;//Open  正常 ；close关闭，Cancel取消
    private String STOCK_WORKFLOW;//"新建"
    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getBUSINESS_NAME() {
        return BUSINESS_NAME;
    }

    public void setBUSINESS_NAME(String BUSINESS_NAME) {
        this.BUSINESS_NAME = BUSINESS_NAME;
    }

    public String getPARTY_CODE() {
        return PARTY_CODE;
    }

    public void setPARTY_CODE(String PARTY_CODE) {
        this.PARTY_CODE = PARTY_CODE;
    }

    public String getPARTY_NAME() {
        return PARTY_NAME;
    }

    public void setPARTY_NAME(String PARTY_NAME) {
        this.PARTY_NAME = PARTY_NAME;
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

    public String getENT_IDX() {
        return ENT_IDX;
    }

    public void setENT_IDX(String ENT_IDX) {
        this.ENT_IDX = ENT_IDX;
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

    public String getBUSINESS_CODE() {
        return BUSINESS_CODE;
    }

    public void setBUSINESS_CODE(String BUSINESS_CODE) {
        this.BUSINESS_CODE = BUSINESS_CODE;
    }

    public String getSTOCK_STATE() {
        return STOCK_STATE;
    }

    public void setSTOCK_STATE(String STOCK_STATE) {
        this.STOCK_STATE = STOCK_STATE;
    }

    public String getSTOCK_WORKFLOW() {
        return STOCK_WORKFLOW;
    }

    public void setSTOCK_WORKFLOW(String STOCK_WORKFLOW) {
        this.STOCK_WORKFLOW = STOCK_WORKFLOW;
    }

    @Override
    public String toString() {
        return "Fleet{" +
                "IDX='" + IDX + '\'' +
                ", BUSINESS_NAME='" + BUSINESS_NAME + '\'' +
                ", PARTY_CODE='" + PARTY_CODE + '\'' +
                ", PARTY_NAME='" + PARTY_NAME + '\'' +
                ", STOCK_DATE='" + STOCK_DATE + '\'' +
                ", SUBMIT_DATE='" + SUBMIT_DATE + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                ", ENT_IDX='" + ENT_IDX + '\'' +
                ", ADD_DATE='" + ADD_DATE + '\'' +
                ", EDIT_DATE='" + EDIT_DATE + '\'' +
                '}';
    }
}
