package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ca.connect.dal.dalconnect.model.Message;
import ca.connect.dal.dalconnect.util.AuthUtils;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnSend;
    private EditText editWriteMessage;
    private String userName = null;
    private String roomId = null;
    private ListView listView = null;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setBackgroundResource(R.drawable.ic_send);

        editWriteMessage = (EditText) findViewById(R.id.editWriteMessage);


        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        itemsAdapter.clear();
        listView = (ListView)findViewById(R.id.listChat);
        listView.setAdapter(itemsAdapter);

        Intent intent = getIntent();


        userName = AuthUtils.getInstance().getCurrentUser().getDisplayName();
        roomId = intent.getStringExtra("room_id");

        TextView tvHeader=new TextView(ChatActivity.this);
        tvHeader.setText(userName);
        listView.addHeaderView(tvHeader);



        FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();

                    String sender = (String) mapMessage.get("idSender");
                    //if(!userName.equals(sender))
                    //{
                    itemsAdapter.add(sender + ": " + mapMessage.get("text"));
                    //}


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

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                editWriteMessage.setText("");
                Message newMessage = new Message();
                newMessage.text = content;
                newMessage.idSender = userName;
                newMessage.idReceiver = "1";
                newMessage.timestamp = System.currentTimeMillis();
                FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).push().setValue(newMessage);

                //itemsAdapter.add(userName + ": " + content);
            }
        }

    }
}
