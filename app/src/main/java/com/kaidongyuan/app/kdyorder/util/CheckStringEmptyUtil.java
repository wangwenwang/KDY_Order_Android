package com.kaidongyuan.app.kdyorder.util;

import android.text.TextUtils;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;

/**
 * Created by Administrator on 2016/5/20.
 * 检查数据源字符串是否为空
 */
public class CheckStringEmptyUtil {

    /**
     * 检查字符串是否为空
     * @param str 要检查的字符串
     * @return 字符串，如果为空 返回 “暂无设置”
     */
    public static String checkStringIsEmptyWithNoSet(String str) {
        if (TextUtils.isEmpty(str)) {
            str = MyApplication.getmRes().getString(R.string.no_set);
        }
        return str;
    }

}
