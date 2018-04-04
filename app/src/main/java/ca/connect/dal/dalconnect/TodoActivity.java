package ca.connect.dal.dalconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.connect.dal.dalconnect.Adapter.ListItemAdapter;
import ca.connect.dal.dalconnect.Model.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import ca.connect.dal.dalconnect.R;
import dmax.dialog.SpotsDialog;

public class TodoActivity extends AppCompatActivity {

    public MaterialEditText titleEditText, detailEditText;
    private FloatingActionButton btnAdd;
    private RecyclerView myList;
    LinearLayoutManager llm;
    private ListItemAdapter myAdapter;

    private AlertDialog alertDialog;
    private List<Todo> todoList = new ArrayList<>();
    public boolean isUpdate = false;
    public String idUpdate = "";
    public int curPosition = -1;

    private static final String SHARED_PREFS_FILE = "myPref";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        titleEditText = (MaterialEditText) findViewById(R.id.title);
        detailEditText = (MaterialEditText) findViewById(R.id.detail);
        btnAdd = (FloatingActionButton) findViewById(R.id.add);

        myList = (RecyclerView) findViewById(R.id.todolist);
        myList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);

        myAdapter = new ListItemAdapter(TodoActivity.this, todoList);
        myList.setAdapter(myAdapter);

        alertDialog = new SpotsDialog(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleEditText.getText().toString().equals("") || detailEditText.getText().toString().equals("")) {
                    Toast.makeText(TodoActivity.this, "Please fill the input fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isUpdate){
                    saveData(titleEditText.getText().toString(), detailEditText.getText().toString());
                }
                else
                {
                    updateData(curPosition, titleEditText.getText().toString(), detailEditText.getText().toString());
                    isUpdate = false;
                }
                titleEditText.setText("");
                detailEditText.setText("");
            }
        });

        loadData();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(item.getTitle().equals("Delete")){
            deleteData(item.getOrder());
        }else if(item.getTitle().equals("Set Reminder")){
            setReminder(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    public void saveData(String title, String detail) {
        Todo todo = new Todo(title, detail);
        todoList.add(todo);

        myAdapter = new ListItemAdapter(TodoActivity.this, todoList);
        myList.setAdapter(myAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(todoList);

        editor.putString("TASKS", json);
        editor.commit();
    }

    public void updateData(int index, String title, String detail) {
        Todo todo = new Todo(title, detail);
        todoList.set(index, todo);

        myAdapter = new ListItemAdapter(TodoActivity.this, todoList);
        myList.setAdapter(myAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(todoList);

        editor.putString("TASKS", json);
        editor.commit();
    }

    private void loadData() {
        alertDialog.show();

        // load tasks from preference
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPrefs.getString("TASKS", null);
        Type type = new TypeToken<ArrayList<Todo>>() {}.getType();
        ArrayList<Todo> arrayList = gson.fromJson(json, type);

        if (arrayList != null)
            todoList = arrayList;

        myAdapter = new ListItemAdapter(TodoActivity.this, todoList);
        myList.setAdapter(myAdapter);

        alertDialog.dismiss();
    }

    private void setReminder(int position){
        TimeZone timeZone = TimeZone.getDefault();

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, todoList.get(position).getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, todoList.get(position).getDetails())
                .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                .putExtra(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void deleteData(int position){
        todoList.remove(position);

        myAdapter = new ListItemAdapter(TodoActivity.this, todoList);
        myList.setAdapter(myAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(todoList);

        editor.putString("TASKS", json);
        editor.commit();
    }

}
