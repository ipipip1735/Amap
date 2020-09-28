package mine.amap;

import android.app.SearchableInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.core.AMapException;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import static com.amap.api.maps2d.model.MyLocationStyle.LOCATION_TYPE_LOCATE;
import static com.amap.api.services.routepoisearch.RoutePOISearch.RoutePOISearchType.TypeATM;

/**
 * Created by Administrator on 2020/9/27.
 */
public class SearchActivity extends AppCompatActivity {
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

        PoiSearch.Query query = new PoiSearch.Query("肯德基", "", "027");
        query.setPageSize(25);
        query.setPageNum(pageNum);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                System.out.println("~~onPoiSearched~~");
                System.out.println("poiResult is " + poiResult);
                System.out.println("i is " + i);

                System.out.println("getBound is " + poiResult.getBound());
                System.out.println("getPageCount is " + poiResult.getPageCount());
                System.out.println("getSearchSuggestionCitys is " + poiResult.getSearchSuggestionCitys());
                System.out.println("getSearchSuggestionKeywords is " + poiResult.getSearchSuggestionKeywords());
                System.out.println("-------------");

                aMap.clear();//清除所有覆盖物（Marker，Overlay，Polyline），如果只删除Marker需要自己使用List维护

                for (PoiItem poiItem : poiResult.getPois()) {
                    System.out.println(poiItem);

                    aMap.addMarker(new MarkerOptions()
                            .position(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()))
                            .title(poiItem.getTitle())
                            .snippet(poiItem.getSnippet()))
                            .showInfoWindow();

                    System.out.println("getAdCode is " + poiItem.getAdCode());
                    System.out.println("getAdName is " + poiItem.getAdName());
                    System.out.println("getBusinessArea is " + poiItem.getBusinessArea());
                    System.out.println("getCityCode is " + poiItem.getCityCode());
                    System.out.println("getCityName is " + poiItem.getCityName());
                    System.out.println("getDirection is " + poiItem.getDirection());
                    System.out.println("getDistance is " + poiItem.getDistance());
                    System.out.println("getEmail is " + poiItem.getEmail());
                    System.out.println("getEnter is " + poiItem.getEnter());
                    System.out.println("getExit is " + poiItem.getExit());
                    System.out.println("getIndoorData is " + poiItem.getIndoorData());
                    System.out.println("getLatLonPoint is " + poiItem.getLatLonPoint());
                    System.out.println("getParkingType is " + poiItem.getParkingType());
                    System.out.println("getPhotos is " + poiItem.getPhotos());
                    System.out.println("getPoiExtension is " + poiItem.getPoiExtension());
                    System.out.println("getPoiId is " + poiItem.getPoiId());
                    System.out.println("getPostcode is " + poiItem.getPostcode());
                    System.out.println("getProvinceCode is " + poiItem.getProvinceCode());
                    System.out.println("getProvinceName is " + poiItem.getProvinceName());
                    System.out.println("getSnippet is " + poiItem.getSnippet());
                    System.out.println("getSubPois is " + poiItem.getSubPois());
                    System.out.println("getTel is " + poiItem.getTel());
                    System.out.println("getTitle is " + poiItem.getTitle());
                    System.out.println("getTitle is " + poiItem.getShopID());
                    System.out.println("getTypeCode is " + poiItem.getTypeCode());
                    System.out.println("getTypeDes is " + poiItem.getTypeDes());
                    System.out.println("getWebsite is " + poiItem.getWebsite());
                    System.out.println("~~~~~~");

                }
                System.out.println("-------------");


                PoiSearch.Query query = poiResult.getQuery();
                System.out.println("query.getBuilding is " + query.getBuilding());
                System.out.println("query.getCategory is " + query.getCategory());
                System.out.println("query.getCity is " + query.getCity());
                System.out.println("query.getCityLimit is " + query.getCityLimit());
                System.out.println("query.getExtensions is " + query.getExtensions());
                System.out.println("query.getLocation is " + query.getLocation());
                System.out.println("query.getPageNum is " + query.getPageNum());
                System.out.println("query.getPageSize is " + query.getPageSize());
                System.out.println("query.getQueryString is " + query.getQueryString());

                pageNum = pageNum == poiResult.getPageCount() ? 1 : pageNum + 1;

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                System.out.println("~~onPoiItemSearched~~");
                System.out.println("poiItem is " + poiItem);
                System.out.println("i is " + i);

                aMap.addMarker(new MarkerOptions()
                        .position(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()))
                        .title(poiItem.getTitle())
                        .snippet(poiItem.getSnippet()))
                        .showInfoWindow();

            }
        });

