package com.kaidongyuan.app.kdyorder.bean;


import java.util.List;

public class Order implements java.io.Serializable {
    private String TMS_DATE_LOAD;
    private String TMS_DATE_ISSUE;
    private String TMS_SHIPMENT_NO;
    private String TMS_FLEET_NAME;
    private String ORD_IDX;

    private String IDX;
    private String ORD_NO;
    private String ORD_TO_NAME;
    private String PARTY_TYPE;
    private String ORD_TO_CNAME;
    private String ORD_TO_ADDRESS;
    private String ORD_QTY;
    private String ORD_WEIGHT;
    private String ORD_VOLUME;
    private String ORD_ISSUE_QTY;
    private String ORD_ISSUE_WEIGHT;
    private String ORD_ISSUE_VOLUME;
    private String ORD_WORKFLOW;
    private String OMS_SPLIT_TYPE;
    private String OMS_PARENT_NO;
    private String ORD_STATE;
    private String ORD_DATE_ADD;
    private String ADD_CODE;
    private String ORD_REQUEST_ISSUE;
    private String ORD_FROM_NAME;
    private String FROM_COORDINATE;//起始点坐标
    private String TO_COORDINATE;//到达点坐标

    private String ORD_REMARK_CLIENT;
    private String TMS_DRIVER_NAME;//司机姓名
    private String TMS_PLATE_NUMBER;//车牌号
    private String TMS_DRIVER_TEL;//司机电话
    private String PAYMENT_TYPE;
    private double ORG_PRICE;
    private double ACT_PRICE;

    private double MJ_PRICE;
    private String ORD_REMARK_CONSIGNEE;
    private String MJ_REMARK;

    private String DRIVER_PAY;

    private String IS_SAAS;

    private List<OrderDetails> OrderDetails;
    private List<StateTack> StateTack;

    public String getTMS_DATE_LOAD() {
        return TMS_DATE_LOAD;
    }

    public void setTMS_DATE_LOAD(String TMS_DATE_LOAD) {
        this.TMS_DATE_LOAD = TMS_DATE_LOAD;
    }

    public String getTMS_DATE_ISSUE() {
        return TMS_DATE_ISSUE;
    }

    public void setTMS_DATE_ISSUE(String TMS_DATE_ISSUE) {
        this.TMS_DATE_ISSUE = TMS_DATE_ISSUE;
    }

    public String getTMS_SHIPMENT_NO() {
        return TMS_SHIPMENT_NO;
    }

    public void setTMS_SHIPMENT_NO(String TMS_SHIPMENT_NO) {
        this.TMS_SHIPMENT_NO = TMS_SHIPMENT_NO;
    }

    public String getTMS_FLEET_NAME() {
        return TMS_FLEET_NAME;
    }

    public void setTMS_FLEET_NAME(String TMS_FLEET_NAME) {
        this.TMS_FLEET_NAME = TMS_FLEET_NAME;
    }

    public String getORD_IDX() {
        return ORD_IDX;
    }

    public void setORD_IDX(String ORD_IDX) {
        this.ORD_IDX = ORD_IDX;
    }

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

    public String getORD_TO_NAME() {
        return ORD_TO_NAME;
    }

    public void setORD_TO_NAME(String ORD_TO_NAME) {
        this.ORD_TO_NAME = ORD_TO_NAME;
    }

    public String getPARTY_TYPE() {
        return PARTY_TYPE;
    }

    public void setPARTY_TYPE(String PARTY_TYPE) {
        this.PARTY_TYPE = PARTY_TYPE;
    }

    public String getORD_TO_CNAME() {
        return ORD_TO_CNAME;
    }

    public void setORD_TO_CNAME(String ORD_TO_CNAME) {
        this.ORD_TO_CNAME = ORD_TO_CNAME;
    }

    public String getORD_TO_ADDRESS() {
        return ORD_TO_ADDRESS;
    }

