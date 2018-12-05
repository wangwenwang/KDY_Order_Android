package com.kaidongyuan.app.kdyorder.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.kaidongyuan.app.kdyorder.app.MyApplication;

public class DensityUtil {
	private static Context context;
	private static int width;
	private static int height;
	private static int width_dp;
	private static int height_dp;

	public static void checkContext() {
		if (DensityUtil.context==null) {
			DensityUtil.context = MyApplication.getAppContext();
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue) {
		checkContext();
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(float pxValue) {
		checkContext();
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getWidth() {
		checkContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;// 屏幕的宽px
		return width;
	}

	public static int getHeight() {
		checkContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		height = metrics.heightPixels;// 屏幕的高px
		return height;
	}

	public static int getWidth_dp() {
		checkContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		width_dp = metrics.widthPixels;
		return px2dip(width_dp);// 屏幕的宽dp
	}

	public static int getHeight_dp() {
		checkContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		height_dp = metrics.heightPixels;
		return px2dip(height_dp);// 屏幕的宽dp
	}

	public static int getStatusBarHeight(Activity activity) {
		checkContext();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static int getStatusHeight() {
		checkContext();
		int statusHeight = -1;
		try {
			Class clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

}
