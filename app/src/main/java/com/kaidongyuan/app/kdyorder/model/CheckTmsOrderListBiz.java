package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.bean.TmsOrder;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.CheckTmsOrderListActivity;
import com.kaidongyuan.app.kdyorder.ui.pagerinviewpage.CheckOrderFragmentInTransOrderPage;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/19.
 * 在途订单界面的业务类
 */
public class CheckTmsOrderListBiz {

    private CheckTmsOrderListActivity mActivity;

    /**
     * 默认获取数据是为第一页
     */
    private final int mInitPagerIndex = 1;
    /**
     * 分页加载时加载的页数
     */
    private int mPageIndex = mInitPagerIndex;
    /**
     * 分页加载时每页加载的数据数量
     */
    private final int mPageSize = 20;

    private String mPartyAdressId;
    /**
     * 保存物流订单数据的集合
     */
    private List<TmsOrder> mOrderList;
    /**
     * 网络请求订单数据是的标记
     */
    private final String TAG_GET_INTRANSIT_DATA = "TAG_GET_INTRANSIT_DATA";

    public CheckTmsOrderListBiz(CheckTmsOrderListActivity activity) {
        try {
            this.mActivity = activity;
            mOrderList = new ArrayList<>();
            Logger.w(this.getClass() + ":onCreage");
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 初次加载，只加载第一页
     *
     * @return 发送请求是否成功
     */
    public boolean getTmsOrderData(String partyAdressId) {
        try {
            mPageIndex = mInitPagerIndex;
            mPartyAdressId=partyAdressId;
            return getOrderData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 刷新在途订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean reFreshTmsOrderData(String partyAdressId) {
        try {
            mPageIndex = mInitPagerIndex;
            mPartyAdressId=partyAdressId;
            return getOrderData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 加载跟多的在途订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreTmsOrderData() {
        try {
            return getOrderData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 获取在途订单数据
     *
     * @return 发送请求是否成功
     */
    private boolean getOrderData() {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetTmsOrderByAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CheckTmsOrderListBiz.this.getClass() + ".getOrderDataSuccess:" + response);
                    getOrderDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(CheckTmsOrderListBiz.this.getClass() + ".getOrderDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getDataError("获取订单失败!");
                    } else {
                        mActivity.getDataError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    JSONObject joParams=new JSONObject();
                    String strBusinessidx=MyApplication.getInstance().getBusiness().getBUSINESS_IDX();
                    if (strBusinessidx==null||strBusinessidx.trim().isEmpty()){
                        joParams.put("BusinessId",SharedPreferencesUtil.getValueByName(SharedPreferenceConstants.BUSSINESS_CODE,SharedPreferenceConstants.IDX,1));
                    }else {
                        joParams.put("BusinessId",MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    }
                    String strUserId=MyApplication.getInstance().getUser().getIDX();
                    if (strUserId==null||strUserId.trim().isEmpty()){
                        mActivity.getDataError("登录帐号已下线，请退出重新登录后使用");
                    }else {
                        joParams.put("UserIdx",strUserId);
                    }
                    if (mPartyAdressId==null){
                        mPartyAdressId="";
                    }
                    joParams.put("PartyAdressId", mPartyAdressId);
                    joParams.put("Page", mPageIndex + "");
                    joParams.put("Pagesize", mPageSize + "");
                    params.put("strParams", joParams.toJSONString());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(TAG_GET_INTRANSIT_DATA);
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
     * 处理网络请求返回数据成功
     * @param response 返回的数据
     */
    private void getOrderDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex) {//刷新或初次加载，清除集合中的数据
                    mOrderList.clear();
                }
                JSONObject jo=object.getJSONObject("result");
                List<TmsOrder> tmpOrderList = JSON.parseArray(jo.getString("List"), TmsOrder.class);
                mOrderList.addAll(tmpOrderList);
                mActivity.getDataSuccess();
                mPageIndex++;
            } else {
                mActivity.getDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("获取订单失败!");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(TAG_GET_INTRANSIT_DATA);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取在途订数据单集合
     * @return 在途订单数据
     */
    public List<TmsOrder> getOrderList() {
        try {
            return mOrderList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }


}















