package ca.connect.dal.dalconnect;

        import android.app.AlertDialog;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.rengwuxian.materialedittext.MaterialEditText;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.UUID;

        import ca.connect.dal.dalconnect.Adapter.ListItemAdapter;
        import ca.connect.dal.dalconnect.Model.ToDo;
        import dmax.dialog.SpotsDialog;

public class TodoActivity extends AppCompatActivity {

    List<ToDo> todoList = new ArrayList<>();

    FirebaseFirestore db;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;

    public MaterialEditText title, detail;

    ListItemAdapter adapter;
    public boolean isUpdate = false;
    public String idUpdate = "";

    FloatingActionButton btn;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        db = FirebaseFirestore.getInstance();
        dialog = new SpotsDialog(this);
        title = (MaterialEditText) findViewById(R.id.title);
        detail = (MaterialEditText) findViewById(R.id.detail);
        btn = (FloatingActionButton) findViewById(R.id.add);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUpdate){
                    setData(title.getText().toString(), detail.getText().toString());
                }
                else
                {
                    updateData(title.getText().toString(), detail.getText().toString());
                    isUpdate = !isUpdate;
                }
            }
        });

        listItem = (RecyclerView) findViewById(R.id.todolist);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);
        listItem.setAdapter(adapter);

        loadData();

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Delete")){
            deleteItem(item.getOrder());
        }else if(item.getTitle().equals("Set Reminder")){

        }
        return super.onContextItemSelected(item);

    }

    private void deleteItem(int index) {
        db.collection("ToDoList")
                .document(todoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }

    private void updateData(String title, String detail) {
        db.collection("ToDoList").document(idUpdate)
                .update("title", title, "detail", detail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TodoActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("ToDoList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });

    }

    private void setData(String title, String detail) {
        String id = UUID.randomUUID().toString();
        Map<String,Object> todo = new HashMap<>();
        todo.put("id",id);
        todo.put("title",title);
        todo.put("detail",detail);

        db.collection("TodoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadData();
            }
        });
    }

    private void loadData() {
        dialog.show();
        if(todoList.size() > 0)
            todoList.clear();
        db.collection("TodoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc:task.getResult()){
                            ToDo todo = new ToDo(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("detail"));
                            todoList.add(todo);
                        }
                        adapter = new ListItemAdapter(TodoActivity.this, todoList);
                        listItem.setAdapter(adapter);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TodoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
