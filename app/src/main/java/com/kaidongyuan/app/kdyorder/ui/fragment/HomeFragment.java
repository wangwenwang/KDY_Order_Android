package com.kaidongyuan.app.kdyorder.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.constants.BroadcastConstants;
import com.kaidongyuan.app.kdyorder.constants.BusinessConstants;
import com.kaidongyuan.app.kdyorder.ui.activity.BusinessInventoryActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.ChartCheckActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.CheckTmsOrderListActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.CustomerMeetingsActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.FeePartyActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.HotSellProductActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.InventoryManageActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.InventoryPartyListActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.NewestInformationActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PartyInventoryActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.PreOrderManageActivity;
import com.kaidongyuan.app.kdyorder.ui.activity.SearchOrderTrajectoryActivity;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.OrderUtil;
import com.kaidongyuan.app.kdyorder.util.SharedPreferencesUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.widget.CycleViewpager;
import com.kaidongyuan.app.kdyorder.widget.MoveButton;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/4/1.
 * 主页 Fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private View mParentView;

    /**
     * 广告轮播控件
     */
    private CycleViewpager mCycleviewpager;
    /**
     * 跳转到下单界面按钮
     */
    private MoveButton mMoveButtonMakeOrder;
    /**
     * 跳转到查看表表界面
     */
    private PercentRelativeLayout mPercentrlChart;
    /**
     * 跳转到查看最新资讯界面
     */
    private PercentRelativeLayout mPercentrlInformation;
    /**
     * 跳转到热销产品界面
     */
    private PercentRelativeLayout mPercentrlSelling;
    /**
     * 跳转到查看货物轨迹界面
     */
    private PercentRelativeLayout mPercentrlTrackPress;
    /**
     * 跳转到查看订单的 Fragmeng
     */
    private PercentRelativeLayout mPercentrlCheckOrder;
    /**
     *跳转到查看库存登记表
     */
    private PercentRelativeLayout mPercentrlInventoryManage;
    /**
     *跳转到费用账单
     */
    private PercentRelativeLayout mPercentrlBillFeeManage;

    /**
     * 跳转到库存管理
     */
    private PercentRelativeLayout mPercentrlPartyInventory;

    /**
     * 跳转到计划订单管理
     */
    public PercentRelativeLayout mPercentrlPreorderManage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mParentView = inflater.inflate(R.layout.fragment_home, null);
            initView();
            initCycleViewData();
            setListener();
            return mParentView;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return mParentView;
        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            mCycleviewpager.stopPlay();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initView() {
        try {
            mCycleviewpager = (CycleViewpager) mParentView.findViewById(R.id.cycleViewpager_carousel);
            mMoveButtonMakeOrder = (MoveButton) mParentView.findViewById(R.id.movebutton_makeOrder);
            mPercentrlChart = (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_chart);
            mPercentrlInformation = (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_information);
            mPercentrlSelling = (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_selling);
            mPercentrlTrackPress = (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_trackpress);
            mPercentrlCheckOrder = (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_checkorder);
            mPercentrlInventoryManage= (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_inventory_manage);
            mPercentrlBillFeeManage= (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_fee_manage);
            mPercentrlPartyInventory= (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_party_inventory);
            mPercentrlPreorderManage= (PercentRelativeLayout) mParentView.findViewById(R.id.percentrl_preorder_manage);
            if (OrderUtil.getBusinessType()==BusinessConstants.BUSINESS_TYPE_YIBAO){
                mPercentrlInventoryManage.setVisibility(View.VISIBLE);
                mPercentrlBillFeeManage.setVisibility(View.VISIBLE);
                mPercentrlPartyInventory.setVisibility(View.VISIBLE);
                mPercentrlPreorderManage.setVisibility(View.VISIBLE);
            }else if (OrderUtil.getBusinessType()==BusinessConstants.BUSINESS_TYPE_DIKUI){
                mPercentrlInventoryManage.setVisibility(View.INVISIBLE);
                mPercentrlBillFeeManage.setVisibility(View.INVISIBLE);
                mPercentrlPartyInventory.setVisibility(View.VISIBLE);
                mPercentrlPreorderManage.setVisibility(View.VISIBLE);
            }else if (OrderUtil.getBusinessType()==BusinessConstants.BUSINESS_TYPE_KANGSHIFU){
                mPercentrlInventoryManage.setVisibility(View.INVISIBLE);
                mPercentrlBillFeeManage.setVisibility(View.INVISIBLE);
                mPercentrlPartyInventory.setVisibility(View.VISIBLE);
                mPercentrlPreorderManage.setVisibility(View.VISIBLE);
            }else if (OrderUtil.getBusinessType()==BusinessConstants.BUSINESS_TYPE_KDYMY){
                mPercentrlInventoryManage.setVisibility(View.INVISIBLE);
                mPercentrlBillFeeManage.setVisibility(View.INVISIBLE);
                mPercentrlPartyInventory.setVisibility(View.VISIBLE);
                mPercentrlPreorderManage.setVisibility(View.VISIBLE);
            }else {
                mPercentrlInventoryManage.setVisibility(View.INVISIBLE);
                mPercentrlBillFeeManage.setVisibility(View.INVISIBLE);
                mPercentrlPartyInventory.setVisibility(View.VISIBLE);
                mPercentrlPreorderManage.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置轮播的图片和圆点图片
     */
    private void initCycleViewData() {
        try {
            mCycleviewpager.setImagesData(new CycleViewpager.ICycleViewpager() {
                @Override
                public List<Integer> setImageResourcesId() {
                    int businessType = OrderUtil.getBusinessType();
                    return getImageList(businessType);
                }

                @Override
                public Map<String, Integer> setPointResourcesId() {
                    HashMap<String, Integer> pointMap = new HashMap<>();
                    pointMap.put(CycleViewpager.SELECTED, R.drawable.point_focused);
                    pointMap.put(CycleViewpager.UNSELECTED, R.drawable.point_unfocused);
                    return pointMap;
                }
            });
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mMoveButtonMakeOrder.setOnMoveToDoSomeThingListener(new MoveButton.MoveToStartDoSomeThingInterface() {
                @Override
                public void doRightSuccess() {//滑动到右侧，跳转到下单 Fragment 界面
                    Intent intent = new Intent(BroadcastConstants.JUMPTO_MAKEORDER_FRAGMENT);
                    MyApplication.getAppContext().sendBroadcast(intent);
                }

                @Override
                public void doLeftSuccess() {//滑动到左侧，跳转到查单 Fragment 界面
                    Intent checkOrderIntent = new Intent(BroadcastConstants.JUMPTO_CHECKORDER_FRAGMENT);
                    MyApplication.getAppContext().sendBroadcast(checkOrderIntent);
                }
            });
            mPercentrlChart.setOnClickListener(this);
            mPercentrlInformation.setOnClickListener(this);
            mPercentrlSelling.setOnClickListener(this);
            mPercentrlTrackPress.setOnClickListener(this);
            mPercentrlCheckOrder.setOnClickListener(this);
            mPercentrlInventoryManage.setOnClickListener(this);
            mPercentrlBillFeeManage.setOnClickListener(this);
            mPercentrlPartyInventory.setOnClickListener(this);
            mPercentrlPreorderManage.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 根据用户登录的业务类型获取图片资源集合
     *
     * @param businessType 用户登录业务类型
     * @return 图片资源集合
     */
    private List<Integer> getImageList(int businessType) {
        List<Integer> imageList = new ArrayList<>();
        try {
            switch (businessType) {
                case BusinessConstants.BUSINESS_TYPE_YIBAO:
                    imageList.add(R.drawable.ad_pic_0);
                    imageList.add(R.drawable.ad_pic_1);
                    imageList.add(R.drawable.ad_pic_2);
                    imageList.add(R.drawable.ad_pic_3);
                    break;
                case BusinessConstants.BUSINESS_TYPE_DIKUI:
                    imageList.add(R.drawable.ad_pic_10);
                    imageList.add(R.drawable.ad_pic_11);
                    imageList.add(R.drawable.ad_pic_12);
                    imageList.add(R.drawable.ad_pic_13);
                    break;
                case BusinessConstants.BUSINESS_TYPE_KANGSHIFU:
                    imageList.add(R.drawable.ad_pic_0);
                    imageList.add(R.drawable.ad_pic_1);
                    imageList.add(R.drawable.ad_pic_2);
                    imageList.add(R.drawable.ad_pic_3);
                    break;
                case BusinessConstants.BUSINESS_TYPE_KDYMY:
                    imageList.add(R.drawable.ad_pic_00);
                    imageList.add(R.drawable.ad_pic_11);
                    imageList.add(R.drawable.ad_pic_00);
                    imageList.add(R.drawable.ad_pic_11);
                    break;
                default:
                    imageList.add(R.drawable.ad_pic_00);
                    break;
            }
            return imageList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return imageList;
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.percentrl_chart://跳转到查看报表界面
                    Intent chartCheckIntent = new Intent(this.getActivity(), ChartCheckActivity.class);
                    startActivity(chartCheckIntent);
                    break;
                case R.id.percentrl_information:
                    //跳转到最新资讯界面
//                    Intent newestInformationIntent = new Intent(this.getActivity(), NewestInformationActivity.class);
//                    startActivity(newestInformationIntent);
                    startActivity(new Intent(this.getActivity(), CustomerMeetingsActivity.class));
                    break;
                case R.id.percentrl_selling://跳转到热销产品界面
                    Intent hotSellProductIntent = new Intent(this.getActivity(), HotSellProductActivity.class);
                    startActivity(hotSellProductIntent);
                    break;
                case R.id.percentrl_trackpress://（跳转到货物轨迹界面）20180103 替换成查看物流订单列表

//                    Intent trackPressIntent = new Intent(this.getActivity(), SearchOrderTrajectoryActivity.class);
//                    startActivity(trackPressIntent);
                    Intent checkTmsIrdersIntent=new Intent(this.getActivity(), CheckTmsOrderListActivity.class);
                    startActivity(checkTmsIrdersIntent);
                    break;
                case R.id.percentrl_checkorder://跳转到查看订单 Fragment
                    Intent checkOrderIntent = new Intent(BroadcastConstants.JUMPTO_CHECKORDER_FRAGMENT);
                    MyApplication.getAppContext().sendBroadcast(checkOrderIntent);
                    break;
                case R.id.percentrl_inventory_manage://跳转到库存登记 Activity
                    Intent inventoryManageIntent = new Intent(this.getActivity(), InventoryManageActivity.class);
                    startActivity(inventoryManageIntent);
                    break;
                case R.id.percentrl_fee_manage://跳转到费用账单客户选择 Activity
                    Intent feePartyIntent=new Intent(this.getActivity(), FeePartyActivity.class);
                    startActivity(feePartyIntent);
                    break;
                case R.id.percentrl_party_inventory://跳转到库存管理客户选择 Activity
                  if (OrderUtil.getBusinessType()==BusinessConstants.BUSINESS_TYPE_DIKUI){
                      Intent partyInventoryIntent=new Intent(this.getActivity(), BusinessInventoryActivity.class);
                      startActivity(partyInventoryIntent);
                  }else {
                      Intent inventoryPartyListIntent=new Intent(this.getActivity(),InventoryPartyListActivity.class);
                      startActivity(inventoryPartyListIntent);
                  }
                    break;
                case R.id.percentrl_preorder_manage://跳转到计划订单预约管理 Activity(普洛斯项目)
                    Intent preorderManageIntent=new Intent(this.getActivity(), PreOrderManageActivity.class);
                    startActivity(preorderManageIntent);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }


}























