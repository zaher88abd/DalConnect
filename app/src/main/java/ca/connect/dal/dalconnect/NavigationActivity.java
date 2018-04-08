package ca.connect.dal.dalconnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import ca.connect.dal.dalconnect.chat.UserListFlagment;
import ca.connect.dal.dalconnect.util.FileStorageUtils;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView = null;
    Toolbar toolbar = null;


    private TextView userName;
    private TextView userEmail;
    private ImageView userImageView;

    private ProgressDialog progressDialog;

    private Preferences pref;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //  Set the fragment

        firebaseAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        UserListFlagment fragment = new UserListFlagment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, "UserListFlagment");
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        pref = new Preferences(NavigationActivity.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        userName = header.findViewById(R.id.user_name);
        userEmail = header.findViewById(R.id.user_email);
        userImageView = header.findViewById(R.id.user_image);

        setNavHeaderDetails();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{

            //add by gaoyounan, chatfragment back to friendlist
            FragmentManager fragManager = this.getSupportFragmentManager();
            int count = this.getSupportFragmentManager().getBackStackEntryCount();
            Fragment frag = fragManager.getFragments().get(count>0?count-1:count);

            if(frag.getTag().equals("ChatFragment"))
            {
                UserListFlagment fragment = new UserListFlagment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "UserListFlagment");
                fragmentTransaction.commit();
            }
            else
            {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            UserListFlagment fragment = new UserListFlagment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "UserListFlagment");
            fragmentTransaction.commit();


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, TodoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            try {
                preLogoutMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setNavHeaderDetails() {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               try {

                   UserInformation userInfo = pref.getUserDetails();

                   String user_name = userInfo.getUsername();
                   String user_email = userInfo.getEmail();
                   String user_image = userInfo.getUserImage();

                   System.out.println("user_name: " + user_name);
                   System.out.println("user_email: " + user_email);
                   System.out.println("user_image: " + user_image);

                   userName.setText(user_name);
                   userEmail.setText(user_email);

                   if(user_image != null && !user_image.equalsIgnoreCase("")){
                       //set glide
                       Glide.with(NavigationActivity.this).load(user_image).into(userImageView);
                   }

                   else{
                       // If theres no user image, show default image
                       Glide.with(NavigationActivity.this)
                               .load(getResources()
                                       .getIdentifier("icon", "drawable", getPackageName()))
                               .into(userImageView);

                   }

               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });
    }

    public void preLogoutMessage() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
            builder.setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new LogoutProcess().execute();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LogoutProcess extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Logging out.....");
        }

        @Override
        protected Integer doInBackground(String... param) {

            int result = 0;

            try {
                SharedPreferences settings = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                settings.edit().clear().apply();

                firebaseAuth.signOut();

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            startActivity(new Intent(NavigationActivity.this, LoginActivity.class));

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FileStorageUtils.getInstance(getCacheDir()).clearCacheFile();
    }
}
