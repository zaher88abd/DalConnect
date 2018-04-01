package ca.connect.dal.dalconnect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<String> usernameList = new ArrayList<>();
    private ArrayList<UserInformation> userList;

    private FirebaseAuth firebaseAuth;

    private Preferences pref;

    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        firebaseAuth = FirebaseAuth.getInstance();

        pref = new Preferences(MatchActivity.this);

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class)); //profile activity here
        }

        setUpViews();
        setUpListeners();


/*
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernameList);
        textUserListview.setAdapter(arrayAdapter);*/

    }

    public void setUpViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        userList = new ArrayList<UserInformation>();
        adapter = new UserListAdapter(MatchActivity.this, userList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void setUpListeners(){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String usernameFirebase = (String) dataSnapshot.child("Username").getValue();
                String countryFirebase = (String) dataSnapshot.child("Country").getValue();
                String programFirebase = (String) dataSnapshot.child("Program").getValue();


                //Get User's country
                UserInformation userInfo = pref.getUserDetails();
                String usersCountry = userInfo.getCountry();

                System.out.println("UsersCountry: " + usersCountry);

                //so its to write a code that'll filter by country

                if(!countryFirebase.equalsIgnoreCase("") && !countryFirebase.equalsIgnoreCase("null") && countryFirebase != null ){
                    if(countryFirebase.equalsIgnoreCase(usersCountry)){
                       /* usernameList.add(usernameFirebase);
                        arrayAdapter.notifyDataSetChanged();*/

                        UserInformation userInformation = new UserInformation();
                        userInformation.setUsername(usernameFirebase != null ? usernameFirebase : "");
                        userInformation.setProgram(programFirebase != null ? programFirebase : "");

                        userList.add(userInformation);

                        adapter.notifyDataSetChanged();

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

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
