package mine.amap;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import com.amap.api.services.traffic.RoadTrafficQuery;
import com.amap.api.services.traffic.TrafficSearch;
import com.amap.api.services.traffic.TrafficStatusEvaluation;
import com.amap.api.services.traffic.TrafficStatusInfo;
import com.amap.api.services.traffic.TrafficStatusResult;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

/**
 * Created by Administrator on 2020/9/28.
 */
public class TrafficSearchActivity extends AppCompatActivity {
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

        TrafficSearch trafficSearch = new TrafficSearch(this);
        trafficSearch.setTrafficSearchListener(new TrafficSearch.OnTrafficSearchListener() {
            @Override
            public void onRoadTrafficSearched(TrafficStatusResult trafficStatusResult, int i) {
                System.out.println("~~onRoadTrafficSearched~~");
                System.out.println("trafficStatusResult is " + trafficStatusResult);
                System.out.println("i is " + i);


                System.out.println("getDescription is " + trafficStatusResult.getDescription());

                System.out.println("getEvaluation is " + trafficStatusResult.getEvaluation());
                TrafficStatusEvaluation trafficStatusEvaluation = trafficStatusResult.getEvaluation();
                System.out.println("getBlocked is " + trafficStatusEvaluation.getBlocked());
                System.out.println("getCongested is " + trafficStatusEvaluation.getCongested());
                System.out.println("getDescription is " + trafficStatusEvaluation.getDescription());
                System.out.println("getExpedite is " + trafficStatusEvaluation.getExpedite());
                switch (trafficStatusEvaluation.getStatus()) {
                    case "0":
                        System.out.println("getStatus is 未知");break;
                    case "1":
                        System.out.println("getStatus is 畅通");break;
                    case "2":
                        System.out.println("getStatus is 缓行");break;
                    case "3":
                        System.out.println("getStatus is 拥堵");break;
                    default:
                        System.out.println("getStatus is default!");

                }
                System.out.println("getUnknown is " + trafficStatusEvaluation.getUnknown());
                System.out.println("---------");

                System.out.println("getRoads is " + trafficStatusResult.getRoads());
                for (TrafficStatusInfo info : trafficStatusResult.getRoads()) {
                    System.out.println("getAngle is " + info.getAngle());
                    System.out.println("getCoordinates is " + info.getCoordinates());
                    System.out.println("getDirection is " + info.getDirection());
                    System.out.println("getLcodes is " + info.getLcodes());
                    System.out.println("getName is " + info.getName());
                    System.out.println("getSpeed is " + info.getSpeed());
                    System.out.println("getStatus is " + info.getStatus());
                    System.out.println("~~~~");
                }


            }
        });


        RoadTrafficQuery roadTrafficQuery = new RoadTrafficQuery("将台路", "110000", TrafficSearch.ROAD_LEVEL_NORMAL_WAY);
//        RoadTrafficQuery roadTrafficQuery = new RoadTrafficQuery("和平大道", "027", TrafficSearch.ROAD_LEVEL_NORMAL_WAY);
        trafficSearch.loadTrafficByRoadAsyn(roadTrafficQuery);

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
