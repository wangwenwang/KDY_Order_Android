package com.kaidongyuan.app.kdyorder.bean;

/**
 * 用户信息实体类
 */
public class User implements java.io.Serializable {

	/**
	 * 用户名
	 */
	private String USER_NAME;
	/**
	 * 用户类型
	 */
	private String USER_TYPE;
	/**
	 * 用户手机号
	 */
	private String USER_CODE;
	/**
	 * IDX
	 */
	private String IDX;

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String USER_NAME) {
		this.USER_NAME = USER_NAME;
	}

	public String getUSER_TYPE() {
		return USER_TYPE;
	}

	public void setUSER_TYPE(String USER_TYPE) {
		this.USER_TYPE = USER_TYPE;
	}

	public String getUSER_CODE() {
		return USER_CODE;
	}

	public void setUSER_CODE(String USER_CODE) {
		this.USER_CODE = USER_CODE;
	}

	public String getIDX() {
		return IDX;
	}

	public void setIDX(String IDX) {
		this.IDX = IDX;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (USER_NAME != null ? !USER_NAME.equals(user.USER_NAME) : user.USER_NAME != null)
			return false;
		if (USER_TYPE != null ? !USER_TYPE.equals(user.USER_TYPE) : user.USER_TYPE != null)
			return false;
		if (USER_CODE != null ? !USER_CODE.equals(user.USER_CODE) : user.USER_CODE != null)
			return false;
		return !(IDX != null ? !IDX.equals(user.IDX) : user.IDX != null);

	}

	@Override
	public int hashCode() {
		int result = USER_NAME != null ? USER_NAME.hashCode() : 0;
		result = 31 * result + (USER_TYPE != null ? USER_TYPE.hashCode() : 0);
		result = 31 * result + (USER_CODE != null ? USER_CODE.hashCode() : 0);
		result = 31 * result + (IDX != null ? IDX.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User{" +
				"USER_NAME='" + USER_NAME + '\'' +
				", USER_TYPE='" + USER_TYPE + '\'' +
				", USER_CODE='" + USER_CODE + '\'' +
				", IDX='" + IDX + '\'' +
				'}';
	}
}
