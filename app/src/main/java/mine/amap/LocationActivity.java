package mine.amap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.sql.SQLOutput;

import static com.amap.api.maps2d.AMapOptions.LOGO_POSITION_BOTTOM_LEFT;
import static com.amap.api.maps2d.AMapOptions.ZOOM_POSITION_RIGHT_CENTER;

/**
 * Created by Administrator on 2020/9/25.
 */
public class LocationActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap = null;
    int n = 0;
    double longitude = 114.420535d;
    double latitude = 30.592849d;
    LocationSource.OnLocationChangedListener onLocationChangedListener;
    AMapLocationClient aMapLocationClient;
    TextView textView;
    SensorEventHelper sensorEventHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("*********  " + getClass().getSimpleName() + ".onCreate  *********");
        setContentView(R.layout.activity_main);

        System.out.println("MapsInitializer.getVersion is " + MapsInitializer.getVersion());
        System.out.println("MapsInitializer.getProtocol is " + MapsInitializer.getProtocol());

        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            for (String key : bundle.keySet()) {
                System.out.println("key is " + key);
                System.out.println(bundle.getString(key));
            }

            MapsInitializer.setApiKey(bundle.getString("com.amap.api.v2.apikey"));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        LatLng centerBJPoint = new LatLng(latitude, longitude);
        AMapOptions mapOptions = new AMapOptions();
        mapOptions.camera(new CameraPosition(centerBJPoint, 10f, 0, 0));
        mMapView = new MapView(this, mapOptions);

        ViewGroup viewGroup = findViewById(R.id.fl);
        viewGroup.addView(mMapView);
        textView = new TextView(this);
        viewGroup.addView(textView);

        mMapView.onCreate(savedInstanceState);


        if (aMap == null) {
            aMap = mMapView.getMap();
            System.out.println("getMaxZoomLevel is " + aMap.getMaxZoomLevel());
            System.out.println("getMinZoomLevel() is " + aMap.getMinZoomLevel());
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("*********  " + getClass().getSimpleName() + ".onStart  *********");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("*********  " + getClass().getSimpleName() + ".onRestoreInstanceState  *********");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("*********  " + getClass().getSimpleName() + ".onRestart  *********");

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("*********  " + getClass().getSimpleName() + ".onResume  *********");

//        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                System.out.println("~~onMyLocationChange~~");
//                System.out.println(location.getLatitude() + ", " + location.getLongitude());
////                camera();
//
//            }
//        });



        mMapView.onResume();
        aMap.setMyLocationEnabled(true);
        sensorEventHelper = new SensorEventHelper(this);
        sensorEventHelper.registerSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("*********  " + getClass().getSimpleName() + ".onPause  *********");

        if (sensorEventHelper != null) {
            sensorEventHelper.unRegisterSensorListener();
            sensorEventHelper.setCurrentMarker(null);
            sensorEventHelper = null;
        }

        aMap.setOnMyLocationChangeListener(null);
        mMapView.onPause();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("*********  " + getClass().getSimpleName() + ".onBackPressed  *********");
    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("*********  " + getClass().getSimpleName() + ".onStop  *********");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("*********  " + getClass().getSimpleName() + ".onSaveInstanceState  *********");

        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("*********  " + getClass().getSimpleName() + ".onDestroy  *********");
        mMapView.onDestroy();
    }


    public void start(View view) {
        System.out.println("~~button.start~~");

        MyLocationStyle myLocationStyle = new MyLocationStyle();

        //单次定位
//        myLocationStyle.myLocationType(LOCATION_TYPE_SHOW);
//        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATE);

        myLocationStyle.interval(2000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);


        //隐藏导航图标
//        myLocationStyle.showMyLocation(false);


        //设置图标
        BitmapDescriptor myLocationIcon = BitmapDescriptorFactory.fromResource(R.drawable.ox);
        myLocationStyle.myLocationIcon(myLocationIcon);
        //精度圈设置
        myLocationStyle.strokeColor(Color.argb(255, 255, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 255));
        myLocationStyle.anchor(0f, 0f);


//        aMap.setMyLocationStyle(myLocationStyle);


        //控制定位源
        aMap.setLocationSource(new LocationSource() {
            boolean enable = true;

            @Override
            public void activate(final OnLocationChangedListener onLocationChangedListener) {
                System.out.println("~~LocationSource.activate~~");
                System.out.println("onLocationChangedListener is " + onLocationChangedListener);
                LocationActivity.this.onLocationChangedListener = onLocationChangedListener;


                if (aMapLocationClient != null) return;

                AMapLocationClientOption option = new AMapLocationClientOption();
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

                aMapLocationClient = new AMapLocationClient(LocationActivity.this);
                aMapLocationClient.setLocationOption(option);
                aMapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        System.out.println("~~aMapLocationClient.AMapLocationListener.onLocationChanged~~");
                        System.out.println("aMapLocation is " + aMapLocation);
                        System.out.println("getAccuracy is " + aMapLocation.getAccuracy());
                        System.out.println("getAdCode is " + aMapLocation.getAdCode());
                        System.out.println("getAddress is " + aMapLocation.getAddress());
                        System.out.println("getAltitude is " + aMapLocation.getAltitude());
                        System.out.println("getAoiName is " + aMapLocation.getAoiName());
                        System.out.println("getBearing is " + aMapLocation.getBearing());
                        System.out.println("getBuildingId is " + aMapLocation.getBuildingId());
                        System.out.println("getCity is " + aMapLocation.getCity());
                        System.out.println("getCityCode is " + aMapLocation.getCityCode());
                        System.out.println("getCountry is " + aMapLocation.getCountry());
                        System.out.println("getDescription is " + aMapLocation.getDescription());
                        System.out.println("getDistrict is " + aMapLocation.getDistrict());
                        System.out.println("getErrorCode is " + aMapLocation.getErrorCode());
                        System.out.println("getErrorInfo is " + aMapLocation.getErrorInfo());
                        System.out.println("getFloor is " + aMapLocation.getFloor());
                        System.out.println("getGpsAccuracyStatus is " + aMapLocation.getGpsAccuracyStatus());
                        System.out.println("getLatitude is " + aMapLocation.getLatitude());
                        System.out.println("getLocationDetail is " + aMapLocation.getLocationDetail());
                        System.out.println("getLocationQualityReport is " + aMapLocation.getLocationQualityReport());
                        System.out.println("getLocationType is " + aMapLocation.getLocationType());
                        System.out.println("getLongitude is " + aMapLocation.getLongitude());
                        System.out.println("getPoiName is " + aMapLocation.getPoiName());
                        System.out.println("getProvider is " + aMapLocation.getProvider());
                        System.out.println("getProvince is " + aMapLocation.getProvince());
                        System.out.println("getSatellites is " + aMapLocation.getSatellites());
                        System.out.println("getSpeed is " + aMapLocation.getSpeed());
                        System.out.println("getStreet is " + aMapLocation.getStreet());
                        System.out.println("getStreetNum is " + aMapLocation.getStreetNum());


                        textView.setText(aMapLocation.toString());
                    }
                });

                aMapLocationClient.startLocation();


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (enable) {
//
//
//                            mMapView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Location location = new Location("");
//                                    location.setLongitude(longitude += 0.01);
//                                    location.setLatitude(latitude += 0.01);
//                                    onLocationChangedListener.onLocationChanged(location);
//                                }
//                            });
//                            System.out.println("go!");
//                            try {
//                                Thread.sleep(5000L);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();

            }

            @Override
            public void deactivate() {
                System.out.println("~~LocationSource.deactivate~~");
                enable = false;
                aMap.setLocationSource(null);
                onLocationChangedListener = null;

                if (aMapLocationClient != null) {
                    aMapLocationClient.stopLocation();
                    aMapLocationClient.onDestroy();
                }
                aMapLocationClient = null;


            }
        });
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);


