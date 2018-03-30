package ca.connect.dal.dalconnect.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.connect.dal.dalconnect.Model.ToDo;
import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.TodoActivity;

/**
 * Created by Jesuseyi Fasuyi on 3/19/2018.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
{
    ClickListener clickListener;


    TextView i_title, i_detail;

    public ListItemViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        i_title = (TextView) itemView.findViewById(R.id.task_title);
        i_detail = (TextView) itemView.findViewById(R.id.task_details);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public void onClick(View view) {
        clickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
        contextMenu.add(0,0,getAdapterPosition(),"Set Reminder");

    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{

   TodoActivity mainActivity;
    List<ToDo> todoList;

    public ListItemAdapter(TodoActivity mainActivity, List<ToDo> todoList) {
        this.mainActivity = mainActivity;
        this.todoList = todoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity.getBaseContext());
        View v = inflater.inflate(R.layout.list_item,parent,false);
        return new ListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        holder.i_title.setText(todoList.get(position).getTitle());
        holder.i_detail.setText(todoList.get(position).getDetailsT());

        holder.setClickListener(new ClickListener(){
            @Override
            public void onClick(View v, int position, boolean longTouch) {
                mainActivity.title.setText(todoList.get(position).getTitle());
                mainActivity.detail.setText(todoList.get(position).getDetailsT());

                mainActivity.isUpdate = true;
                mainActivity.idUpdate = todoList.get(position).getId();
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}

