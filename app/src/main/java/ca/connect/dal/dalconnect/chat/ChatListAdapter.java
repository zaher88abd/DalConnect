package ca.connect.dal.dalconnect.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.chat.model.Message;
import ca.connect.dal.dalconnect.util.AuthUtils;
import ca.connect.dal.dalconnect.util.FileStorageUtils;

/**
 * Created by gaoyounan on 2018/3/19.
 */

public class ChatListAdapter extends BaseAdapter
{
    private List<Message> messagelist;
    private FileStorageUtils fileStorageUtils;
    private Map<String, Bitmap> bitMapMemoryCache;

    public void addMessage(Message item) {
        messagelist.add(item);
        notifyDataSetChanged();
    }

    public ChatListAdapter(Context context, Map<String, Bitmap> bitMapMemoryCache) {
        this.messagelist = new ArrayList<Message>();
        fileStorageUtils = FileStorageUtils.getInstance(context.getCacheDir());
        this.bitMapMemoryCache = bitMapMemoryCache;
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

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        java.util.Date date = new Date(message.getTimestamp());
        String str = sdf.format(date);

        if(idSender.equals(AuthUtils.getInstance().getCurrentUId()))
        {
            //myself
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.chat_list_view_items_right, null);

            ImageView my_portrait = (ImageView) view.findViewById(R.id.id_image_myself);
            TextView chat_text = (TextView) view.findViewById(R.id.id_chat_myself);
            TextView time_text_my = (TextView) view.findViewById(R.id.id_chat_time);
            time_text_my.setText(str);
            chat_text.setText(message.getText());

            my_portrait.setImageBitmap(bitMapMemoryCache.get(message.getSenderPortrait()));
        }
        else
        {
            //other
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.chat_list_view_items_left, null);
            ImageView other_portrait = (ImageView) view.findViewById(R.id.id_image_other);
            TextView chat_text = (TextView) view.findViewById(R.id.id_chat_text_other);

            TextView time_text_other = (TextView) view.findViewById(R.id.id_chat_time_other);
            time_text_other.setText(str);
            chat_text.setText(message.getText());

            other_portrait.setImageBitmap(bitMapMemoryCache.get(message.getSenderPortrait()));
        }

        return view;

    }
}
