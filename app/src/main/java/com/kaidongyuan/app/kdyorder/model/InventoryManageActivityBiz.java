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
import com.kaidongyuan.app.kdyorder.bean.Fleet;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.InventoryManageActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/7.
 */
public class InventoryManageActivityBiz {
    private InventoryManageActivity manageActivity;
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
    /**
     * 保存已取消订单数据的集合
     */
    private List<Fleet> mFleets;
    /**
     * 网络请求客户库存登记表时的标记
     */
    private final String TAG_GetStockList = "GetStockList";

    public InventoryManageActivityBiz(InventoryManageActivity manageActivity) {
       try {
           this.manageActivity = manageActivity;
           mFleets=new ArrayList<>();
           Logger.w(this.getClass() + ":onCreate");
       }catch (Exception ex){
           ex.printStackTrace();
       }

    }

    public List<Fleet> getmFleets() {
        return mFleets;
    }

    /**
     * 初次加载，只加载第一页
     *
     * @return 发送请求是否成功
     */
    public boolean getFirstFleetData() {
        try {
            mPageIndex = mInitPagerIndex;
            return getFleetData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }
    /**
     * 刷新数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean reFreshFleetData() {
        try {
            mPageIndex = mInitPagerIndex;
            return getFleetData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 加页跟多的数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreFleetData() {
        try {
            return getFleetData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    private boolean getFleetData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetStockList1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(InventoryManageActivityBiz.this.getClass() + "getCustomerData.Success:" + response);
                       getFleetDataResponseSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(InventoryManageActivityBiz.this.getClass() + "getCustomerData.VolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        manageActivity.getStockListError("获取客户库存登记表失败!");
                    } else {
                        manageActivity.getStockListError("获取失败!" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strUserId",MyApplication.getInstance().getUser().getIDX());
                    params.put("strBusinessId",MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("UserName", MyApplication.getInstance().getUser().getUSER_NAME());
                    params.put("strPage", mPageIndex+"");
                    params.put("strPageCount", mPageSize+"");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(TAG_GetStockList);
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

    private void getFleetDataResponseSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex) {//刷新或初次加载，清除集合中的数据
                    mFleets.clear();
                }
                JSONObject jo= JSON.parseObject(object.getString("result"));
                List<Fleet> tmpFleetList = JSON.parseArray(jo.getString("Stock"), Fleet.class);
                mFleets.addAll(tmpFleetList);
                manageActivity.getStockListSuccess(mPageSize-tmpFleetList.size());
                mPageIndex++;
            } else {
                manageActivity.getStockListError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            manageActivity.getStockListError("获取客户库存登记表失败!");
        }
    }
    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(TAG_GetStockList);
    }
}
