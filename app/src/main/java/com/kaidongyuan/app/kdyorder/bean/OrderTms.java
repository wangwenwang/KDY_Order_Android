package com.kaidongyuan.app.kdyorder.bean;


import java.util.List;

/**
 *客户地址信息
 */
public class OrderTms implements java.io.Serializable {
	private String IDX;
	private String ORD_TO_NAME;
	private String ORD_TO_ADDRESS;
	private String ORD_QTY;
	private String ORD_WEIGHT;
	private String ORD_VOLUME;
	private String TMS_QTY;
	private String TMS_WEIGHT;
	private String TMS_VOLUME;
	private List<Order> TmsList;

	public String getIDX() {
		return IDX;
	}

	public void setIDX(String IDX) {
		this.IDX = IDX;
	}

	public String getORD_TO_NAME() {
		return ORD_TO_NAME;
	}

	public void setORD_TO_NAME(String ORD_TO_NAME) {
		this.ORD_TO_NAME = ORD_TO_NAME;
	}

	public String getORD_TO_ADDRESS() {
		return ORD_TO_ADDRESS;
	}

	public void setORD_TO_ADDRESS(String ORD_TO_ADDRESS) {
		this.ORD_TO_ADDRESS = ORD_TO_ADDRESS;
	}

	public String getORD_QTY() {
		return ORD_QTY;
	}

	public void setORD_QTY(String ORD_QTY) {
		this.ORD_QTY = ORD_QTY;
	}

	public String getORD_WEIGHT() {
		return ORD_WEIGHT;
	}

	public void setORD_WEIGHT(String ORD_WEIGHT) {
		this.ORD_WEIGHT = ORD_WEIGHT;
	}

	public String getORD_VOLUME() {
		return ORD_VOLUME;
	}

	public void setORD_VOLUME(String ORD_VOLUME) {
		this.ORD_VOLUME = ORD_VOLUME;
	}

	public String getTMS_QTY() {
		return TMS_QTY;
	}

	public void setTMS_QTY(String TMS_QTY) {
		this.TMS_QTY = TMS_QTY;
	}

	public String getTMS_WEIGHT() {
		return TMS_WEIGHT;
	}

	public void setTMS_WEIGHT(String TMS_WEIGHT) {
		this.TMS_WEIGHT = TMS_WEIGHT;
	}

	public String getTMS_VOLUME() {
		return TMS_VOLUME;
	}

	public void setTMS_VOLUME(String TMS_VOLUME) {
		this.TMS_VOLUME = TMS_VOLUME;
	}

	public List<Order> getTmsList() {
		return TmsList;
	}

	public void setTmsList(List<Order> tmsList) {
		TmsList = tmsList;
	}

	@Override
	public String toString() {
		return "OrderTms{" +
				"IDX='" + IDX + '\'' +
				", ORD_TO_NAME='" + ORD_TO_NAME + '\'' +
				", ORD_TO_ADDRESS='" + ORD_TO_ADDRESS + '\'' +
				", ORD_QTY='" + ORD_QTY + '\'' +
				", ORD_WEIGHT='" + ORD_WEIGHT + '\'' +
				", ORD_VOLUME='" + ORD_VOLUME + '\'' +
				", TMS_QTY='" + TMS_QTY + '\'' +
				", TMS_WEIGHT='" + TMS_WEIGHT + '\'' +
				", TMS_VOLUME='" + TMS_VOLUME + '\'' +
				", TmsList=" + TmsList +
				'}';
	}
}