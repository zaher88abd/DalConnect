package ca.connect.dal.dalconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import ca.connect.dal.dalconnect.model.User;
import ca.connect.dal.dalconnect.util.FileStorageUtils;
import android.support.v4.util.LruCache;

/**
 * Created by gaoyounan on 2018/3/8.
 */

public class UserListAdapter extends BaseAdapter
{
    private List<User> user_list;
    private LayoutInflater inflater;
    private LruCache<String, BitmapDrawable> mImageCache;

    public UserListAdapter() {}

    public UserListAdapter(List<User> user_list ,Context context)
    {
        super();
        this.user_list = user_list;
        this.inflater = LayoutInflater.from(context);

        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
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


        User user = (User) getItem(i);

        ImageView image_portrait = (ImageView) convertView.findViewById(R.id.id_image);
        TextView tv_username = (TextView) convertView.findViewById(R.id.id_username);
        TextView tv_email = (TextView) convertView.findViewById(R.id.id_email);

        tv_username.setText(user.getUser_name());
        tv_email.setText(user.getEmail() + "(" + user.getCountry() + ")");

        String portrait_name = user.getPortrait().split("\\.")[0];
        image_portrait.setTag(portrait_name);

        BitmapDrawable bitmapDrawableTemp = mImageCache.get(portrait_name);

        if(bitmapDrawableTemp != null)
        {
            image_portrait.setImageDrawable(bitmapDrawableTemp);
        }
        else
        {
            FileStorageUtils.getInstance().loadImage(portrait_name , viewGroup, mImageCache);
        }




        return convertView;
    }



}
