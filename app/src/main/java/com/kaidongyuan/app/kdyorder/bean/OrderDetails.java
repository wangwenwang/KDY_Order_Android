package com.kaidongyuan.app.kdyorder.bean;

/**
 * Order、PreOrder中的OrderDetail
 */
public  class OrderDetails implements java.io.Serializable {
	private String PRODUCT_NO;
	private String PRODUCT_NAME;
	private double ORDER_QTY;
	private String ORDER_UOM;
	private String ORDER_WEIGHT;
	private String ORDER_VOLUME;
	private String ISSUE_QTY;
	private String ISSUE_WEIGHT;
	private String ISSUE_VOLUME;
	private String PRODUCT_PRICE;
	private double ACT_PRICE;
	private String MJ_PRICE;
	private String MJ_REMARK;
	private double ORG_PRICE;
	private String PRODUCT_URL;
	private String PRODUCT_TYPE;

	private double PO_QTY;//PreOrder中的字段
	private String PO_UOM;//PreOrder中的字段
	private String PO_WEIGHT;//PreOrder中的字段
	private String PO_VOLUME;//PreOrder中的字段

	public String getPRODUCT_TYPE() {
		return PRODUCT_TYPE;
	}

	public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
		this.PRODUCT_TYPE = PRODUCT_TYPE;
	}

	public double getACT_PRICE() {
		return ACT_PRICE;
	}

	public void setACT_PRICE(double ACT_PRICE) {
		this.ACT_PRICE = ACT_PRICE;
	}

	public String getMJ_PRICE() {
		return MJ_PRICE;
	}

	public void setMJ_PRICE(String MJ_PRICE) {
		this.MJ_PRICE = MJ_PRICE;
	}

	public String getMJ_REMARK() {
		return MJ_REMARK;
	}

	public void setMJ_REMARK(String MJ_REMARK) {
		this.MJ_REMARK = MJ_REMARK;
	}

	public String getPRODUCT_URL() {
		return PRODUCT_URL;
	}

	public void setPRODUCT_URL(String PRODUCT_URL) {
		this.PRODUCT_URL = PRODUCT_URL;
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

	public double getORDER_QTY() {
		return ORDER_QTY;
	}

	public void setORDER_QTY(double ORDER_QTY) {
		this.ORDER_QTY = ORDER_QTY;
	}

	public double getORG_PRICE() {
		return ORG_PRICE;
	}

	public void setORG_PRICE(double ORG_PRICE) {
		this.ORG_PRICE = ORG_PRICE;
	}

	public String getORDER_UOM() {
		return ORDER_UOM;
	}

	public void setORDER_UOM(String ORDER_UOM) {
		this.ORDER_UOM = ORDER_UOM;
	}

	public String getORDER_WEIGHT() {
		return ORDER_WEIGHT;
	}

	public void setORDER_WEIGHT(String ORDER_WEIGHT) {
		this.ORDER_WEIGHT = ORDER_WEIGHT;
	}

	public String getORDER_VOLUME() {
		return ORDER_VOLUME;
	}

	public void setORDER_VOLUME(String ORDER_VOLUME) {
		this.ORDER_VOLUME = ORDER_VOLUME;
	}

	public String getISSUE_QTY() {
		return ISSUE_QTY;
	}

	public void setISSUE_QTY(String ISSUE_QTY) {
		this.ISSUE_QTY = ISSUE_QTY;
	}

	public String getISSUE_WEIGHT() {
		return ISSUE_WEIGHT;
	}

	public void setISSUE_WEIGHT(String ISSUE_WEIGHT) {
		this.ISSUE_WEIGHT = ISSUE_WEIGHT;
	}

	public String getISSUE_VOLUME() {
		return ISSUE_VOLUME;
	}

	public void setISSUE_VOLUME(String ISSUE_VOLUME) {
		this.ISSUE_VOLUME = ISSUE_VOLUME;
	}

	public String getPRODUCT_PRICE() {
		return PRODUCT_PRICE;
	}

	public void setPRODUCT_PRICE(String PRODUCT_PRICE) {
		this.PRODUCT_PRICE = PRODUCT_PRICE;
	}

	public double getPO_QTY() {
		return PO_QTY;
	}

	public void setPO_QTY(double PO_QTY) {
		this.PO_QTY = PO_QTY;
	}

	public String getPO_UOM() {
		return PO_UOM;
	}

	public void setPO_UOM(String PO_UOM) {
		this.PO_UOM = PO_UOM;
	}

	public String getPO_WEIGHT() {
		return PO_WEIGHT;
	}

	public void setPO_WEIGHT(String PO_WEIGHT) {
		this.PO_WEIGHT = PO_WEIGHT;
	}

	public String getPO_VOLUME() {
		return PO_VOLUME;
	}

	public void setPO_VOLUME(String PO_VOLUME) {
		this.PO_VOLUME = PO_VOLUME;
	}

	@Override
	public String toString() {
		return "OrderDetails{" +
				"PRODUCT_NO='" + PRODUCT_NO + '\'' +
				", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
				", ORDER_QTY=" + ORDER_QTY +
				", ORDER_UOM='" + ORDER_UOM + '\'' +
				", ORDER_WEIGHT='" + ORDER_WEIGHT + '\'' +
				", ORDER_VOLUME='" + ORDER_VOLUME + '\'' +
				", ISSUE_QTY='" + ISSUE_QTY + '\'' +
				", ISSUE_WEIGHT='" + ISSUE_WEIGHT + '\'' +
				", ISSUE_VOLUME='" + ISSUE_VOLUME + '\'' +
				", PRODUCT_PRICE='" + PRODUCT_PRICE + '\'' +
				", ACT_PRICE=" + ACT_PRICE +
				", MJ_PRICE='" + MJ_PRICE + '\'' +
				", MJ_REMARK='" + MJ_REMARK + '\'' +
				", ORG_PRICE=" + ORG_PRICE +
				", PRODUCT_URL='" + PRODUCT_URL + '\'' +
				", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
				", PO_QTY='" + PO_QTY + '\'' +
				", PO_UOM='" + PO_UOM + '\'' +
				", PO_WEIGHT='" + PO_WEIGHT + '\'' +
				", PO_VOLUME='" + PO_VOLUME + '\'' +
				'}';
	}
}