//        widget();//启用控件
//        gesture();//启用手势
//        camera();

    }

    private void camera() {

        CameraUpdate mCameraUpdate = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 16, 0, 90));


//        aMap.animateCamera(mCameraUpdate);

        aMap.animateCamera(mCameraUpdate, 500L, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
                System.out.println("~~CancelableCallback.onFinish~~");
            }

            @Override
            public void onCancel() {
                System.out.println("~~CancelableCallback.onFinish~~");

            }
        });


    }

    private void gesture() {
        System.out.println("isZoomGesturesEnabled is " + aMap.getUiSettings().isZoomGesturesEnabled());//缩放手势
        System.out.println("isScrollGesturesEnabled is " + aMap.getUiSettings().isScrollGesturesEnabled());//滑动手势


        aMap.getUiSettings().setAllGesturesEnabled(false);//禁用所有手势，然后按需打开

        aMap.getUiSettings().setZoomGesturesEnabled(true);//缩放手势

        aMap.getUiSettings().setScrollGesturesEnabled(true);//滑动手势


    }

    private void widget() {
        aMap.getUiSettings().setZoomPosition(ZOOM_POSITION_RIGHT_CENTER);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);//控制比例尺控件是否显示
        aMap.getUiSettings().setLogoPosition(LOGO_POSITION_BOTTOM_LEFT);//设置logo位置;//控制比例尺控件是否显示
    }

    public void stop(View view) {
        System.out.println("~~button.stop~~");

        aMap.setTrafficEnabled(true);

        if (++n % 2 == 0) {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        } else {
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        }


    }

    public void pause(View view) {
        System.out.println("~~button.pause~~");

    }

    public void resume(View view) {
        System.out.println("~~button.resume~~");
    }

    public void reloading(View view) {
        System.out.println("~~button.reloading~~");

    }


    public void del(View view) {
        System.out.println("~~button.del~~");

    }


    public void query(View view) {
        System.out.println("~~button.query~~");

        camera();

    }
}