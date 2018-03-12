package ca.connect.dal.dalconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Seyi Eroz on 10/03/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    public List<UserInformation> userList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName;
        public TextView program;
        public ImageView userImage;
        public RelativeLayout relativeLayout_listitem;

        public MyViewHolder(View view) {
            super(view);
            fullName = (TextView) view.findViewById(R.id.full_name);
            program = (TextView) view.findViewById(R.id.program);
            userImage = (ImageView) view.findViewById(R.id.user_image);
            relativeLayout_listitem = (RelativeLayout) view.findViewById(R.id.relativeLayout_listitem);
        }
    }

    public UserListAdapter(Context context, List<UserInformation> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserInformation userInformation = userList.get(position);

        holder.fullName.setText(userInformation.getUsername() != null ? userInformation.getUsername() : "");
        holder.program.setText(userInformation.getProgram() != null ? userInformation.getProgram() : "");


        Drawable defaultDrawable = ContextCompat.getDrawable(context, R.drawable.icon);

        holder.userImage.setImageDrawable(defaultDrawable);

       /* try {
            if (userInformation.getUserImage() != null) {

                if (bitmap != null) {
                    holder.userCardImage.setImageBitmap(bitmap);
                } else {
                    holder.userCardImage.setImageDrawable(defaultDrawable);
                }
            } else {
                holder.userCardImage.setImageDrawable(defaultDrawable);
            }
        } catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

