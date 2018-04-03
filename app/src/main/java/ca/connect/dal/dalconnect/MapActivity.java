package ca.connect.dal.dalconnect;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by zaher on 2018-02-10.
 * this activity for buildings list and direct to google map application.
 */

public class MapActivity extends AppCompatActivity implements LocationListener {

    String[] buildings;
    private Location userLocation;
    LocationManager locationManager;
    RecyclerView rv_Buildings;
    MapAdapterList mapAdapterList;
    ArrayList<String> buildingsList;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buildings_list);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 101);
        } else
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        Resources res = getResources();
        buildings = res.getStringArray(R.array.buildings);
        buildingsList = new ArrayList<>();
        for (String building :
                buildings) {
            String buildingName = building.split(",")[0];
            buildingsList.add(buildingName);

        }
        mapAdapterList = new MapAdapterList(buildingsList, this, new MapAdapterList.RVClickListener() {
            @Override
            public void recyclerViewListClicked(int position) {
                if (userLocation == null) {
                    Toast.makeText(MapActivity.this, "No location", Toast.LENGTH_SHORT).show();
                    return;
                }
                String buildingInfo = buildings[position];
                //User location
                String latitude1 = String.valueOf(userLocation.getLatitude());
                String longitude1 = String.valueOf(userLocation.getLongitude());
//               Building Location
                String latitude2 = buildingInfo.split(",")[1];
                String longitude2 = buildingInfo.split(",")[2];

                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude1 + "," + longitude1 + "&daddr=" + latitude2 + "," + longitude2;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));
            }
        });
        rv_Buildings = findViewById(R.id.lvBuildings);
        rv_Buildings.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)));
        rv_Buildings.setAdapter(mapAdapterList);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 101);
        } else
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "asdasdasd", Toast.LENGTH_SHORT).show();
        userLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

//https://www.youtube.com/watch?v=QNb_3QKSmMk
//https://stackoverflow.com/questions/25190886/android-open-map-intent-with-directions-with-two-points
