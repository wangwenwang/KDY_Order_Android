package com.kaidongyuan.app.kdyorder.bean;
// default package


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 订单产品信息
 */
public class StockProduct implements Parcelable {
    private long IDX;
    private String BUSINESS_IDX;
    private String PRODUCT_NO;
    private String PRODUCT_NAME;
    private String PRODUCT_DESC;
    private String PRODUCT_BARCODE;
    private String PRODUCT_TYPE;
    private String PRODUCT_CLASS;
    private double PRODUCT_PRICE;
    private String PRODUCT_URL;
    private double PRODUCT_VOLUME;
    private double PRODUCT_WEIGHT;
    private List<StockProductCount> PRODUCT_POLICY;//产品库存条目集合
    private String ISINVENTORY="N";//是否考虑库存情况
    private int PRODUCT_INVENTORY;//产品库存数目
    /**
     * 根据用户选择的支付类型设置的产品价格
     */
    private double PRODUCT_CURRENT_PRICE;
    /**
     * 用户下单时选择的数量
     */
    private int CHOICED_SIZE = 0;

    public StockProduct(){

    }

    public String getISINVENTORY() {
        return ISINVENTORY;
    }

    public void setISINVENTORY(String ISINVENTORY) {
        this.ISINVENTORY = ISINVENTORY;
    }

    public int getPRODUCT_INVENTORY() {
        return PRODUCT_INVENTORY;
    }

    public void setPRODUCT_INVENTORY(int PRODUCT_INVENTORY) {
        this.PRODUCT_INVENTORY = PRODUCT_INVENTORY;
    }

    public int getCHOICED_SIZE() {
        return CHOICED_SIZE;
    }

    public void setCHOICED_SIZE(int CHOICED_SIZE) {
        this.CHOICED_SIZE = CHOICED_SIZE;
    }

    public double getPRODUCT_VOLUME() {
        return PRODUCT_VOLUME;
    }

    public void setPRODUCT_VOLUME(double PRODUCT_VOLUME) {
        this.PRODUCT_VOLUME = PRODUCT_VOLUME;
    }

    public double getPRODUCT_WEIGHT() {
        return PRODUCT_WEIGHT;
    }

    public void setPRODUCT_WEIGHT(double PRODUCT_WEIGHT) {
        this.PRODUCT_WEIGHT = PRODUCT_WEIGHT;
    }

    public List<StockProductCount> getPRODUCT_POLICY() {
        return PRODUCT_POLICY;
    }

    public void setPRODUCT_POLICY(List<StockProductCount> PRODUCT_POLICY) {
        this.PRODUCT_POLICY = PRODUCT_POLICY;
    }

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
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

    public String getPRODUCT_BARCODE() {
        return PRODUCT_BARCODE;
    }

    public void setPRODUCT_BARCODE(String PRODUCT_BARCODE) {
        this.PRODUCT_BARCODE = PRODUCT_BARCODE;
    }

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

    public double getPRODUCT_PRICE() {
        return PRODUCT_PRICE;
    }

    public void setPRODUCT_PRICE(double PRODUCT_PRICE) {
        this.PRODUCT_PRICE = PRODUCT_PRICE;
    }

    public String getPRODUCT_URL() {
        return PRODUCT_URL;
    }

    public void setPRODUCT_URL(String PRODUCT_URL) {
        this.PRODUCT_URL = PRODUCT_URL;
    }

    public double getPRODUCT_CURRENT_PRICE() {
        return PRODUCT_CURRENT_PRICE;
    }

    public void setPRODUCT_CURRENT_PRICE(double PRODUCT_CURRENT_PRICE) {
        this.PRODUCT_CURRENT_PRICE = PRODUCT_CURRENT_PRICE;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(IDX);
        dest.writeString(BUSINESS_IDX);
        dest.writeString(PRODUCT_NO);
        dest.writeString(PRODUCT_NAME);
        dest.writeString(PRODUCT_DESC);
        dest.writeString(PRODUCT_BARCODE);
        dest.writeString(PRODUCT_TYPE);
        dest.writeString(PRODUCT_CLASS);
        dest.writeDouble(PRODUCT_PRICE);
        dest.writeDouble(PRODUCT_VOLUME);
        dest.writeDouble(PRODUCT_WEIGHT);
        dest.writeString(PRODUCT_URL);
        dest.writeList(PRODUCT_POLICY);
        dest.writeString(ISINVENTORY);
        dest.writeInt(PRODUCT_INVENTORY);
        dest.writeInt(CHOICED_SIZE);
        dest.writeDouble(PRODUCT_CURRENT_PRICE);
    }

    public static final Creator<StockProduct> CREATOR = new Creator<StockProduct>() {
        @Override
        public StockProduct[] newArray(int size) {
            return new StockProduct[size];
        }

        @Override
        public StockProduct createFromParcel(Parcel in) {
            return new StockProduct(in);
        }
    };

    public StockProduct(Parcel in) {
        IDX = in.readLong();
        BUSINESS_IDX = in.readString();
        PRODUCT_NO = in.readString();
        PRODUCT_NAME = in.readString();
        PRODUCT_DESC = in.readString();
        PRODUCT_BARCODE = in.readString();
        PRODUCT_TYPE = in.readString();
        PRODUCT_CLASS = in.readString();
        PRODUCT_PRICE = in.readDouble();
        PRODUCT_VOLUME = in.readDouble();
        PRODUCT_WEIGHT = in.readDouble();
        PRODUCT_URL = in.readString();
        PRODUCT_POLICY = in.readArrayList(ProductPolicy.class.getClassLoader());
        ISINVENTORY=in.readString();
        PRODUCT_INVENTORY=in.readInt();
        CHOICED_SIZE=in.readInt();
        PRODUCT_CURRENT_PRICE=in.readDouble();
    }

    @Override
    public String toString() {
        return "Product{" +
                "IDX=" + IDX +
//                ", BUSINESS_IDX='" + BUSINESS_IDX + '\'' +
//                ", PRODUCT_NO='" + PRODUCT_NO + '\'' +
                ", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
//                ", PRODUCT_DESC='" + PRODUCT_DESC + '\'' +
//                ", PRODUCT_BARCODE='" + PRODUCT_BARCODE + '\'' +
//                ", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
//                ", PRODUCT_CLASS='" + PRODUCT_CLASS + '\'' +
//                ", PRODUCT_PRICE=" + PRODUCT_PRICE +
//                ", PRODUCT_URL='" + PRODUCT_URL + '\'' +
//                ", PRODUCT_VOLUME=" + PRODUCT_VOLUME +
//                ", PRODUCT_WEIGHT=" + PRODUCT_WEIGHT +
//                ", PRODUCT_POLICY=" + PRODUCT_POLICY +
//                ", ISINVENTORY='" + ISINVENTORY + '\'' +
//                ", PRODUCT_INVENTORY=" + PRODUCT_INVENTORY +
                '}';
    }
}