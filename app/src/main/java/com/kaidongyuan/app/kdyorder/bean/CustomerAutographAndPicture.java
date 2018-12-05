package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/8.
 * 客户签名和交货现场图片的实体类
 */
public class CustomerAutographAndPicture implements Serializable {

    /**
     * 签名图片
     */
    public static final String AUTOGRAPH = "Autograph";

    /**
     * 现场图片
     */
    public static final String PICTURE = "pricture";

    /**
     * 图片的 idx
     */
    private String IDX;

    /**
     * 订单的 idx
     */
    private String PRODUCT_IDX;

    /**
     * 图片的 Url
     */
    private String PRODUCT_URL;

    /**
     * 用于标记是签名还是交货现场图片
     * "Autograph" 客户签名
     * "pricture" 现场图片
     */
    private String REMARK;

    public void setIDX(String IDX){
        this.IDX = IDX;
    }
    public String getIDX(){
        return this.IDX;
    }
    public void setPRODUCT_IDX(String PRODUCT_IDX){
        this.PRODUCT_IDX = PRODUCT_IDX;
    }
    public String getPRODUCT_IDX(){
        return this.PRODUCT_IDX;
    }
    public void setPRODUCT_URL(String PRODUCT_URL){
        this.PRODUCT_URL = PRODUCT_URL;
    }
    public String getPRODUCT_URL(){
        return this.PRODUCT_URL;
    }
    public void setREMARK(String REMARK){
        this.REMARK = REMARK;
    }
    public String getREMARK(){
        return this.REMARK;
    }

    @Override
    public String toString() {
        return "CustomerAutographAndPicture{" +
                "IDX='" + IDX + '\'' +
                ", PRODUCT_IDX='" + PRODUCT_IDX + '\'' +
                ", PRODUCT_URL='" + PRODUCT_URL + '\'' +
                ", REMARK='" + REMARK + '\'' +
                '}';
    }
}


