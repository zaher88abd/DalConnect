package ca.connect.dal.dalconnect.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.connect.dal.dalconnect.NavigationActivity;
import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.UserInformation;
import ca.connect.dal.dalconnect.util.AuthUtils;

public class UserListFlagment extends Fragment
{
    private ListView listView;
    private UserListAdapter userListAdapter;
    private List<UserInformation> user_list = new ArrayList<UserInformation>();
    private List<UserInformation> user_list_all = new ArrayList<UserInformation>();
    private String hostCountry = "";
    private String hostStartTerm = "";
    private List<String> room_id_list = new ArrayList<String>();

    private ValueEventListener valueEventListener;
    private ChildEventListener childEventListener;

    private FragmentManager fragmentManager;

    private AuthUtils authUtilsInstance = AuthUtils.getInstance();

    @SuppressLint("HandlerLeak")
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
                            UserInformation secondUser = user_list.get(i);
                            FirebaseUser currentUser = AuthUtils.getInstance().getCurrentUser();

                            String room_id = AuthUtils.getInstance().generateRoomId(currentUser.getUid(), secondUser.getUID());
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, ChatFragment.newInstance(secondUser.getUID(), secondUser.getUsername(), secondUser.getUserImage(), room_id));
                            fragmentTransaction.commit();

                        }
                    });

                    onReceiveMessages();

                    break;

                default:
                    break;
            }
        }

    };

    public UserListFlagment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference().child("users");

        if(valueEventListener != null)
        {
            myTopPostsQuery.removeEventListener(valueEventListener);
        }

        if(childEventListener !=null )
        {
            FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/").removeEventListener(childEventListener);
        }

    }


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

        valueEventListener = myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Success!!!!");
                UserInformation user = null;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    System.out.println("Success");
                    user = postSnapshot.getValue(UserInformation.class);
                    user.setUID(postSnapshot.getKey());
                    user_list_all.add(user);

                    if(user.getUID().equals(authUtilsInstance.getCurrentUId()))
                    {
                        hostCountry = user.getCountry().toLowerCase();
                        hostStartTerm = user.getStartTerm().toLowerCase();
                    }
                    //room_id_list.add(authUtilsInstance.generateRoomId(user.getUID(), authUtilsInstance.getCurrentUId()));
                }

                if(user_list_all.size() > 0)
                {
                    for(UserInformation userTemp : user_list_all)
                    {
                        /**
                         * 1. The same country
                         * 2. Not the current User
                         * 3. the same start term
                         */
                        if(userTemp.getCountry().toLowerCase().equals(hostCountry)
                                && !userTemp.getUID().equals(authUtilsInstance.getCurrentUId())
                                && userTemp.getStartTerm().toLowerCase().equals(hostStartTerm))
                        {
                            user_list.add(userTemp);
                            room_id_list.add(authUtilsInstance.generateRoomId(userTemp.getUID(), authUtilsInstance.getCurrentUId()));
                        }

                    }
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

    private void handleUnReadMessage(HashMap hashMap, String roomID)
    {
        int num_of_unread_message = 0;
        HashMap temp = null;
        for (Object value : hashMap.values())
        {
            temp = (HashMap)value;
            if("false".equals(temp.get("read").toString()) && authUtilsInstance.getCurrentUId().equals(temp.get("idReceiver")))
            {
                num_of_unread_message++;
            }
        }

        int index = this.room_id_list.indexOf(roomID);


        View view = listView.getChildAt(index - listView.getFirstVisiblePosition());
        if(index > -1)
        {
            if(view != null)
            {
                TextView textView = (TextView) view.findViewById(R.id.id_message_numbers);
                if(num_of_unread_message > 0)
                {
                    textView.setText(""+num_of_unread_message);
                }
                else
                {
                    textView.setText("");
                }
            }

            userListAdapter.putUnReadMessageMap(index, num_of_unread_message);
        }

    }

    private void onReceiveMessages()
    {
        childEventListener = FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                if (dataSnapshot.getValue() != null) {
                    HashMap hashMap = (HashMap) dataSnapshot.getValue();

                    handleUnReadMessage(hashMap, dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getValue() != null) {
                    HashMap hashMap = (HashMap) dataSnapshot.getValue();

                    handleUnReadMessage(hashMap, dataSnapshot.getKey());
                }

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
}
