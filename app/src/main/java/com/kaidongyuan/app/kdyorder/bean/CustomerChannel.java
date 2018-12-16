package com.kaidongyuan.app.kdyorder.bean;

public class CustomerChannel implements java.io.Serializable {
    private String IDX;
    private String ITEM_NAME;

    public CustomerChannel() {
    }

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }
}
