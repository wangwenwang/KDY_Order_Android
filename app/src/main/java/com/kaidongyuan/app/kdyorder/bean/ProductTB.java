package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/21.
 * 产品的品牌、分类信息类
 */
public class ProductTB implements Serializable {
    private long IDX;
    private String PRODUCT_TYPE;
    private String PRODUCT_CLASS;

    public long getIDX() {
        return IDX;
    }

    public void setIDX(long IDX) {
        this.IDX = IDX;
    }

    public String getPRODUCT_TYPE() {
        return PRODUCT_TYPE;
    }

    public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
        this.PRODUCT_TYPE = PRODUCT_TYPE;
    }

    public String getPRODUCT_CLASS() {
        return PRODUCT_CLASS;
    }

    public void setPRODUCT_CLASS(String PRODUCT_CLASS) {
        this.PRODUCT_CLASS = PRODUCT_CLASS;
    }

    @Override
    public String toString() {
        return "ProductTB{" +
                "IDX=" + IDX +
                ", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
                ", PRODUCT_CLASS='" + PRODUCT_CLASS + '\'' +
                '}';
    }
}
