package ca.connect.dal.dalconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.connect.dal.dalconnect.model.User;

/**
 * Created by gaoyounan on 2018/3/8.
 */

public class UserListAdapter extends BaseAdapter
{
    private List<User> user_list;
    private LayoutInflater inflater;

    public UserListAdapter() {}

    public UserListAdapter(List<User> user_list ,Context context)
    {
        super();
        this.user_list = user_list;
        this.inflater=LayoutInflater.from(context);
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

        //加载布局为一个视图
        View view = inflater.inflate(R.layout.listview_item,null);
        User user = (User) getItem(i);
        //在view视图中查找id为image_photo的控件
        ImageView image_portrait = (ImageView) view.findViewById(R.id.id_image);
        TextView tv_username = (TextView) view.findViewById(R.id.id_username);
        TextView tv_email = (TextView) view.findViewById(R.id.id_email);

        image_portrait.setImageResource(R.drawable.ic_launcher);

        tv_username.setText(user.getUser_name());
        tv_email.setText(user.getEmail() + "(" + user.getCountry() + ")");
        return view;
    }

}
