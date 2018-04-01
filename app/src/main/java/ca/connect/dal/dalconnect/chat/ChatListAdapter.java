package ca.connect.dal.dalconnect.chat;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.chat.model.Message;
import ca.connect.dal.dalconnect.util.AuthUtils;
import ca.connect.dal.dalconnect.util.FileStorageUtils;
import ca.connect.dal.dalconnect.util.PortraitUtils;

/**
 * Created by gaoyounan on 2018/3/19.
 */

public class ChatListAdapter extends BaseAdapter
{
    private List<Message> messagelist;
    private PortraitUtils portraitInstance;

    public void addMessage(Message item) {
        messagelist.add(item);
        notifyDataSetChanged();
    }

    public ChatListAdapter() {
        this.messagelist = new ArrayList<Message>();
        portraitInstance = PortraitUtils.getInstance();
    }

    @Override
    public int getCount() {
        return messagelist.size();
    }

    @Override
    public Object getItem(int i) {
        return messagelist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Message message = (Message) getItem(i);
        String idSender = message.getIdSender();

        if(idSender.equals(AuthUtils.getInstance().getCurrentUId()))
        {
            //myself
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.chat_list_view_items_right, null);

            ImageView my_portrait = (ImageView) view.findViewById(R.id.id_image_myself);
            TextView chat_text = (TextView) view.findViewById(R.id.id_chat_myself);

            BitmapDrawable bitmapDrawable = PortraitUtils.getInstance().getPortraitByName(message.getSenderPortrait());

            if(bitmapDrawable != null)
            {
                my_portrait.setImageDrawable(PortraitUtils.getInstance().getPortraitByName(message.getSenderPortrait()));
            }
            else
            {
                FileStorageUtils.getInstance().loadImage(message.getSenderPortrait(), my_portrait);
            }


            chat_text.setText(message.getText());
        }
        else
        {
            //other
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.chat_list_view_items_left, null);
            ImageView other_portrait = (ImageView) view.findViewById(R.id.id_image_other);
            TextView chat_text = (TextView) view.findViewById(R.id.id_chat_text_other);

            BitmapDrawable bitmapDrawable = PortraitUtils.getInstance().getPortraitByName(message.getSenderPortrait());

            if(bitmapDrawable != null)
            {
                other_portrait.setImageDrawable(PortraitUtils.getInstance().getPortraitByName(message.getSenderPortrait()));
            }
            else
            {
                FileStorageUtils.getInstance().loadImage(message.getSenderPortrait(), other_portrait);
            }


            chat_text.setText(message.getText());
        }

        return view;

    }
}
