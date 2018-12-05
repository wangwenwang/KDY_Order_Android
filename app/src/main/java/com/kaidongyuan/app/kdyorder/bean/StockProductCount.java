package com.kaidongyuan.app.kdyorder.bean;
// default package


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * 订单产品信息
 */
public class StockProductCount implements Parcelable {
    private String PRODUCTION_DATE;//产品库存数目
    /**
     * 用户下单时选择的数量
     */
    private int STOCK_QTY = 0;

    public StockProductCount() {
    }

    public static final Creator<StockProductCount> CREATOR = new Creator<StockProductCount>() {
        @Override
        public StockProductCount createFromParcel(Parcel in) {
            return new StockProductCount(in);
        }

        @Override
        public StockProductCount[] newArray(int size) {
            return new StockProductCount[size];
        }
    };

    public String getPRODUCTION_DATE() {
        return PRODUCTION_DATE;
    }

    public void setPRODUCTION_DATE(String PRODUCTION_DATE) {
        this.PRODUCTION_DATE = PRODUCTION_DATE;
    }

    public int getSTOCK_QTY() {
        return STOCK_QTY;
    }

    public void setSTOCK_QTY(int STOCK_QTY) {
        this.STOCK_QTY = STOCK_QTY;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PRODUCTION_DATE);
        dest.writeInt(STOCK_QTY);
    }
    public StockProductCount(Parcel in) {
        PRODUCTION_DATE = in.readString();
        STOCK_QTY = in.readInt();
    }

    @Override
    public String toString() {
        return "StockProductCount{" +
                "PRODUCTION_DATE='" + PRODUCTION_DATE + '\'' +
                ", STOCK_QTY=" + STOCK_QTY +
                '}';
    }
}