package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Location;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderPathActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/27.
 * 订单线路界面的业务类
 */
public class OrderPathActivityBiz {

    private OrderPathActivity mActivity;

    /**
     * 获取订单线路时的标记
     */
    private final String mTagGetOrderPathData = "mTagGetOrderPathData";
    /**
     * 保存订单路线集合
     */
    private List<Location> mLocationsList;

    public OrderPathActivityBiz(OrderPathActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取订单线路信息
     * @param orderId 订单编号
     * @return 是否成功发送请求
     */
    public boolean getOrderPathData(final String orderId) {
        try {
            if (orderId == null || orderId.length() <= 0) {
                mActivity.getOrderPathDataError("获取订单路线失败！");
            }
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_LOCATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderPathActivityBiz.this.getClass() + "getOrderPathDataSuccess:" + response);
                    getOrderPathDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderPathActivityBiz.this.getClass() + "getOrderPathDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderPathDataError("获取订单路线失败！");
                    } else {
                        mActivity.getOrderPathDataError("获取订单路线失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderId", orderId);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetOrderPathData);
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
     * 网络请求订单线路数据成功返回数据
     * @param response 返回的信息
     */
    private void getOrderPathDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mLocationsList = JSON.parseArray(object.getString("result"), Location.class);
                mActivity.getOrderPathDataSuccess();
            } else {
                String msg = object.getString("msg");
                mActivity.getOrderPathDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetOrderPathData);
    }

    /**
     * 获取订单线路集合
     * @return 订单线路集合
     */
    public List<Location> getOrdrePathData() {
        return mLocationsList;
    }


    /**
     * 计算从起点到终点的路程
     * @param locationList 路程上所有坐标点的集合
     * @return 起点到终点的距离
     */
    public double getDistance(List<Location> locationList){
        try {
            double distance = 0;
            int size = locationList.size() - 1;
            for (int i = 1; i < size; i++) {
                Location nowLocation = locationList.get(i);
                Location perviousLocation = locationList.get(i - 1);
                LatLng nowLatLng = new LatLng(nowLocation.getCORDINATEY(), nowLocation.getCORDINATEX());
                LatLng perviousLatLng = new LatLng(perviousLocation.getCORDINATEY(), perviousLocation.getCORDINATEX());
                distance += DistanceUtil.getDistance(nowLatLng, perviousLatLng);
            }
            return distance;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return 0;
        }
    }


}











