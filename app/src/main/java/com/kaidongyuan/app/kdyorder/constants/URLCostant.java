package com.kaidongyuan.app.kdyorder.constants;

import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.kdyorder.util.StringUtils;

/**
 * Created by Administrator on 2016/5/16.
 * 网络请求常量
 */
public class URLCostant {

    public static final int LONG_TIMEOUT_MS=5*1000;
    public static final int LONG_MAX_RETRIES=1;
    public static final float LONG_BACKOFF_MULT=2.0f;

    /**
     * OrderUtil 中使用到
     */
    public static final long ENT_IDX = 9008;

    /**
     * 服务器地址
     */
    public static final String BASE_URL = "http://oms.kaidongyuan.com:8088/api/";

    /**
     * 文件下载根地址
     */
    public static final String LOA_URL = "http://oms.kaidongyuan.com:8088/";

    /**
     * 最新资讯图片下载的根地址
     */
    public static final String INFORMATION_PICTURE_URL = "http://china56pd.com:8090/uploadfile/";

    /**
     * 登录接口
     */
    public static final String LOGIN_URL = BASE_URL + "Login";//登录
    /**
     * 注册账户
     */
    public static final String REGISTER=BASE_URL+"register";
    /**
     * 获取省市区县四级的联动列表
     */
    public static final String NormalAdressList=BASE_URL+"NormalAdressList";
    /**
     * 加入客户资料
     */
    public static final String AddParty=BASE_URL+"AddParty";

    /**
     * 解绑客户资料
     */
    public static final String DeleteAppUserParty=BASE_URL+"DeleteAppUserParty";
    /**
     * 添加客户地址
     */
    public static final String  AddPartyAddress=BASE_URL+"AddAddress";
    /**
     * 删除客户地址
     */
    public static final String  deletePartyAddress=BASE_URL+"DeleteAddress";
    /**
     * 修改客户地址
     */
    public static final String UpdatePartyAddress=BASE_URL+"UpdateAddress";
    /**
     * 获取用户业务类型
     */
    public static final String GET_BUISNESS_LIST = BASE_URL + "GetBuisnessList";

    /**
     * 获取客户列表
     */
    public static final String GET_PARTY_LIST = BASE_URL + "GetPartyList";

    /**
     * 获取在途订单列表
     */
    public static final String GET_ORDER_LIST = BASE_URL + "GetOrderList";

    /**
     * 获取客户地址列表
     */
    public static final String GET_ADDRESS = BASE_URL + "GetAddress";

    /**
     * 获取订单轨迹
     */
    public static final String GET_ORDER_TRAJECTORY = BASE_URL + "GetLocaltionForOrdNo";

    /**
     * 获取客户报表
     */
    public static final String GET_CUSTOMER_CHART_DATA = BASE_URL + "CustomerCount";

    /**
     * 获取产品报表
     */
    public static final String GET_PRODUCT_CHART_DATA = BASE_URL + "ProductCount";

    /**
     * 获取热销产品列表
     */
    public static final String GET_HOT_SELL_PRODUCT = BASE_URL + "GetProductList";

    /**
     * 获取最新资讯
     */
    public static final String GET_NEWEST_INFORMATION = BASE_URL + "Information";

    /**
     * 获取资讯详情
     */
    public static final String GET_INFORMATION_DETAILS = BASE_URL + "GetNewDetail";

    /**
     * 获取订单详情
     */
    public static final String GET_ORDER_DETAIL = BASE_URL + "GetOrderDetail";

    /**
     * 获取订单物流信息
     */
    public static final String GET_ORDER_TMSLIST = BASE_URL + "GetOrderTmsList";

    /**
     * 获取订单物流信息
     */
    public static final String GET_ORDER_TMSLIST_SAAS = BASE_URL + "GetOrderTmsListSaaS";

//    /**
//     * 获取物流信息详情
//     */
//    public static final String GET_ORDER_TMS_INFORMATION = BASE_URL + "GetOrderTmsInfo";

    /**
     * 获取物流信息详情
     */
    public static final String GET_ORDER_TMS_INFORMATION = BASE_URL + "GetOrderTmsOrderNoInfo";

