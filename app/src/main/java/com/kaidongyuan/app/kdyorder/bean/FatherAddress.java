package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FatherAddress  implements Parcelable {
    private String ADDRESS_ALIAS;
    private String ADDRESS_CODE;
    private String ADDRESS_IDX;
    private String ADDRESS_INFO;
    private String CONTACT_PERSON;
    private String CONTACT_TEL;
    private String IDX;
    private String PARTY_CITY;
    private String PARTY_CLASS;
    private String PARTY_CODE;
    private String PARTY_COUNTRY;
    private String PARTY_NAME;
    private String PARTY_PROPERTY;
    public FatherAddress() {
    }

    protected FatherAddress(Parcel in) {
        ADDRESS_ALIAS = in.readString();
        ADDRESS_CODE = in.readString();
        ADDRESS_IDX = in.readString();
        ADDRESS_INFO = in.readString();
        CONTACT_PERSON = in.readString();
        CONTACT_TEL = in.readString();
        IDX = in.readString();
        PARTY_CITY = in.readString();
        PARTY_CLASS = in.readString();
        PARTY_CODE = in.readString();
        PARTY_COUNTRY = in.readString();
        PARTY_NAME = in.readString();
        PARTY_PROPERTY = in.readString();
    }

    public static final Creator<FatherAddress> CREATOR = new Creator<FatherAddress>() {
        @Override
        public FatherAddress createFromParcel(Parcel in) {
            return new FatherAddress(in);
        }

        @Override
        public FatherAddress[] newArray(int size) {
            return new FatherAddress[size];
        }
    };



    public String getADDRESS_ALIAS() {
        return ADDRESS_ALIAS;
    }
    public void setADDRESS_ALIAS(String ADDRESS_ALIAS) {
        this.ADDRESS_ALIAS = ADDRESS_ALIAS;
    }

    public String getADDRESS_CODE() {
        return ADDRESS_CODE;
    }
    public void setADDRESS_CODE(String ADDRESS_CODE) {
        this.ADDRESS_CODE = ADDRESS_CODE;
    }

    public String getADDRESS_IDX() {
        return ADDRESS_IDX;
    }
    public void setADDRESS_IDX(String ADDRESS_IDX) {
        this.ADDRESS_IDX = ADDRESS_IDX;
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
        this.CONTACT_TEL = CONTACT_TEL;
    }

    public String getIDX() {
        return IDX;
    }
    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getPARTY_CITY() {
        return PARTY_CITY;
    }
    public void setPARTY_CITY(String PARTY_CITY) {
        this.PARTY_CITY = PARTY_CITY;
    }

    public String getPARTY_CLASS() {
        return PARTY_CLASS;
    }
    public void setPARTY_CLASS(String PARTY_CLASS) {
        this.PARTY_CLASS = PARTY_CLASS;
    }

    public String getPARTY_CODE() {
        return PARTY_CODE;
    }
    public void setPARTY_CODE(String PARTY_CODE) {
        this.PARTY_CODE = PARTY_CODE;
    }

    public String getPARTY_COUNTRY() {
        return PARTY_COUNTRY;
    }
    public void setPARTY_COUNTRY(String PARTY_COUNTRY) {
        this.PARTY_COUNTRY = PARTY_COUNTRY;
    }

    public String getPARTY_NAME() {
        return PARTY_NAME;
    }
    public void setPARTY_NAME(String PARTY_NAME) {
        this.PARTY_NAME = PARTY_NAME;
    }

    public String getPARTY_PROPERTY() {
        return PARTY_PROPERTY;
    }
    public void setPARTY_PROPERTY(String PARTY_PROPERTY) {
        this.PARTY_PROPERTY = PARTY_PROPERTY;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(ADDRESS_ALIAS);
        dest.writeString(ADDRESS_CODE);
        dest.writeString(ADDRESS_IDX);
        dest.writeString(ADDRESS_INFO);
        dest.writeString(CONTACT_PERSON);
        dest.writeString(CONTACT_TEL);
        dest.writeString(IDX);
        dest.writeString(PARTY_CITY);
        dest.writeString(PARTY_CLASS);
        dest.writeString(PARTY_CODE);
        dest.writeString(PARTY_COUNTRY);
        dest.writeString(PARTY_NAME);
        dest.writeString(PARTY_PROPERTY);
    }
}