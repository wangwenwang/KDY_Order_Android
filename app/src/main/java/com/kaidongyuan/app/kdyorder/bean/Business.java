package com.kaidongyuan.app.kdyorder.bean;
// default package


/**
 * 业务代码
 */
public class Business implements java.io.Serializable {
    /**
     * IDX
     */
    private String BUSINESS_IDX;
    /**
     *
     */
    private String BUSINESS_CODE;
    /**
     * 业务名称
     */
    private String BUSINESS_NAME;

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
    }

    public String getBUSINESS_CODE() {
        return BUSINESS_CODE;
    }

    public void setBUSINESS_CODE(String BUSINESS_CODE) {
        this.BUSINESS_CODE = BUSINESS_CODE;
    }

    public String getBUSINESS_NAME() {
        return BUSINESS_NAME;
    }

    public void setBUSINESS_NAME(String BUSINESS_NAME) {
        this.BUSINESS_NAME = BUSINESS_NAME;
    }

    @Override
    public String toString() {
        return "Business{" +
                "BUSINESS_IDX='" + BUSINESS_IDX + '\'' +
                ", BUSINESS_CODE='" + BUSINESS_CODE + '\'' +
                ", BUSINESS_NAME='" + BUSINESS_NAME + '\'' +
                '}';
    }
}












