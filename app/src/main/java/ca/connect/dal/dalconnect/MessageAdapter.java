package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;

/**
 * Created by hugokwe on 3/4/2018.
 * This binds the data to the listview. It calls the MessageData  object, then boolean function
 * checks if the action called is a request or response using the boolean.
 */

public class MessageAdapter extends RecyclerView.Adapter<ChatViewHlder> {
    private static LayoutInflater inflater = null;
    ArrayList<MessageData> chatMessageList;
    Context mContext;

    public MessageAdapter(Activity activity, ArrayList<MessageData> messageDataArrayList) {
        mContext = activity;
        chatMessageList = messageDataArrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public ChatViewHlder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout, parent, false);
        return new ChatViewHlder(view,mContext);
    }

    @Override
    public void onBindViewHolder(ChatViewHlder holder, int position) {
        holder.bindMessage(chatMessageList.get(position));
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}
