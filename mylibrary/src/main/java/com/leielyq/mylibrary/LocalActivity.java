package com.leielyq.mylibrary;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.rong.message.LocationMessage;

public class LocalActivity extends BaseActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap mMapboxMap;
    private LocationMessage mLocation;

    public void onBack(View view){
        finish();
    }

    public void go(View view) {
        go(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, mAccessToken);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        mLocation = getIntent().getParcelableExtra("location");
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mMapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.OUTDOORS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mMapboxMap);

                        ImageView imageView = new ImageView(LocalActivity.this);
                        imageView.setImageResource(R.drawable.ic_loacl_map);
                        imageView.setLayoutParams(new FrameLayout.LayoutParams(DensityUtil.dip2px(LocalActivity.this,50),DensityUtil.dip2px(LocalActivity.this,50)));
                        MarkerView markerView = new MarkerView(new LatLng(mLocation.getLat(),
                                mLocation.getLng()), imageView);
                        markerViewManager.addMarker(markerView);

                        mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(mLocation.getLat(),
                                                mLocation.getLng()))
                                        .zoom(15)
                                        .build()), 1000);


                    }
                });
    }



    private void go(Context context) {
        Intent intent;
        LocationMessage locationMessage = (LocationMessage) ((LocalActivity) context).getIntent().getParcelableExtra("location");
        double lat = locationMessage.getLat();
        double lng = locationMessage.getLng();
        if (isAvilible(context, "com.autonavi.minimap")) {
            try {
                intent = Intent.getIntent("androidamap://navi?sourceApplication=TCHAT&poiname=我的目的地&lat=" + lat + "&lon=" + lng + "&dev=0");
                context.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (isAvilible(context, "com.baidu.BaiduMap")) {//传入指定应用包名
            try {
//                          intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                intent = Intent.getIntent("intent://map/direction?" +
                        //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                        "destination=latlng:" + lat + "," + lng + "|name:我的目的地" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "region=北京" +           //
                        "&src=TCHAT#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                context.startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else if (isAvilible(context, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng + ", + Sydney +Australia");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            Toast.makeText(context, "请安装MAP App", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }


    //    * 检查手机上是否安装了指定的软件
//     * @param context
//     * @param packageName：应用包名
//     * @return
//             */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

}
