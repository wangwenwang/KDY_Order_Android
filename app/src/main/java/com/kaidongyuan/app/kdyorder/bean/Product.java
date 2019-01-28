package com.kaidongyuan.app.kdyorder.bean;
// default package


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 订单产品信息
 */
public class Product implements Parcelable {
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
    private List<ProductPolicy> PRODUCT_POLICY;
    private String ISINVENTORY="N";//是否考虑库存情况
    private int PRODUCT_INVENTORY;//产品库存数目
    //20170922 流通服务商经销商库存管理 所用 库存、单位
    private double STOCK_QTY;
    private String PRODUCT_UOM;
    // 20190128 客户拜访时，判断产品是否有折算率
    private double BASE_RATE;
    /**
     * 根据用户选择的支付类型设置的产品价格
     */
    private double PRODUCT_CURRENT_PRICE;
    /**
     * 用户下单时选择的数量
     */
    private int CHOICED_SIZE = 0;

    public Product(){

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

    public double getSTOCK_QTY() {
        return STOCK_QTY;
    }

    public void setSTOCK_QTY(double STOCK_QTY) {
        this.STOCK_QTY = STOCK_QTY;
    }

    public String getPRODUCT_UOM() {
        return PRODUCT_UOM;
    }

    public void setPRODUCT_UOM(String PRODUCT_UOM) {
        this.PRODUCT_UOM = PRODUCT_UOM;
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

    public List<ProductPolicy> getPRODUCT_POLICY() {
        return PRODUCT_POLICY;
    }

    public void setPRODUCT_POLICY(List<ProductPolicy> PRODUCT_POLICY) {
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
    public double getBASE_RATE() {
        return BASE_RATE;
    }

    public void setBASE_RATE(double BASE_RATE) {
        this.BASE_RATE = BASE_RATE;
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
        dest.writeDouble(STOCK_QTY);
        dest.writeString(PRODUCT_UOM);
        dest.writeDouble(PRODUCT_CURRENT_PRICE);
        dest.writeDouble(BASE_RATE);

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }

        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }
    };

    public Product(Parcel in) {
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
        STOCK_QTY=in.readDouble();
        PRODUCT_UOM=in.readString();
        PRODUCT_CURRENT_PRICE=in.readDouble();
        BASE_RATE=in.readDouble();
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