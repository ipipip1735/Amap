package mine.amap;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2020/9/27.
 */
public class MarkActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap = null;
    double longitude = 114.420535d;
    double latitude = 30.592849d;
    Marker marker;


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

        MyLocationStyle myLocationStyle = new MyLocationStyle();

        //单次定位
//        myLocationStyle.myLocationType(LOCATION_TYPE_SHOW);
//        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATE);

        myLocationStyle.interval(2000);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);


        //隐藏导航图标
//        myLocationStyle.showMyLocation(false);


        //设置图标
//        BitmapDescriptor myLocationIcon = BitmapDescriptorFactory.fromResource(R.drawable.ox);
//        myLocationStyle.myLocationIcon(myLocationIcon);
        //精度圈设置
//        myLocationStyle.strokeColor(Color.argb(255,255,0,0));
//        myLocationStyle.radiusFillColor(Color.argb(100,0,0,255));


        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");
        LatLng latLng = new LatLng(latitude, longitude);



        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("WH")
                .snippet("DefaultMarker")
                .draggable(true);


//        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ta)))
//                .anchor(1, 0);


        marker = aMap.addMarker(options);
        marker.showInfoWindow();

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                System.out.println("~~InfoWindowAdapter.getInfoWindow~~");

//                TextView textView = new TextView(MarkActivity.this);
//                textView.setText("YYY" + marker.getTitle() + "\n" + marker.getSnippet());
//                return textView;

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                System.out.println("~~InfoWindowAdapter.getInfoContents~~");

                TextView textView = new TextView(MarkActivity.this);
                textView.setText("XXX" + marker.getTitle() + "\n" + marker.getSnippet());


                return textView;
            }
        });
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                System.out.println("~~onInfoWindowClick~~");
                System.out.println("[" + marker.getTitle() + "]" + marker.getSnippet());
            }
        });






//       aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//           @Override
//           public boolean onMarkerClick(Marker marker) {
//               System.out.println("~~onMarkerClick~~");
//               return true;
//           }
//       });

//       aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
//           @Override
//           public void onMarkerDragStart(Marker marker) {
//               System.out.println("~~onMarkerDragStart~~");
//
//           }
//
//           @Override
//           public void onMarkerDrag(Marker marker) {
//               System.out.println("~~onMarkerDrag~~");
//
//           }
//
//           @Override
//           public void onMarkerDragEnd(Marker marker) {
//               System.out.println("~~onMarkerDragEnd~~");
//
//           }
//       });




    }

    public void pause(View view) {
        System.out.println("~~button.pause~~");

        marker.setTitle("ooo");
        marker.setSnippet("TTTTTTTTTT");
        marker.showInfoWindow();


    }

    public void resume(View view) {
        System.out.println("~~button.resume~~");



        for (int i = 0; i < 10; i++) {

            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude + Math.random() * 0.1, longitude + Math.random() * 0.1))
                    .title("title|" + i)
                    .snippet("snippet|" + Math.random()))
                    .showInfoWindow();
        }


        Random random = new Random();
        List<LatLng> latLngs = new ArrayList<>();
        PolylineOptions options = new PolylineOptions();
        options.addAll(latLngs)
                .width(10)
                .color(Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256)));
        aMap.addPolyline(options);


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