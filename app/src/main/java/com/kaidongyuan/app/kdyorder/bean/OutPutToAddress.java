package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 出库收货人地址信息
 * Created by ${tom} on 2017/9/22.
 */
//public class OutPutToAddress {
//    private String IDX;
//    private String ITEM_CODE;//	客户代码
//    private String PARTY_NAME;//	客户名称
//    private String ADDRESS_INFO;//	客户地址
//    private String CONTACT_PERSON;
//    private String CONTACT_TEL;
//
//    public String getIDX() {
//        return IDX;
//    }
//
//    public void setIDX(String IDX) {
//        this.IDX = IDX;
//    }
//
//    public String getITEM_CODE() {
//        return ITEM_CODE;
//    }
//
//    public void setITEM_CODE(String ITEM_CODE) {
//        this.ITEM_CODE = ITEM_CODE;
//    }
//
//    public String getPARTY_NAME() {
//        return PARTY_NAME;
//    }
//
//    public void setPARTY_NAME(String PARTY_NAME) {
//        this.PARTY_NAME = PARTY_NAME;
//    }
//
//    public String getADDRESS_INFO() {
//        return ADDRESS_INFO;
//    }
//
//    public void setADDRESS_INFO(String ADDRESS_INFO) {
//        this.ADDRESS_INFO = ADDRESS_INFO;
//    }
//
//    public String getCONTACT_PERSON() {
//        return CONTACT_PERSON;
//    }
//
//    public void setCONTACT_PERSON(String CONTACT_PERSON) {
//        this.CONTACT_PERSON = CONTACT_PERSON;
//    }
//
//    public String getCONTACT_TEL() {
//        return CONTACT_TEL;
//    }
//
//    public void setCONTACT_TEL(String CONTACT_TEL) {
//        this.CONTACT_TEL = CONTACT_TEL;
//    }
//}

/**
 * 出库收货人地址信息（由Parcelable改写，内容一致）
 * Created by 王文望 on 2018/12/27.
 */
public class OutPutToAddress implements Parcelable {
    private String IDX;
    private String ITEM_CODE;//	客户代码
    private String PARTY_NAME;//	客户名称
    private String ADDRESS_INFO;//	客户地址
    private String CONTACT_PERSON;
    private String CONTACT_TEL;
    public OutPutToAddress() {
    }

    protected OutPutToAddress(Parcel in) {
        IDX = in.readString();
        ITEM_CODE = in.readString();
        PARTY_NAME = in.readString();
        ADDRESS_INFO = in.readString();
        CONTACT_PERSON = in.readString();
        CONTACT_TEL = in.readString();
    }

    public static final Creator<OutPutToAddress> CREATOR = new Creator<OutPutToAddress>() {
        @Override
        public OutPutToAddress createFromParcel(Parcel in) {
            return new OutPutToAddress(in);
        }

        @Override
        public OutPutToAddress[] newArray(int size) {
            return new OutPutToAddress[size];
        }
    };



    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getITEM_CODE() {
        return ITEM_CODE;
    }

    public void setITEM_CODE(String ITEM_CODE) {
        this.ITEM_CODE = ITEM_CODE;
    }

    public String getPARTY_NAME() {
        return PARTY_NAME;
    }

    public void setPARTY_NAME(String PARTY_NAME) {
        this.PARTY_NAME = PARTY_NAME;
    }

    public String getADDRESS_INFO() {
        return ADDRESS_INFO;
    }

    public void setADDRESS_INFO(String ADDRESS_INFO) {
        this.ADDRESS_INFO = ADDRESS_INFO;
    }

    public String getCONTACT_PERSON() {
        return CONTACT_PERSON;
    }

    public void setCONTACT_PERSON(String CONTACT_PERSON) {
        this.CONTACT_PERSON = CONTACT_PERSON;
    }

    public String getCONTACT_TEL() {
        return CONTACT_TEL;
    }

    public void setCONTACT_TEL(String CONTACT_TEL) {
        this.CONTACT_PERSON = CONTACT_TEL;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IDX);
        dest.writeString(ITEM_CODE);
        dest.writeString(PARTY_NAME);
        dest.writeString(ADDRESS_INFO);
        dest.writeString(CONTACT_PERSON);
        dest.writeString(CONTACT_TEL);
    }
}
