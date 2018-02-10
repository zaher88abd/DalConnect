package ca.connect.dal.dalconnect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by zaher on 2018-02-10.
 * this activity for buildings list and direct to google map application.
 */

public class ListViewActivity extends AppCompatActivity {
    private ListView lvBuildings;
    ArrayAdapter<String> buildingAdapter;
    String[] buildings;
    private Location userLocation;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buildings_list);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 101);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }

        lvBuildings = findViewById(R.id.lvBuildings);
        buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        Resources res = getResources();
        buildings = res.getStringArray(R.array.buildings);
        lvBuildings.setAdapter(buildingAdapter);
        for (String building :
                buildings) {
            String buildingName = building.split(",")[0];
            buildingAdapter.add(buildingName);

        }
        lvBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (userLocation == null) {
                    Toast.makeText(ListViewActivity.this, "No location", Toast.LENGTH_SHORT).show();
                    return;
                }
                String buildingInfo = buildings[i];
//                https://stackoverflow.com/questions/25190886/android-open-map-intent-with-directions-with-two-points
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
    }
}

//https://www.youtube.com/watch?v=QNb_3QKSmMk
