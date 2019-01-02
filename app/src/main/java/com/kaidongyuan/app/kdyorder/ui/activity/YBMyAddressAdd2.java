package com.kaidongyuan.app.kdyorder.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
//import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.kaidongyuan.app.kdyorder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */

public class YBMyAddressAdd2 extends Activity implements View.OnClickListener{
    private ImageView titleLeftImage;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    //    private BitmapDescriptor bitmap;
//    private String address = "";
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationClient mLocClient;
    //    private Button dw_bt;
    private boolean touch =false;

    private ListView blsLv;
    private ListView searchLv;
    private ImageView edtDel;
    private EditText edt;
    private YBMyAddrBlsItemAdapter blsAdapter;
    private YBMyAddrBlsItemAdapter searchAdapter;
    private ArrayList<YBMyAddrBlsItemBean> blsList=new ArrayList<>();
    private ArrayList<YBMyAddrBlsItemBean> searchList=new ArrayList<>();
    PoiSearch mPoiSearch;
    private ImageView blsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.yb_my_address_two_activity);

        initView();

    }
    private void initView(){
        ((TextView) findViewById(R.id.title_text)).setText("地址详细信息");
        titleLeftImage = (ImageView) findViewById(R.id.title_left_image);
        titleLeftImage.setOnClickListener(this);
        blsImg=(ImageView)findViewById(R.id.yb_my_addr_bls_img);
        blsAdapter=new YBMyAddrBlsItemAdapter(this);
        searchAdapter=new YBMyAddrBlsItemAdapter(this);


        blsLv=(ListView)findViewById(R.id.yb_my_addr_bls_lv);
        blsLv.setAdapter(blsAdapter);
        blsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                if(!blsList.get(i).getCity().equals("深圳市")){
////                    YBUIUtils.showToast(YBMyAddressAdd2.this,"不在服务范围内，请重新选择");
//                    return;
//                }
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
//                intent.putExtra("address", blsList.get(i).getAddrName());
//                intent.putExtra("address",blsList.get(i).getCity()+blsList.get(i).getArea()+blsList.get(i).getAddrName());
                intent.putExtra("addressDetail", blsList.get(i).getAddrDetail());
                intent.putExtra("latitude", blsList.get(i).getLatitude());
                intent.putExtra("longitude", blsList.get(i).getLongitude());
                //设置返回数据
                YBMyAddressAdd2.this.setResult(120, intent);
                YBMyAddressAdd2.this.finish();

            }
        });
        searchLv=(ListView)findViewById(R.id.yb_my_addr_search_lv);
        searchLv.setAdapter(searchAdapter);
        searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(!blsList.get(i).getCity().equals("深圳市")){
////                    YBUIUtils.showToast(YBMyAddressAdd2.this,"不在服务范围内，请重新选择");
//                    return;
//                }
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
//                intent.putExtra("address", searchList.get(i).getCity()+searchList.get(i).getArea()+searchList.get(i).getAddrName());
                intent.putExtra("addressDetail", searchList.get(i).getAddrDetail());
                intent.putExtra("latitude", blsList.get(i).getLatitude());
                intent.putExtra("longitude", blsList.get(i).getLongitude());
                //设置返回数据
                YBMyAddressAdd2.this.setResult(120, intent);
                YBMyAddressAdd2.this.finish();
            }
        });


        edtDel=(ImageView)findViewById(R.id.yb_my_addr_search_edt_del);
        edtDel.setOnClickListener(this);
        edtDel.setVisibility(View.GONE);

        edt=(EditText)findViewById(R.id.yb_my_addr_search_edt);

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                YBLogUtils.e("sssssssddddd="+s);

                if(s.length()==0){
                    edtDel.setVisibility(View.GONE);
                    searchLv.setVisibility(View.GONE);
                }else {
                    searchLv.setVisibility(View.VISIBLE);
                    edtDel.setVisibility(View.VISIBLE);
                    searchNeayBy(s.toString());
                }
            }
        });
        blsImg=(ImageView)findViewById(R.id.yb_my_addr_bls_img);
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //todo 这里是保存字体
                if ((actionId == 0 || actionId == 3) && event != null) {
//                    YBUIUtils.closeSoftKeyboard(YBMyAddressAdd2.this,edt);

                    return true;
                }
                return false;
            }
        });
        edt.setFocusable(false);
        edt.setFocusableInTouchMode(false);
        edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt.setFocusable(true);
                edt.setFocusableInTouchMode(true);
                return false;
            }

        });

