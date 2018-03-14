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

import java.util.HashMap;

import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.chat.model.Message;
import ca.connect.dal.dalconnect.util.AuthUtils;

public class ChatFragment extends Fragment implements View.OnClickListener {

    private ImageButton btnSend;
    private EditText editWriteMessage;
    private String userName = null;
    private String roomId = null;
    private ListView listView = null;
    private ArrayAdapter<String> itemsAdapter;

    public static final String USER_NAME = "user_name";
    public static final String ROOM_ID = "room_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            userName = bundle.getString(USER_NAME);
            roomId = bundle.getString(ROOM_ID);
        }
    }

    public static ChatFragment newInstance(String user_name, String room_id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME, user_name);
        bundle.putString(ROOM_ID, room_id);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_chat, container, false);
        listView = (ListView) view.findViewById(R.id.lv_id);

        btnSend = (ImageButton) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setBackgroundResource(R.drawable.ic_send);

        editWriteMessage = (EditText) view.findViewById(R.id.editWriteMessage);


        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        itemsAdapter.clear();

        listView = (ListView)view.findViewById(R.id.listChat);
        listView.setAdapter(itemsAdapter);

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
                newMessage.text = content;
                newMessage.idSender = AuthUtils.getInstance().getCurrentUserDisplayName();
                newMessage.idReceiver = "";
                newMessage.timestamp = System.currentTimeMillis();
                FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).push().setValue(newMessage);

            }
        }

    }
}
