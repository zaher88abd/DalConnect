package ca.connect.dal.dalconnect.chat;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.UserInformation;
import ca.connect.dal.dalconnect.util.FileStorageUtils;
import ca.connect.dal.dalconnect.util.PortraitUtils;

/**
 * Created by gaoyounan on 2018/3/8.
 */

public class UserListAdapter extends BaseAdapter
{
    private List<UserInformation> user_list;
    private PortraitUtils portraitInstance;

    private Map<Integer, Integer> unReadMessageMap = new HashMap<Integer, Integer>();

    public UserListAdapter() {}

    public UserListAdapter(List<UserInformation> user_list)
    {
        super();
        this.user_list = user_list;

        portraitInstance = PortraitUtils.getInstance();

    }

    @Override
    public int getCount() {
        return user_list.size();
    }

    @Override
    public Object getItem(int i) {
        return user_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.listview_item, null);
        }


        UserInformation user = (UserInformation) getItem(i);

        ImageView image_portrait = (ImageView) convertView.findViewById(R.id.id_image);
        TextView tv_username = (TextView) convertView.findViewById(R.id.id_username);
        TextView tv_country = (TextView) convertView.findViewById(R.id.id_country);
        TextView tv_unRead_message = (TextView) convertView.findViewById(R.id.id_message_numbers);
        tv_unRead_message.setText("");

        ListView listView = (ListView) viewGroup;

        Integer num = unReadMessageMap.get(i);

        if(num != null)
        {
            if(num > 0)
            {
                tv_unRead_message.setText(""+num);
            }
            else
            {
                tv_unRead_message.setText("");
            }
        }

        tv_username.setText(user.getUsername());
        tv_country.setText(user.getCountry());

        image_portrait.setTag(user.getUsername());

        String portrait_name = user.getUserImage();

        if(portrait_name != null && !"".equals(portrait_name))
        {
            BitmapDrawable bitmapDrawableTemp = portraitInstance.getPortraitByName(portrait_name);

            if(bitmapDrawableTemp != null)
            {
                image_portrait.setImageDrawable(bitmapDrawableTemp);
            }
            else
            {
                FileStorageUtils.getInstance().loadImage(portrait_name , user.getUsername(), viewGroup);
            }
        }

        return convertView;
    }

    public void putUnReadMessageMap(Integer index, Integer num)
    {
        unReadMessageMap.put(index, num);
    }

    public Integer getUnReadMessageNumByIndex(Integer index)
    {
        return unReadMessageMap.get(index);
    }
}