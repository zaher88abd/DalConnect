package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by zaher on 2018-02-10.
 * this activity for buildings list and direct to google map application.
 */

public class ListViewActivity extends AppCompatActivity {
    private ListView lvBuildings;
    ArrayAdapter<String> buildingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvBuildings = findViewById(R.id.lvBuildings);
        buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        Resources res = getResources();
        final String[] buildings = res.getStringArray(R.array.buildings);
        for (String building :
                buildings) {
            buildingAdapter.add(building.split(",")[0]);
        }
        lvBuildings.setAdapter(buildingAdapter);
        lvBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String buildingInfo = buildings[i];
//                https://stackoverflow.com/questions/25190886/android-open-map-intent-with-directions-with-two-points
                //User location
                String latitude1 = "";
                String longitude1 = "";
//               Building Location
                String latitude2 = "";
                String longitude2 = "";

                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude1 + "," + longitude1 + "&daddr=" + latitude2 + "," + longitude2;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));
            }
        });
    }
}
