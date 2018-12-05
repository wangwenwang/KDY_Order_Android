package com.kaidongyuan.app.kdyorder.bean;

import java.io.Serializable;

/**
 * ${PEOJECT_NAME}
 * 业务代码仓库产品库存简表对象 GetWmsProductZong接口返回bean
 * Created by Administrator on 2017/9/19.
 */
public class BusinessProductStock implements Serializable {
    private String sku;//商品代码
    private String descr;//商品名称
    private String kesum;//可用数量
    private String QTY;//库存数
    private String susr2;//单位
    private String QTYALLOCATED;//已分配数量
    private String WeiQTYALLOCATED;//未配货需求

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


    public String getSusr2() {
        return susr2;
    }

    public void setSusr2(String susr2) {
        this.susr2 = susr2;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getQTYALLOCATED() {
        return QTYALLOCATED;
    }

    public void setQTYALLOCATED(String QTYALLOCATED) {
        this.QTYALLOCATED = QTYALLOCATED;
    }

    public String getWeiQTYALLOCATED() {
        return WeiQTYALLOCATED;
    }

    public void setWeiQTYALLOCATED(String weiQTYALLOCATED) {
        WeiQTYALLOCATED = weiQTYALLOCATED;
    }
}