    public void setORD_TO_ADDRESS(String ORD_TO_ADDRESS) {
        this.ORD_TO_ADDRESS = ORD_TO_ADDRESS;
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

    public String getORD_ISSUE_QTY() {
        return ORD_ISSUE_QTY;
    }

    public void setORD_ISSUE_QTY(String ORD_ISSUE_QTY) {
        this.ORD_ISSUE_QTY = ORD_ISSUE_QTY;
    }

    public String getORD_ISSUE_WEIGHT() {
        return ORD_ISSUE_WEIGHT;
    }

    public void setORD_ISSUE_WEIGHT(String ORD_ISSUE_WEIGHT) {
        this.ORD_ISSUE_WEIGHT = ORD_ISSUE_WEIGHT;
    }

    public String getORD_ISSUE_VOLUME() {
        return ORD_ISSUE_VOLUME;
    }

    public void setORD_ISSUE_VOLUME(String ORD_ISSUE_VOLUME) {
        this.ORD_ISSUE_VOLUME = ORD_ISSUE_VOLUME;
    }

    public String getORD_WORKFLOW() {
        return ORD_WORKFLOW;
    }

    public void setORD_WORKFLOW(String ORD_WORKFLOW) {
        this.ORD_WORKFLOW = ORD_WORKFLOW;
    }

    public String getOMS_SPLIT_TYPE() {
        return OMS_SPLIT_TYPE;
    }

    public void setOMS_SPLIT_TYPE(String OMS_SPLIT_TYPE) {
        this.OMS_SPLIT_TYPE = OMS_SPLIT_TYPE;
    }

    public String getOMS_PARENT_NO() {
        return OMS_PARENT_NO;
    }

    public void setOMS_PARENT_NO(String OMS_PARENT_NO) {
        this.OMS_PARENT_NO = OMS_PARENT_NO;
    }

    public String getORD_STATE() {
        return ORD_STATE;
    }

    public void setORD_STATE(String ORD_STATE) {
        this.ORD_STATE = ORD_STATE;
    }

    public String getORD_DATE_ADD() {
        return ORD_DATE_ADD;
    }

    public void setORD_DATE_ADD(String ORD_DATE_ADD) {
        this.ORD_DATE_ADD = ORD_DATE_ADD;
    }

    public String getADD_CODE() {
        return ADD_CODE;
    }

    public void setADD_CODE(String ADD_CODE) {
        this.ADD_CODE = ADD_CODE;
    }

    public String getORD_REQUEST_ISSUE() {
        return ORD_REQUEST_ISSUE;
    }

    public void setORD_REQUEST_ISSUE(String ORD_REQUEST_ISSUE) {
        this.ORD_REQUEST_ISSUE = ORD_REQUEST_ISSUE;
    }

    public String getORD_FROM_NAME() {
        return ORD_FROM_NAME;
    }

    public void setORD_FROM_NAME(String ORD_FROM_NAME) {
        this.ORD_FROM_NAME = ORD_FROM_NAME;
    }

    public String getFROM_COORDINATE() {
        return FROM_COORDINATE;
    }

    public void setFROM_COORDINATE(String FROM_COORDINATE) {
        this.FROM_COORDINATE = FROM_COORDINATE;
    }

    public String getTO_COORDINATE() {
        return TO_COORDINATE;
    }

    public void setTO_COORDINATE(String TO_COORDINATE) {
        this.TO_COORDINATE = TO_COORDINATE;
    }

    public String getORD_REMARK_CLIENT() {
        return ORD_REMARK_CLIENT;
    }

    public void setORD_REMARK_CLIENT(String ORD_REMARK_CLIENT) {
        this.ORD_REMARK_CLIENT = ORD_REMARK_CLIENT;
    }

    public String getTMS_DRIVER_NAME() {
        return TMS_DRIVER_NAME;
    }

    public void setTMS_DRIVER_NAME(String TMS_DRIVER_NAME) {
        this.TMS_DRIVER_NAME = TMS_DRIVER_NAME;
    }

    public String getTMS_PLATE_NUMBER() {
        return TMS_PLATE_NUMBER;
    }

    public void setTMS_PLATE_NUMBER(String TMS_PLATE_NUMBER) {
        this.TMS_PLATE_NUMBER = TMS_PLATE_NUMBER;
    }

    public String getTMS_DRIVER_TEL() {
        return TMS_DRIVER_TEL;
    }

    public void setTMS_DRIVER_TEL(String TMS_DRIVER_TEL) {
        this.TMS_DRIVER_TEL = TMS_DRIVER_TEL;
    }

    public String getPAYMENT_TYPE() {
        return PAYMENT_TYPE;
    }

    public void setPAYMENT_TYPE(String PAYMENT_TYPE) {
        this.PAYMENT_TYPE = PAYMENT_TYPE;
    }

    public double getORG_PRICE() {
        return ORG_PRICE;
    }

    public void setORG_PRICE(double ORG_PRICE) {
        this.ORG_PRICE = ORG_PRICE;
    }

    public double getACT_PRICE() {
        return ACT_PRICE;
    }

    public void setACT_PRICE(double ACT_PRICE) {
        this.ACT_PRICE = ACT_PRICE;
    }

    public double getMJ_PRICE() {
        return MJ_PRICE;
    }

    public void setMJ_PRICE(double MJ_PRICE) {
        this.MJ_PRICE = MJ_PRICE;
    }

    public String getORD_REMARK_CONSIGNEE() {
        return ORD_REMARK_CONSIGNEE;
    }

    public void setORD_REMARK_CONSIGNEE(String ORD_REMARK_CONSIGNEE) {
        this.ORD_REMARK_CONSIGNEE = ORD_REMARK_CONSIGNEE;
    }

    public String getMJ_REMARK() {
        return MJ_REMARK;
    }

    public void setMJ_REMARK(String MJ_REMARK) {
        this.MJ_REMARK = MJ_REMARK;
    }

    public String getDRIVER_PAY() {
        return DRIVER_PAY;
    }

    public void setDRIVER_PAY(String DRIVER_PAY) {
        this.DRIVER_PAY = DRIVER_PAY;
    }

    public List<OrderDetails> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        OrderDetails = orderDetails;
    }

