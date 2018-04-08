package ca.connect.dal.dalconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

class ChatViewHlder extends RecyclerView.ViewHolder {

    Context mContext;

    public ChatViewHlder(View itemView,Context mComtext) {
        super(itemView);
        this.mContext =mComtext;
    }

    public void bindMessage(final MessageData  message) {
        TextView txtMsg = itemView.findViewById(R.id.txtMessage);
        txtMsg.setText(message.messageBody);

        Button btnFunction = itemView.findViewById(R.id.btn_function);

        LinearLayout linearLayout =  itemView.findViewById(R.id.message_layout);
        LinearLayout linearLayoutParent =  itemView.findViewById(R.id.message_layout_parent);

        // checks if action is from User then align the message to the right
        if (message.isUser) {
            linearLayout.setGravity(Gravity.END);
            btnFunction.setVisibility(View.GONE);
            txtMsg.setBackgroundResource(R.drawable.outgoing_message);
            linearLayoutParent.setGravity(Gravity.END);
        }
        // checks if action is from the server and align the message to the left
        else {
            linearLayout.setGravity(Gravity.START);
            btnFunction.setVisibility(View.GONE);
            txtMsg.setBackgroundResource(R.drawable.incoming_message);
            linearLayoutParent.setGravity(Gravity.START);
            if (!message.extraLink.isEmpty()) {
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
                }
                if (message.extraLink.contains("https://")) {
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
                    txtMsg.setVisibility(View.GONE);
                }
            }
        }
    }
}
