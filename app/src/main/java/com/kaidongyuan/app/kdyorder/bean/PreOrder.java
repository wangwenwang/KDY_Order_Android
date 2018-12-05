package com.kaidongyuan.app.kdyorder.bean;

/**
 * 订单详情java bean
 */

import java.util.List;

/**
 * PreOrderDetailActivity中的实体类
 */
public class PreOrder implements java.io.Serializable {
    private String IDX;
    private String ORD_NO;
    private String TO_NAME;
    private String ORG_IDX;
    private String TO_CNAME;
    private String TO_ADDRESS;
    private String ORD_QTY;
    private String ORD_WEIGHT;
    private String ORD_VOLUME;
    private String BUSINESS_IDX;
    private String TO_PROPERTY;
    private String ORD_WORKFLOW;
    private String ORD_STATE;
    private String CONSIGNEE_REMARK;
    private String TO_CODE;
    private String REQUEST_ISSUE;
    private String REQUEST_DELIVERY;
    private String ADD_DATE;
    private String ORG_PRICE;
    private String ACT_PRICE;
    private String ISFACTOR;
    private String FACTOR_COMPANY;
    private String WITH_MONEY;
    private String UPDATE06;
    private List<OrderDetails> OrderDetails;

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getORD_NO() {
        return ORD_NO;
    }

    public void setORD_NO(String ORD_NO) {
        this.ORD_NO = ORD_NO;
    }

    public String getTO_NAME() {
        return TO_NAME;
    }

    public void setTO_NAME(String TO_NAME) {
        this.TO_NAME = TO_NAME;
    }

    public String getORG_IDX() {
        return ORG_IDX;
    }

    public void setORG_IDX(String ORG_IDX) {
        this.ORG_IDX = ORG_IDX;
    }

    public String getTO_CNAME() {
        return TO_CNAME;
    }

    public void setTO_CNAME(String TO_CNAME) {
        this.TO_CNAME = TO_CNAME;
    }

    public String getTO_ADDRESS() {
        return TO_ADDRESS;
    }

    public void setTO_ADDRESS(String TO_ADDRESS) {
        this.TO_ADDRESS = TO_ADDRESS;
    }

    public String getORD_QTY() {
        return ORD_QTY;
    }

    public void setORD_QTY(String ORD_QTY) {
        this.ORD_QTY = ORD_QTY;
    }

    public String getORD_WEIGHT() {
        return ORD_WEIGHT;
    }

    public void setORD_WEIGHT(String ORD_WEIGHT) {
        this.ORD_WEIGHT = ORD_WEIGHT;
    }

    public String getORD_VOLUME() {
        return ORD_VOLUME;
    }

