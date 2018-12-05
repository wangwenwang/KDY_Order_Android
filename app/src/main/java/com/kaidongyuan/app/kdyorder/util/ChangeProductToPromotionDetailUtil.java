package com.kaidongyuan.app.kdyorder.util;

import android.util.Log;


import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Product;
import com.kaidongyuan.app.kdyorder.bean.PromotionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 * 将 Product 实体类转换成 PromotionDetail 实体类
 */
public class ChangeProductToPromotionDetailUtil {

    public static List<PromotionDetail> change(List<Product> productList){
        PromotionDetail promotionDetail;
        List<PromotionDetail> promotionDetailList = new ArrayList<>();
        for (Product product:productList) {
            promotionDetail = new PromotionDetail();
            promotionDetail.ENT_IDX = 9008;
            promotionDetail.PRODUCT_IDX = product.getIDX();
            promotionDetail.PRODUCT_TYPE = "GF";
            promotionDetail.PRODUCT_NO = product.getPRODUCT_NO();
            promotionDetail.PRODUCT_NAME = product.getPRODUCT_NAME();
            promotionDetail.LINE_NO = 0;
            promotionDetail.PO_QTY = 0;
            promotionDetail.PO_WEIGHT = product.getPRODUCT_WEIGHT();
            promotionDetail.PO_VOLUME = product.getPRODUCT_VOLUME();
            promotionDetail.ORG_PRICE = product.getPRODUCT_PRICE();
            promotionDetail.SALE_REMARK = product.getPRODUCT_NAME() + "分类赠品";
            promotionDetail.MJ_PRICE = 0;
            promotionDetail.LOTTABLE02 = "NR";
            promotionDetail.OPERATOR_IDX = Long.parseLong(MyApplication.getInstance().getUser().getIDX().trim());
            promotionDetail.LOTTABLE09 = product.getISINVENTORY();
            promotionDetail.LOTTABLE11 = product.getPRODUCT_INVENTORY();
            promotionDetailList.add(promotionDetail);
        }

        return promotionDetailList;
    }

}






















