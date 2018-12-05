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
import com.kaidongyuan.app.kdyorder.ui.activity.InventoryPartyActivity;
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
 * 下单页面的工具类
 */
public class PartyManageActivityBiz {

    private PartyManageActivity mActivity;

    /**
     * 获取客户列表的网络请求标记
     */
    private final String mTagGetCustomerList = "mTagGetCustomerList";
    /**
     * 获取客户地址的网络请求标记
     */
    private final String mTagDeleteParty = "mTagDeleteParty";
    /**
     * 添加客户的网络请求标记
     */
    private final String mTagAddParty = "mTagAddParty";
    /**
     * 添加客户地址的网络请求标记
     */
    private final String mTagAddAddress="mTagAddAddress";

    /**
     * 存放显示在 ListView 中的客户数据集合
     */
    private List<Party> mCustomerList;
    /**
     * 存放从后台获取的所有客户数据集合
     */
    private List<Party> mTotalCustomerList;
    /**
     * 存放从后台获取的客户的地址集合
     */
    private List<Address> mCustomerAddressList;
    /**
     * 用户选中的下单客户
     */
    private Party mSelectedParty;

    public PartyManageActivityBiz(PartyManageActivity activity) {
        try {
            this.mActivity = activity;
            this.mCustomerList = new ArrayList<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 获取客户列表
     *
     * @return 是否成功发送请求
     */
    public boolean getCustomerData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_PARTY_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "getCustomerData.Success:" + response);
                    getCustomerDataResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "getCustomerData.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getCustomerDataError("获取客户列表失败!");
                    } else {
                        mActivity.getCustomerDataError("获取客户列表失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetCustomerList);
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
     * @param response 返回的数据
     */
    private void getCustomerDataResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//获取数据成功
                mTotalCustomerList = JSON.parseArray(object.getString("result"), Party.class);
                mCustomerList = mTotalCustomerList;
                mActivity.getCustomerDataSuccess();
            } else {
                mActivity.getCustomerDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getCustomerDataError("获取客户列表失败!");
        }
    }

    /**
     * 获取客户列表
     *
     * @return 是否成功发送请求
     */
    public boolean deleteParty(final String partyid) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.DeleteAppUserParty, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "deleteParty.Success:" + response);
                    deletePartySuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "deleteParty.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.deletePartyError("删除客户失败!");
                    } else {
                        mActivity.deletePartyError("删除客户失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("strPartyId",partyid);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagDeleteParty);
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

    private void deletePartySuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//删除成功
               mActivity.getCustomerData();
            } else {
                mActivity.deletePartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.deletePartyError("删除客户失败!");
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetCustomerList);
            HttpUtil.cancelRequest(mTagDeleteParty);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * 获取客户列表
     *
     * @return 是否成功发送请求
     */
    public boolean addParty(final String partyName, final String partyRemark,final String partyCitty, final String pv, final String ct, final String ar, final String ru,
                            final String address_address,final String addressInfo, final String addressPerson, final String addressTel) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.AddParty, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "addParty.Success:" + response);
                    addPartySuccess(response,pv,ct,ar,ru,address_address,addressInfo,addressPerson,addressTel);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "addParty.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.deletePartyError("添加客户失败!");
                    } else {
                        mActivity.deletePartyError("添加客户失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId", MyApplication.getInstance().getUser().getIDX());
                    params.put("PARTY_NAME",partyName);
                    params.put("PARTY_CITY",partyCitty);
                    params.put("PARTY_REMARK",partyRemark);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagAddParty);
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

    private void addPartySuccess(String response, String pv, String ct, String ar, String ru, String address_address,
                                 String addressInfo, String addressPerson, String addressTel) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//添加成功
                JSONObject ob=object.getJSONObject("result");
                String partyid=ob.getString("IDX");
                addPartyAddress(partyid,pv,ct,ar,ru,address_address,addressInfo,addressPerson,addressTel);

            } else {
                mActivity.deletePartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.deletePartyError("添加客户失败!");
        }

    }

    private void addPartyAddress(final String partyid, final String pv, final String ct, final String ar, final String ru, final String address_address,
                                 final String addressInfo, final String addressPerson, final String addressTel) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.AddPartyAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "addPartyAddress.Success:" + response);
                    addPartyAddressSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(PartyManageActivityBiz.this.getClass() + "addPartyAddress.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getCustomerAdressDataError("客户地址配置失败!");
                    } else {
                        mActivity.getCustomerAdressDataError("客户地址配置失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
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
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void addPartyAddressSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {//添加成功
                mActivity.addPartySuccess();
            } else {
                mActivity.deletePartyError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.deletePartyError("客户地址配置失败!");
        }
    }


    /**
     * 获取客户列表
     *
     * @return 客户列表
     */
    public List<Party> getmCustomerList() {
        return mCustomerList;
    }

    /**
     * 根据用户输入查找客户
     *
     * @param msg 输入的信息
     */
    public void searchParty(String msg) {
        try {
            if (msg == null || msg.length() <= 0) {
                mCustomerList = mTotalCustomerList;
            } else {
                ArrayList<Party> repartylist = new ArrayList<>();
                for (Party party : mTotalCustomerList) {
                    if (party.getPARTY_NAME().contains(msg)) {
                        repartylist.add(party);
                    }
                }
                mCustomerList = repartylist;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mCustomerList.clear();
        }
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













