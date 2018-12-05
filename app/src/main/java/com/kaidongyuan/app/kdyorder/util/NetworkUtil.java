package com.kaidongyuan.app.kdyorder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.kaidongyuan.app.kdyorder.app.MyApplication;

/**
 * 网络工具类
 * 
 * @author gdpancheng@gmail.com 2013-10-22 下午1:08:35
 */
public class NetworkUtil {
	/**
	 * 检测手机是否开启GPRS网络,需要调用ConnectivityManager,TelephonyManager 服务.
	 * @return boolean GPRS 是否开启
	 */
	public static boolean checkGprsNetwork() {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) MyApplication.getAppContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) MyApplication.getAppContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			has = info.isConnected();
		}
		return has;

	}

	/**
	 * 检测当前手机是否联网
	 * @return boolean 网络是否开启
	 */
	public static boolean isNetworkAvailable() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getAppContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}


}
