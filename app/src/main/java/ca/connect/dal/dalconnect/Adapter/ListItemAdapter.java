package ca.connect.dal.dalconnect.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.connect.dal.dalconnect.Model.Todo;
import ca.connect.dal.dalconnect.R;
import ca.connect.dal.dalconnect.TodoActivity;

/**
 * Created by Jesuseyi Fasuyi on 3/19/2018.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
        {
        ClickListener clickListener;

public TextView txtTitle, txtDetails;

public ListItemViewHolder(View v){
        super(v);
        v.setOnClickListener(this);
        v.setOnCreateContextMenuListener(this);

        txtTitle = v.findViewById(R.id.task_title);
        txtDetails = v.findViewById(R.id.task_details);
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

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>  {

    TodoActivity todoActivity;
    private List<Todo> todoList;

    public ListItemAdapter(TodoActivity todoActivity, List<Todo> todoList) {
        this.todoActivity = todoActivity;
        this.todoList = todoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(todoActivity.getBaseContext());
        View v = inflater.inflate(R.layout.list_item,parent,false);
        return new ListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        holder.txtTitle.setText(todo.getTitle());
        holder.txtDetails.setText(todo.getDetails());

        holder.setClickListener(new ClickListener(){
            @Override
            public void onClick(View v, int position, boolean longTouch) {
                todoActivity.titleEditText.setText(todoList.get(position).getTitle());
                todoActivity.detailEditText.setText(todoList.get(position).getDetails());

                todoActivity.isUpdate = true;
                todoActivity.curPosition = position;
                todoActivity.idUpdate = todoList.get(position).getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