    public List<StateTack> getStateTack() {
        return StateTack;
    }

    public void setStateTack(List<StateTack> stateTack) {
        StateTack = stateTack;
    }

    public String getIS_SAAS() {
        return IS_SAAS;
    }

    public void setIS_SAAS(String IS_SAAS) {
        this.IS_SAAS = IS_SAAS;
    }

    @Override
    public String toString() {
        return "Order{" +
                "TMS_DATE_LOAD='" + TMS_DATE_LOAD + '\'' +
                ", TMS_DATE_ISSUE='" + TMS_DATE_ISSUE + '\'' +
                ", TMS_SHIPMENT_NO='" + TMS_SHIPMENT_NO + '\'' +
                ", TMS_FLEET_NAME='" + TMS_FLEET_NAME + '\'' +
                ", ORD_IDX='" + ORD_IDX + '\'' +
                ", IDX='" + IDX + '\'' +
                ", ORD_NO='" + ORD_NO + '\'' +
                ", ORD_TO_NAME='" + ORD_TO_NAME + '\'' +
                ", PARTY_TYPE='" + PARTY_TYPE + '\'' +
                ", ORD_TO_CNAME='" + ORD_TO_CNAME + '\'' +
                ", ORD_TO_ADDRESS='" + ORD_TO_ADDRESS + '\'' +
                ", ORD_QTY='" + ORD_QTY + '\'' +
                ", ORD_WEIGHT='" + ORD_WEIGHT + '\'' +
                ", ORD_VOLUME='" + ORD_VOLUME + '\'' +
                ", ORD_ISSUE_QTY='" + ORD_ISSUE_QTY + '\'' +
                ", ORD_ISSUE_WEIGHT='" + ORD_ISSUE_WEIGHT + '\'' +
                ", ORD_ISSUE_VOLUME='" + ORD_ISSUE_VOLUME + '\'' +
                ", ORD_WORKFLOW='" + ORD_WORKFLOW + '\'' +
                ", OMS_SPLIT_TYPE='" + OMS_SPLIT_TYPE + '\'' +
                ", OMS_PARENT_NO='" + OMS_PARENT_NO + '\'' +
                ", ORD_STATE='" + ORD_STATE + '\'' +
                ", ORD_DATE_ADD='" + ORD_DATE_ADD + '\'' +
                ", ADD_CODE='" + ADD_CODE + '\'' +
                ", ORD_REQUEST_ISSUE='" + ORD_REQUEST_ISSUE + '\'' +
                ", ORD_FROM_NAME='" + ORD_FROM_NAME + '\'' +
                ", FROM_COORDINATE='" + FROM_COORDINATE + '\'' +
                ", TO_COORDINATE='" + TO_COORDINATE + '\'' +
                ", ORD_REMARK_CLIENT='" + ORD_REMARK_CLIENT + '\'' +
                ", TMS_DRIVER_NAME='" + TMS_DRIVER_NAME + '\'' +
                ", TMS_PLATE_NUMBER='" + TMS_PLATE_NUMBER + '\'' +
                ", TMS_DRIVER_TEL='" + TMS_DRIVER_TEL + '\'' +
                ", PAYMENT_TYPE='" + PAYMENT_TYPE + '\'' +
                ", IS_SAAS='" + IS_SAAS + '\'' +
                ", ORG_PRICE=" + ORG_PRICE +
                ", ACT_PRICE=" + ACT_PRICE +
                ", MJ_PRICE=" + MJ_PRICE +
                ", ORD_REMARK_CONSIGNEE='" + ORD_REMARK_CONSIGNEE + '\'' +
                ", MJ_REMARK='" + MJ_REMARK + '\'' +
                ", DRIVER_PAY='" + DRIVER_PAY + '\'' +
                ", OrderDetails=" + OrderDetails +
                ", StateTack=" + StateTack +
                '}';
    }
}
