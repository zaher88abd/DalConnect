package ca.connect.dal.dalconnect.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.chat.model.Message;
import ca.connect.dal.dalconnect.util.AuthUtils;
import ca.connect.dal.dalconnect.util.FileStorageUtils;

import static android.content.Context.INPUT_METHOD_SERVICE;

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
    private Map<String, Bitmap> bitMapMemoryCache = new HashMap<String, Bitmap>();

    public static final String USER_NAME = "user_name";
    public static final String ROOM_ID = "room_id";
    public static final String USER_PORTRAIT = "user_portrait";
    public static final String UID_STR = "uid";

    private ChildEventListener childEventListener;
    private FileStorageUtils fileStorageUtils = null;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    chatListAdapter = new ChatListAdapter(mContext, bitMapMemoryCache);
                    listView.setAdapter(chatListAdapter);
                    onReceiveMessages();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        if(childEventListener!=null)
        {
            FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).removeEventListener(childEventListener);
        }

        if(editWriteMessage != null)
        {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editWriteMessage.getWindowToken(), 0);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            uid = bundle.getString(UID_STR);
            userName = bundle.getString(USER_NAME);
            userPortrait = bundle.getString(USER_PORTRAIT);
            roomId = bundle.getString(ROOM_ID);
        }

        fileStorageUtils = FileStorageUtils.getInstance(getContext().getCacheDir());
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
        childEventListener = FirebaseDatabase.getInstance().getReference().child("Dal_Chat/message/" + roomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();

                    Message messenge = dataSnapshot.getValue(Message.class);
                    chatListAdapter.addMessage(messenge);

                    if(messenge.getIdReceiver().equals(AuthUtils.getInstance().getCurrentUId()))
                    {
                        StringBuffer path_str = new StringBuffer("Dal_Chat/message/");
                        path_str.append(roomId);
                        path_str.append('/');
                        path_str.append(dataSnapshot.getKey());
                        path_str.append('/');
                        path_str.append("read");

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(path_str.toString(), true);
                        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_chat, container, false);
        listView = (ListView)view.findViewById(R.id.listChat);

        btnSend = (ImageButton) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnSend.setBackgroundResource(R.drawable.ic_send);

        editWriteMessage = (EditText) view.findViewById(R.id.editWriteMessage);
        getActivity().setTitle(userName);

        Bitmap bitmapTemp = fileStorageUtils.getPortraitByName(uid);
        if(bitmapTemp != null)
        {
            bitMapMemoryCache.put(uid, bitmapTemp);
        }
        else
        {
            fileStorageUtils.loadImage(uid, bitMapMemoryCache, mHandler, getResources());
        }

        String currentUserId = AuthUtils.getInstance().getCurrentUId();
        bitmapTemp = fileStorageUtils.getPortraitByName(currentUserId);
        if(bitmapTemp != null)
        {
            bitMapMemoryCache.put(currentUserId, bitmapTemp);
        }
        else
        {
            fileStorageUtils.loadImage(currentUserId, bitMapMemoryCache, mHandler, getResources());
        }

        mHandler.obtainMessage(bitMapMemoryCache.size()).sendToTarget();

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                editWriteMessage.setText("");
                Message newMessage = new Message();
                newMessage.setRead(false);
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
