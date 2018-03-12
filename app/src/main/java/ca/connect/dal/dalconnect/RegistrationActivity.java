package ca.connect.dal.dalconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(),profileActivity.class)); //profile activity here
        }

        setUpViews();
        setUpListeners();
        setUpValidationCheckers();

    }

    private void setUpViews(){
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
    }

    private void setUpListeners(){
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void setUpValidationCheckers(){
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emailFromEdittext = editTextEmail.getText().toString().trim();

                checkCorrectEmail(emailFromEdittext);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordFromEdittext = editTextPassword.getText().toString().trim();

                checkCorrectPassword(passwordFromEdittext);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                            Toast.makeText(RegistrationActivity.this, "failed to register",Toast.LENGTH_SHORT).show();
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
    }

    public void checkCorrectEmail(String emailEntered) {

        boolean isValidated;

        if (!validateEmail(emailEntered)) {
            isValidated = false;
            editTextEmail.setError("Please enter a valid email address");
            editTextEmail.requestFocus();
        } else {
            isValidated = true;
            editTextEmail.setError(null);
        }
    }

    public void checkCorrectPassword(String passwordEntered) {

        boolean isValidated;

        if (!validatePassword(passwordEntered)) {
            isValidated = false;
            editTextPassword.setError("Please enter a valid password");
            editTextPassword.requestFocus();
        } else {
            isValidated = true;
            editTextPassword.setError(null);
        }
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,15}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