//        closeInputMethod(edt);





        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        // 设置是否显示缩放控件
        mMapView.showZoomControls(true);
        // 设置是否显示定位按钮
        // 删除百度地图LoGo
        mMapView.removeViewAt(1);

//        // 设置marker图标
//        bitmap = BitmapDescriptorFactory
//                .fromResource(R.mipmap.ssdk_oks_ptr_ptr);

        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                touch=true;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener(){

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

//            @Override
//            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
//                mBaiduMap.clear();
//            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

                if(!touch){
                    return;
                }
                int[] location = new int[2];
                mMapView.getLocationOnScreen(location);
                Point p=new Point(mMapView.getWidth()/2, mMapView.getHeight()/2);
                //TODO 已经获取到屏幕中心经纬度，可上传或者地理转码
                LatLng latLng=mBaiduMap.getProjection().fromScreenLocation(p);
                Log.i("location",latLng.toString());

                mCurrentLat = latLng.latitude;
                mCurrentLon = latLng.longitude;

                LatLng ll = new LatLng(latLng.latitude,
                        latLng.longitude);
                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(18.0f);
                builder.target(ll);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));

                mBaiduMap.clear();
//                // 构建MarkerOption，用于在地图上添加Marker
//                MarkerOptions options = new MarkerOptions().position(ll)
//                        .icon(bitmap);
//                // 在地图上添加Marker，并显示
//                mBaiduMap.addOverlay(options);






                // 实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                // 设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(latLng);
                // 发起反地理编码请求(经纬度->地址信息)
                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(
                            ReverseGeoCodeResult arg0) {

                        pingYiAmin(blsImg);
                        String area=arg0.getAddressDetail().district;
                        List<PoiInfo> aa= arg0.getPoiList();
                        if(aa!=null&&aa.size()>0){
                            blsList.clear();
                            for(int i=0;i<aa.size();i++){
                                YBMyAddrBlsItemBean bb=new YBMyAddrBlsItemBean();
                                bb.setAddrName(aa.get(i).name);
                                bb.setAddrDetail(aa.get(i).address);
                                bb.setLatitude(aa.get(i).location.latitude+"");
                                bb.setLongitude(aa.get(i).location.longitude+"");
                                bb.setUid(aa.get(i).uid);
                                bb.setCity(aa.get(i).city);
//                                bb.setArea(area);
                                blsList.add(bb);
                            }
                            blsAdapter.setDatas(blsList);
                        }

                        // 获取点击的坐标地址
//                        address = arg0.getAddress();
//                        city_tv.setText(arg0.getAddressDetail().city);
//                        address_tv.setText(address);

                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
//                searchNeayBy();
                touch=false;
            }
        });


        mPoiSearch=PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {

                //todo 获取POI的检索结果
//                YBLogUtils.e("poiResult="+poiResult.toString());
                List<PoiInfo> aa= poiResult.getAllPoi();

                if(aa!=null&&aa.size()>0){
                    searchList.clear();
                    for(int i=0;i<aa.size();i++){
                        YBMyAddrBlsItemBean bb=new YBMyAddrBlsItemBean();
                        bb.setAddrName(aa.get(i).name);
                        bb.setAddrDetail(aa.get(i).address);
                        bb.setLatitude(aa.get(i).location.latitude+"");
                        bb.setLongitude(aa.get(i).location.longitude+"");
                        bb.setUid(aa.get(i).uid);
                        bb.setCity(aa.get(i).city);
//                        bb.setArea(aa.get(i).area);
                        searchList.add(bb);
                    }
                    searchAdapter.setDatas(searchList);
                }


            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                //todo 获取place 详情页检索结果
//                YBLogUtils.e("poiDetailResult="+poiDetailResult.toString());

            }

