package ca.connect.dal.dalconnect;

<<<<<<< HEAD
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
=======
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> seyieroz

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private Button btnUserA, btnUserB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        btnUserA = findViewById(R.id.buttonUserA);

        btnUserA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("user_name","UserA");
                intent.putExtra("email","33@qq.com");
                intent.putExtra("password","123456");
                intent.putExtra("room_id","1");

                MainActivity.this.finish();
                startActivity(intent);

            }
        });

        btnUserB = findViewById(R.id.buttonUserB);

        btnUserB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("user_name","UserB");
                intent.putExtra("email","22@qq.com");
                intent.putExtra("password","123456");
                intent.putExtra("room_id","1");

                MainActivity.this.finish();
                startActivity(intent);

            }
        });





=======

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(),profileActivity.class)); //profile activity here
        }


        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty (email)){
            Toast.makeText(this, "Please enter email",Toast.LENGTH_SHORT).show();
            //stop excuting fuctions
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password",Toast.LENGTH_SHORT).show();
            //stop executing fuctions
            return;
        }
        //validation okay

        progressDialog.setMessage("Registering User......");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           finish();
                           startActivity(new Intent(getApplicationContext(),profileActivity.class)); //profile activity here


                       } else {
                           Toast.makeText(MainActivity.this, "failed to register",Toast.LENGTH_SHORT).show();
                           progressDialog.cancel();
                       }
                    }
                });

    }

    @Override
    public void onClick(View view){
        if(view == buttonRegister){
            registerUser();
        }

        if (view == textViewSignin) {
            startActivity(new Intent(this,loginActivity.class));

        }
>>>>>>> seyieroz
    }
}
