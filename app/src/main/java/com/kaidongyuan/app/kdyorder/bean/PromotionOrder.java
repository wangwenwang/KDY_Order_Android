package com.kaidongyuan.app.kdyorder.bean;

import java.util.List;


public class PromotionOrder implements java.io.Serializable {
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

    public int TOTAL_QTY;
    public double TOTAL_WEIGHT;
    public double TOTAL_VOLUME;

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

    public List<PromotionDetail> OrderDetails;

    //赠品用
    public String HAVE_GIFT;
    public List<OrderGift> GiftClasses;

    // 客户拜访ID 添加者 王文望 20190128
    public String VISIT_IDX;

}














