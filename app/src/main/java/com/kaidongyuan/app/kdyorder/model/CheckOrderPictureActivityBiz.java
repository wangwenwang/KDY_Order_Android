package com.kaidongyuan.app.kdyorder.model;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.CustomerAutographAndPicture;
import com.kaidongyuan.app.kdyorder.constants.FileConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CheckOrderPictureActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/8.
 * 查看电子回单客户签名和交货现场图片的业务类
 */
public class CheckOrderPictureActivityBiz {

    private CheckOrderPictureActivity mActivity;

    /**
     * 网络获取签名和现场图片的标记
     */
    private final String mTagGetAutographAndPictureData = "mTagGetAutographAndPictureData";

    /**
     * 订单电子回单图片集合
     */
    private List<CustomerAutographAndPicture> mCustomerAutographAndPictures;

    public CheckOrderPictureActivityBiz(CheckOrderPictureActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取签名和交货现场图片数据
     * @param orderIdx 订单编号
     * @return 发送请求是否成功
     */
    public boolean getAutographAndPictureData(final String orderIdx) {
        try {

            String url = "";
            if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("Y")) {

                url = URLCostant.GETAUTOGRAPH_SAAS;
            }else if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("N")) {

                url = URLCostant.GETAUTOGRAPH;
            }else {

                ToastUtil.showToastBottom("IS_SAAS不合法", Toast.LENGTH_SHORT);
            }

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CheckOrderPictureActivityBiz.this.getClass() + "getAutographAndPictureDataSuccess:" + response);
                    getAutographAndPictureDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CheckOrderPictureActivityBiz.this.getClass() + "getAutographAndPictureDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderPathDataError("获取电子签名和现场图片失败！");
                    } else {
                        mActivity.getOrderPathDataError("获取电子签名和现场图片失败，请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", orderIdx);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetAutographAndPictureData);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 网络请求成功返回数据
     *
     * @param response 后台返回的信息
     */
    private void getAutographAndPictureDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mCustomerAutographAndPictures = JSON.parseArray(object.getString("result"), CustomerAutographAndPicture.class);
                mActivity.getAutographAndPictureDataSuccess();
            } else {
                mActivity.getOrderPathDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetAutographAndPictureData);
    }

    /**
     * 获取签名和交货现场图片集合
     * @return 签名和交货现场图片集合
     */
    public List<CustomerAutographAndPicture> getCustomerAutographAndPictures() {
        return mCustomerAutographAndPictures;
    }

    /**
     * 获取图片的 url
     *
     * @param remarkInt 标记
     *                  0 客户签名
     *                  1 现场图片1
     *                  2 现场图片2
     * @return 图片的url 路径
     */
    public String getPictureUrl(int remarkInt) {
        try {
            List<CustomerAutographAndPicture> customerAutographAndPictures = getCustomerAutographAndPictures();
            if (customerAutographAndPictures == null) {
                return "";
            }
            int i = 1;
            for (CustomerAutographAndPicture customerAutographAndPicture : customerAutographAndPictures) {
                try {
                    String productUrl = customerAutographAndPicture.getPRODUCT_URL();
                    if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("Y")) {

                        productUrl = "http://k56.kaidongyuan.com/" + productUrl;
                    }else if(MyApplication.getInstance().getBusiness().getIS_SAAS().equals("N")) {

                        productUrl = URLCostant.LOA_URL + FileConstants.SERVER_AUTOGRAPH_AND_PICTURE_FILE + File.separator + productUrl;
                    }else {

                        ToastUtil.showToastBottom("IS_SAAS不合法", Toast.LENGTH_SHORT);
                    }
                    String remark = customerAutographAndPicture.getREMARK();
                    if (CustomerAutographAndPicture.AUTOGRAPH.equals(remark) && remarkInt == 0) {//为客户签名图片
                        return productUrl;
                    } else if (CustomerAutographAndPicture.PICTURE.equals(remark)) {//为现场交货场景图片
                        if (i == 1 && remarkInt == 1) {
                            return productUrl;
                        }
                        if (i == 2 && remarkInt == 2) {
                            return productUrl;
                        }
                        i++;
                    }
                } catch (Exception e) {
                    ExceptionUtil.handlerException(e);
                }
            }
            return "";
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }

}
















