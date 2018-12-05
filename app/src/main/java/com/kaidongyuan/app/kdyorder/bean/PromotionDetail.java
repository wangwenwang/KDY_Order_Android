package com.kaidongyuan.app.kdyorder.bean;

import android.os.Parcel;
import android.os.Parcelable;

//Parcelable序列化PromotionDetail类，方便使用 intent1.putParcelableArrayListExtra（）进行数组值传递
public class PromotionDetail implements Parcelable {

	public long IDX;
	public long ENT_IDX;
	public long ORDER_IDX;
	public String PRODUCT_TYPE;  //NR：正常品  GF：赠品
	public long PRODUCT_IDX;
	public String PRODUCT_NO;
	public String PRODUCT_NAME;
	public int LINE_NO;
	public int PO_QTY;
	public String PO_UOM;
	public double PO_WEIGHT;
	public double PO_VOLUME;
	public double ORG_PRICE;
	public double ACT_PRICE;
	public String SALE_REMARK;//促销备注信息
	public String MJ_REMARK;
	public double MJ_PRICE;
	public String LOTTABLE01;
	public String LOTTABLE02; //NR：正常品  GF：赠品
	public String LOTTABLE03;
	public String LOTTABLE04;
	public String LOTTABLE05;
	/** 此赠品信息是否要可以调价 “Y”考虑 “N”不考虑 */
	public String LOTTABLE06="N";
	public String LOTTABLE07;
	public String LOTTABLE08;
	/** 此赠品信息是否要考虑库存 “Y”考虑 “N”不考虑 */
	public String LOTTABLE09;
	public String LOTTABLE10;//产品所属的分类名
	/** 此赠品信息对应的库存 */
	public int LOTTABLE11;
	/** 此赠品信息调价上限数 */
	public double LOTTABLE12;
	/** 此赠品信息调价下限数 */
	public double LOTTABLE13;
	public long OPERATOR_IDX;
	public String ADD_DATE;
	public String EDIT_DATE;
	public String PRODUCT_URL;

