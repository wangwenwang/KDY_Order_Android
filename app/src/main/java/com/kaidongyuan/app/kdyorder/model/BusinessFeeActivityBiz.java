package com.kaidongyuan.app.kdyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.BusinessFee;
import com.kaidongyuan.app.kdyorder.bean.BusinessFeeItem;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.BusinessFeeActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.OrderDetailsActivity;
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
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/30.
 */
public class BusinessFeeActivityBiz {
    private BusinessFeeActivity mActivity;
    private String mouth,partyIdx;
    /**
     * 网络请求订单详情数据的标记
     */
    private final String mTagGetAppBusinessFeeList="GetAppBusinessFeeList";

    /**
     * 月份账单
     */
    private BusinessFee mfee;

    /**
     * 月份账单费用条目
     */
    private List<BusinessFeeItem> businessFeeItems;
    public BusinessFeeActivityBiz(BusinessFeeActivity mActivity, String mouth, String partyIdx) {
        this.mActivity = mActivity;
        this.mouth = mouth;
        this.partyIdx = partyIdx;
        businessFeeItems=new ArrayList<>();
    }

    /**
     * 网络请求月份账单费用详情数据
     * @return 发送请求是否成功
     */
    public boolean getBusinessFeeData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetAppBusinessFeeList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(BusinessFeeActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    getOrderDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(BusinessFeeActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getfeeDetailsDataError("获取账单详情失败！");
                    } else {
                        mActivity.getfeeDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("MONTH_DATE", mouth);//BILL_DATE	账单月份
                    params.put("BUSINESS_IDX",MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("PARTY_IDX",partyIdx);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetAppBusinessFeeList);
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
     * 网络请求费用详情数据成功返回信息
     *
     * @param response 返回的信息
     */
    private void getOrderDetailsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONObject jo = JSON.parseObject(object.getString("result"));
                mfee=JSON.parseObject(jo.getString("AppBusinessFee"),BusinessFee.class);
                businessFeeItems=JSON.parseArray(jo.getString("AppBusinessFeeList"),BusinessFeeItem.class);

                mActivity.getfeeDetailsDataSuccess();
            } else {
                mActivity.getfeeDetailsDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getfeeDetailsDataError("没有获取到此客户"+mouth+"的账单！");
            mActivity.finish();
        }
    }

    public BusinessFee getfee() {
        return mfee;
    }

    public List<BusinessFeeItem> getBusinessFeeItems(){return businessFeeItems;}

    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetAppBusinessFeeList);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
}
