package ca.connect.dal.dalconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MatchListFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ListView textUserListview;
    private ArrayList<String> usernameList = new ArrayList<>();
    private ArrayList<UserInformation> userList;

    private FirebaseAuth firebaseAuth;

    private Preferences pref;

    private UserListAdapter adapter;

    private RecyclerView recyclerView;


    public MatchListFragment() {
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

        setUpViews(view);
        setUpListeners();

        return view;
    }

    private void setUpViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        userList = new ArrayList<UserInformation>();
        adapter = new UserListAdapter(getActivity(), userList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpListeners(){

        Query myTopPostsQuery = databaseReference.child("users/");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInformation userInformation = null;
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren())
                {

                    userInformation = userSnapshot.getValue(UserInformation.class);
                    userList.add(userInformation);




                    /*String usernameFromFirebase = (String) userSnapshot.child("username").getValue();
                    String countryFromFirebase = (String) userSnapshot.child("country").getValue();
                    String startTermFromFirebase = (String) userSnapshot.child("startTerm").getValue();
                    String programFromFirebase = (String) userSnapshot.child("Program").getValue();

                    System.out.println("usernameFromFirebase: " + usernameFromFirebase);
                    System.out.println("countryFromFirebase: " + countryFromFirebase);
                    System.out.println("startTermFromFirebase: " + startTermFromFirebase);
                    System.out.println("programFromFirebase: " + programFromFirebase);

                    //Get User's country
                    UserInformation userInfo = pref.getUserDetails();
                    String usersCountry = userInfo.getCountry();
                    String usersStartTerm = userInfo.getStartTerm();

                    System.out.println("UsersCountry: " + usersCountry);
                    System.out.println("usersStartTerm: " + usersStartTerm);

                    //so its to write a code that'll filter by country

                    if(countryFromFirebase != null && !countryFromFirebase.equalsIgnoreCase("")
                            && !countryFromFirebase.equalsIgnoreCase("null")){

                        if(countryFromFirebase.equalsIgnoreCase(usersCountry)){

                            if(startTermFromFirebase != null && !startTermFromFirebase.equalsIgnoreCase("") &&
                                    !startTermFromFirebase.equalsIgnoreCase("null")){

                                if(startTermFromFirebase.equalsIgnoreCase(usersStartTerm)){
                                    UserInformation userInformation = new UserInformation();
                                    userInformation.setUsername(usernameFromFirebase != null ? usernameFromFirebase : "");
                                    userInformation.setProgram(programFromFirebase != null ? programFromFirebase : "");

                                    userList.add(userInformation);

                                    adapter.notifyDataSetChanged();

                                }
                            }

                        }
                    }*/
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
                System.out.println("Failure");
            }
        });

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MatchActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MatchActivity.ClickListener clickListener) {
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
