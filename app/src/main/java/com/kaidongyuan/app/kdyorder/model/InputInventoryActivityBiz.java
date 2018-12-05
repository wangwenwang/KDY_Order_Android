package com.kaidongyuan.app.kdyorder.model;

import android.text.TextUtils;
import android.widget.Toast;

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
import com.kaidongyuan.app.kdyorder.bean.PayType;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.ProductPolicy;
import com.kaidongyuan.app.kdyorder.bean.ProductTB;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;
import com.kaidongyuan.app.kdyorder.ui.activity.InputInventoryActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.OutputInventoryActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.HttpUtil;
import com.kaidongyuan.app.kdyorder.util.NetworkUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库下单的业务类
 * Created by ${tom} on 2017/9/21.
 */
public class InputInventoryActivityBiz {
    private InputInventoryActivity mActivity;

    /**
     * 网络请求支付类型是的标记
     */
    private final String mTagGetPaymentTypeData = "mTagGetPaymentTypeData";
    /**
     * 网络获取产品品牌分类和品类时的标记
     */
    private final String mTagGetProductTypesData = "mTagGetProductTypesData";
    /**
     * 网络获取产品数据时的标记
     */
    private final String mTagGetProducts = "mTagGetProducts";
    /**
     * 支付类型数据集合
     */
    private List<PayType> mPayTypes;
    /**
     * 用户当前选择的支付类型
     */
    private PayType mCurrentPaymentTypes;
    /**
     * 产品的品牌类型集合
     */
    private List<String> mOrderBrands;
    /**
     * 产品分类集合
     */
    private List<String> mOrderTypes;
    /**
     * 用户选择的当前品牌
     */
    private String mCurrentOrderBrand;
    /**
     * 记录用户请求时的品牌
     */
    private String mTempCurrrentOrderBrand;
    /**
     * 用户选择的当前商品品类
     */
    private String mCurrentProductType;
    /**
     * 记录用户请求是的商品品类
     */
    private String mTempCurrentProductType;
    /**
     * 产品品牌数据集合
     */
    private List<ProductTB> mProductBrandsType;
    /**
     * 存放产品数据的集合
     */
    private List<Product> mCurrentProductData;
    /**
     * 用户选择的下单产品
     */
    private List<Product> mChoiceProducts = new ArrayList<>();
    /**
     * 保存从后台获取到的产品数据集合，按产品类型分类
     */
    private List<Product>[] mProductListArr;
    /**
     * 用户更换品类是记录用户选择品类在集合中的位置
     */
    private int mTempSelectedOrderTypeIndexInOrderTypes = 0;

    public InputInventoryActivityBiz(InputInventoryActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取支付类型数据
     *
     * @return 发送请求是否成功
     */
    public boolean getPayTypeData() {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_PAYMENT_TYPE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + "getPayTypeDataSuccess:" + response);
                    getPayTypeDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + "getPayTypeDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getDataError("获取支付类型失败！");
                    } else {
                        mActivity.getDataError("获取支付类型失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetPaymentTypeData);
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
     * 网络获取支付类型数据成功返回数据
     *
     * @param response 返回的信息
     */
    private void getPayTypeDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 0) {
                mPayTypes = JSON.parseArray(object.getString("result"), PayType.class);
                if (mPayTypes.size() > 0) {
                    mCurrentPaymentTypes = mPayTypes.get(0);
                    mActivity.getPayTypeDataSuccess();
                } else {
                    mActivity.getDataError("获取支付类型失败，数据为空！");
                }
            } else {
                String msg = object.getString("msg");
                mActivity.getDataError(msg);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("获取支付类型失败！");
        }
    }

