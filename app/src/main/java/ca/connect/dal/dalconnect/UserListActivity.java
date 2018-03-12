package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.connect.dal.dalconnect.model.User;
import ca.connect.dal.dalconnect.util.AuthUtils;

public class UserListActivity extends AppCompatActivity
{
    private ListView listView;
    private  UserListAdapter userListAdapter;
    private List<User> user_list = new ArrayList<User>();

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    userListAdapter = new UserListAdapter(user_list, UserListActivity.this);
                    listView.setAdapter(userListAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            User secondUser = user_list.get(i);
                            FirebaseUser currentUser = AuthUtils.getInstance().getCurrentUser();

                            Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
                            String id = AuthUtils.getInstance().generateRoomId(secondUser.getUser_name(), currentUser.getDisplayName());

                            intent.putExtra("room_id",id);
                            intent.putExtra("user_name",currentUser.getDisplayName());

                            UserListActivity.this.finish();
                            startActivity(intent);

                        }
                    });
                    break;

                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = (ListView) this.findViewById(R.id.lv_id);

        Intent intent = getIntent();

        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        AuthUtils.getInstance().signIn(email, password, this);

        loadUserList();

    }

    private void loadUserList()
    {
        Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference().child("Dal_Chat/Users").orderByChild("country");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Success!!!!");
                User user = null;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    System.out.println("Success");
                    user = postSnapshot.getValue(User.class);
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
