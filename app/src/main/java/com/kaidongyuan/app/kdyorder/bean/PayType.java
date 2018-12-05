package com.kaidongyuan.app.kdyorder.bean;

/**
 * Created by Administrator on 2016/1/4.
 * 支付类型
 */
public class PayType implements java.io.Serializable {
    private String Key;
    private String Text;
    private double SALE_PRICE;

    public PayType() {
    }

    public PayType(String key, String text, double SALE_PRICE) {
        Key = key;
        Text = text;
        this.SALE_PRICE = SALE_PRICE;
    }

    public double getSALE_PRICE() {
        return SALE_PRICE;
    }

    public void setSALE_PRICE(double SALE_PRICE) {
        this.SALE_PRICE = SALE_PRICE;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayType payType = (PayType) o;

        return !(Key != null ? !Key.equals(payType.Key) : payType.Key != null);

    }

    @Override
    public int hashCode() {
        return Key != null ? Key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PayType{" +
                "Key='" + Key + '\'' +
                ", Text='" + Text + '\'' +
                ", SALE_PRICE=" + SALE_PRICE +
                '}';
    }
}