    public void setORD_VOLUME(String ORD_VOLUME) {
        this.ORD_VOLUME = ORD_VOLUME;
    }

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
    }

    public String getTO_PROPERTY() {
        return TO_PROPERTY;
    }

    public void setTO_PROPERTY(String TO_PROPERTY) {
        this.TO_PROPERTY = TO_PROPERTY;
    }

    public String getORD_WORKFLOW() {
        return ORD_WORKFLOW;
    }

    public void setORD_WORKFLOW(String ORD_WORKFLOW) {
        this.ORD_WORKFLOW = ORD_WORKFLOW;
    }

    public String getORD_STATE() {
        return ORD_STATE;
    }

    public void setORD_STATE(String ORD_STATE) {
        this.ORD_STATE = ORD_STATE;
    }

    public String getCONSIGNEE_REMARK() {
        return CONSIGNEE_REMARK;
    }

    public void setCONSIGNEE_REMARK(String CONSIGNEE_REMARK) {
        this.CONSIGNEE_REMARK = CONSIGNEE_REMARK;
    }

    public String getTO_CODE() {
        return TO_CODE;
    }

    public void setTO_CODE(String TO_CODE) {
        this.TO_CODE = TO_CODE;
    }

    public String getREQUEST_ISSUE() {
        return REQUEST_ISSUE;
    }

    public void setREQUEST_ISSUE(String REQUEST_ISSUE) {
        this.REQUEST_ISSUE = REQUEST_ISSUE;
    }

    public String getREQUEST_DELIVERY() {
        return REQUEST_DELIVERY;
    }

    public void setREQUEST_DELIVERY(String REQUEST_DELIVERY) {
        this.REQUEST_DELIVERY = REQUEST_DELIVERY;
    }

    public String getADD_DATE() {
        return ADD_DATE;
    }

    public void setADD_DATE(String ADD_DATE) {
        this.ADD_DATE = ADD_DATE;
    }

    public String getORG_PRICE() {
        return ORG_PRICE;
    }

    public void setORG_PRICE(String ORG_PRICE) {
        this.ORG_PRICE = ORG_PRICE;
    }

    public String getACT_PRICE() {
        return ACT_PRICE;
    }

    public void setACT_PRICE(String ACT_PRICE) {
        this.ACT_PRICE = ACT_PRICE;
    }

    public String getISFACTOR() {
        return ISFACTOR;
    }

    public void setISFACTOR(String ISFACTOR) {
        this.ISFACTOR = ISFACTOR;
    }

    public String getFACTOR_COMPANY() {
        return FACTOR_COMPANY;
    }

    public void setFACTOR_COMPANY(String FACTOR_COMPANY) {
        this.FACTOR_COMPANY = FACTOR_COMPANY;
    }

    public String getWITH_MONEY() {
        return WITH_MONEY;
    }

    public void setWITH_MONEY(String WITH_MONEY) {
        this.WITH_MONEY = WITH_MONEY;
    }

    public String getUPDATE06() {
        return UPDATE06;
    }

    public void setUPDATE06(String UPDATE06) {
        this.UPDATE06 = UPDATE06;
    }

    public List<com.kaidongyuan.app.kdyorder.bean.OrderDetails> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<com.kaidongyuan.app.kdyorder.bean.OrderDetails> orderDetails) {
        OrderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "PreOrder{" +
                "IDX='" + IDX + '\'' +
                ", ORD_NO='" + ORD_NO + '\'' +
                ", TO_NAME='" + TO_NAME + '\'' +
                ", ORG_IDX='" + ORG_IDX + '\'' +
                ", TO_CNAME='" + TO_CNAME + '\'' +
                ", TO_ADDRESS='" + TO_ADDRESS + '\'' +
                ", ORD_QTY='" + ORD_QTY + '\'' +
                ", ORD_WEIGHT='" + ORD_WEIGHT + '\'' +
                ", ORD_VOLUME='" + ORD_VOLUME + '\'' +
                ", BUSINESS_IDX='" + BUSINESS_IDX + '\'' +
                ", TO_PROPERTY='" + TO_PROPERTY + '\'' +
                ", ORD_WORKFLOW='" + ORD_WORKFLOW + '\'' +
                ", ORD_STATE='" + ORD_STATE + '\'' +
                ", CONSIGNEE_REMARK='" + CONSIGNEE_REMARK + '\'' +
                ", TO_CODE='" + TO_CODE + '\'' +
                ", REQUEST_ISSUE='" + REQUEST_ISSUE + '\'' +
                ", REQUEST_DELIVERY='" + REQUEST_DELIVERY + '\'' +
                ", ADD_DATE='" + ADD_DATE + '\'' +
                ", ORG_PRICE='" + ORG_PRICE + '\'' +
                ", ACT_PRICE='" + ACT_PRICE + '\'' +
                ", ISFACTOR='" + ISFACTOR + '\'' +
                ", FACTOR_COMPANY='" + FACTOR_COMPANY + '\'' +
                ", WITH_MONEY='" + WITH_MONEY + '\'' +
                ", UPDATE06='" + UPDATE06 + '\'' +
                ", OrderDetails=" + OrderDetails +
                '}';
    }
}
