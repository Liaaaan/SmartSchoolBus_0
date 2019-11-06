package com.demo.smartschoolbus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

public class MainActivity extends Activity {
    private MapView mMapView = null;//地图控件
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;//用户定位监听
    private double latitude;
    private double longtitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();//获得地图控件中的地图
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
//        MapView mapView = new MapView(this);
//        setContentView(mapView);


//        //手机用户定位初始化
//        mLocationClient = new LocationClient(this);
//
//        //通过LocationClientOption设置LocationClient相关参数
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//
//        //设置locationClientOption
//        mLocationClient.setLocOption(option);
//
//        //注册LocationListener监听器
//        MyLocationListener myLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(myLocationListener);
//        //开启地图定位图层
//        mLocationClient.start();



        String data = "$GNRMC,032051.00,A,2809.69820,N,11255.78593,E,000.0,163.5,021119,OK*0F嶬";
        latitude = Double.parseDouble(data.substring(19, 21)) + Double.parseDouble(data.substring(21, 29)) / 60.0D;
        longtitude = Double.parseDouble(data.substring(32, 35)) + Double.parseDouble(data.substring(35, 43)) / 60.0D;
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longtitude);
        //LatLng point = new LatLng(28.164351910542, 112.94162260049);//这个是对的
        CoordinateConverter converter  = new CoordinateConverter()
                .from(CoordinateConverter.CoordType.GPS)
                .coord(point);

        //desLatLng GPS转换成百度地图后的坐标
        LatLng desLatLng = converter.convert();

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(desLatLng);
        OverlayOptions option = markerOptions.icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        //用户定位监听关闭
//        mLocationClient.stop();
//        mBaiduMap.setMyLocationEnabled(false);
//        mMapView = null;
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }



    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
        }
    }
}