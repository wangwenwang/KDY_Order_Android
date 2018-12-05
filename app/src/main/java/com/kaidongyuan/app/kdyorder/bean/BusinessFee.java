package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class BusinessFee implements Serializable {
    private String PARTY_CODE;//	客户代码
    private String PARTY_NAME;//	客户名称
    private String BUSINESS_CODE;//	业务代码
    private String BUSINESS_NAME;//	业务名称
    private String LastMonth;//	上月留存提货余额
    private String ThisMonthPostive;//	加本月累计付款及代垫费用金额
    private String ThisMonthMinus;//	减本月累计提货总额
    private String ThisMonth;//	本月留存提货余额

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

    public String getBUSINESS_CODE() {
        return BUSINESS_CODE;
    }

    public void setBUSINESS_CODE(String BUSINESS_CODE) {
        this.BUSINESS_CODE = BUSINESS_CODE;
    }

    public String getBUSINESS_NAME() {
        return BUSINESS_NAME;
    }

    public void setBUSINESS_NAME(String BUSINESS_NAME) {
        this.BUSINESS_NAME = BUSINESS_NAME;
    }

    public String getLastMonth() {
        return LastMonth;
    }

    public void setLastMonth(String lastMonth) {
        LastMonth = lastMonth;
    }

    public String getThisMonthPostive() {
        return ThisMonthPostive;
    }

    public void setThisMonthPostive(String thisMonthPostive) {
        ThisMonthPostive = thisMonthPostive;
    }

    public String getThisMonthMinus() {
        return ThisMonthMinus;
    }

    public void setThisMonthMinus(String thisMonthMinus) {
        ThisMonthMinus = thisMonthMinus;
    }

    public String getThisMonth() {
        return ThisMonth;
    }

    public void setThisMonth(String thisMonth) {
        ThisMonth = thisMonth;
    }

    @Override
    public String toString() {
        return "BusinessFee{" +
                "PARTY_CODE='" + PARTY_CODE + '\'' +
                ", PARTY_NAME='" + PARTY_NAME + '\'' +
                ", BUSINESS_CODE='" + BUSINESS_CODE + '\'' +
                ", BUSINESS_NAME='" + BUSINESS_NAME + '\'' +
                ", LastMonth='" + LastMonth + '\'' +
                ", ThisMonthPostive='" + ThisMonthPostive + '\'' +
                ", ThisMonthMinus='" + ThisMonthMinus + '\'' +
                ", ThisMonth='" + ThisMonth + '\'' +
                '}';
    }
}
