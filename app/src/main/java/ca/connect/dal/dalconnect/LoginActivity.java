package ca.connect.dal.dalconnect;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private Button buttonForgotPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), NavigationActivity.class)); //profile activity here
        }

        setUpViews();
        setUpListeners();
        setUpValidationCheckers();

    }

    private void setUpViews() {
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonForgotPassword = (Button) findViewById(R.id.btn_forgot_password);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);

        progressDialog = new ProgressDialog(LoginActivity.this);
    }

    private void setUpListeners() {
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpValidationCheckers() {
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

    }

    private void userLogin() {
        try {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                //stop excuting fuctions
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                //stop executing fuctions
                return;
            }
            progressDialog.setMessage("Registering User......");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                //uder registred succcefulluy\
                                finish();
                                startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                            } else {
                                showErrorDialog();
                            }

                        }

                    });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            userLogin();
        }

        if (view == textViewSignup) {
            finish();
            startActivity(new Intent(this, RegistrationActivity.class));

            ///login activity
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

    private boolean validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("User Not Found")
                .setMessage("Email/Password is incorrect")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
