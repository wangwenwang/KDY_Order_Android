package com.kaidongyuan.app.kdyorder.bean;
// default package


/**
 *客户地址信息
 */
public class Address implements java.io.Serializable {
	private String IDX;
	private String ADDRESS_CODE;
	private String ADDRESS_ALIAS;
	private String ADDRESS_REGION;
	private String ADDRESS_ZIP;
	private String ADDRESS_INFO;
	private String CONTACT_PERSON;
	private String CONTACT_TEL;
	private String CONTACT_FAX;
	private String CONTACT_SMS;
	private String ADDRESS_REMARK;
	private String COORDINATE;

	public String getIDX() {
		return IDX;
	}

	public void setIDX(String IDX) {
		this.IDX = IDX;
	}

	public String getADDRESS_CODE() {
		return ADDRESS_CODE;
	}

	public void setADDRESS_CODE(String ADDRESS_CODE) {
		this.ADDRESS_CODE = ADDRESS_CODE;
	}

	public String getADDRESS_ALIAS() {
		return ADDRESS_ALIAS;
	}

	public void setADDRESS_ALIAS(String ADDRESS_ALIAS) {
		this.ADDRESS_ALIAS = ADDRESS_ALIAS;
	}

	public String getADDRESS_REGION() {
		return ADDRESS_REGION;
	}

	public void setADDRESS_REGION(String ADDRESS_REGION) {
		this.ADDRESS_REGION = ADDRESS_REGION;
	}

	public String getADDRESS_ZIP() {
		return ADDRESS_ZIP;
	}

	public void setADDRESS_ZIP(String ADDRESS_ZIP) {
		this.ADDRESS_ZIP = ADDRESS_ZIP;
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

	public String getCONTACT_FAX() {
		return CONTACT_FAX;
	}

	public void setCONTACT_FAX(String CONTACT_FAX) {
		this.CONTACT_FAX = CONTACT_FAX;
	}

	public String getCONTACT_SMS() {
		return CONTACT_SMS;
	}

	public void setCONTACT_SMS(String CONTACT_SMS) {
		this.CONTACT_SMS = CONTACT_SMS;
	}

	public String getADDRESS_REMARK() {
		return ADDRESS_REMARK;
	}

	public void setADDRESS_REMARK(String ADDRESS_REMARK) {
		this.ADDRESS_REMARK = ADDRESS_REMARK;
	}

	public String getCOORDINATE() {
		return COORDINATE;
	}

	public void setCOORDINATE(String COORDINATE) {
		this.COORDINATE = COORDINATE;
	}

	@Override
	public String toString() {
		return "Address{" +
				"IDX='" + IDX + '\'' +
				", ADDRESS_CODE='" + ADDRESS_CODE + '\'' +
				", ADDRESS_ALIAS='" + ADDRESS_ALIAS + '\'' +
				", ADDRESS_REGION='" + ADDRESS_REGION + '\'' +
				", ADDRESS_ZIP='" + ADDRESS_ZIP + '\'' +
				", ADDRESS_INFO='" + ADDRESS_INFO + '\'' +
				", CONTACT_PERSON='" + CONTACT_PERSON + '\'' +
				", CONTACT_TEL='" + CONTACT_TEL + '\'' +
				", CONTACT_FAX='" + CONTACT_FAX + '\'' +
				", CONTACT_SMS='" + CONTACT_SMS + '\'' +
				", ADDRESS_REMARK='" + ADDRESS_REMARK + '\'' +
				", COORDINATE='" + COORDINATE + '\'' +
				'}';
	}
}