    /**
     * 获取物流信息详情
     */
    public static final String GET_ORDER_TMS_INFORMATION_SAAS = BASE_URL + "GetOrderTmsOrderNoInfoSaaS";

    /**
     * 获取订单位置信息
     */
    public static final String GET_LOCATION = BASE_URL + "GetLocaltion";

    /**
     * 获取订单位置信息
     */
    public static final String GET_LOCATION_SAAS = BASE_URL + "GetLocaltionSaaS";

    /**
     * 获取最新版本 app 信息
     */
    public static final String CHECK_VERSION = BASE_URL + "GetVersion";

    /**
     * 修改密码接口
     */
    public static final String UPDATA_PASSWORD = BASE_URL + "modifyPassword";

    /**
     * 获取付款方式 post strLicense  过来就行了
     */
    public static final String GET_PAYMENT_TYPE = BASE_URL + "GetPaymentType";

    /**
     * 获取产品品牌和分类列表
     */
    public static final String GET_PRODUCT_TYPE = BASE_URL + "GetProductType";

    /**
     * 根据品牌分类信息获取产品列表
     */
    public static final String GET_PRDUCT_LIST_TYPE = BASE_URL + "GetProductListType";

    /**
     * 获取促销信息
     */
    public static final String GET_PROMOTION_INFORMATION = BASE_URL + "GetPartySalePolicy";

    /**
     * 提交获取策略促销信息
     */
    public static final String SUBMIT_ORDER = BASE_URL + "SubmitOrder1";

    /**
     * 最终提交订单
     */
    public static final String CONFIRM_ORDER = BASE_URL + "ConfirmOrder";

    /**
     * 获取赠品品类详细信息
     */
    public static final String GET_COMMODITY_DATA = BASE_URL + "GetProductListType";

    /**
     * 获取电子签名和交货现场图片
     */
    public static final String GETAUTOGRAPH = BASE_URL + "GetAutograph";

    /**
     * 获取电子签名和交货现场图片
     */
    public static final String GETAUTOGRAPH_SAAS = BASE_URL + "GetAutographSaaS";

    /**
     * 1.1 、添加客户库存
     */
    public static final String AddStock=BASE_URL+"AddStock";

    /**
     * 1.2 展示客户库存登记表
     */
    public static final String GetStockList=BASE_URL+"GetStockList";
    /**
     * 1.2.1 展示客户库存登记表
     */
    public static final String GetStockList1=BASE_URL+"GetStockList1";
    /**
     * 1.3 展示客户库存详细登记
     */
    public static final String GetAppStockList=BASE_URL+"GetAppStockList";
    /**
     * 1.7 取消客户库存
     */
    public static final String CancelStock=BASE_URL+"CancelStock";
    /**
     * 取消待接收状态的订单
     */
    public static final String OrderCancel=BASE_URL+"OrderCancel";
    /**
     * 1.9 账单列表
     */
    public static final String GetAppBillFeeList=BASE_URL+"GetAppBillFeeList";
    /**
     * 2.0 费用列表
     */
    public static final String GetAppBusinessFeeList=BASE_URL+"GetAppBusinessFeeList";

