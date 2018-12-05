package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/19.
 */
public class ProductStock implements Serializable {
    private String IDX;//ID号
    private String ENT_IDX;//企业ID号
    private String BUSINESS_IDX;//业务代码
    private String ADDRESS_IDX;//客户地址id
    private String ADDRESS_CODE;//客户地址代码
    private String ADDRESS_NAME;//客户地址名称
    private String PRODUCT_IDX;//产品id
    private String PRODUCT_NO;//产品号
    private String PRODUCT_NAME;//产品名
    private String STOCK_NO	;//	批次
    private String STOCK_QTY;//	库存数量
    private String STOCK_UOM;//	库存量单位
    private String AB_WORKFLOW;//货物状态
    private String PRODUCT_STATE;//生产日期
    private String BATCH_NUMBER;//批号
    private String PRICE;//	库存产品单价
    private String SUM;//库存产品金额
    private String ADD_DATE;//	创建时间
    private String EDIT_DATE;//	修改时间
    private String OPERATOR_NAME;//	操作人名
    private String CHANGE_DATE;//操作时间

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

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
    }

    public String getADDRESS_IDX() {
        return ADDRESS_IDX;
    }

    public void setADDRESS_IDX(String ADDRESS_IDX) {
        this.ADDRESS_IDX = ADDRESS_IDX;
    }

    public String getADDRESS_CODE() {
        return ADDRESS_CODE;
    }

    public void setADDRESS_CODE(String ADDRESS_CODE) {
        this.ADDRESS_CODE = ADDRESS_CODE;
    }

    public String getADDRESS_NAME() {
        return ADDRESS_NAME;
    }

    public void setADDRESS_NAME(String ADDRESS_NAME) {
        this.ADDRESS_NAME = ADDRESS_NAME;
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

    public String getSTOCK_NO() {
        return STOCK_NO;
    }

    public void setSTOCK_NO(String STOCK_NO) {
        this.STOCK_NO = STOCK_NO;
    }

    public String getSTOCK_QTY() {
        return STOCK_QTY;
    }

    public void setSTOCK_QTY(String STOCK_QTY) {
        this.STOCK_QTY = STOCK_QTY;
    }

    public String getSTOCK_UOM() {
        return STOCK_UOM;
    }

    public void setSTOCK_UOM(String STOCK_UOM) {
        this.STOCK_UOM = STOCK_UOM;
    }

    public String getAB_WORKFLOW() {
        return AB_WORKFLOW;
    }

    public void setAB_WORKFLOW(String AB_WORKFLOW) {
        this.AB_WORKFLOW = AB_WORKFLOW;
    }

    public String getPRODUCT_STATE() {
        return PRODUCT_STATE;
    }

    public void setPRODUCT_STATE(String PRODUCT_STATE) {
        this.PRODUCT_STATE = PRODUCT_STATE;
    }

    public String getBATCH_NUMBER() {
        return BATCH_NUMBER;
    }

    public void setBATCH_NUMBER(String BATCH_NUMBER) {
        this.BATCH_NUMBER = BATCH_NUMBER;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getSUM() {
        return SUM;
    }

    public void setSUM(String SUM) {
        this.SUM = SUM;
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

    public String getOPERATOR_NAME() {
        return OPERATOR_NAME;
    }

    public void setOPERATOR_NAME(String OPERATOR_NAME) {
        this.OPERATOR_NAME = OPERATOR_NAME;
    }

    public String getCHANGE_DATE() {
        return CHANGE_DATE;
    }

    public void setCHANGE_DATE(String CHANGE_DATE) {
        this.CHANGE_DATE = CHANGE_DATE;
    }
}
