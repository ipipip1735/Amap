package mine.amap;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2020/9/27.
 */
public class DrawActivity extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap = null;
    double longitude = 114.420535d;
    double latitude = 30.592849d;
    Marker marker;
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

        mMapView = new MapView(this);

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

        //绘制线段（画三角型）
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(39.898323, 116.057694));
        latLngs.add(new LatLng(39.900430, 116.265061));
        latLngs.add(new LatLng(39.999391, 116.135972));

        PolylineOptions options = new PolylineOptions();
        options.add(new LatLng(39.999391, 116.135972))//增加一个点
                .addAll(latLngs)//增加一组点
                .width(10)
                .color(Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256)))
                .setDottedLine(true);
        aMap.addPolyline(options);

    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");

        //绘制图形
        LatLng latLng = new LatLng(39.984059,116.307771);
        CircleOptions options = new CircleOptions().
                center(latLng).
                radius(1000).
                fillColor(Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256))).
                strokeColor(Color.argb(255, 0, 0, 0)).
                strokeWidth(5);


        aMap.addCircle(options);

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