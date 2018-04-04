package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.net.Uri;
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

public class MessageAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<MessageData> chatMessageList;
    Context mContext;

    public MessageAdapter(Activity activity, ArrayList<MessageData> messageDataArrayList) {
        mContext = activity;
        chatMessageList = messageDataArrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void add(MessageData object) {
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
    public View getView(int i, View view, ViewGroup parent) {
        final MessageData message = (MessageData) chatMessageList.get(i);
        View vi = view;

        if (view == null)
            vi = inflater.inflate(R.layout.activity_layout, null);

        TextView txtMsg = (TextView) vi.findViewById(R.id.txtMessage);
        txtMsg.setText(message.messageBody);

        Button btnFunction = vi.findViewById(R.id.btn_function);

        LinearLayout linearLayout = (LinearLayout) vi.findViewById(R.id.message_layout);
        LinearLayout linearLayoutParent = (LinearLayout) vi.findViewById(R.id.message_layout_parent);

        // checks if action is from User then align the message to the right
        if (message.isUser) {
            linearLayout.setGravity(Gravity.END);
            txtMsg.setBackgroundResource(R.drawable.outgoing_message);
            linearLayoutParent.setGravity(Gravity.END);
        }
        // checks if action is from the server and align the message to the left
        else {
            if (message.extraLink.contains("map")) {
                btnFunction.setVisibility(View.VISIBLE);
                btnFunction.setText(mContext.getString(R.string.direction));
                btnFunction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(message.extraLink));
                        mContext.startActivity(Intent.createChooser(intent, "Select an application"));
                    }
                });
                txtMsg.setVisibility(View.GONE);
            }  if (message.extraLink.contains("https://")) {
                btnFunction.setVisibility(View.VISIBLE);
                txtMsg.setVisibility(View.GONE);
                btnFunction.setText(mContext.getString(R.string.link));

                btnFunction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(message.extraLink));
                        mContext.startActivity(browserIntent);
                    }
                });
            }
            linearLayout.setGravity(Gravity.START);
            txtMsg.setBackgroundResource(R.drawable.incoming_message);
            linearLayoutParent.setGravity(Gravity.START);
        }
        return vi;
    }
}
