package com.kaidongyuan.app.kdyorder.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * ${PEOJECT_NAME}
 * 用于AndroidSDK 23及以上使用的权限管理工具类
 * Created by Administrator on 2016/9/12.
 */
public class MPermissionsUtil {
    // 状态码、标志位
   // public static final int REQUEST_STATUS_CODE = 0x881;
    public static final int REQUEST_PERMISSION_SETTING = 0x882;

    /**
     *
     * @param activity
     * @param strPermissons
     * @param REQUEST_STATUS_CODE
     * @return
     */
    public static boolean checkAndRequestPermissions(Activity activity, String[] strPermissons,int REQUEST_STATUS_CODE){
        if (strPermissons.length<=0)return false;
        ArrayList<String>denidArray=new ArrayList<>();
        for (String strPermisson:strPermissons) {
            int grantcode = ContextCompat.checkSelfPermission(activity, strPermisson);

            if (grantcode==PackageManager.PERMISSION_DENIED){
                denidArray.add(strPermisson);
            }
        }
        if (denidArray.size()>0){
            //将Arraylist转换成String[]
            String[] denidPermissions=denidArray.toArray(new String[denidArray.size()]);
            //请求系统对权限数组进行授权，如果用户同意则在onRequestPermissionsResult中返回GRANTED，
            // 如果用户拒绝则在onRequestPermissionsResult中返回DENIED，
            // 如果用户拒绝并设置不再提示则在onRequestPermissionsResult中返回DENIED
            //重要授权对后两种情况请在目标activity的onRequestPermissionsResult方法中进行提示snackbar处理
            ActivityCompat.requestPermissions(activity,denidPermissions,REQUEST_STATUS_CODE);
            return false;
        }else return true;
    }


}
