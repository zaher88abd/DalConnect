package ca.connect.dal.dalconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ListView textUserListview;
    private ArrayList<String> usernameList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;

    private Preferences pref;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
// so its working but after the add information page ,it goes to the navigation drawer ,but when i  click on match it doesnt goes to the pagere u have been working on

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        pref = new Preferences(getActivity());

        if(firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), loginActivity.class)); //profile activity here
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        textUserListview = (ListView) view.findViewById(R.id.textUserListview);


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, usernameList);
        textUserListview.setAdapter(arrayAdapter);

        //Get User's country
        UserInformation userInfo = pref.getUserDetails();
        final String usersCountry = userInfo.getCountry();

        System.out.println("UsersCountry: " + usersCountry);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String usernameFirebase = (String) dataSnapshot.child("username").getValue();
                String countryFirebase = (String) dataSnapshot.child("country").getValue();

                System.out.println("usernameFirebase: " + usernameFirebase);
                System.out.println("countryFirebase: " + countryFirebase);

                //Get User's country
                UserInformation userInfo = pref.getUserDetails();
                String usersCountry = userInfo.getCountry();

                System.out.println("UsersCountry: " + usersCountry);

                //so its to write a code that'll filter by country

                if(!countryFirebase.equalsIgnoreCase("") && !countryFirebase.equalsIgnoreCase("null")){
                    if(countryFirebase.equalsIgnoreCase(usersCountry)){
                        usernameList.add(usernameFirebase);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
///what should i fo
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

        return view;
    }

}
