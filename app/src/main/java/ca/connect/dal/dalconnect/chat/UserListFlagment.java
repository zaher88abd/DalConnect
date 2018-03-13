package ca.connect.dal.dalconnect.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.connect.dal.dalconnect.NavigationActivity;
import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.UserInformation;

public class UserListFlagment extends Fragment
{
    private ListView listView;
    private UserListAdapter userListAdapter;
    private List<UserInformation> user_list = new ArrayList<UserInformation>();

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    userListAdapter = new UserListAdapter(user_list);
                    listView.setAdapter(userListAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            /*UserInformation secondUser = user_list.get(i);
                            FirebaseUser currentUser = AuthUtils.getInstance().getCurrentUser();

                            Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
                            String id = AuthUtils.getInstance().generateRoomId(secondUser.getUsername(), currentUser.getDisplayName());

                            intent.putExtra("room_id",id);
                            intent.putExtra("user_name",currentUser.getDisplayName());

                            UserListActivity.this.finish();
                            startActivity(intent);*/

                        }
                    });
                    break;

                default:
                    break;
            }
        }

    };

    public UserListFlagment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_list, container, false);
        listView = (ListView) view.findViewById(R.id.lv_id);

        getActivity().setTitle("Friendlist");

        loadUserList();

        return view;
    }

    private void loadUserList()
    {
       // Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("country");
        Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference().child("users");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Success!!!!");
                UserInformation user = null;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    System.out.println("Success");
                    user = postSnapshot.getValue(UserInformation.class);
                    user_list.add(user);
                }

                mHandler.obtainMessage(0).sendToTarget();
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
}
