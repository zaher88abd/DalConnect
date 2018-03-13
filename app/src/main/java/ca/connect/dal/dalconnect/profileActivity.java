package ca.connect.dal.dalconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class profileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewUserEmail;
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private DatabaseReference databaseReference;

    private EditText editTextUsername,editTextDalid,editTextCountry,editTextProgram,editTextPhonenumber,
            editTextAddress,editTextDate;
    private Button buttonSave, buttonChooseImage, buttonUploadImage;

    private ImageView userProfilePicture;

    private Spinner startTermSpinner;

    private String startTerm;

    private Preferences pref;

    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this,loginActivity.class)); //profile activity here
        }
         databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        setUpViews();
        setUpListeners();

        textViewUserEmail.setText("Welcome, " +user.getEmail());
    }

    private void setUpViews(){
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextDalid = (EditText) findViewById(R.id.editTextDalid);
        editTextCountry = (EditText) findViewById(R.id.editTextCountry);
        editTextProgram = (EditText) findViewById(R.id.editTextProgram);
        editTextPhonenumber = (EditText) findViewById(R.id.editTextPhonenumber);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonChooseImage = (Button) findViewById(R.id.chooseImageButton);
        buttonUploadImage = (Button) findViewById(R.id.uploadButton);

        userProfilePicture = (ImageView) findViewById(R.id.userProfilePicture);

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        pref = new Preferences(profileActivity.this);

        startTermSpinner = (Spinner) findViewById(R.id.start_term_spinner);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.start_term_array));

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        startTermSpinner.setAdapter(dataAdapter);
    }

    private void setUpListeners(){
        startTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                startTerm = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    private void saveUserInformation(){
        String Username = editTextUsername.getText().toString().trim();
        String Dalid =  editTextDalid.getText().toString().trim();
        String Country = editTextCountry.getText().toString().trim();
        String Program = editTextProgram.getText().toString().trim();
        String Phonenumber= editTextPhonenumber.getText().toString().trim();
        String Address = editTextAddress .getText().toString().trim();
        String Date = editTextDate.getText().toString().trim();

        UserInformation userInformation = new UserInformation(Username,Dalid,Country,Program,startTerm,Phonenumber,Address,Date);

        FirebaseUser user =firebaseAuth.getCurrentUser();
        databaseReference.child("/users").child(user.getUid()).setValue(userInformation);

        // For shared preferences
        UserInformation userInfo = new UserInformation();
        userInfo.setUsername(Username);
        userInfo.setAddress(Address);
        userInfo.setEmail(user.getEmail());
        userInfo.setPhonenumber(Phonenumber);
        userInfo.setProgram(Program);
        userInfo.setStartTerm(startTerm);
        userInfo.setDalid(Dalid);
        userInfo.setDate(Date);
        userInfo.setCountry(Country);


        System.out.println("Country before pref: " + Country);
        System.out.println("StartTerm before pref: " + Country);

        pref.saveUserDetails(userInfo);

        UserInformation testUserInfo = pref.getUserDetails();
        System.out.println("Country after pref: " + testUserInfo.getCountry());
        System.out.println("StartTerm after pref: " + testUserInfo.getStartTerm());

        Toast.makeText(this, "INFORMATION SAVED",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(profileActivity.this, NavigationActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view){

        if (view == buttonLogout) {
           firebaseAuth.signOut();
           finish();
           startActivity(new Intent(this,loginActivity.class));
            ///login activity
        }

        if(view == buttonSave){
            saveUserInformation();
           // startActivity(new Intent(this, MatchActivity.class));
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userProfilePicture.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(profileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(profileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
