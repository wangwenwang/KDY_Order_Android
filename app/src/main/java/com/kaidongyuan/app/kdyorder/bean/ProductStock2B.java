package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/9/19.
 */


public class ProductStock2B implements Serializable {
    private String sku;//商品代码
    private String descr;//商品名称
    private String kesum;//可用数量
    private String QTY;//库存数
    private String susr2;//单位
    private String QTYALLOCATED;//已分配数量
    private String lottable04;//生产日期
    private String WeiQTYALLOCATED;//未配货需求
    private String Loc;//库位
    private double Casecnt	;//入数

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getKesum() {
        return kesum;
    }

    public void setKesum(String kesum) {
        this.kesum = kesum;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getSusr2() {
        return susr2;
    }

    public void setSusr2(String susr2) {
        this.susr2 = susr2;
    }

    public String getQTYALLOCATED() {
        return QTYALLOCATED;
    }

    public void setQTYALLOCATED(String QTYALLOCATED) {
        this.QTYALLOCATED = QTYALLOCATED;
    }

    public String getLottable04() {
        return lottable04;
    }

    public void setLottable04(String lottable04) {
        this.lottable04 = lottable04;
    }

    public String getWeiQTYALLOCATED() {
        return WeiQTYALLOCATED;
    }

    public void setWeiQTYALLOCATED(String weiQTYALLOCATED) {
        WeiQTYALLOCATED = weiQTYALLOCATED;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public double getCasecnt() {
        return Casecnt;
    }

    public void setCasecnt(double casecnt) {
        Casecnt = casecnt;
    }
}
