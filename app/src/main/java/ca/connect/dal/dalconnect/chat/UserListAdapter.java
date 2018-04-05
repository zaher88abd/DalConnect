package ca.connect.dal.dalconnect.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by gaoyounan on 2018/3/8.
 */

public class UserListAdapter extends BaseAdapter
{
    private List<UserInformation> user_list;
    private FileStorageUtils fileStorageUtils;

    private Context mContext;
    private Map<Integer, Integer> unReadMessageMap = new HashMap<Integer, Integer>();
    public UserListAdapter() {}

    public UserListAdapter(List<UserInformation> user_list, Context context)
    {
        super();
        this.user_list = user_list;
        mContext = context;
        fileStorageUtils = FileStorageUtils.getInstance(context.getCacheDir());

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

        image_portrait.setTag(user.getUID());

        String portrait_name = user.getUserImage();

        if(portrait_name != null && !"".equals(portrait_name))
        {
            Bitmap bitmap = fileStorageUtils.getPortraitByName(portrait_name);

            if(bitmap != null)
            {
                image_portrait.setImageBitmap(bitmap);
            }
            else
            {
                fileStorageUtils.loadImage(portrait_name , user.getUID(), viewGroup, mContext.getResources());
            }
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon);
            image_portrait.setImageBitmap(bitmap);
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
