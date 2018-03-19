package ca.connect.dal.dalconnect.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.UserInformation;
import ca.connect.dal.dalconnect.chat.model.Message;
import ca.connect.dal.dalconnect.util.AuthUtils;

public class ChatFragment extends Fragment implements View.OnClickListener {

    private ImageButton btnSend;
    private EditText editWriteMessage;
    //receiver info
    private String uid = null;
    private String userName = null;
    private String userPortrait = null;
    ///////////////////////////////////
    private String roomId = null;
    private ListView listView = null;

    private ChatListAdapter chatListAdapter;


    //private ArrayAdapter<String> itemsAdapter;

    public static final String USER_NAME = "user_name";
    public static final String ROOM_ID = "room_id";
    public static final String USER_PORTRAIT = "user_portrait";
    public static final String UID_STR = "uid";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            uid = bundle.getString(UID_STR);
            userName = bundle.getString(USER_NAME);
            userPortrait = bundle.getString(USER_PORTRAIT);
            roomId = bundle.getString(ROOM_ID);

        }
    }

    public static ChatFragment newInstance(String uid, String user_name, String user_portrait, String room_id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(UID_STR, uid);
        bundle.putString(USER_NAME, user_name);
        bundle.putString(ROOM_ID, room_id);
        bundle.putString(USER_PORTRAIT, user_portrait);
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    private void onReceiveMessages()
    {
        FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();

                    Message messenge = dataSnapshot.getValue(Message.class);
                    chatListAdapter.addMessage(messenge);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_chat, container, false);
        listView = (ListView)view.findViewById(R.id.listChat);
        chatListAdapter = new ChatListAdapter();
        listView.setAdapter(chatListAdapter);


        /*itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        itemsAdapter.clear();*/
        //listView.setAdapter(itemsAdapter);


        btnSend = (ImageButton) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setBackgroundResource(R.drawable.ic_send);

        editWriteMessage = (EditText) view.findViewById(R.id.editWriteMessage);


        onReceiveMessages();
        getActivity().setTitle(userName);
        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                editWriteMessage.setText("");
                Message newMessage = new Message();
                newMessage.setText(content);
                newMessage.setIdSender(AuthUtils.getInstance().getCurrentUId());
                newMessage.setSenderPortrait(AuthUtils.getInstance().getCurrentUId());
                newMessage.setIdReceiver(this.uid);
                newMessage.setReceiverPortrait(this.userPortrait);
                newMessage.setTimestamp(System.currentTimeMillis());
                FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).push().setValue(newMessage);

            }
        }

    }
}
