package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profileActivity extends AppCompatActivity implements View.OnClickListener {

    private  TextView textViewUserEmail;
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private DatabaseReference databaseReference;

    private EditText editTextUsername,editTextDalid,editTextCountry,editTextProgram,editTextPhonenumber,editTextAddress,editTextDate;
    private Button buttonSave;

    private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this,loginActivity.class)); //profile activity here
        }
         databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextDalid = (EditText) findViewById(R.id.editTextDalid);
        editTextCountry = (EditText) findViewById(R.id.editTextCountry);
        editTextProgram = (EditText) findViewById(R.id.editTextProgram);
        editTextPhonenumber = (EditText) findViewById(R.id.editTextPhonenumber);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        buttonSave = (Button) findViewById(R.id.buttonSave);





        pref = new Preferences(profileActivity.this);


        FirebaseUser user =firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome" +user.getEmail());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

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

        UserInformation userInformation = new UserInformation(Username,Dalid,Country,Program,Phonenumber,Address,Date);

        FirebaseUser user =firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userInformation);

        // For shared preferences
        UserInformation userInfo = new UserInformation();
        userInfo.setUsername(Username);
        userInfo.setAddress(Address);
        userInfo.setEmail(user.getEmail());
        userInfo.setPhonenumber(Phonenumber);
        userInfo.setProgram(Program);
        userInfo.setDalid(Dalid);
        userInfo.setDate(Date);
        userInfo.setCountry(Country);


        System.out.println("Country before pref: " + Country);

        pref.saveUserDetails(userInfo);

        UserInformation testUserInfo = pref.getUserDetails();
        System.out.println("Country after pref: " + testUserInfo.getCountry());


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
}
