package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kaidongyuan.app.kdyorder.R;
import com.kaidongyuan.app.kdyorder.app.MyApplication;
import com.kaidongyuan.app.kdyorder.bean.Location;
import com.kaidongyuan.app.kdyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.kdyorder.model.OrderPathActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.baidumap.DrivingRouteOverlay;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 * 订单轨迹界面
 */
public class OrderPathActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 对应的业务类
     */
    private OrderPathActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 显示订单的公里数
     */
    private TextView mTextViewOrderDistance;
    /**
     * 百度地图控件
     */
    private MapView mMapView;
    /**
     * 百度地图
     */
    private BaiduMap mBaiduMap;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    /**
     * 线路距离
     */
    private double distance = 0;
    /**
     * 线路距离
     */
    private double distance0 = 0;
    /**
     * 订单线路查询时显示的 Dialog
     */
    private ProgressDialog mProgressDialog;

    /**
     * 路线规划失败后递归回调次数
     */
    private int mGetDrivingRouteResultTime = 0;
    /**
     * 路线失败规划递归最多次数
     */
    private final int mGetDrivingRouteResultMamTime = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_path);
        try {
            initData();
            setTop();
            initView();
            setListener();
            getOrderPathData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            mMapView.onResume();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            mMapView.onPause();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mMapView.onDestroy();
            mMapView = null;
            mBaiduMap = null;
            mBiz.cancelRequest();
            mBiz = null;
            if (mLoadingDialog!=null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            if (mProgressDialog!=null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
            mImageViewGoBack = null;
            mTextViewOrderDistance = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new OrderPathActivityBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setTop () {
        //版本4.4以上设置状态栏透明，界面布满整个界面
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View topView = findViewById(R.id.topview);
            ViewGroup.LayoutParams topParams = topView.getLayoutParams();
            topParams.height = DensityUtil.getStatusHeight()*16/30;
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mTextViewOrderDistance = (TextView) this.findViewById(R.id.tv_distance);
            mMapView = (MapView) this.findViewById(R.id.baidumap);
            mBaiduMap = mMapView.getMap();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单线路数据
     */
    public void getOrderPathData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID)) {
                String orderId = intent.getStringExtra(EXTRAConstants.ORDER_DETAILSACTIVITY_ORDER_ID);
                if (orderId == null || orderId.length() <= 0) {
                    ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
                } else {
                    if (mBiz.getOrderPathData(orderId)) {
                        showLoadingDialog();
                    } else {
                        ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
                    }
                }
            } else {
                ToastUtil.showToastBottom("请重新进入该界面，如果重新进入还是不能正常显示，请联系供应商！", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示的 Dialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_goback://返回上一界面
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求失败时调用
     *
     * @param message 显示的信息
     */
    public void getOrderPathDataError(String message) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求成功时调用
     */
    public void getOrderPathDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            List<Location> locations = mBiz.getOrdrePathData();
            if (locations != null && locations.size() > 1) {
                showOrderPathInMap(locations);
                showSearchPathDialog();
            } else {
                ToastUtil.showToastBottom("订单线路数据为空!", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 将订单线路显示到地图上
     *
     * @param locations 线路点位集合
     */
    private void showOrderPathInMap(List<Location> locations) {
        try {
            //绘制路线
            for (int i = 0; i < locations.size() / 100; i++) {
                List<Location> locationListfd = locations.subList(i * 100, i * 100 + 101);
                searchInMap(locationListfd);
            }
            List<Location> locationListmr = locations.subList(locations.size() - locations.size() % 100, locations.size());
            searchInMap(locationListmr);

            //计算路程，并显示到界面上
            distance0 = mBiz.getDistance(locations);
            String dis = String.valueOf(distance0 / 1000);
            int index = dis.indexOf(".");
            if (index != -1 && (dis.length() - index) > 6) {
                dis = dis.substring(0, index + 6);
            }
            mTextViewOrderDistance.setText("公里数：" + dis + "公里");

            //绘制起点和终点的图标
            drawStartAndEndPicture(locations);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 绘制起点终点标记和路线
     *
     * @param locationList 路程中坐标点的集合
     */
    private void searchInMap(final List<Location> locationList) {
        try {
            if (mBaiduMap == null) return;
            LatLng stLatLng = new LatLng(locationList.get(0).getCORDINATEY(), locationList.get(0).getCORDINATEX());
            LatLng enLatLng = new LatLng(locationList.get(locationList.size() - 1).getCORDINATEY(), locationList.get(locationList.size() - 1).getCORDINATEX());
            final RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
            PlanNode stNode = PlanNode.withLocation(stLatLng);
            PlanNode enNode = PlanNode.withLocation(enLatLng);
            List<PlanNode> passBy = new ArrayList<>();
            for (int i = 1; i < (locationList.size() - 2); i++) {
                passBy.add(PlanNode.withLocation(new LatLng(locationList.get(i).getCORDINATEY(), locationList.get(i).getCORDINATEX())));
            }
            DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption().from(stNode).passBy(passBy).policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST).to(enNode);
            OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
                public void onGetWalkingRouteResult(WalkingRouteResult result) {
                    //获取步行线路规划结果
                }
                public void onGetTransitRouteResult(TransitRouteResult result) {
                    //获取公交换乘路径规划结果
                }
                public void onGetDrivingRouteResult(DrivingRouteResult result) {
                    //获取驾车线路规划结果
                    getDrivingRouteResultSuccess(result, locationList, mSearch);
                }
                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                }
            };
            mSearch.setOnGetRoutePlanResultListener(listener);
            mSearch.drivingSearch(drivingRoutePlanOption);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 百度地图成功返回驾车路线结果
     *
     * @param result       结果
     * @param locationList 订单线路点位集合
     * @param mSearch      RoutePlanSearch
     */
    private void getDrivingRouteResultSuccess(DrivingRouteResult result, List<Location> locationList, RoutePlanSearch mSearch) {
        try {
            if (result == null || result.error!=SearchResult.ERRORNO.NO_ERROR || result.error==SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                Logger.w(this.getClass() + ":getDrivingRouteResultSuccess.error:" + "抱歉，未找到结果");
                if (!OrderPathActivity.this.isFinishing()) {
                    if (mGetDrivingRouteResultTime < mGetDrivingRouteResultMamTime) {
                        mGetDrivingRouteResultTime++;
                        searchInMap(locationList);
                    } else {
                        mProgressDialog.dismiss();
                    }
                }
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                try {
                    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    if (result.getRouteLines().get(0).getDistance() > mBiz.getDistance(locationList)) {
                        distance += result.getRouteLines().get(0).getDistance();
                    } else {
                        distance += mBiz.getDistance(locationList);
                    }
                    if (distance0 < distance) {
                        String dis = String.valueOf(distance / 1000);
                        int index = dis.indexOf(".");
                        if (index != -1 && (dis.length() - index) > 6) {
                            dis = dis.substring(0, index + 6);
                        }
                        mTextViewOrderDistance.setText("公里数：" + dis + "公里");
                    }
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    mSearch.destroy();
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mProgressDialog.dismiss();
        }
    }

    /**
     * 绘制起点和重点图标
     *
     * @param locations 订单线路点位集合
     */
    private void drawStartAndEndPicture(List<Location> locations) {
        try {
            Location startLocation = locations.get(0);
            Location endLocation = locations.get(locations.size() - 1);
            LatLng stLatLng = new LatLng(startLocation.getCORDINATEY(), startLocation.getCORDINATEX());
            LatLng enLatLng = new LatLng(endLocation.getCORDINATEY(), endLocation.getCORDINATEX());
            BitmapDescriptor stbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            BitmapDescriptor enbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            MarkerOptions stOption = new MarkerOptions().position(stLatLng).icon(stbitmap).zIndex(5);
            MarkerOptions enOption = new MarkerOptions().position(enLatLng).icon(enbitmap).zIndex(5);
            mBaiduMap.addOverlay(stOption);
            mBaiduMap.addOverlay(enOption);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(stLatLng));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 定制RouteOverly
     */
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        private boolean useDefaultST = false;
        private boolean useDefaultEN = false;

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        public MyDrivingRouteOverlay(BaiduMap baiduMap, boolean stIcon, boolean enIcon) {
            super(baiduMap);
            useDefaultST = stIcon;
            useDefaultEN = enIcon;
        }
        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.chose_cardwhite);
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.chose_cardwhite);
        }
        @Override
        public List<BitmapDescriptor> getCustomTextureList() {
            return super.getCustomTextureList();
        }
    }

    /**
     * 查询路线时显示的 Dialog
     */
    private void showSearchPathDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("查询路线中");
                mProgressDialog.setCanceledOnTouchOutside(false);
            }
            mProgressDialog.show();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

}















