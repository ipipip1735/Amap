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
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.routepoisearch.RoutePOIItem;
import com.amap.api.services.routepoisearch.RoutePOISearch;
import com.amap.api.services.routepoisearch.RoutePOISearchQuery;
import com.amap.api.services.routepoisearch.RoutePOISearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.amap.api.services.routepoisearch.RoutePOISearch.RoutePOISearchType.TypeATM;

/**
 * Created by Administrator on 2020/9/27.
 */
public class BusSearchActivity extends AppCompatActivity {
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


        BusStationQuery busStationQuery = new BusStationQuery("中南路", "027");
        busStationQuery.setPageNumber(1);
        busStationQuery.setPageSize(25);

        BusStationSearch busStationSearch = new BusStationSearch(this, busStationQuery);
        busStationSearch.setOnBusStationSearchListener(new BusStationSearch.OnBusStationSearchListener() {
            @Override
            public void onBusStationSearched(BusStationResult busStationResult, int i) {
                System.out.println("~~onBusStationSearched~~");
                System.out.println("busStationResult is " + busStationResult);
                System.out.println("i is " + i);

                System.out.println("getBusStations is " + busStationResult.getBusStations());
                for (BusStationItem item : busStationResult.getBusStations()) {
                    System.out.println("getAdCode is " + item.getAdCode());
                    System.out.println("getBusLineItems is " + item.getBusLineItems());
                    System.out.println("getBusStationId is " + item.getBusStationId());
                    System.out.println("getBusStationName is " + item.getBusStationName());
                    System.out.println("getCityCode is " + item.getCityCode());
                    System.out.println("getLatLonPoint is " + item.getLatLonPoint());

                    aMap.addMarker(new MarkerOptions()
                            .position(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude()))
                            .title(item.getBusStationName())
                            .snippet(item.getBusLineItems().toString()))
                            .showInfoWindow();

                    System.out.println("~~~~");
                }
                System.out.println("---------");





                System.out.println("getPageCount is " + busStationResult.getPageCount());
                System.out.println("getSearchSuggestionCities is " + busStationResult.getSearchSuggestionCities());
                System.out.println("getSearchSuggestionKeywords is " + busStationResult.getSearchSuggestionKeywords());
                System.out.println(busStationResult.getSearchSuggestionKeywords());
                System.out.println("---------");


                System.out.println(busStationResult.getSearchSuggestionCities());
                for (SuggestionCity suggestionCity : busStationResult.getSearchSuggestionCities()) {
                    System.out.println("getAdCode is " + suggestionCity.getAdCode());
                    System.out.println("getCityCode is " + suggestionCity.getCityCode());
                    System.out.println("getCityName is " + suggestionCity.getCityName());
                    System.out.println("getSuggestionNum is " + suggestionCity.getSuggestionNum());
                }
                System.out.println("---------");


                BusStationQuery query = busStationResult.getQuery();
                System.out.println("getCity is " + query.getCity());
                System.out.println("getPageNumber is " + query.getPageNumber());
                System.out.println("getPageSize is " + query.getPageSize());
                System.out.println("getQueryString is " + query.getQueryString());

            }
        });

        busStationSearch.searchBusStationAsyn();

    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");


//        final BusLineQuery busLineQuery = new BusLineQuery("514", BusLineQuery.SearchType.BY_LINE_NAME, "027");
        final BusLineQuery busLineQuery = new BusLineQuery("514", BusLineQuery.SearchType.BY_LINE_ID, "027");
        busLineQuery.setPageSize(10);
        busLineQuery.setPageNumber(pageNum);
        BusLineSearch busLineSearch = new BusLineSearch(this, busLineQuery);
        busLineSearch.setOnBusLineSearchListener(new BusLineSearch.OnBusLineSearchListener() {
            @Override
            public void onBusLineSearched(BusLineResult busLineResult, int i) {
                System.out.println("~~onBusLineSearched~~");
                System.out.println("busLineResult is " + busLineResult);
                System.out.println("i is " + i);

                Random random = new Random();

                System.out.println(busLineResult.getBusLines());
                for (BusLineItem busLineItem : busLineResult.getBusLines()) {
                    System.out.println("getBasicPrice is " + busLineItem.getBasicPrice());
                    System.out.println("getBounds is " + busLineItem.getBounds());
                    System.out.println("getBusCompany is " + busLineItem.getBusCompany());
                    System.out.println("getBusLineId is " + busLineItem.getBusLineId());
                    System.out.println("getBusLineName is " + busLineItem.getBusLineName());
                    System.out.println("getBusLineType is " + busLineItem.getBusLineType());
                    System.out.println("getBusStations is " + busLineItem.getBusStations());
                    System.out.println("getCityCode is " + busLineItem.getCityCode());
                    System.out.println("getDistance is " + busLineItem.getDistance());
                    System.out.println("getFirstBusTime is " + busLineItem.getFirstBusTime());
                    System.out.println("getLastBusTime is " + busLineItem.getLastBusTime());
                    System.out.println("getOriginatingStation is " + busLineItem.getOriginatingStation());
                    System.out.println("getTerminalStation is " + busLineItem.getTerminalStation());
                    System.out.println("getTotalPrice is " + busLineItem.getTotalPrice());


                    System.out.println("getDirectionsCoordinates is " + busLineItem.getDirectionsCoordinates());
//                    aMap.clear();
                    List<LatLng> latLngs = new ArrayList<>();
                    for (LatLonPoint point : busLineItem.getDirectionsCoordinates()) {
                        latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                    }


                    PolylineOptions options = new PolylineOptions();
                    options.addAll(latLngs)//增加一组点
                            .width(10)
                            .color(Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    aMap.addPolyline(options);
                    System.out.println("~~~~");
                }
                System.out.println("-----------------");

                System.out.println(busLineResult.getSearchSuggestionCities());
                for (SuggestionCity suggestionCity : busLineResult.getSearchSuggestionCities()) {
                    System.out.println("getAdCode is " + suggestionCity.getAdCode());
                    System.out.println("getCityCode is " + suggestionCity.getCityCode());
                    System.out.println("getCityName is " + suggestionCity.getCityName());
                    System.out.println("getSuggestionNum is " + suggestionCity.getSuggestionNum());
                    System.out.println("~~~~");
                }
                System.out.println("-----------------");


                System.out.println("getPageCount is " + busLineResult.getPageCount());
                System.out.println("getSearchSuggestionKeywords() is " + busLineResult.getSearchSuggestionKeywords());

            }
        });
        busLineSearch.searchBusLineAsyn();



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
