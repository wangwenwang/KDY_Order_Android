package com.kaidongyuan.app.kdyorder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Business;
import com.kaidongyuan.app.kdyorder.bean.User;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.LoginActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/16.
 * 登录界面业务类
 */
public class LoginActivityBiz {

    private Context mContext;
    private LoginActivity mActivity;

    /**
     * 登录网络请求标记
     */
    private final String mTagLogin = "mTagLogin";
    /**
     * 获取用户业务类型网络请求标记
     */
    private final String mTagGetBusinessList = "mTagGetBusinessList";

    public LoginActivityBiz(LoginActivity activity) {
        this.mContext = activity;
        this.mActivity = activity;
    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return 是否成功发送请求
     */
    public boolean login(final String userName, final String password) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(LoginActivityBiz.this.getClass() + "loginSuccess:" + response);
                    loginSuccess(response, userName, password);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(LoginActivityBiz.this.getClass() + "loginError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("登录失败!");
                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserName", userName);
                    params.put("strPassword", password);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagLogin);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 登录成功
     *
     * @param response 返回的消息
     * @param userName 登录用户名
     * @param password 登录用户密码
     */
    private void loginSuccess(String response, String userName, String password) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            String msg = object.getString("msg");
            if (type == 1) {//登录成功
                List<User> users = JSON.parseArray(object.getString("result"), User.class);
                if (users != null && users.size() > 0) {//登录成功
                    EditText editTextUserName = mActivity.getmEtUserNameEditText();
                    if (editTextUserName!=null) {
                        users.get(0).setUSER_CODE(editTextUserName.getText().toString().trim());
                    } else {
                        users.get(0).setUSER_CODE("null");
                    }
                    MyApplication.getInstance().setUser(users.get(0));
                    MyApplication.IS_LOGIN = true;
                    saveSharedData(userName, password);//保存用户信息到本地
                    getBusinessList();
                } else {//登录失败
                    mActivity.loginError(msg);
                }
            } else {//登录失败
                mActivity.loginError(msg);
            }
        } catch (Exception e) {
            mActivity.loginError("登录失败!");
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 保存用户的配置信息
     *
     * @param name     用户名 String
     * @param password 密码 String
     */
    public void saveSharedData(String name, String password) {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedPreferenceConstants.SHARED_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("name", name);
            editor.putString("pwd", password);
            editor.apply();//提交修改
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取用户的业务类型集合
     */
    private void getBusinessList() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_BUISNESS_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(LoginActivityBiz.this.getClass() + "getBusinessListSuccess:" + response);
                    getBusinessListSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(LoginActivityBiz.this.getClass() + "getBusinessListError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("登录失败!");
                    } else {
                        mActivity.loginError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserIdx", MyApplication.getInstance().getUser().getIDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetBusinessList);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    URLCostant.LONG_TIMEOUT_MS,
                    URLCostant.LONG_MAX_RETRIES,
                    URLCostant.LONG_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取用户列表返回信息成功
     * @param response 返回的消息
     */
    private void getBusinessListSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            List<Business> businessList = JSON.parseArray(object.getString("result"), Business.class);
            if (businessList.size() == 0) {
                mActivity.loginError("查询业务列表失败");
            } else if (businessList.size() == 1) {
                writeBusinessToApplicationAndSharedPreference(businessList.get(0));
                mActivity.loginSuccess();
            } else {
                mActivity.showBusinessDialog(businessList);
            }
        } catch (JSONException e) {
            mActivity.loginError("登录失败! ");
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 将用户业务类型保存到 Application 和 SharedPreference 中
     * @param business 用户的业务类型
     */
    public void writeBusinessToApplicationAndSharedPreference(Business business) {
        SharedPreferencesUtil.WriteSharedPreferences(SharedPreferenceConstants.BUSSINESS_CODE,
                SharedPreferenceConstants.NAME, business.getBUSINESS_CODE());
        SharedPreferencesUtil.WriteSharedPreferences(SharedPreferenceConstants.BUSSINESS_CODE,
                SharedPreferenceConstants.IDX, business.getBUSINESS_IDX());
        MyApplication.getInstance().setBusiness(business);
    }

    /**
     * 取消所有的网络请求
     */
    public void cancelAllRequest(){
        try {
            HttpUtil.cancelRequest(mTagLogin, mTagGetBusinessList);
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取本地保存的用户名
     * @return 用户名
     */
    public String getUserName() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedPreferenceConstants.SHARED_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(SharedPreferenceConstants.NAME, "");
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }

    /**
     * 获取本地保存的用户密码
     * @return 用户密码
     */
    public String getPassword() {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedPreferenceConstants.SHARED_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(SharedPreferenceConstants.PASSWORD, "");
        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }
}