    /************************经销商库存进销存管理系列接口******************************/
    //1.1出库的收货人
    public static final String GetToAddressList=BASE_URL+"GetToAddressList";
    //1.2获取产品列表
    public static final String GetOutProductList=BASE_URL+"GetOutProductList";
    //1.3 保存出库单
    public static final String SaveOutput=BASE_URL+"SaveOutput";
    //1.4 确认出库单
    public static final String OutPutWorkflow=BASE_URL+"OutPutWorkflow";
    //1.5 取消出库单
    public static final String OutPutCancel=BASE_URL+"OutPutCancel";
    //1.6 获取出库列表
    public static final String GetOupputList=BASE_URL+"GetOupputList";
    //1.7 获取出库详情
    public static final String GetOupputInfo=BASE_URL+"GetOupputInfo";
    //1.8 获取库存列表数据
    public static final String GetPartyStockList=BASE_URL+"GetPartyStockList";
    //1.9 获取库存批次
    public static final String GetStockNoList=BASE_URL+"GetStockNoList";
    //1.91 20171114 产品库存批次数据，出入库明细
    public static final String GetNewStockNoList=BASE_URL+"GetNewStockNoList";
    //2.0 入库确认
    public static final String InPutWorkflow=BASE_URL+"InPutWorkflow";
    //2.1 入库列表
    public static final String GetInputList=BASE_URL+"GetInputList";
    //2.2 入库详情
    public static final String GetInputInfo=BASE_URL+"GetInputInfo";
    //2.3 保存入库单
    public static final String SaveInput=BASE_URL+"SaveInput";
    //2.4 上一级地址
    private static final String	GetInputToPartySearch=BASE_URL+"GetInputToPartySearch";
    //2.5 获取产品分类列表
    public static final String GetOutProductType=BASE_URL+"GetOutProductType";

    /***************************普洛斯计划订单管理接口*****************************/
    //1.1 下计划单
    public static final String ImportToOrderPlanList=BASE_URL+"ImportToOrderPlanList";
    //1.2 获取采购计划信息
    public static final String GetOrderPlanList=BASE_URL+"GetOrderPlanList";
    //1.3 获取订单详情列表
    public static final String GetOrderPlanDetail=BASE_URL+"GetOrderPlanDetail";

    /****************************20171225 上海项目部增加直接查询物流订单、查询物流仓库库存功能********************************/
    //2.41查询同一到达地址订单列表
    public static final String GetTmsOrderByAddress=BASE_URL+"GetTmsOrderByAddress";
    //2.42查询订单物流信息
    public static final String GetOrderTms=BASE_URL+"GetOrderTms";
    //1.1 总的列表
    public static final String GetWmsProductZong=BASE_URL+"GetWmsProductZong";
    //1.2 分的列表
    public static final String GetWmsProductSum=BASE_URL+"GetWmsProductSum";
    /*******************************20181210 客户拜访 start*******************************************************************/
    // 客户拜访线路
    public static final String GetPartyVisitLine=BASE_URL+"GetPartyVisitLine";
    // 获取客户拜访列表
    public static final String GetPartyVisitList=BASE_URL+"GetPartyVisitList";
    // 客户拜访：获取渠道
    public static final String GetPartyVisitChannel=BASE_URL+"GetPartyVisitChannel";
    // 增加地址
    public static final String AddAddress=BASE_URL+"AddAddress";
    // 添加客户拜访
    public static final String GetPartyVisitInsert=BASE_URL+"GetPartyVisitInsert";
    // 确认客户资料信息
    public static final String GetVisitConfirmCustomer=BASE_URL+"GetVisitConfirmCustomer";
    // 进店
    public static final String GetVisitEnterShop=BASE_URL+"GetVisitEnterShop";
    // 检查库存
    public static final String GetVisitCheckInventory=BASE_URL+"GetVisitCheckInventory";
    // 建议订单
    public static final String GetVisitRecommendedOrder=BASE_URL+"GetVisitRecommendedOrder";
    // 生动化陈列
    public static final String GetVisitVividDisplay=BASE_URL+"GetVisitVividDisplay";
    // 离店
    public static final String GetVisitLeaveShop=BASE_URL+"GetVisitLeaveShop";
    // 获取客户拜访订单
    public static final String GetVisitAppOrder=BASE_URL+"GetVisitAppOrder";
    // 获取客户拜访照片
    public static final String GetPictureByVisitIdx=BASE_URL+"GetPictureByVisitIdx";
    // 获取生动化陈列
    public static final String VividDisplayCBX=BASE_URL+"VividDisplayCBX";
    // 根据客户地址id，获取上级地址id
    public static final String GetFatherAddress=BASE_URL+"GetFatherAddress";
    // 获取经销商客户列表
    public static final String GetFirstPartyList=BASE_URL+"GetFirstPartyList";
    // 根据业务代码，获取客户编号
    public static final String ObtainPartyCode=BASE_URL+"ObtainPartyCode";
}
















