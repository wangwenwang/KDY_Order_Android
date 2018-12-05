package com.kaidongyuan.app.kdyorder.bean;

import java.util.List;

/**
 * Created by ${tom} on 2017/9/25.
 */
public class InPutOrder {
    public String BUSINESS_IDX;//	业务代码
    public String INPUT_TYPE;//	出库类型
    public String ADDRESS_IDX;//	库存客户IDX
    public String ADDRESS_CODE;//	库存客户代码
    public String ADDRESS_NAME;//	库存客户名称
    public String ADDRESS_INFO;//	库存客户地址
    public String OUTPUT_NO;//	原单出库单号
    public String INPUT_NO;//	入库单号
    public String SUPPLIER_CODE;//	出库客户编码
    public String SUPPLIER_NAME;//	出库客户名称
    public String SUPPLIER_ADDRESS;//	出库客户地址
    public int INPUT_QTY;//	出库数量
    public double INPUT_SUM;//	出库总金额
    public double PRICE;//	总原价
    public String INPUT_DATE;//	出库时间
    public double INPUT_WEIGHT;//	出库重量
    public double INPUT_VOLUME;//	出库体积
    public String PARTY_MARK;//	客户备注
    public String ADUT_MARK;//	审核备注
    public String ADD_USER;//	制单人
    public String ADD_DATE;//	制单时间
    public String OPER_USER;//	操作人


    public InPutInfo Info;

    public static class InPutInfo{
        public List<InputModel> Result;
    }

    public static class InputModel {
        public String PRODUCT_TYPE;	//	产品类型
        public Long PRODUCT_IDX;//	产品IDX
        public String PRODUCT_NO;//	产品代码
        public int LINE_NO;//行号
        public String PRODUCT_NAME;//	产品名称
        public String PRODUCT_DESC;//	产品描述
        public double SUM;//	金额
        public double PRODUCT_WEIGHT;//	产品重量
        public double PRODUCT_VOLUME;//	产品体积
        public int INPUT_QTY;//	出库数量
        public String INPUT_UOM;//	出库单位
        public double INPUT_WEIGHT;//	出库重量
        public double INPUT_VOLUME ;//	出库体积
        public double PRICE;//价格
//        public double ORG_PRICE;//	产品原价
//        public double ACT_PRICE;//	促销价
//        public String SALE_REMARK;//	促销备注
//        public double MJ_PRICE;//	满减价格
//        public String MJ_REMARK;//	满减备注
        public String PRODUCTION_DATE;//	生产日期
        public String BATCH_NUMBER;//	批次号
        public String PRODUCT_STATE;//	货物状态
//        public String OPER_USER;//	操作人





    }

}
