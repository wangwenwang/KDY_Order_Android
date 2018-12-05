package com.kaidongyuan.app.kdyorder.bean;
// default package


import java.util.Date;

/**
 *客户地址信息
 */
public class Location implements java.io.Serializable {
	private String id;
	private String userIdx;
	private Double CORDINATEX;
	private Double CORDINATEY;
	private String ADDRESS;
	private Date CREATETIME;

	public String getId() {
		return id;
	}

	public String getUserIdx() {
		return userIdx;
	}

	public Double getCORDINATEX() {
		return CORDINATEX;
	}

	public Double getCORDINATEY() {
		return CORDINATEY;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public Date getCREATETIME() {
		return CREATETIME;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserIdx(String userIdx) {
		this.userIdx = userIdx;
	}

	public void setCORDINATEX(Double CORDINATEX) {
		this.CORDINATEX = CORDINATEX;
	}

	public void setCORDINATEY(Double CORDINATEY) {
		this.CORDINATEY = CORDINATEY;
	}

	public void setADDRESS(String ADDRESS) {
		this.ADDRESS = ADDRESS;
	}

	public void setCREATETIME(Date CREATETIME) {
		this.CREATETIME = CREATETIME;
	}

	@Override
	public String toString() {
		return "Location{" +
				"id='" + id + '\'' +
				", userIdx='" + userIdx + '\'' +
				", CORDINATEX=" + CORDINATEX +
				", CORDINATEY=" + CORDINATEY +
				", ADDRESS='" + ADDRESS + '\'' +
				", CREATETIME=" + CREATETIME +
				'}';
	}
}