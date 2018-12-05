package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * Created by ${tom} on 2017/12/7.
 */

public class OrderPlanSimple implements Serializable {
    private String IDX;//ID号
    private String ORD_NO;//计划订单号
    private String ORD_STATE;//订单状态
    private String ORD_WORKFLOW;//工作流程
    private String CONSIGNEE_REMARK;//收货人备注
    private String REQUEST_ISSUE;//预计发货时间
    private String REQUEST_DELIVERY;//预计交付时间
    private String OPERATOR_IDX;//操作人ID号
    private String ADD_DATE;//创建时间
    private String EDIT_DATE;//修改时间
    private String TO_CODE;//到达方代码
    private String TO_NAME;//到达方名称
    private String TO_ADDRESS;//到达方地址
    private String TO_CNAME;//到达方联系人
    private String ORD_QTY;//订单总数量
    private String ORD_WEIGHT;//订单总重量
    private String ORD_VOLUME;//订单总体积
    private String ORG_PRICE;//订单采购价格
    private String ACT_PRICE;//订单销售价格

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

    public String getORD_STATE() {
        return ORD_STATE;
    }

    public void setORD_STATE(String ORD_STATE) {
        this.ORD_STATE = ORD_STATE;
    }

    public String getORD_WORKFLOW() {
        return ORD_WORKFLOW;
    }

    public void setORD_WORKFLOW(String ORD_WORKFLOW) {
        this.ORD_WORKFLOW = ORD_WORKFLOW;
    }

    public String getCONSIGNEE_REMARK() {
        return CONSIGNEE_REMARK;
    }

    public void setCONSIGNEE_REMARK(String CONSIGNEE_REMARK) {
        this.CONSIGNEE_REMARK = CONSIGNEE_REMARK;
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

    public String getOPERATOR_IDX() {
        return OPERATOR_IDX;
    }

    public void setOPERATOR_IDX(String OPERATOR_IDX) {
        this.OPERATOR_IDX = OPERATOR_IDX;
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

    public String getTO_CODE() {
        return TO_CODE;
    }

    public void setTO_CODE(String TO_CODE) {
        this.TO_CODE = TO_CODE;
    }

    public String getTO_NAME() {
        return TO_NAME;
    }

    public void setTO_NAME(String TO_NAME) {
        this.TO_NAME = TO_NAME;
    }

    public String getTO_ADDRESS() {
        return TO_ADDRESS;
    }

    public void setTO_ADDRESS(String TO_ADDRESS) {
        this.TO_ADDRESS = TO_ADDRESS;
    }

    public String getTO_CNAME() {
        return TO_CNAME;
    }

    public void setTO_CNAME(String TO_CNAME) {
        this.TO_CNAME = TO_CNAME;
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

    @Override
    public String toString() {
        return "OrderPlanSimple{" +
                "IDX='" + IDX + '\'' +
                ", ORD_NO='" + ORD_NO + '\'' +
                ", ORD_STATE='" + ORD_STATE + '\'' +
                ", ORD_WORKFLOW='" + ORD_WORKFLOW + '\'' +
                ", CONSIGNEE_REMARK='" + CONSIGNEE_REMARK + '\'' +
                ", REQUEST_ISSUE='" + REQUEST_ISSUE + '\'' +
                ", REQUEST_DELIVERY='" + REQUEST_DELIVERY + '\'' +
                ", OPERATOR_IDX='" + OPERATOR_IDX + '\'' +
                ", ADD_DATE='" + ADD_DATE + '\'' +
                ", EDIT_DATE='" + EDIT_DATE + '\'' +
                ", TO_CODE='" + TO_CODE + '\'' +
                ", TO_NAME='" + TO_NAME + '\'' +
                ", TO_ADDRESS='" + TO_ADDRESS + '\'' +
                ", TO_CNAME='" + TO_CNAME + '\'' +
                ", ORD_QTY='" + ORD_QTY + '\'' +
                ", ORD_WEIGHT='" + ORD_WEIGHT + '\'' +
                ", ORD_VOLUME='" + ORD_VOLUME + '\'' +
                ", ORG_PRICE='" + ORG_PRICE + '\'' +
                ", ACT_PRICE='" + ACT_PRICE + '\'' +
                '}';
    }
}
