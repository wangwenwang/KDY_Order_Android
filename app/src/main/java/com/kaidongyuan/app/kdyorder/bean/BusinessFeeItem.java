package com.kaidongyuan.app.kdyorder.bean;

import com.kaidongyuan.app.kdyorder.util.StringUtils;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * 月份账单费用条目
 * Created by Administrator on 2017/7/6.
 */
public class BusinessFeeItem implements Serializable {
    private String FEE_NAME;
    private String FEE_AMOUNT;
    private String FEE_DATE;

    public String getFEE_DATE() {
        return FEE_DATE;
    }

    public void setFEE_DATE(String FEE_DATE) {
        this.FEE_DATE = FEE_DATE;
    }

    public String getFEE_NAME() {
        return FEE_NAME;
    }

    public void setFEE_NAME(String FEE_NAME) {
        this.FEE_NAME = FEE_NAME;
    }

    public String getFEE_AMOUNT() {
        return FEE_AMOUNT;
    }

    public void setFEE_AMOUNT(String FEE_AMOUNT) {
        this.FEE_AMOUNT = FEE_AMOUNT;
    }

    @Override
    public String toString() {
        return "BusinessFeeItem{" +
                "FEE_NAME='" + FEE_NAME + '\'' +
                ", FEE_AMOUNT='" + FEE_AMOUNT + '\'' +
                ", FEE_DATE='" + FEE_DATE + '\'' +
                '}';
    }
}
