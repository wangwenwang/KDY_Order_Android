package com.kaidongyuan.app.kdyorder.util;


import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Business;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.kdyorder.constants.URLCostant;

/**
 * 存储一些关于服务器地址的常量信息
 *
 */
public class OrderUtil {

    /**
     *
     * @param product 需要转换的产品实体类
     * @param PRODUCT_TYPE 产品类型默认NR
     * @param LINE_NO 行号，标记先后顺序
     * @param PO_QTY 数量
     * @param OPERATOR_IDX 操作者IDX，也就是登录用户的ID
     * @return
     */
    public static PromotionDetail getPromotionDetailByProduct(Product product, String PRODUCT_TYPE, int LINE_NO, int PO_QTY, long OPERATOR_IDX, double ACT_PRICE,String UOM){
        if (product == null)return null;
        PromotionDetail p = new PromotionDetail();
        p.LOTTABLE10=product.getPRODUCT_TYPE();
        p.ENT_IDX = URLCostant.ENT_IDX;
        p.PRODUCT_TYPE = PRODUCT_TYPE;
        p.PRODUCT_IDX = product.getIDX();
        p.PRODUCT_NO = product.getPRODUCT_NO();
        p.PRODUCT_NAME = product.getPRODUCT_NAME();
        p.PRODUCT_NAME = product.getPRODUCT_NAME();
        p.PRODUCT_URL = product.getPRODUCT_URL();
        p.LINE_NO = LINE_NO;
        p.PO_QTY = PO_QTY;
        p.ORG_PRICE = product.getPRODUCT_PRICE();
        p.OPERATOR_IDX = OPERATOR_IDX;
        p.ACT_PRICE = ACT_PRICE;
        p.PO_VOLUME = product.getPRODUCT_VOLUME() * PO_QTY;
        p.PO_WEIGHT = product.getPRODUCT_WEIGHT() * PO_QTY;
        p.PO_UOM=UOM;
        return p;
    }



    /**
     * 获取付款方式
     * @param key
     * @return
     */
    public static String getPaymentType(String key){
        switch (key){
            case "FPAD":
                return "预付";
            case "FDAP":
                return  "到付";
            case "MP":
                return  "月结";
            case "DJ":
                return "兑奖";
            default:
                return "未知";
        }
    }

    // 在详情页不需要
    public static String getPromotionRemark(String org, boolean isDetail){
        String[] remarks = org.split("\\+\\|\\+");
        String result = "";
        for (int i = 0; i < remarks.length; i ++){
            result += remarks[i];
            if (i != remarks.length - 1 && !remarks[i].equals("")){
                result += "\n";
                if (!isDetail)result+="\t\t";
            }
        }
        return result;
    }

    /**
     * 获取登录的业务类型
     * @return 业务类型 BusinessConstants 中常量
     */
    public static int getBusinessType() {
        try {
            String bussinessName = SharedPreferencesUtil.getValueByName(SharedPreferenceConstants.BUSSINESS_CODE, SharedPreferenceConstants.NAME, 0);

//            if (BusinessConstants.TYPE_YIBAO.equals(bussinessName)) {
//                return BusinessConstants.BUSINESS_TYPE_YIBAO;
//            } else if (BusinessConstants.TYPE_DIKUI.equals(bussinessName)) {
//                return BusinessConstants.BUSINESS_TYPE_DIKUI;
//            } else if (BusinessConstants.TYPE_KANGSHIFU.equals(bussinessName)) {
//                return BusinessConstants.BUSINESS_TYPE_KANGSHIFU;
//            } else {
//                return -1;
//            }
            if (bussinessName.contains(BusinessConstants.TYPE_YIBAO)){
                return BusinessConstants.BUSINESS_TYPE_YIBAO;
            } else if (bussinessName.contains(BusinessConstants.TYPE_DIKUI)) {
                return BusinessConstants.BUSINESS_TYPE_DIKUI;
            } else if (bussinessName.contains(BusinessConstants.TYPE_KANGSHIFU)) {
                return BusinessConstants.BUSINESS_TYPE_KANGSHIFU;
            }else if(bussinessName.contains(BusinessConstants.TYPE_KDYMY)){
                return BusinessConstants.BUSINESS_TYPE_KDYMY;
            } else {
                return -1;
            }

        }catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return -1;
        }
    }
}
