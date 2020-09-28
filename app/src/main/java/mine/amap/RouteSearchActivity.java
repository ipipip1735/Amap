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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearchCity;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.amap.api.services.route.RouteSearch.BUS_DEFAULT;
import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT;

/**
 * Created by Administrator on 2020/9/28.
 */
public class RouteSearchActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap = null;
    double longitude = 114.420535d;
    double latitude = 30.592849d;
    private int pageNum = 1;
    Random random = new Random();


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
        aMap.clear();

        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                System.out.println("~~onBusRouteSearched~~");
                System.out.println("busRouteResult is " + busRouteResult);
                System.out.println("i is " + i);
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                System.out.println("~~onDriveRouteSearched~~");
                System.out.println("driveRouteResult is " + driveRouteResult);
                System.out.println("i is " + i);

                List<LatLng> latLngs = new ArrayList<>();

                System.out.println("getTaxiCost is " + driveRouteResult.getTaxiCost());
                System.out.println("-----------------");
                for (DrivePath drivePath : driveRouteResult.getPaths()) {
                    System.out.println("getRestriction is " + drivePath.getRestriction());
                    System.out.println("...................");
                    System.out.println("getSteps(" + drivePath.getSteps().size() + ")|" + drivePath.getSteps());
                    for (DriveStep driveStep : drivePath.getSteps()) {
                        System.out.println("getAction is " + driveStep.getAction());
                        System.out.println("getAssistantAction is " + driveStep.getAssistantAction());
                        System.out.println("getDistance is " + driveStep.getDistance());
                        System.out.println("getDuration is " + driveStep.getDuration());
                        System.out.println("getInstruction is " + driveStep.getInstruction());
                        System.out.println("getOrientation is " + driveStep.getOrientation());
                        System.out.println("''''''''''");
                        System.out.println("getPolyline(" + driveStep.getPolyline().size() + ")|" + driveStep.getPolyline());
                        latLngs.clear();
                        for (LatLonPoint latLonPoint : driveStep.getPolyline()) {
                            System.out.println("latLonPoint is " + latLonPoint);
                            latLngs.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                        }
                        PolylineOptions options = new PolylineOptions();
                        options.addAll(latLngs)
                                .width(10)
                                .color(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                        aMap.addPolyline(options);
                        System.out.println("''''''''''");
                        System.out.println("getTMCs is " + driveStep.getTMCs());
                        System.out.println("getRouteSearchCityList is " + driveStep.getRouteSearchCityList());
                        System.out.println("getRoad is " + driveStep.getRoad());
                        System.out.println("getTollDistance is " + driveStep.getTollDistance());
                        System.out.println("getTollRoad is " + driveStep.getTollRoad());
                        System.out.println("getTolls is " + driveStep.getTolls());
                    }

                    System.out.println("...................");
                    System.out.println("getStrategy is " + drivePath.getStrategy());
                    System.out.println("getTollDistance is " + drivePath.getTollDistance());
                    System.out.println("getTolls is " + drivePath.getTolls());
                    System.out.println("getTotalTrafficlights is " + drivePath.getTotalTrafficlights());
                    System.out.println("~~~~~");
                }
                System.out.println("-----------------");
                RouteSearch.DriveRouteQuery query = driveRouteResult.getDriveQuery();
                System.out.println("getAvoidpolygons is " + query.getAvoidpolygons());
                System.out.println("getAvoidpolygonsStr is " + query.getAvoidpolygonsStr());
                System.out.println("getAvoidRoad is " + query.getAvoidRoad());
                System.out.println("getCarType is " + query.getCarType());
                System.out.println("getExclude is " + query.getExclude());
                System.out.println("getExtensions is " + query.getExtensions());
                System.out.println("getFromAndTo is " + query.getFromAndTo());
                System.out.println("getMode is " + query.getMode());
                System.out.println("getPassedByPoints is " + query.getPassedByPoints());
                System.out.println("getPassedPointStr is " + query.getPassedPointStr());

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                System.out.println("~~onWalkRouteSearched~~");
                System.out.println("walkRouteResult is " + walkRouteResult);
                System.out.println("i is " + i);

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                System.out.println("~~onRideRouteSearched~~");
                System.out.println("rideRouteResult is " + rideRouteResult);
                System.out.println("i is " + i);

            }
        });


        double[] doubles = {latitude, longitude, latitude + Math.random() * 0.5, longitude - Math.random() * 0.5};

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(doubles[0], doubles[1]), new LatLonPoint(doubles[2], doubles[3]));
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, DRIVING_SINGLE_DEFAULT, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);


        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(doubles[0], doubles[1]))
                .title("title|start")
                .snippet("snippet|"))
                .showInfoWindow();
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(doubles[2], doubles[3]))
                .title("title|end")
                .snippet("snippet|"))
                .showInfoWindow();


    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");

        aMap.clear();
        RouteSearch routeSearch = new RouteSearch(this);

        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                System.out.println("~~onBusRouteSearched~~");
                System.out.println("busRouteResult is " + busRouteResult);
                System.out.println("i is " + i);
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                System.out.println("~~onDriveRouteSearched~~");
                System.out.println("driveRouteResult is " + driveRouteResult);
                System.out.println("i is " + i);
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                System.out.println("~~onWalkRouteSearched~~");
                System.out.println("walkRouteResult is " + walkRouteResult);
                System.out.println("i is " + i);


                List<LatLng> latLngs = new ArrayList<>();

                System.out.println("getPaths(" + walkRouteResult.getPaths().size() + ")|" + walkRouteResult.getPaths());
                for (WalkPath walkPath : walkRouteResult.getPaths()) {
                    System.out.println("getSteps(" + walkPath.getSteps().size() + ")|" + walkPath.getSteps());
                    for (WalkStep walkStep : walkPath.getSteps()) {
                        System.out.println("getAction is " + walkStep.getAction());
                        System.out.println("getAssistantAction is " + walkStep.getAssistantAction());
                        System.out.println("getDistance is " + walkStep.getDistance());
                        System.out.println("getDuration is " + walkStep.getDuration());
                        System.out.println("getInstruction is " + walkStep.getInstruction());
                        System.out.println("getOrientation is " + walkStep.getOrientation());
                        System.out.println("---------");
                        System.out.println("getPolyline(" + walkStep.getPolyline().size() + ")|" + walkStep.getPolyline());
                        latLngs.clear();
                        for (LatLonPoint latLonPoint : walkStep.getPolyline()) {
                            System.out.println("latLonPoint is " + latLonPoint);
                            latLngs.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                        }

                        PolylineOptions options = new PolylineOptions();
                        options.addAll(latLngs)
                                .width(10)
                                .color(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                        aMap.addPolyline(options);
                        System.out.println("~~~~");
                    }
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                System.out.println("~~onRideRouteSearched~~");
                System.out.println("rideRouteResult is " + rideRouteResult);
                System.out.println("i is " + i);

            }
        });


        double[] doubles = {latitude, longitude, 30.537193d, 114.371961d};
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(doubles[0], doubles[1]), new LatLonPoint(doubles[2], doubles[3]));

        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
        routeSearch.calculateWalkRouteAsyn(query);

        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(doubles[0], doubles[1]))
                .title("title|start")
                .snippet("snippet|"))
                .showInfoWindow();
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(doubles[2], doubles[3]))
                .title("title|end")
                .snippet("snippet|"))
                .showInfoWindow();
    }

    public void pause(View view) {
        System.out.println("~~button.pause~~");

        aMap.clear();
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                System.out.println("~~onBusRouteSearched~~");
                System.out.println("busRouteResult is " + busRouteResult);
                System.out.println("i is " + i);

                List<LatLng> latLngs = new ArrayList<>();

                System.out.println("getPaths(" + busRouteResult.getPaths().size() + ")|" + busRouteResult.getPaths());
                for (BusPath busPath : busRouteResult.getPaths()) {
                    System.out.println("getBusDistance is " + busPath.getBusDistance());
                    System.out.println("getCost is " + busPath.getCost());
                    System.out.println("getWalkDistance is " + busPath.getWalkDistance());
                    System.out.println("-------");
                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    System.out.println("getSteps(" + busPath.getSteps().size() + ")|" + busPath.getSteps());
                    for (BusStep busStep : busPath.getSteps()) {
                        System.out.println("getEntrance is " + busStep.getEntrance());
                        System.out.println("getExit is " + busStep.getExit());
                        System.out.println("getRailway is " + busStep.getRailway());
                        System.out.println("getTaxi is " + busStep.getTaxi());
                        System.out.println("getWalk is " + busStep.getWalk());
                        System.out.println("........");
                        System.out.println("getBusLines(" + busStep.getBusLines().size() + ")|" + busStep.getBusLines());
                        for (RouteBusLineItem item : busStep.getBusLines()) {
                            latLngs.clear();
                            for (LatLonPoint latLonPoint : item.getPolyline()) {
                                System.out.println("latLonPoint is " + latLonPoint);
                                latLngs.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                            }
                            PolylineOptions options = new PolylineOptions();
                            options.addAll(latLngs)
                                    .width(10)
                                    .color(color);
                            aMap.addPolyline(options);
                        }
                    }

                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                System.out.println("~~onDriveRouteSearched~~");
                System.out.println("driveRouteResult is " + driveRouteResult);
                System.out.println("i is " + i);

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                System.out.println("~~onWalkRouteSearched~~");
                System.out.println("walkRouteResult is " + walkRouteResult);
                System.out.println("i is " + i);

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
                System.out.println("~~onRideRouteSearched~~");
                System.out.println("rideRouteResult is " + rideRouteResult);
                System.out.println("i is " + i);

            }
        });

        double[] doubles = {latitude, longitude, 30.542692d, 114.324330d};

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(doubles[0], doubles[1]), new LatLonPoint(doubles[2], doubles[3]));
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, BUS_DEFAULT, "027", 0);

        routeSearch.calculateBusRouteAsyn(query);

        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(doubles[0], doubles[1]))
                .title("title|start")
                .snippet("snippet|"))
                .showInfoWindow();
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(doubles[2], doubles[3]))
                .title("title|end")
                .snippet("snippet|"))
                .showInfoWindow();
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
