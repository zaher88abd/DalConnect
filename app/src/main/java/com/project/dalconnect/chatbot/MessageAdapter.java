package com.project.dalconnect.chatbot;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;

/**
 * Created by hugokwe on 3/4/2018.
 * This binds the data to the listview. It calls the ChatData  object, then boolean function checks if the action called is a request or response using the boolean.
 */

public class MessageAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<MessageData> chatMessageList;
    Context mContext;

    public MessageAdapter(Activity activity, ArrayList<MessageData> messageDataArrayList) {
        mContext = activity;
        chatMessageList = messageDataArrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void add(MessageData object){
        chatMessageList.add(object);
    }
    @Override
    public int getCount() {

        return chatMessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        MessageData message = (MessageData) chatMessageList.get(i);
        View vi = convertView;

        if(convertView == null)
            vi = inflater.inflate(R.layout.activity_layout, null);

        TextView txtMsg = (TextView) vi.findViewById(R.id.txtMessage);
        txtMsg.setText(message.messageBody);

        LinearLayout linearLayout = (LinearLayout) vi.findViewById(R.id.message_layout);
        LinearLayout linearLayoutParent = (LinearLayout) vi.findViewById(R.id.message_layout_parent);

        // checks if action is from User and align the message to the right
        if (message.isUser) {
            System.out.println("Yes This is a User++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+message.isUser);
            linearLayout.setGravity(Gravity.RIGHT);
            txtMsg.setBackgroundResource(R.drawable.outgoing_message);
            linearLayoutParent.setGravity(Gravity.RIGHT);
        }
        // checks if action is from google and align the message to the left
        else {
            linearLayout.setGravity(Gravity.LEFT);
            txtMsg.setBackgroundResource(R.drawable.incoming_message);
            linearLayoutParent.setGravity(Gravity.LEFT);
        }
        return vi;
    }
}