//        poiSearch.searchPOIAsyn();
        poiSearch.searchPOIIdAsyn("B0FFF5W2SV");//使用ID搜索
    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");


        InputtipsQuery inputquery = new InputtipsQuery("横店 电影院", "027");
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                System.out.println("~~onGetInputtips~~");
                System.out.println("list is " + list.size());
                System.out.println("i is " + i);


                for (Tip tip : list) {
                    System.out.println(tip);
                    System.out.println("getAdcode is " + tip.getAdcode());
                    System.out.println("getAddress is " + tip.getAddress());
                    System.out.println("getDistrict is " + tip.getDistrict());
                    System.out.println("getName is " + tip.getName());
                    System.out.println("getPoiID is " + tip.getPoiID());
                }
            }
        });

        //同步查询
//        try {
//            inputTips.requestInputtips();
//        } catch (AMapException e) {
//            e.printStackTrace();
//        }

        //异步查询
        inputTips.requestInputtipsAsyn();


    }

    public void pause(View view) {
        System.out.println("~~button.pause~~");

        LatLonPoint fromPoint = new LatLonPoint(latitude, longitude);
        LatLonPoint toPoint = new LatLonPoint(latitude + Math.random()*0.5d, longitude);

        RoutePOISearchQuery query = new RoutePOISearchQuery(fromPoint ,toPoint, 0, TypeATM, 250);
        RoutePOISearch search = new RoutePOISearch(this, query);
        search.setPoiSearchListener(new RoutePOISearch.OnRoutePOISearchListener() {
            @Override
            public void onRoutePoiSearched(RoutePOISearchResult routePOISearchResult, int i) {
                System.out.println("~~onRoutePoiSearched~~");

                System.out.println("getQuery is " + routePOISearchResult.getQuery());
                System.out.println("getRoutePois is " + routePOISearchResult.getRoutePois());

                for (RoutePOIItem routePOIItem : routePOISearchResult.getRoutePois()) {
                    System.out.println("getDistance() is " + routePOIItem.getDistance());
                    System.out.println("getDuration() is " + routePOIItem.getDuration());
                    System.out.println("getID() is " + routePOIItem.getID());
                    System.out.println("getPoint() is " + routePOIItem.getPoint());
                    System.out.println("getTitle() is " + routePOIItem.getTitle());
                }
            }
        });
        search.searchRoutePOIAsyn();


    }

    public void resume(View view) {
        System.out.println("~~button.resume~~");

        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                System.out.println("~~onRegeocodeSearched~~");
                System.out.println("regeocodeResult is " + regeocodeResult);
                System.out.println("i is " + i);


                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                System.out.println("getAdCode is " + regeocodeAddress.getAdCode());
                System.out.println("getAois is " + regeocodeAddress.getAois());
                System.out.println("getBuilding is " + regeocodeAddress.getBuilding());
                System.out.println("getBusinessAreas is " + regeocodeAddress.getBusinessAreas());
                System.out.println("getCity is " + regeocodeAddress.getCity());
                System.out.println("getCityCode is " + regeocodeAddress.getCityCode());
                System.out.println("getCountry is " + regeocodeAddress.getCountry());
                System.out.println("getCountryCode is " + regeocodeAddress.getCountryCode());
                System.out.println("getCrossroads is " + regeocodeAddress.getCrossroads());
                System.out.println("getDistrict is " + regeocodeAddress.getDistrict());
                System.out.println("getFormatAddress is " + regeocodeAddress.getFormatAddress());
                System.out.println("getNeighborhood is " + regeocodeAddress.getNeighborhood());
                System.out.println("getPois is " + regeocodeAddress.getPois());
                System.out.println("getProvince is " + regeocodeAddress.getProvince());
                System.out.println("getRoads is " + regeocodeAddress.getRoads());
                System.out.println("getStreetNumber is " + regeocodeAddress.getStreetNumber());
                System.out.println("getTowncode is " + regeocodeAddress.getTowncode());
                System.out.println("getTownship is " + regeocodeAddress.getTownship());


                System.out.println("-----------------");

                RegeocodeQuery regeocodeQuery = regeocodeResult.getRegeocodeQuery();
                System.out.println("getExtensions is " + regeocodeQuery.getExtensions());
                System.out.println("getLatLonType is " + regeocodeQuery.getLatLonType());
                System.out.println("getMode is " + regeocodeQuery.getMode());
                System.out.println("getPoint is " + regeocodeQuery.getPoint());
                System.out.println("getPoiType is " + regeocodeQuery.getPoiType());
                System.out.println("getRadius is " + regeocodeQuery.getRadius());

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                System.out.println("~~onGeocodeSearched~~");
                System.out.println("geocodeResult is " + geocodeResult);
                System.out.println("i is " + i);


                System.out.println(geocodeResult.getGeocodeAddressList());
                for (GeocodeAddress geocodeAddress : geocodeResult.getGeocodeAddressList()) {
//                    System.out.println("getAdCode is " + geocodeAddress.getAdCode());
//                    System.out.println("getAois is " + geocodeAddress.getAois());
//                    System.out.println("getBusinessAreas is " + geocodeAddress.getBusinessAreas());
//                    System.out.println("getCityCode is " + geocodeAddress.getCityCode());
//                    System.out.println("getCountryCode is " + geocodeAddress.getCountryCode());
//                    System.out.println("getCrossroads is " + geocodeAddress.getCrossroads());
//                    System.out.println("getPois is " + geocodeAddress.getPois());
//                    System.out.println("getRoads is " + geocodeAddress.getRoads());
//                    System.out.println("getStreetNumber is " + geocodeAddress.getStreetNumber());
//                    System.out.println("getTowncode is " + geocodeAddress.getTowncode());
                    System.out.println("getBuilding is " + geocodeAddress.getBuilding());
                    System.out.println("getCity is " + geocodeAddress.getCity());
                    System.out.println("getCountry is " + geocodeAddress.getCountry());
                    System.out.println("getDistrict is " + geocodeAddress.getDistrict());
                    System.out.println("getFormatAddress is " + geocodeAddress.getFormatAddress());
                    System.out.println("getNeighborhood is " + geocodeAddress.getNeighborhood());
                    System.out.println("getProvince is " + geocodeAddress.getProvince());
                    System.out.println("getTownship is " + geocodeAddress.getTownship());
                }


                System.out.println("----------");
                GeocodeQuery geocodeQuery = geocodeResult.getGeocodeQuery();
                System.out.println("getCity is " + geocodeQuery.getCity());
                System.out.println("getCountry is " + geocodeQuery.getCountry());
                System.out.println("getLocationName is " + geocodeQuery.getLocationName());



            }
        });
//        GeocodeQuery query = new GeocodeQuery("公安局", "027");
//        geocoderSearch.getFromLocationNameAsyn(query);

        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 2000, GeocodeSearch.AMAP);//AMAP是高德坐标，也可以使用原生GPS坐标
        geocoderSearch.getFromLocationAsyn(query);





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
