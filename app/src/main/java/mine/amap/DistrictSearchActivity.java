package mine.amap;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2020/9/27.
 */
public class DistrictSearchActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap = null;
    double longitude = 114.420535d;
    double latitude = 30.592849d;
    private int pageNum = 1;


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

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("*********  " + getClass().getSimpleName() + ".onPause  *********");


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

        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("青山区");//传入关键字
        query.setShowBoundary(true);//是否返回边界值

        DistrictSearch search = new DistrictSearch(this);
        search.setQuery(query);
        search.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult districtResult) {
                System.out.println("~~onDistrictSearched~~");
                Random random = new Random();
                System.out.println(districtResult.getDistrict().size());
                for (DistrictItem item : districtResult.getDistrict()) {


                    List<LatLng> latLngs = new ArrayList<>();
                    for (int i = 0; i < item.districtBoundary().length; i++) {

                        latLngs.clear();
                        String[] s  = item.districtBoundary()[i].split(";");
                        for (int j = 0; j < s.length; j++) {
                            longitude = Double.valueOf(s[j].substring(0, s[j].indexOf(",")));
                            latitude = Double.valueOf(s[j].substring(s[j].indexOf(",")+1));
                            System.out.println(latitude + "," + longitude);
                            LatLng latLng = new LatLng(latitude, longitude);
                            latLngs.add(latLng);
                        }
                        PolylineOptions options = new PolylineOptions();
                        options.addAll(latLngs)
                                .width(10)
                                .color(Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256)));
                        aMap.addPolyline(options);

                    }
//                    System.out.println("getAdcode is " + item.getAdcode());
//                    System.out.println("getCenter is " + item.getCenter());
//                    System.out.println("getCitycode is " + item.getCitycode());
//                    System.out.println("getLevel is " + item.getLevel());
//                    System.out.println("getName is " + item.getName());
//                    System.out.println("getSubDistrict is " + item.getSubDistrict());
                    System.out.println("~~~~");
                }

//                System.out.println("getKeywords is " + districtResult.getQuery().getKeywords());
//                System.out.println("getPageNum is " + districtResult.getQuery().getPageNum());
//                System.out.println("getPageSize is " + districtResult.getQuery().getPageSize());
//                System.out.println("getSubDistrict is " + districtResult.getQuery().getSubDistrict());


            }
        });
        search.searchDistrictAsyn();
    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");

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
    }
}
