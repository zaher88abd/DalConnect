package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ListView textUserListview;
    private ArrayList<String> usernameList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;

    private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        firebaseAuth = FirebaseAuth.getInstance();

        pref = new Preferences(MatchActivity.this);

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this,loginActivity.class)); //profile activity here
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        textUserListview = (ListView) findViewById(R.id.textUserListview);


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernameList);
        textUserListview.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String usernameFirebase = (String) dataSnapshot.child("Username").getValue();
                String countryFirebase = (String) dataSnapshot.child("Country").getValue();

                //Get User's country
                UserInformation userInfo = pref.getUserDetails();
                String usersCountry = userInfo.getCountry();

                System.out.println("UsersCountry: " + usersCountry);

                //so its to write a code that'll filter by country

                if(!countryFirebase.equalsIgnoreCase("") && !countryFirebase.equalsIgnoreCase("null") && countryFirebase != null ){
                    if(countryFirebase.equalsIgnoreCase(usersCountry)){
                        usernameList.add(usernameFirebase);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

   // Intent intent = new Intent(MatchActivity.this, MainFragment.class);startActivity(intent);
      //  finish();

    }

}