	public PromotionDetail(){}
//通过Parcel接口来获取PromotionDetail实例
	protected PromotionDetail(Parcel in) {
		IDX = in.readLong();
		ENT_IDX = in.readLong();
		ORDER_IDX = in.readLong();
		PRODUCT_TYPE = in.readString();
		PRODUCT_IDX = in.readLong();
		PRODUCT_NO = in.readString();
		PRODUCT_NAME = in.readString();
		LINE_NO = in.readInt();
		PO_QTY = in.readInt();
		PO_UOM = in.readString();
		PO_WEIGHT = in.readDouble();
		PO_VOLUME = in.readDouble();
		ORG_PRICE = in.readDouble();
		ACT_PRICE = in.readDouble();
		SALE_REMARK = in.readString();
		MJ_REMARK = in.readString();
		MJ_PRICE = in.readDouble();
		LOTTABLE01 = in.readString();
		LOTTABLE02 = in.readString();
		LOTTABLE03 = in.readString();
		LOTTABLE04 = in.readString();
		LOTTABLE05 = in.readString();
		LOTTABLE06 = in.readString();
		LOTTABLE07 = in.readString();
		LOTTABLE08 = in.readString();
		LOTTABLE09 = in.readString();
		LOTTABLE10 = in.readString();
		LOTTABLE11 = in.readInt();
		LOTTABLE12=in.readDouble();
		LOTTABLE13=in.readDouble();
		OPERATOR_IDX = in.readLong();
		ADD_DATE = in.readString();
		EDIT_DATE = in.readString();
		PRODUCT_URL = in.readString();
	}
	//读取接口，以Creator嵌入
	public static final Creator<PromotionDetail> CREATOR = new Creator<PromotionDetail>() {
		@Override
		public PromotionDetail createFromParcel(Parcel in) {
			return new PromotionDetail(in);
		}

		@Override
		public PromotionDetail[] newArray(int size) {
			return new PromotionDetail[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(IDX);
		dest.writeLong(ENT_IDX);
		dest.writeLong(ORDER_IDX);
		dest.writeString(PRODUCT_TYPE);
		dest.writeLong(PRODUCT_IDX);
		dest.writeString(PRODUCT_NO);
		dest.writeString(PRODUCT_NAME);
		dest.writeInt(LINE_NO);
		dest.writeInt(PO_QTY);
		dest.writeString(PO_UOM);
		dest.writeDouble(PO_WEIGHT);
		dest.writeDouble(PO_VOLUME);
		dest.writeDouble(ORG_PRICE);
		dest.writeDouble(ACT_PRICE);
		dest.writeString(SALE_REMARK);
		dest.writeString(MJ_REMARK);
		dest.writeDouble(MJ_PRICE);
		dest.writeString(LOTTABLE01);
		dest.writeString(LOTTABLE02);
		dest.writeString(LOTTABLE03);
		dest.writeString(LOTTABLE04);
		dest.writeString(LOTTABLE05);
		dest.writeString(LOTTABLE06);
		dest.writeString(LOTTABLE07);
		dest.writeString(LOTTABLE08);
		dest.writeString(LOTTABLE09);
		dest.writeString(LOTTABLE10);
		dest.writeInt(LOTTABLE11);
		dest.writeDouble(LOTTABLE12);
		dest.writeDouble(LOTTABLE13);
		dest.writeLong(OPERATOR_IDX);
		dest.writeString(ADD_DATE);
		dest.writeString(EDIT_DATE);
		dest.writeString(PRODUCT_URL);
	}


	@Override
	public String toString() {
		return "PromotionDetail{" +
				"IDX=" + IDX +
				", ENT_IDX=" + ENT_IDX +
				", ORDER_IDX=" + ORDER_IDX +
				", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
				", PRODUCT_IDX=" + PRODUCT_IDX +
				", PRODUCT_NO='" + PRODUCT_NO + '\'' +
				", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
				", LINE_NO=" + LINE_NO +
				", PO_QTY=" + PO_QTY +
				", PO_UOM='" + PO_UOM + '\'' +
				", PO_WEIGHT=" + PO_WEIGHT +
				", PO_VOLUME=" + PO_VOLUME +
				", ORG_PRICE=" + ORG_PRICE +
				", ACT_PRICE=" + ACT_PRICE +
				", SALE_REMARK='" + SALE_REMARK + '\'' +
				", MJ_REMARK='" + MJ_REMARK + '\'' +
				", MJ_PRICE=" + MJ_PRICE +
				", LOTTABLE01='" + LOTTABLE01 + '\'' +
				", LOTTABLE02='" + LOTTABLE02 + '\'' +
				", LOTTABLE03='" + LOTTABLE03 + '\'' +
				", LOTTABLE04='" + LOTTABLE04 + '\'' +
				", LOTTABLE05='" + LOTTABLE05 + '\'' +
				", LOTTABLE06='" + LOTTABLE06 + '\'' +
				", LOTTABLE07='" + LOTTABLE07 + '\'' +
				", LOTTABLE08='" + LOTTABLE08 + '\'' +
				", LOTTABLE09='" + LOTTABLE09 + '\'' +
				", LOTTABLE10='" + LOTTABLE10 + '\'' +
				", LOTTABLE11='" + LOTTABLE11 + '\'' +
				", LOTTABLE12='" + LOTTABLE12 + '\'' +
				", LOTTABLE13='" + LOTTABLE13 + '\'' +
				", OPERATOR_IDX=" + OPERATOR_IDX +
				", ADD_DATE='" + ADD_DATE + '\'' +
				", EDIT_DATE='" + EDIT_DATE + '\'' +
				", PRODUCT_URL='" + PRODUCT_URL + '\'' +
				'}';
	}

}
