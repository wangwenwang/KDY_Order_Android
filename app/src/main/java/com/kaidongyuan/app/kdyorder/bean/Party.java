package com.kaidongyuan.app.kdyorder.bean;
// default package


/**
 *客户资料
 */
public class Party implements java.io.Serializable {
	private String IDX;
	private String PARTY_CODE;
	private String PARTY_NAME;
	private String PARTY_PROPERTY;
	private String PARTY_CLASS;
	private String PARTY_TYPE;
	private String PARTY_COUNTRY;
	private String PARTY_PROVINCE;
	private String PARTY_CITY;
	private String PARTY_REMARK;

	public String getIDX() {
		return IDX;
	}

	public void setIDX(String IDX) {
		this.IDX = IDX;
	}

	public String getPARTY_CODE() {
		return PARTY_CODE;
	}

	public void setPARTY_CODE(String PARTY_CODE) {
		this.PARTY_CODE = PARTY_CODE;
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

	public String getPARTY_CLASS() {
		return PARTY_CLASS;
	}

	public void setPARTY_CLASS(String PARTY_CLASS) {
		this.PARTY_CLASS = PARTY_CLASS;
	}

	public String getPARTY_TYPE() {
		return PARTY_TYPE;
	}

	public void setPARTY_TYPE(String PARTY_TYPE) {
		this.PARTY_TYPE = PARTY_TYPE;
	}

	public String getPARTY_COUNTRY() {
		return PARTY_COUNTRY;
	}

	public void setPARTY_COUNTRY(String PARTY_COUNTRY) {
		this.PARTY_COUNTRY = PARTY_COUNTRY;
	}

	public String getPARTY_PROVINCE() {
		return PARTY_PROVINCE;
	}

	public void setPARTY_PROVINCE(String PARTY_PROVINCE) {
		this.PARTY_PROVINCE = PARTY_PROVINCE;
	}

	public String getPARTY_CITY() {
		return PARTY_CITY;
	}

	public void setPARTY_CITY(String PARTY_CITY) {
		this.PARTY_CITY = PARTY_CITY;
	}

	public String getPARTY_REMARK() {
		return PARTY_REMARK;
	}

	public void setPARTY_REMARK(String PARTY_REMARK) {
		this.PARTY_REMARK = PARTY_REMARK;
	}

	@Override
	public String toString() {
		return "Party{" +
				"IDX='" + IDX + '\'' +
				", PARTY_CODE='" + PARTY_CODE + '\'' +
				", PARTY_NAME='" + PARTY_NAME + '\'' +
				", PARTY_PROPERTY='" + PARTY_PROPERTY + '\'' +
				", PARTY_CLASS='" + PARTY_CLASS + '\'' +
				", PARTY_TYPE='" + PARTY_TYPE + '\'' +
				", PARTY_COUNTRY='" + PARTY_COUNTRY + '\'' +
				", PARTY_PROVINCE='" + PARTY_PROVINCE + '\'' +
				", PARTY_CITY='" + PARTY_CITY + '\'' +
				", PARTY_REMARK='" + PARTY_REMARK + '\'' +
				'}';
	}
}