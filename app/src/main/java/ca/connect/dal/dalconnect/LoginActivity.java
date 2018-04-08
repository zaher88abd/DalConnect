package ca.connect.dal.dalconnect;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), NavigationActivity.class)); //profile activity here
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        pref = new Preferences(LoginActivity.this);

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
            progressDialog.setMessage("Checking. Please wait......");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = task.getResult().getUser();
                                System.out.println("firebaseUser: " + firebaseUser);
                                System.out.println("firebaseUser UID: " + firebaseUser.getUid());

                                getAndSaveUserInfo(firebaseUser);

                                //login succcefulluy\
                                startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                                finish();
                            } else {
                                showErrorDialog();
                            }

                        }

                    });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void getAndSaveUserInfo(final FirebaseUser firebaseUser){
        try{
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
            databaseReference.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try{
                                //Get user info from firebase datasnapshot
                                String address = (String) dataSnapshot.child("address").getValue();
                                String country = (String) dataSnapshot.child("country").getValue();
                                String dalid = (String) dataSnapshot.child("dalid").getValue();
                                String date = (String) dataSnapshot.child("date").getValue();
                                String phoneNumber = (String) dataSnapshot.child("phonenumber").getValue();
                                String program = (String) dataSnapshot.child("program").getValue();
                                String startTerm = (String) dataSnapshot.child("startTerm").getValue();
                                //String userImage = (String) dataSnapshot.child("userImage").getValue();
                                String username = (String) dataSnapshot.child("username").getValue();
                                String email = firebaseUser.getEmail();

                                final UserInformation userInformation = new UserInformation();
                                userInformation.setAddress(address);
                                userInformation.setCountry(country);
                                userInformation.setDalid(dalid);
                                userInformation.setDate(date);
                                userInformation.setPhonenumber(phoneNumber);
                                userInformation.setProgram(program);
                                userInformation.setStartTerm(startTerm);
                                userInformation.setUsername(username);
                                userInformation.setEmail(email);


                                String portraitId = firebaseAuth.getCurrentUser().getUid();

                                StorageReference ref = storageReference.child("Portraits/" + portraitId);

                                Log.i("TAG", "portraitId: " + portraitId);
                                Log.i("TAG", "ref: " + ref);
                                Log.i("TAG", "ref.getDownloadUrl(): " + ref.getDownloadUrl());

                                try {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Local temp file has been created

                                            Log.i("TAG", "uri: " + uri);

                                            userInformation.setUserImage(uri.toString());


                                            Log.i("TAG", "before pref");
                                            Log.i("TAG", "userInfo.getUserImage(): " +  userInformation.getUserImage());
                                            Log.i("TAG", "userInfo.getDate(): " +  userInformation.getDate());

                                            //Save User info into shared preferences
                                            pref.saveUserDetails(userInformation);

                                            Log.i("TAG", "after pref");

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            userLogin();
        }

        if (view == textViewSignup) {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();

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
