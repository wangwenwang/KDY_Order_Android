package com.kaidongyuan.app.kdyorder.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kaidongyuan.app.kdyorder.app.MyApplication;

import java.util.HashMap;

/**
 * SharePerfance 工具类
 */
public class SharedPreferencesUtil {

	/**
	 * 获取到的数据类型为 String 类型
	 */
	public static final int STRING = 0;
	/**
	 * 获取到的数据类型为 int 类型
	 */
	public static final int INT = 1;
	/**
	 * 获取到的数据类型为 boolean 类型
	 */
	public static final int BOOLEAN = 2;
	/**
	 * 获取到的数据类型为 long 类型
	 */
	public static final int LONG = 3;

	/**
	 * 清除 SharePerference 文件中的数据
	 * 清除 SharePerference 文件中的数据
	 * @param dataBasesName 要清除文件名
	 */
	public static void ClearSharedPreferences(String dataBasesName) {
		SharedPreferences user = MyApplication.getInstance().getSharedPreferences(dataBasesName, 0);
		Editor editor = user.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 从 SharePerference 移除单个属性
	 * @param dataBasesName 文件名
	 * @param key 属性名
	 */
	public static void removeSharedPreferences(String dataBasesName, String key) {
		SharedPreferences user = MyApplication.getInstance().getSharedPreferences(dataBasesName, 0);
		Editor editor = user.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 获取 SharePerference
	 * @param dataBasesName 文件名
	 * @return SharePerfance 数据
	 */
	public static SharedPreferences ReadSharedPreferences(String dataBasesName) {
		SharedPreferences user = MyApplication.getInstance().getSharedPreferences(dataBasesName, 0);
		return user;
	}

	public static HashMap<String, Object> getAllByBasesName(String dataBasesName) {
		SharedPreferences user = MyApplication.getInstance().getSharedPreferences(dataBasesName, 0);
		HashMap<String, Object> map = (HashMap<String, Object>) user.getAll();
		return map;
	}

	/**
	 * 从 SharePerference 中获取单个属性
	 * @param dataBasesName 文件名
	 * @param key 属性名
	 * @param type 需要获取到的数据类型，此工具类中的常量
	 * @param <T> 需要获取到的数据的类型
	 * @return 属性对应的内容
	 */
	public static <T> T getValueByName(String dataBasesName, String key, int type) {
		SharedPreferences user = MyApplication.getInstance().getSharedPreferences(dataBasesName, 0);
		Object value = null;
		switch (type) {
		case STRING:
			value = user.getString(key, "");
			break;
		case INT:
			value = user.getInt(key, 0);
			break;
		case BOOLEAN:
			value = user.getBoolean(key, false);
			break;
		case LONG:
			value = user.getLong(key, 0);
			break;
		}
		return (T) value;
	}

	/**
	 * 将数据写入到 SharePerference 中
	 * @param dataBasesName 文件名
	 * @param name 属性名
	 * @param value 属性值
	 */
	public static void WriteSharedPreferences(String dataBasesName,	String name, Object value) {
		if (name == null || value == null) {
			return;
		}
		SharedPreferences user = MyApplication.getInstance().getSharedPreferences(
				dataBasesName, 0);
		Editor editor = user.edit();
		if (value instanceof Integer) {
			editor.putInt(name, Integer.parseInt(value.toString()));
		} else if (value instanceof Long) {
			editor.putLong(name, Long.parseLong(value.toString()));
		} else if (value instanceof Boolean) {
			editor.putBoolean(name, Boolean.parseBoolean(value.toString()));
		} else if (value instanceof String) {
			editor.putString(name, value.toString());
		} else if (value instanceof Float) {
			editor.putFloat(name, Float.parseFloat(value.toString()));
		} 
		editor.apply();
	}

}
