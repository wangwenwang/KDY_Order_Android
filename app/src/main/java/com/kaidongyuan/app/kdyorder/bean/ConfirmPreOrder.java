package com.kaidongyuan.app.kdyorder.bean;

import java.util.List;

/**
 * 计划订单下单最终提交的javabean
 */
//IDX	字符串	企业ID号		默认9008
//        ENT_IDX	字符串	组织ID号	是
//        ORG_IDX	字符串	组织idx	是
//        BUSINESS_IDX	字符串	业务id	是
//        ORD_NO	字符串	计划订单号
//        CONSIGNEE_REMARK	字符串	收货人备注
//        REQUEST_ISSUE	字符串	预计发货时间
//        REQUEST_DELIVERY	字符串	预计交付时间
//        OPERATOR_IDX	字符串	操作人ID号
//        TO_CODE	字符串	到达方代码
//        TO_NAME	字符串	到达方名称
//        TO_ADDRESS	字符串	到达方地址
//        TO_PROPERTY	字符串	到达方属性
//        TO_CNAME	字符串	到达方联系人
//
//        TO_CTEL	字符串	到达方联系电话
//
//        TO_CSMS	字符串	到达方短信号
//        TO_COUNTRY	字符串	到达方国家
//        TO_PROVINCE	字符串	到达方省份
//        TO_CITY	字符串	到达方城市
//        TO_REGION	字符串	到达方区域
//        TO_ZIP	字符串	到达方邮编
//        TO_IDX	字符串	到达方IDX	是
//    9    ORD_QTY	字符串	订单总数量	是
//    9    ORD_WEIGHT	字符串	订单总重量	是
//    9    ORD_VOLUME	字符串	订单总体积	是
//        ORG_PRICE	字符串	订单采购价格	是
//        ACT_PRICE	字符串	销售价格
//        ISFACTOR	字符串	是否保理		Y 和N
//        FACTOR_COMPANY	字符串	保理公司
//        WITH_MONEY	字符串	用款金额
//        UPDATE01	字符串	是否保理成功		Y 和N
public class ConfirmPreOrder implements java.io.Serializable {
    public long IDX;
    public long ENT_IDX;
    public long ORG_IDX;
    public String BUSINESS_IDX;
    public long BUSINESS_TYPE;

    public String PAYMENT_TYPE;

    public String GROUP_NO;
    public String ORD_NO;
    public String ORD_NO_CLIENT;
    public String ORD_NO_CONSIGNEE;
    public String ORD_STATE;

    public String REQUEST_ISSUE;
    public String REQUEST_DELIVERY;
    public String CONSIGNEE_REMARK;

    public double ORG_PRICE;
    public double ACT_PRICE;
    public double MJ_PRICE;
    public String MJ_REMARK;

    public int ORD_QTY;
    public double ORD_WEIGHT;
    public double ORD_VOLUME;

    public long OPERATOR_IDX;
    public String ADD_DATE;
    public String EDIT_DATE;

    public long FROM_IDX;
    public String FROM_CODE;
    public String FROM_NAME;
    public String FROM_ADDRESS;
    public String FROM_PROPERTY;
    public String FROM_CNAME;
    public String FROM_CTEL;
    public String FROM_CSMS;
    public String FROM_COUNTRY;
    public String FROM_PROVINCE;
    public String FROM_CITY;
    public String FROM_REGION;
    public String FROM_ZIP;

    public long TO_IDX;
    public String TO_CODE;
    public String TO_NAME;
    public String TO_ADDRESS;
    public String TO_PROPERTY;
    public String TO_CNAME;
    public String TO_CTEL;
    public String TO_CSMS;
    public String TO_COUNTRY;
    public String TO_PROVINCE;
    public String TO_CITY;
    public String TO_REGION;
    public String TO_ZIP;
    public String PARTY_IDX;
    public List<PromotionDetail> OrderPlanDetails;
    public String ISFACTOR;
    public String FACTOR_COMPANY;
    public String WITH_MONEY;
    public String UPDATE01;
}














