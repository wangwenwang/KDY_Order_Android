package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class BillFee implements Serializable {
    private String IDX;//	ID号
     private String ENT_IDX;//	企业ID号
    private String BILL_NAME;//	账单名称
    private String BILL_DATE;//	账单月份
    private String BILL_STATE;//	状态
    private String BILL_WORKFLOW;//	流程
    private String USER_NAME;//	ID号

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

    public String getBILL_NAME() {
        return BILL_NAME;
    }

    public void setBILL_NAME(String BILL_NAME) {
        this.BILL_NAME = BILL_NAME;
    }

    public String getBILL_DATE() {
        return BILL_DATE;
    }

    public void setBILL_DATE(String BILL_DATE) {
        this.BILL_DATE = BILL_DATE;
    }

    public String getBILL_STATE() {
        return BILL_STATE;
    }

    public void setBILL_STATE(String BILL_STATE) {
        this.BILL_STATE = BILL_STATE;
    }

    public String getBILL_WORKFLOW() {
        return BILL_WORKFLOW;
    }

    public void setBILL_WORKFLOW(String BILL_WORKFLOW) {
        this.BILL_WORKFLOW = BILL_WORKFLOW;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    @Override
    public String toString() {
        return "BillFee{" +
                "IDX='" + IDX + '\'' +
                ", ENT_IDX='" + ENT_IDX + '\'' +
                ", BILL_NAME='" + BILL_NAME + '\'' +
                ", BILL_DATE='" + BILL_DATE + '\'' +
                ", BILL_STATE='" + BILL_STATE + '\'' +
                ", BILL_WORKFLOW='" + BILL_WORKFLOW + '\'' +
                ", USER_NAME='" + USER_NAME + '\'' +
                '}';
    }
}