//            @Override
//            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//
//            }
        });

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void pingYiAmin(ImageView iv) {
        TranslateAnimation animation;
//        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, 0,
//                -90);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        iv.startAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, -90,
                0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                this.finish();
                break;



            default:
                break;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            // 获取经纬度
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();

            // 先清除图层
            mBaiduMap.clear();
            // 定义Maker坐标点
            LatLng point = new LatLng(mCurrentLat, mCurrentLon);
//            // 构建MarkerOption，用于在地图上添加Marker
//            MarkerOptions options = new MarkerOptions().position(point).icon(
//                    bitmap);
//            // 在地图上添加Marker，并显示
//            mBaiduMap.addOverlay(options);

            final LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
//            LatLng center = new LatLng(39.92235, 116.380338);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(builder.build()));
//            // 定位成功后就停止
//            mBaiduMap.setMyLocationEnabled(false);
//            if (mLocClient.isStarted()) {
//                mLocClient.stop();
//            };

//            String bb=location.getAddrStr();
//            String bb=location.getAddrStr();
//            searchNeayBy("楼");
//            boundSearch(10);
//            boundSearch(0);



//                    address_tv.setText(address);
            // 定位成功后就停止
//                    searchNeayBy();
            mBaiduMap.setMyLocationEnabled(false);
            if (mLocClient.isStarted()) {
                mLocClient.stop();
            }

//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword("楼")
//                            .sortType(PoiSortType.distance_from_near_to_far).location(ll)
//                            .radius(100).pageNum(0);
//                    mPoiSearch.searchNearby(nearbySearchOption);
//                }
//            },2000);

//            boundSearch(0);



            // 实例化一个地理编码查询对象
            GeoCoder geoCoder = GeoCoder.newInstance();
            // 设置反地理编码位置坐标
            ReverseGeoCodeOption op = new ReverseGeoCodeOption();
            op.location(point);
            // 发起反地理编码请求(经纬度->地址信息)
            geoCoder.reverseGeoCode(op);
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                }
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                    // 获取点击的坐标地址
                    String cc = arg0.getAddress();
                    String vv=arg0.getAddressDetail().city;
                    String vv2=arg0.getAddressDetail().street;
                    String area=arg0.getAddressDetail().district;
                    List<PoiInfo> aa= arg0.getPoiList();
                    if(aa!=null&&aa.size()>0){
                        blsList.clear();
                        for(int i=0;i<aa.size();i++){
                            YBMyAddrBlsItemBean bb=new YBMyAddrBlsItemBean();
                            bb.setAddrName(aa.get(i).name);
                            bb.setAddrDetail(aa.get(i).address);
                            bb.setLatitude(aa.get(i).location.latitude+"");
                            bb.setLongitude(aa.get(i).location.longitude+"");
                            bb.setUid(aa.get(i).uid);
                            bb.setCity(aa.get(i).city);
//                            bb.setArea(area);
                            blsList.add(bb);
                        }
                        blsAdapter.setDatas(blsList);
                    }
//                    address_tv.setText(address);
                    // 定位成功后就停止
//                    searchNeayBy();
                    mBaiduMap.setMyLocationEnabled(false);
                    if (mLocClient.isStarted()) {
                        mLocClient.stop();
                    }
                }
            });
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));

        }

    }

    /**
     * 范围检索
     */
    private void boundSearch(int page) {
        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();
//        mCurrentLat = location.getLatitude();
//        mCurrentLon = location.getLongitude();
        LatLng southwest = new LatLng(mCurrentLat - 0.01, mCurrentLon - 0.012);// 西南
        LatLng northeast = new LatLng(mCurrentLat + 0.01, mCurrentLon + 0.012);// 东北
        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest)
                .include(northeast).build();// 得到一个地理范围对象
        boundSearchOption.bound(bounds);// 设置poi检索范围
//        boundSearchOption.mPageCapacity=20;
//        boundSearchOption.keyword("小区,公寓,楼,写字楼,工业区,酒店,地铁");// 检索关键字  //@[@"小区",@"公寓",@"写字楼",@"工业区",@"酒店",@"地铁"];
        boundSearchOption.keyword("all");// 检索关键字  //@[@"小区",@"公寓",@"写字楼",@"工业区",@"酒店",@"地铁"];
        boundSearchOption.pageNum(page);
        mPoiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求
    }



    /**
     * 搜索周边地理位置
     *
     */
    private void searchNeayBy(String key) {
        PoiNearbySearchOption option = new PoiNearbySearchOption();
//        option.keyword(address);
        option.keyword(key);
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(new LatLng(mCurrentLat, mCurrentLon));
//        if (radius != 0) {
//            option.radius(radius);
//        } else {
//            option.radius(1000);
//        }
        option.radius(200000);

        option.pageCapacity(20);
        option.pageNum(0);
        mPoiSearch.searchNearby(option);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    }
