package com.kaidongyuan.app.kdyorder.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.kaidongyuan.app.kdyorder.bean.Order;
import com.kaidongyuan.app.kdyorder.model.SearchOrderTrajectoryActivityBiz;
import com.kaidongyuan.app.kdyorder.util.DensityUtil;
import com.kaidongyuan.app.kdyorder.util.ExceptionUtil;
import com.kaidongyuan.app.kdyorder.util.StringUtils;
import com.kaidongyuan.app.kdyorder.util.ToastUtil;
import com.kaidongyuan.app.kdyorder.util.baidumap.DrivingRouteOverlay;
import com.kaidongyuan.app.kdyorder.util.logger.Logger;
import com.kaidongyuan.app.kdyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.List;

/**
 * Created by Administrator on 2016/5/20.
 * 订单查询界面
 */
public class SearchOrderTrajectoryActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 所搜订单轨迹业务类
     */
    private SearchOrderTrajectoryActivityBiz mBiz;
    /**
     * 百度地图控件
     */
    private MapView mMapView;
    /**
     * 百度地图
     */
    private BaiduMap mBaiduMap;
    /**
     * 用户输入订单号编辑框
     */
    private EditText mEditTextSearchOrder;
    /**
     * 搜索订单轨迹按钮
     */
    private Button mButtonSearch;
    /**
     * 返回上一个 Activity
     */
    private ImageView mImageViewGoBack;
    /**
     * 网络请求是的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order_trajectory_search);

            initData();
            setTop();
            initView();
            setListener();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new SearchOrderTrajectoryActivityBiz(this);
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
            mBiz.cancelAllRequest();
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mLoadingDialog = null;
            mMapView = null;
            mBaiduMap = null;
            mEditTextSearchOrder = null;
            mButtonSearch = null;
            mImageViewGoBack = null;
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
            this.mMapView = (MapView) this.findViewById(R.id.mapview_search_order);
            this.mBaiduMap = mMapView.getMap();
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            this.mEditTextSearchOrder = (EditText) this.findViewById(R.id.edittext_headview_content);
            this.mButtonSearch = (Button) this.findViewById(R.id.button_search_ordertrajectory);
            this.mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mButtonSearch.setOnClickListener(this);
            mImageViewGoBack.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_search_ordertrajectory://搜索订单轨迹
                    sendGetTrajectoryRequest();
                    break;
                case R.id.button_goback://返回到上一个 Activity
                    this.finish();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 发送获取订单轨迹网络请求
     */
    private void sendGetTrajectoryRequest() {
        try {
            String text = mEditTextSearchOrder.getText().toString();
            if (TextUtils.isEmpty(text)) {
                ToastUtil.showToastBottom("请输入订单编号!", Toast.LENGTH_SHORT);
                return;
            }
            if (mBiz.getOrderDetailTrajectory(text.trim())) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(MyApplication.getmRes().getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 显示网络请求的 Dialog
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

    /**
     * 获取订单轨迹失败
     *
     * @param message 显示的信息
     */
    public void getOrderTrajectory(String message) {
        try {
            if (message != null) {
                ToastUtil.showToastBottom(message, Toast.LENGTH_SHORT);
            }
            mLoadingDialog.dismiss();
            search();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取订单轨迹成功
     */
    public void getOrderTrajectorySuccess() {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("显示订单轨迹", Toast.LENGTH_SHORT);
            search();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 根据订单轨迹搜索路线
     */
    private void search() {
        try {
            Order order = mBiz.getOrder();
            if (order == null) {
                ToastUtil.showToastBottom("订单轨迹缺失，订单无轨迹！", Toast.LENGTH_SHORT);
                return;
            }
            LatLng startLatLng = StringUtils.getLatLng(order.getFROM_COORDINATE());
            LatLng endLatLng = StringUtils.getLatLng(order.getTO_COORDINATE());
            if (startLatLng == null || endLatLng == null) {
                ToastUtil.showToastBottom("订单轨迹缺失，订单无轨迹！", Toast.LENGTH_SHORT);
                return;
            }
            drawStartAndEndPicture(startLatLng, endLatLng);
            searchInMap(startLatLng, endLatLng);


        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 绘制路线
     *
     */
    private void searchInMap(LatLng stLatLng, LatLng enLatLng) {
        try {
            if (mBaiduMap == null) return;
            final RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
            PlanNode stNode = PlanNode.withLocation(stLatLng);
            PlanNode enNode = PlanNode.withLocation(enLatLng);
            DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption().from(stNode).policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST).to(enNode);
            OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
                public void onGetWalkingRouteResult(WalkingRouteResult result) {
                    //获取步行线路规划结果
                }
                public void onGetTransitRouteResult(TransitRouteResult result) {
                    //获取公交换乘路径规划结果
                }
                public void onGetDrivingRouteResult(DrivingRouteResult result) {
                    //获取驾车线路规划结果
                    getDrivingRouteResultSuccess(result, mSearch);
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
     * @param mSearch      RoutePlanSearch
     */
    private void getDrivingRouteResultSuccess(DrivingRouteResult result, RoutePlanSearch mSearch) {
        try {
            if (result == null || result.error!= SearchResult.ERRORNO.NO_ERROR || result.error==SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                Logger.w(this.getClass() + ":getDrivingRouteResultSuccess.error:" + "抱歉，未找到结果");
                if (!SearchOrderTrajectoryActivity.this.isFinishing()) {
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    try {
                        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(result.getRouteLines().get(0));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                        mSearch.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 绘制起点和终点图标
     * @param stLatLng 起点坐标
     * @param enLatLng 终点坐标
     */
    private void drawStartAndEndPicture(LatLng stLatLng, LatLng enLatLng) {
        try {
            BitmapDescriptor stbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            BitmapDescriptor enbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            MarkerOptions stOption = new MarkerOptions().position(stLatLng).icon(stbitmap);
            MarkerOptions enOption = new MarkerOptions().position(enLatLng).icon(enbitmap);
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

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
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


}


























