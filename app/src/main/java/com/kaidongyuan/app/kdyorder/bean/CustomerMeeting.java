package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CustomerMeeting implements Parcelable {
    private String IDX;
    private String PARTY_NO;
    private String PARTY_NAME;
    private String CONTACTS;//联系人
    private String CONTACTS_TEL;//联系电话
    private String PARTY_ADDRESS;//客户地址
    private String USER_NAME;
    private String USER_NO;
    private String CHANNEL;
    private String PARTY_LEVEL;
    private String WEEKLY_VISIT_FREQUENCY;//eg:两周一次
    private String VISIT_DATE;
    private String ADD_DATE;
    private String EDIT_DATE;
    private String ACTUAL_VISITING_ADDRESS;
    private String PARTY_STATES;
    private String NECESSARY_SKU;
    private String SINGLE_STORE_SALES;
    private String VISIT_THE_TARGET;
    private String REACH_THE_SITUATION;
    private String LINE;
    private String VISIT_STATES;
    private String CHECK_INVENTORY;
    private String RECOMMENDED_ORDER;
    private String VIVID_DISPLAY_CBX;
    private String VIVID_DISPLAY_TEXT;
    private String VISIT_IDX;
    private String ADDRESS_IDX;
    public CustomerMeeting() {
    }

    protected CustomerMeeting(Parcel in) {
        IDX = in.readString();
        PARTY_NO = in.readString();
        PARTY_NAME = in.readString();
        CONTACTS = in.readString();
        CONTACTS_TEL = in.readString();
        PARTY_ADDRESS = in.readString();
        USER_NAME = in.readString();
        USER_NO = in.readString();
        CHANNEL = in.readString();
        PARTY_LEVEL = in.readString();
        WEEKLY_VISIT_FREQUENCY = in.readString();
        VISIT_DATE = in.readString();
        ADD_DATE = in.readString();
        EDIT_DATE = in.readString();
        ACTUAL_VISITING_ADDRESS = in.readString();
        PARTY_STATES = in.readString();
        NECESSARY_SKU = in.readString();
        SINGLE_STORE_SALES = in.readString();
        VISIT_THE_TARGET = in.readString();
        REACH_THE_SITUATION = in.readString();
        LINE = in.readString();
        VISIT_STATES = in.readString();
        CHECK_INVENTORY = in.readString();
        RECOMMENDED_ORDER = in.readString();
        VIVID_DISPLAY_CBX = in.readString();
        VIVID_DISPLAY_TEXT = in.readString();
        VISIT_IDX = in.readString();
        ADDRESS_IDX = in.readString();
    }

    public static final Creator<CustomerMeeting> CREATOR = new Creator<CustomerMeeting>() {
        @Override
        public CustomerMeeting createFromParcel(Parcel in) {
            return new CustomerMeeting(in);
        }

        @Override
        public CustomerMeeting[] newArray(int size) {
            return new CustomerMeeting[size];
        }
    };



    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getPARTY_NO() {
        return PARTY_NO;
    }

    public void setPARTY_NO(String PARTY_NO) {
        this.PARTY_NO = PARTY_NO;
    }

    public String getPARTY_NAME() {
        return PARTY_NAME;
    }

    public void setPARTY_NAME(String PARTY_NAME) {
        this.PARTY_NAME = PARTY_NAME;
    }

    public String getCONTACTS() {
        return CONTACTS;
    }

    public void setCONTACTS(String CONTACTS) {
        this.CONTACTS = CONTACTS;
    }

    public String getCONTACTS_TEL() {
        return CONTACTS_TEL;
    }

    public void setCONTACTS_TEL(String CONTACTS_TEL) {
        this.CONTACTS_TEL = CONTACTS_TEL;
    }

    public String getPARTY_ADDRESS() {
        return PARTY_ADDRESS;
    }

    public void setPARTY_ADDRESS(String PARTY_ADDRESS) {
        this.PARTY_ADDRESS = PARTY_ADDRESS;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_NO() {
        return USER_NO;
    }

    public void setUSER_NO(String USER_NO) {
        this.USER_NO = USER_NO;
    }

    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    public String getPARTY_LEVEL() {
        return PARTY_LEVEL;
    }

    public void setPARTY_LEVEL(String PARTY_LEVEL) {
        this.PARTY_LEVEL = PARTY_LEVEL;
    }

    public String getWEEKLY_VISIT_FREQUENCY() {
        return WEEKLY_VISIT_FREQUENCY;
    }

    public void setWEEKLY_VISIT_FREQUENCY(String WEEKLY_VISIT_FREQUENCY) {
        this.WEEKLY_VISIT_FREQUENCY = WEEKLY_VISIT_FREQUENCY;
    }

    public String getVISIT_DATE() {
        return VISIT_DATE;
    }

    public void setVISIT_DATE(String VISIT_DATE) {
        this.VISIT_DATE = VISIT_DATE;
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

    public String getACTUAL_VISITING_ADDRESS() {
        return ACTUAL_VISITING_ADDRESS;
    }

    public void setACTUAL_VISITING_ADDRESS(String ACTUAL_VISITING_ADDRESS) {
        this.ACTUAL_VISITING_ADDRESS = ACTUAL_VISITING_ADDRESS;
    }

    public String getPARTY_STATES() {
        return PARTY_STATES;
    }

    public void setPARTY_STATES(String PARTY_STATES) {
        this.PARTY_STATES = PARTY_STATES;
    }

    public String getNECESSARY_SKU() {
        return NECESSARY_SKU;
    }

    public void setNECESSARY_SKU(String NECESSARY_SKU) {
        this.NECESSARY_SKU = NECESSARY_SKU;
    }

    public String getSINGLE_STORE_SALES() {
        return SINGLE_STORE_SALES;
    }

    public void setSINGLE_STORE_SALES(String SINGLE_STORE_SALES) {
        this.SINGLE_STORE_SALES = SINGLE_STORE_SALES;
    }

    public String getVISIT_THE_TARGET() {
        return VISIT_THE_TARGET;
    }

    public void setVISIT_THE_TARGET(String VISIT_THE_TARGET) {
        this.VISIT_THE_TARGET = VISIT_THE_TARGET;
    }

    public String getREACH_THE_SITUATION() {
        return REACH_THE_SITUATION;
    }

    public void setREACH_THE_SITUATION(String REACH_THE_SITUATION) {
        this.REACH_THE_SITUATION = REACH_THE_SITUATION;
    }

    public String getLINE() {
        return LINE;
    }

    public void setLINE(String LINE) {
        this.LINE = LINE;
    }

    public String getVISIT_STATES() {
        return VISIT_STATES;
    }

    public void setVISIT_STATES(String VISIT_STATES) {
        this.VISIT_STATES = VISIT_STATES;
    }

    public String getCHECK_INVENTORY() {
        return CHECK_INVENTORY;
    }

    public void setCHECK_INVENTORY(String CHECK_INVENTORY) {
        this.CHECK_INVENTORY = CHECK_INVENTORY;
    }

    public String getRECOMMENDED_ORDER() {
        return RECOMMENDED_ORDER;
    }

    public void setRECOMMENDED_ORDER(String RECOMMENDED_ORDER) {
        this.RECOMMENDED_ORDER = RECOMMENDED_ORDER;
    }

    public String getVIVID_DISPLAY_CBX() {
        return VIVID_DISPLAY_CBX;
    }

    public void setVIVID_DISPLAY_CBX(String VIVID_DISPLAY_CBX) {
        this.VIVID_DISPLAY_CBX = VIVID_DISPLAY_CBX;
    }

    public String getVIVID_DISPLAY_TEXT() {
        return VIVID_DISPLAY_TEXT;
    }

    public void setVIVID_DISPLAY_TEXT(String VIVID_DISPLAY_TEXT) {
        this.VIVID_DISPLAY_TEXT = VIVID_DISPLAY_TEXT;
    }

    public String getVISIT_IDX(){
        return VISIT_IDX;
    }

    public void setVISIT_IDX(String VISIT_IDX) {
        this.VISIT_IDX = VISIT_IDX;
    }

    public String getADDRESS_IDX(){
        return ADDRESS_IDX;
    }

    public void setADDRESS_IDX(String ADDRESS_IDX) {
        this.ADDRESS_IDX = ADDRESS_IDX;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IDX);
        dest.writeString(PARTY_NO);
        dest.writeString(PARTY_NAME);
        dest.writeString(CONTACTS);
        dest.writeString(CONTACTS_TEL);
        dest.writeString(PARTY_ADDRESS);
        dest.writeString(USER_NAME);
        dest.writeString(USER_NO);
        dest.writeString(CHANNEL);
        dest.writeString(PARTY_LEVEL);
        dest.writeString(WEEKLY_VISIT_FREQUENCY);
        dest.writeString(VISIT_DATE);
        dest.writeString(ADD_DATE);
        dest.writeString(EDIT_DATE);
        dest.writeString(ACTUAL_VISITING_ADDRESS);
        dest.writeString(PARTY_STATES);
        dest.writeString(NECESSARY_SKU);
        dest.writeString(SINGLE_STORE_SALES);
        dest.writeString(VISIT_THE_TARGET);
        dest.writeString(REACH_THE_SITUATION);
        dest.writeString(LINE);
        dest.writeString(VISIT_STATES);
        dest.writeString(CHECK_INVENTORY);
        dest.writeString(RECOMMENDED_ORDER);
        dest.writeString(VIVID_DISPLAY_CBX);
        dest.writeString(VIVID_DISPLAY_TEXT);
        dest.writeString(VISIT_IDX);
        dest.writeString(ADDRESS_IDX);
    }
}
