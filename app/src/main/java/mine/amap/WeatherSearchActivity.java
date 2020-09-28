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
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2020/9/27.
 */
public class WeatherSearchActivity extends AppCompatActivity {
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

//        WeatherSearchQuery weatherSearchQuery = new WeatherSearchQuery("武汉", WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearchQuery weatherSearchQuery = new WeatherSearchQuery("武汉", WeatherSearchQuery.WEATHER_TYPE_FORECAST);
        WeatherSearch weatherSearch=new WeatherSearch(this);
        weatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                System.out.println("~~onWeatherLiveSearched~~");
                System.out.println("localWeatherLiveResult is " + localWeatherLiveResult);
                System.out.println("i is " + i);


                LocalWeatherLive localWeatherLive = localWeatherLiveResult.getLiveResult();
                System.out.println("getAdCode is " + localWeatherLive.getAdCode());
                System.out.println("getCity is " + localWeatherLive.getCity());
                System.out.println("getHumidity is " + localWeatherLive.getHumidity());
                System.out.println("getProvince is " + localWeatherLive.getProvince());
                System.out.println("getReportTime is " + localWeatherLive.getReportTime());
                System.out.println("getTemperature is " + localWeatherLive.getTemperature());
                System.out.println("getWeather is " + localWeatherLive.getWeather());
                System.out.println("getWindDirection is " + localWeatherLive.getWindDirection());
                System.out.println("getWindPower is " + localWeatherLive.getWindPower());
                System.out.println("-----------------");


                WeatherSearchQuery searchQuery= localWeatherLiveResult.getWeatherLiveQuery();
                System.out.println("getCity is " + searchQuery.getCity());
                System.out.println("getType is " + searchQuery.getType());



            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                System.out.println("~~onWeatherForecastSearched~~");
                System.out.println("localWeatherForecastResult is " + localWeatherForecastResult);
                System.out.println("i is " + i);


                LocalWeatherForecast localWeatherForecast = localWeatherForecastResult.getForecastResult();
                System.out.println("getAdCode is " + localWeatherForecast.getAdCode());
                System.out.println("getCity is " + localWeatherForecast.getCity());
                System.out.println("getProvince is " + localWeatherForecast.getProvince());
                System.out.println("getReportTime is " + localWeatherForecast.getReportTime());
                for (LocalDayWeatherForecast localDayWeatherForecast : localWeatherForecast.getWeatherForecast()) {
                    System.out.println("getDate is " + localDayWeatherForecast.getDate());
                    System.out.println("getDayTemp is " + localDayWeatherForecast.getDayTemp());
                    System.out.println("getDayWeather is " + localDayWeatherForecast.getDayWeather());
                    System.out.println("getDayWindDirection is " + localDayWeatherForecast.getDayWindDirection());
                    System.out.println("getDayWindPower is " + localDayWeatherForecast.getDayWindPower());
                    System.out.println("getNightTemp is " + localDayWeatherForecast.getNightTemp());
                    System.out.println("getNightWeather is " + localDayWeatherForecast.getNightWeather());
                    System.out.println("getNightWindDirection is " + localDayWeatherForecast.getNightWindDirection());
                    System.out.println("getNightWindPower is " + localDayWeatherForecast.getNightWindPower());
                    System.out.println("getWeek is " + localDayWeatherForecast.getWeek());
                    System.out.println("~~~~");
                }
                System.out.println("-----------------");


                WeatherSearchQuery searchQuery= localWeatherForecastResult.getWeatherForecastQuery();
                System.out.println("getCity is " + searchQuery.getCity());
                System.out.println("getType is " + searchQuery.getType());

            }
        });
        weatherSearch.setQuery(weatherSearchQuery);
        weatherSearch.searchWeatherAsyn();


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