    /**
     * 获取产品类型列表数据
     *
     * @return 发送请求是否成功
     */
    public boolean getProductTypesData(boolean isInputOther) {
        try {
            if (isInputOther){
                StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_PRODUCT_TYPE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.w(this.getClass() + "getProductTypesDataSuccess:" + response);
                        getProductTypesDataSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.w(this.getClass() + "getProductTypesDataVolleyError:" + error.toString());
                        if (NetworkUtil.isNetworkAvailable()) {
                            mActivity.getDataError("获取品牌分类和产品分类失败！");
                        } else {
                            mActivity.getDataError("获取品牌分类和产品分类失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                        }
                        setTempBrandAndTypeToBefore();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                       // params.put("strAddressIdx",mActivity.mOrderAddressIdx);
                        params.put("strLicense", "");
                        return params;
                    }
                };
                request.setTag(mTagGetProductTypesData);
                request.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                HttpUtil.getRequestQueue().add(request);
            }else {
                StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetOutProductType, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.w(this.getClass() + "getProductTypesDataSuccess:" + response);
                        getProductTypesDataSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.w(this.getClass() + "getProductTypesDataVolleyError:" + error.toString());
                        if (NetworkUtil.isNetworkAvailable()) {
                            mActivity.getDataError("获取品牌分类和产品分类失败！");
                        } else {
                            mActivity.getDataError("获取品牌分类和产品分类失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                        }
                        setTempBrandAndTypeToBefore();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("strBusinessId", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                        params.put("strAddressIdx",mActivity.mOrderAddressIdx);
                        params.put("strLicense", "");
                        return params;
                    }
                };
                request.setTag(mTagGetProductTypesData);
                request.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                HttpUtil.getRequestQueue().add(request);
            }

            return true;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 获取品牌和产品分类成功返回数据
     *
     * @param response 返回的信息
     */
    private void getProductTypesDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                setCurrentBrandAndType();
                mProductBrandsType = JSON.parseArray(object.getString("result"), ProductTB.class);
                if (mProductBrandsType.size() > 0) {
                    setOrderBrandsAndTypes();
                    mActivity.getProductTypesDataSuccess();
                } else {
                    mActivity.getDataError("品牌和产品分类为空！");
                }
            } else {
                String msg = object.getString("msg");
                mActivity.getDataError(msg);
                setTempBrandAndTypeToBefore();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("获取品牌分类和产品分类失败！");
        }
    }

    /**
     * 网络请求失败时让网络请求的品牌和类型变为原来的
     */
    private void setTempBrandAndTypeToBefore() {
        try {
            mTempCurrrentOrderBrand = mCurrentOrderBrand;
            mTempCurrentProductType = mCurrentProductType;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置产品品类和品牌集合
     */
    private void setOrderBrandsAndTypes() {
        try {
            if (mOrderBrands == null) {
                mOrderBrands = new ArrayList<>();
            }
            if (mOrderTypes == null) {
                mOrderTypes = new ArrayList<>();
            }
            mOrderBrands.clear();
            mOrderTypes.clear();
            mProductListArr = null;
            mOrderTypes.add(0, MyApplication.getmRes().getString(R.string.all));
            mOrderBrands.add(0, MyApplication.getmRes().getString(R.string.all));
            for (ProductTB productTB : mProductBrandsType) {
                String brand = productTB.getPRODUCT_CLASS();
                if (!TextUtils.isEmpty(brand) && !mOrderBrands.contains(brand)) {
                    mOrderBrands.add(brand);
                }
                String type = productTB.getPRODUCT_TYPE();
                if (!TextUtils.isEmpty(type) && !mOrderTypes.contains(type)) {
                    mOrderTypes.add(type);
                }
            }
            int orderTypesSize = mOrderTypes.size();
            mProductListArr = new List[orderTypesSize];
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取产品数据
     *
     * @param mOrderPartyId    party id
     * @param mOrderAddressIdx address idx
     * @param index            在产品分类列表集合中的位置
     * @return 发送请求是否成功
     */
    public boolean getProductsData(final String mOrderPartyId, final String mOrderAddressIdx, int index) {
        try {
            List<Product> products = mProductListArr[index];
            if (products != null && products.size() > 0 && index>0) {//如果缓存中有产品数据则直接显示，不发送请求
                mCurrentProductData = products;
                setCurrentBrandAndType();
                mActivity.getProductDataSuccess();
                return false;
            }
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetOutProductList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(this.getClass() + "getProductsDataSuccess:" + response);
                    getProductsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(this.getClass() + "getProductsDataVolleyError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getDataError("获取产品数据失败！");
                    } else {
                        mActivity.getDataError("获取产品数据失败！" + MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                    setTempBrandAndTypeToBefore();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if (MyApplication.getmRes().getString(R.string.all).equals(mTempCurrrentOrderBrand)) {
                        mTempCurrrentOrderBrand = "";
                    }
                    if (MyApplication.getmRes().getString(R.string.all).equals(mTempCurrentProductType)) {
                        mTempCurrentProductType = "";
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("Business_idx", MyApplication.getInstance().getBusiness().getBUSINESS_IDX());
                    params.put("strPage","1");//放弃分页加载
                    params.put("strPageCount","999");//放弃分页加载
                    params.put("strPartyAddressIdx", mOrderAddressIdx);
                    params.put("strLicense", "");
                    params.put("strProductType", mTempCurrentProductType);
                    params.put("strProductClass", mTempCurrrentOrderBrand);
                    return params;
                }
            };
            request.setTag(mTagGetProducts);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            return false;
        }
    }

    /**
     * 网络获取产品数据成功返回数据
     *
     * @param response 返回的信息
     */
    private void getProductsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                setCurrentBrandAndType();
                mCurrentProductData = JSON.parseArray(object.getString("result"), Product.class);
                if (mTempSelectedOrderTypeIndexInOrderTypes!=0) { //如果获取的是全部的产品，则不缓存数据，内存占用太大
                    mProductListArr[mTempSelectedOrderTypeIndexInOrderTypes] = mCurrentProductData;
                }
                if (mCurrentProductData.size() > 0) {
                    mActivity.getProductDataSuccess();
                } else {
                    mActivity.getDataError("获取产品数据失败，产品数据为空！");
                }
            } else {
                String msg = object.getString("msg");
                mActivity.getDataError(msg);
                setTempBrandAndTypeToBefore();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.getDataError("获取产品数据失败！");
        }
    }

    /**
     * 请求成功后设置当前的品牌和品类
     */
    private void setCurrentBrandAndType() {
        mCurrentOrderBrand = TextUtils.isEmpty(mTempCurrrentOrderBrand) ? mActivity.getString(R.string.all) : mTempCurrrentOrderBrand;
        mCurrentProductType = TextUtils.isEmpty(mTempCurrentProductType) ? mActivity.getString(R.string.all) : mTempCurrentProductType;
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetPaymentTypeData, mTagGetProductTypesData, mTagGetProducts);
    }

    /**
     * 获取支付类型数据集合
     *
     * @return 支付类型数据集合
     */
    public List<PayType> getPayTypes() {
        return mPayTypes == null ? new ArrayList<PayType>() : mPayTypes;
    }

    /**
     * 获取品牌和产品分类数据集合
     *
     * @return 品牌和产品分类数据集合
     */
    public List<ProductTB> getProductBrandsType() {
        return mProductBrandsType == null ? new ArrayList<ProductTB>() : mProductBrandsType;
    }

    /**
     * 获取用户当前选中产品数据集合
     *
     * @return 产品数据
     */
    public List<Product> getmCurrentProductData() {
        return mCurrentProductData == null ? new ArrayList<Product>() : mCurrentProductData;
    }

    /**
     * 获取用户选择的支付类型
     *
     * @return 支付类型
     */
    public PayType getPayType() {
        return mCurrentPaymentTypes;
    }

    /**
     * 设置支付类型
     *
     * @param index 在支付类型集合中的位置
     */
    public void setPaymentType(int index) {
        mCurrentPaymentTypes = mPayTypes.get(index);
    }

    /**
     * 获取品牌分类数据
     *
     * @return 品牌分类数据
     */
    public List<String> getOrderBrands() {
        return mOrderBrands == null ? new ArrayList<String>() : mOrderBrands;
    }

    /**
     * 获取产品分类集合
     *
     * @return 产品分类集合
     */
    public List<String> getOrderTypes() {
        return mOrderTypes == null ? new ArrayList<String>() : mOrderTypes;
    }

    /**
     * 获取用户选择的当前品牌
     *
     * @return 当前品牌
     */
    public String getCurrentOrderBrand() {
        return mCurrentOrderBrand;
    }

    /**
     * 设置当前选择的商品品牌
     *
     * @param currentProductBrandIndex 在商品品牌集合中的位置
     */
    public void setCurrentProductBrand(int currentProductBrandIndex) {
        try {
            this.mTempCurrrentOrderBrand = mOrderBrands.get(currentProductBrandIndex);
            this.mTempSelectedOrderTypeIndexInOrderTypes = 0;
            int size = mProductListArr.length;
            for (int i = 0; i < size; i++) {
                mProductListArr[i] = null;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置当前选择的商品品牌
     *
     * @param brand 品牌类型 仅限 “全部”
     */
    public void setCurrentProductBrand(String brand) {
        this.mTempCurrrentOrderBrand = brand;
    }

    /**
     * 获取用户选择的当前品类
     *
     * @return 当前品类
     */
    public String getCurrentProductType() {
        return mCurrentProductType;
    }

    /**
     * 设置当前选择的商品分类
     *
     * @param currentProductTypeIndex 在商品分类集合中的位置
     */
    public void setCurrentProductType(int currentProductTypeIndex) {
        this.mTempCurrentProductType = mOrderTypes.get(currentProductTypeIndex);
        this.mTempSelectedOrderTypeIndexInOrderTypes = currentProductTypeIndex;
    }

    /**
     * 设置当前选择的商品分类
     *
     * @param type 商品类型 仅限 “全部”
     */
    public void setCurrentProductType(String type) {
        this.mTempCurrentProductType = type;
    }

    /**
     * 获取用户已选择的商品
     *
     * @return 用户已选的商品
     */
    public List<Product> getChoiceProducts() {
        return mChoiceProducts == null ? new ArrayList<Product>() : mChoiceProducts;
    }

    /**
     * 单个增加商品
     *
     * @param dataIndex 在当前选择品类商品中的位置
     */
    public void addProductSize(int dataIndex) {
        try {
            Product choiceProduct = mCurrentProductData.get(dataIndex);
            int index = getChoiceProductIndexInChoicedProductList(choiceProduct);
            if (index == -1) {
                mChoiceProducts.add(mChoiceProducts.size(), choiceProduct);
            }
            index = getChoiceProductIndexInChoicedProductList(choiceProduct);
            addProductSizeInChoicedProducts(index);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取产品在已选产品集合中的位置
     * @param choiceProduct 需要查询的商品
     * @return 在选择集合中的位置，没有则返回 -1
     */
    private int getChoiceProductIndexInChoicedProductList(Product choiceProduct) {
        try {
            int index = -1;
            int size = mChoiceProducts.size();
            Product productInChoicedList;
            for (int i = 0; i < size; i++) {
                productInChoicedList = mChoiceProducts.get(i);
                if (choiceProduct.getIDX() == productInChoicedList.getIDX()) {
                    index = i;
                }
            }
            return index;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 单个添加商品到已选商品集合中
     *
     * @param dataIndex 在已选商品集合中的位置
     */
    public void addProductSizeInChoicedProducts(int dataIndex) {
        try {
            Product product = mChoiceProducts.get(dataIndex);
            int size = product.getCHOICED_SIZE() + 1;

            //product.setISINVENTORY("Y");
            if (BusinessConstants.ISNEED_CARE_NVENTORY.equals(product.getISINVENTORY())) {
                int maxSize = (int) product.getSTOCK_QTY();
                if (size <= maxSize) {
                    product.setCHOICED_SIZE(size);
                } else {
                    ToastUtil.showToastBottom("产品数量超过库存数量!", Toast.LENGTH_SHORT);
                }
            } else {
                product.setCHOICED_SIZE(size);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 单个减少商品
     *
     * @param dataIndex 在当前选择品类商品中的位置
     */
    public void deleteProductSize(int dataIndex) {
        try {
            Product choiceProduct = mCurrentProductData.get(dataIndex);
            int index = getChoiceProductIndexInChoicedProductList(choiceProduct);
            deleteProductSizeInChoicedProducts(index);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 单个减少商品
     *
     * @param index 在已选商品集合中的位置
     */
    public void deleteProductSizeInChoicedProducts(int index) {
        try {
            if (index != -1) {
                Product product = mChoiceProducts.get(index);
                int choiceSize = product.getCHOICED_SIZE();
                if (choiceSize > 0) {
                    product.setCHOICED_SIZE(choiceSize - 1);
                }
                if (product.getCHOICED_SIZE() <= 0) {
                    mChoiceProducts.remove(product);
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 手动设置下单产品数量
     *
     * @param dataIndex    在当前选择品类商品中的位置
     * @param productCount 下单数量
     */
    public void setProductSize(int dataIndex, int productCount) {
        try {
            Product choiceProdut = mCurrentProductData.get(dataIndex);
            int index = mChoiceProducts.indexOf(choiceProdut);
            if (index == -1) {
                mChoiceProducts.add(mChoiceProducts.size(), choiceProdut);
            }
            index = mChoiceProducts.indexOf(choiceProdut);
            setProductSizeInChoicedProducts(index, productCount);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 手动设置下单产品数量
     *
     * @param index        在已选商品集合中的位置
     * @param productCount 用户输入的数量
     */
    public void setProductSizeInChoicedProducts(int index, int productCount) {
        try {
            Product product = mChoiceProducts.get(index);
           // product.setISINVENTORY("Y");
            if (BusinessConstants.ISNEED_CARE_NVENTORY.equals(product.getISINVENTORY())) {
                int maxSize = (int)product.getSTOCK_QTY();
                if (productCount <= maxSize) {
                    product.setCHOICED_SIZE(productCount);
                } else {
                    ToastUtil.showToastBottom("产品数量超过库存数量!", Toast.LENGTH_SHORT);
                }
            } else {
                product.setCHOICED_SIZE(productCount);
            }
            if (product.getCHOICED_SIZE() <= 0) {
                mChoiceProducts.remove(product);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取用户选择的商品总数
     *
     * @return 商品总数 String 类型
     */
    public String getChoicedProductSize() {
        try {
            int choicedProductSize = 0;
            for (Product product : mChoiceProducts) {
                choicedProductSize += product.getCHOICED_SIZE();
            }
            return String.valueOf(choicedProductSize);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }

    /**
     * 获取已选商品总价格
     *
     * @return 总价格 String 类型
     */
    public String getChoicedProductPrice() {
        try {
            double totalPrice = 0;
            for (Product product : mChoiceProducts) {
                totalPrice += product.getPRODUCT_PRICE() * product.getCHOICED_SIZE();
            }
            String str = String.valueOf(totalPrice);
            int index = str.indexOf(".");
            if (index != -1) {
                str = str.substring(0, index + 2);
            }
            return "¥"+str;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return "";
        }
    }

    /**
     * 当前选中的品牌在集合中的位置
     *
     * @return 位置
     */
    public int getCurrentSelectedBrandIndex() {
        return mOrderBrands.indexOf(mCurrentOrderBrand);
    }

    /**
     * 获取当前选中的品类在集合中的位置
     *
     * @return 位置
     */
    public int getCurrentSelectedOrderTypeIndex() {
        return mOrderTypes.indexOf(mCurrentProductType);
    }

    /**
     * 获取用户当前选择的支付类型
     *
     * @return 支付类型
     */
    public PayType getCurrentPayType() {
        return mCurrentPaymentTypes;
    }

    /**
     * 根据用户选择的付款方式设置已选产品的产品现价
     */
    public void setProdcutCurrentPrice() {
        try {
            String currentPayKey = mCurrentPaymentTypes.getKey();
            for (Product product : mChoiceProducts) {
                List<ProductPolicy> policys = product.getPRODUCT_POLICY();
                List<PayType> payTypeList = getPaymentList(policys);
                boolean isPayTypeListHasPrice = false;
                for (PayType payType:payTypeList) {
                    if (!TextUtils.isEmpty(currentPayKey) && currentPayKey.equals(payType.getKey())) {
                        product.setPRODUCT_CURRENT_PRICE(payType.getSALE_PRICE());
                        isPayTypeListHasPrice = true;
                    }
                }
                if (!isPayTypeListHasPrice) {
                    product.setPRODUCT_CURRENT_PRICE(product.getPRODUCT_PRICE());
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取付款方式
     * "POLICY_NAME": "怡宝饮料先付款再发货优惠",
     * "POLICY_TYPE": "101_FPAD",
     * <p/>
     * "POLICY_NAME": "怡宝饮料货到付款优惠",
     * "POLICY_TYPE": "102_FDAP",
     * <p/>
     * "POLICY_NAME": "怡宝饮料月结",
     * "POLICY_TYPE": "103_MP"
     *
     * * <p/>
     * "POLICY_NAME": "兑奖",
     * "POLICY_TYPE": DJ 暂无设置
     *
     * @param policies 产品的产品策略集合
     * @return 支付类型集合
     */
    private List<PayType> getPaymentList(List<ProductPolicy> policies) {
        try {
            List<PayType> payTypeList = new ArrayList<>();
            for (ProductPolicy productPolicy : policies) {
                String policyType = productPolicy.getPOLICY_TYPE();
                if (policyType.startsWith("1")) {
                    PayType payType = new PayType();
                    String tmp[] = productPolicy.getPOLICY_TYPE().split("_");
                    payType.setKey(tmp[1]);
                    payType.setText(getPayTypeText(tmp[1]));
                    payType.setSALE_PRICE(Double.parseDouble(productPolicy.getSALE_PRICE()));
                    payTypeList.add(payType);
                }
            }
            return payTypeList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据支付类型英文字段获取显示的支付类型的中文字段
     *
     * @param key 英文字段
     * @return 字符类型中文字段
     */
    private String getPayTypeText(String key) {
        try {
            if (key == null || key.equals("")) return "";
            if (key.equals("FPAD")) {
                return "先付款后发货";
            } else if (key.equals("FDAP")) {
                return "货到付款";
            } else if (key.equals("MP")) {
                return "月结";
            } else if ("DJ".equals(key)) {
                return "兑奖";
            }
            return key;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return key;
        }
    }
}
