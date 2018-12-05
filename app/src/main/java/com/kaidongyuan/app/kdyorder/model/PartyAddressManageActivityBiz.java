package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Address;
import com.kaidongyuan.app.kdyorder.bean.Party;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyAddressManageActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyManageActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/18.
 * 客户地址管理的工具类
 */
public class PartyAddressManageActivityBiz {

    private PartyAddressManageActivity mActivity;
    /**
     * 获取客户列表的网络请求标记
     */
    private final String mTagGetCustomerAddressInfo = "mTagGetCustomerAddressInfo";
    /**
     * 客户地址的网络请求标记
     */
    private final String mTagDeleteAddress = "mTagDeleteAddress";
    /**
     * 客户地址的网络请求标记
     */
    private final String mTagAddAddress = "mTagAddAddress";


    /**
     * 存放从后台获取的客户的地址集合,存放显示在 ListView 中
     */
    private List<Address> mCustomerAddressList;
    /**
     * 用户选中的下单客户
     */
    private Party mSelectedParty;

    public PartyAddressManageActivityBiz(PartyAddressManageActivity activity) {
        try {
            this.mActivity = activity;
            this.mCustomerAddressList = new ArrayList<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 获取客户地址
     *
     *
     * @return 发送请求是否成功
     */
    public boolean getPartyAddressInfo(final String partyidx) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_ADDRESS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyAddressManageActivityBiz.this.getClass() + "getPartygetAddressInfo.Success:" + response);
                    getCustomerAddressResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyAddressManageActivityBiz.this.getClass() + "getPartygetAddressInfo.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getCustomerAddressDataError("获取客户地址失败!");
                    } else {
                        mActivity.getCustomerAddressDataError("获取客户地址失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("strPartyId",partyidx);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetCustomerAddressInfo);
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
     * 发送获取用户地址请求成功返回数据
     *
     * @param response 返回的消息
     */
    private void getCustomerAddressResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                mCustomerAddressList = JSON.parseArray(object.getString("result"), Address.class);
                if (mCustomerAddressList.size() < 1) {
                    mActivity.getCustomerAddressDataError("没有获取到客户有效地址！");
                } else {
                    mActivity.getCustomerAddressDataSuccess();
                }
            } else {
                mActivity.getCustomerAddressDataError("获取客户地址失败!" + object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getCustomerAddressDataError("获取客户地址失败!");
        }
    }


    /**
     * 删除客户地址
     *
     * @return 是否成功发送请求
     */
    public boolean deleteAddress(final String addressid) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.deletePartyAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyAddressManageActivityBiz.this.getClass() + "deleteParty.Success:" + response);
                    deleteAddressSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyAddressManageActivityBiz.this.getClass() + "deleteParty.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.deletePartyAddressError("删除客户地址失败!");
                    } else {
                        mActivity.deletePartyAddressError("删除客户地址失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("ADDRESS_IDX",addressid);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagDeleteAddress);
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

    private void deleteAddressSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//删除成功
               mActivity.deletePartyAddressSuccess("地址删除成功");
            } else {
                mActivity.deletePartyAddressError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.deletePartyAddressError("删除客户地址失败!");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetCustomerAddressInfo);
            HttpUtil.cancelRequest(mTagDeleteAddress);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }



    public boolean addPartyAddress(final String partyid, final String pv, final String ct, final String ar, final String ru, final String address_address,
                                 final String addressInfo, final String addressPerson, final String addressTel) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.AddPartyAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyAddressManageActivityBiz.this.getClass() + "addPartyAddress.Success:" + response);
                    addPartyAddressSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyAddressManageActivityBiz.this.getClass() + "addPartyAddress.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getCustomerAddressDataError("客户地址配置失败!");
                    } else {
                        mActivity.getCustomerAddressDataError("客户地址配置失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("PARTY_IDX",partyid);
                    params.put("ADDRESS_PROVINCE",pv);
                    params.put("ADDRESS_CITY",ct);
                    params.put("ADDRESS_AREA",ar);
                    params.put("ADDRESS_RURAL",ru);
                    params.put("ADDRESS_ADDRESS",address_address);
                    params.put("ADDRESS_INFO",addressInfo);
                    params.put("CONTACT_PERSON",addressPerson);
                    params.put("CONTACT_TEL",addressTel);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagAddAddress);
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

    private void addPartyAddressSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//添加成功
                mActivity.getCustomerAddressData();
            } else {
                mActivity.getCustomerAddressDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getCustomerAddressDataError("客户地址配置失败!");
        }
    }




    /**
     * 获取客户列表
     *
     * @return 客户列表
     */
    public List<Address> getmCustomerAddressList() {
        return mCustomerAddressList;
    }


    /**
     * 获取用户选择的客户
     *
     * @return 用户选择的客户
     */
    public Party getmSelectedParty() {
        return mSelectedParty;
    }
}













