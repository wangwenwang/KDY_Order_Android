package com.kaidongyuan.app.kdyorder.bean;

import com.kaidongyuan.app.kdyorder.util.StringUtils;

/**
 * 出库订单详情的产品列表
 * Created by ${tom} on 2017/9/27.
 */
public class InPutOrderProduct {
    private String IDX;//	ID号
    private String ENT_IDX;//	企业ID号
    private String INPUT_IDX;//	出库ID号
    private String PRODUCT_TYPE;//	产品类型
    private String PRODUCT_IDX;//	产品IDX
    private String PRODUCT_NO;//	产品代码
    private String PRODUCT_NAME;//	产品名称
    private String PRODUCT_DESC;//	产品描述
    private String SUM;//	金额
    private String PRODUCT_WEIGHT;//	产品重量
    private String PRODUCT_VOLUME;//	产品体积
    private String LINE_NO;//	行号
    private double INPUT_QTY;//	出库数量
    private String INPUT_UOM;//	出库单位
    private String ORG_PRICE;//	产品原价
    private String ACT_PRICE;//	促销价
    private String SALE_REMARK;//	促销备注
    private String MJ_PRICE;//	满减价格
    private double PRICE;//
    private String MJ_REMARK;//	满减备注
    private String PRODUCTION_DATE;// 生产日期
    private String BATCH_NUMBER;//	批次号
    private String PRODUCT_STATE;//	货物状态
    private String ADD_DATE;//	新建时间
    private String EDIT_DATE;//	修改时间
    private String OPER_USER;//	操作人

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

    public String getINPUT_IDX() {
        return INPUT_IDX;
    }

    public void setINPUT_IDX(String INPUT_IDX) {
        this.INPUT_IDX = INPUT_IDX;
    }

    public String getPRODUCT_TYPE() {
        return PRODUCT_TYPE;
    }

    public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
        this.PRODUCT_TYPE = PRODUCT_TYPE;
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

    public String getPRODUCT_DESC() {
        return PRODUCT_DESC;
    }

    public void setPRODUCT_DESC(String PRODUCT_DESC) {
        this.PRODUCT_DESC = PRODUCT_DESC;
    }

    public String getSUM() {
        return SUM;
    }

    public void setSUM(String SUM) {
        this.SUM = SUM;
    }

    public String getPRODUCT_WEIGHT() {
        return PRODUCT_WEIGHT;
    }

    public void setPRODUCT_WEIGHT(String PRODUCT_WEIGHT) {
        this.PRODUCT_WEIGHT = PRODUCT_WEIGHT;
    }

    public String getPRODUCT_VOLUME() {
        return PRODUCT_VOLUME;
    }

    public void setPRODUCT_VOLUME(String PRODUCT_VOLUME) {
        this.PRODUCT_VOLUME = PRODUCT_VOLUME;
    }

    public String getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(String LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public double getINPUT_QTY() {
        return INPUT_QTY;
    }

    public void setINPUT_QTY(double INPUT_QTY) {
        this.INPUT_QTY = INPUT_QTY;
    }

    public String getINPUT_UOM() {
        return INPUT_UOM;
    }

    public void setINPUT_UOM(String INPUT_UOM) {
        this.INPUT_UOM = INPUT_UOM;
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

    public String getSALE_REMARK() {
        return SALE_REMARK;
    }

    public void setSALE_REMARK(String SALE_REMARK) {
        this.SALE_REMARK = SALE_REMARK;
    }

    public String getMJ_PRICE() {
        return MJ_PRICE;
    }

    public void setMJ_PRICE(String MJ_PRICE) {
        this.MJ_PRICE = MJ_PRICE;
    }

    public String getMJ_REMARK() {
        return MJ_REMARK;
    }

    public void setMJ_REMARK(String MJ_REMARK) {
        this.MJ_REMARK = MJ_REMARK;
    }

    public String getPRODUCTION_DATE() {
        return PRODUCTION_DATE;
    }

    public void setPRODUCTION_DATE(String PRODUCTION_DATE) {
        this.PRODUCTION_DATE = PRODUCTION_DATE;
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String BATCH_NUMBER) {
        this.BATCH_NUMBER = BATCH_NUMBER;
    }

    public String getPRODUCT_STATE() {
        return PRODUCT_STATE;
    }

    public void setPRODUCT_STATE(String PRODUCT_STATE) {
        this.PRODUCT_STATE = PRODUCT_STATE;
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

    public String getOPER_USER() {
        return OPER_USER;
    }

    public void setOPER_USER(String OPER_USER) {
        this.OPER_USER = OPER_USER;
    }

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }
}
