package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 产品策略实体类
 * Created by Administrator on 2016/1/5.
 */
public class ProductPolicy implements Parcelable {
    private String POLICY_NAME;
    private String POLICY_TYPE;
    private String AMOUNT_START;
    private String AMOUNT_END;
    private String REQUEST_BATCH;
    private String SALE_PRICE;
    private List<PolicyItem> PolicyItems;

    public ProductPolicy() {
    }

    public ProductPolicy(String POLICY_NAME, String POLICY_TYPE, String AMOUNT_START, String AMOUNT_END, String REQUEST_BATCH, String SALE_PRICE, List<PolicyItem> PolicyItems) {
        this.POLICY_NAME = POLICY_NAME;
        this.POLICY_TYPE = POLICY_TYPE;
        this.AMOUNT_START = AMOUNT_START;
        this.AMOUNT_END = AMOUNT_END;
        this.REQUEST_BATCH = REQUEST_BATCH;
        this.SALE_PRICE = SALE_PRICE;
        this.PolicyItems = PolicyItems;
    }

    public String getPOLICY_NAME() {
        return POLICY_NAME;
    }

    public void setPOLICY_NAME(String POLICY_NAME) {
        this.POLICY_NAME = POLICY_NAME;
    }

    public String getPOLICY_TYPE() {
        return POLICY_TYPE;
    }

    public void setPOLICY_TYPE(String POLICY_TYPE) {
        this.POLICY_TYPE = POLICY_TYPE;
    }

    public String getAMOUNT_START() {
        return AMOUNT_START;
    }

    public void setAMOUNT_START(String AMOUNT_START) {
        this.AMOUNT_START = AMOUNT_START;
    }

    public String getAMOUNT_END() {
        return AMOUNT_END;
    }

    public void setAMOUNT_END(String AMOUNT_END) {
        this.AMOUNT_END = AMOUNT_END;
    }

    public String getREQUEST_BATCH() {
        return REQUEST_BATCH;
    }

    public void setREQUEST_BATCH(String REQUEST_BATCH) {
        this.REQUEST_BATCH = REQUEST_BATCH;
    }

    public String getSALE_PRICE() {
        return SALE_PRICE;
    }

    public void setSALE_PRICE(String SALE_PRICE) {
        this.SALE_PRICE = SALE_PRICE;
    }

    public List<PolicyItem> getPolicyItems() {
        return PolicyItems;
    }

    public void setPolicyItems(List<PolicyItem> policyItems) {
        PolicyItems = policyItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(POLICY_NAME);
        dest.writeString(POLICY_TYPE);
        dest.writeString(AMOUNT_START);
        dest.writeString(AMOUNT_END);
        dest.writeString(REQUEST_BATCH);
        dest.writeString(SALE_PRICE);
        dest.writeList(PolicyItems);
    }

    public static final Creator<ProductPolicy> CREATOR = new Creator<ProductPolicy>() {
        @Override
        public ProductPolicy[] newArray(int size) {
            return new ProductPolicy[size];
        }

        @Override
        public ProductPolicy createFromParcel(Parcel in) {
            return new ProductPolicy(in);
        }
    };

    public ProductPolicy(Parcel in) {
        POLICY_NAME = in.readString();
        POLICY_TYPE = in.readString();
        AMOUNT_START = in.readString();
        AMOUNT_END = in.readString();
        REQUEST_BATCH = in.readString();
        SALE_PRICE = in.readString();
        PolicyItems = in.readArrayList(ProductPolicy.class.getClassLoader());
    }

}
