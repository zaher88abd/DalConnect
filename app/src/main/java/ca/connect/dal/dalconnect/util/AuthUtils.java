package ca.connect.dal.dalconnect.util;


import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ca.connect.dal.dalconnect.model.User;


/**
 * Created by gaoyounan on 2018/2/10.
 */

public class AuthUtils {

    private static AuthUtils instance;
    private static FirebaseAuth mAuth = null;
    private static DatabaseReference mDatabase;


    private AuthUtils() {}

    public static AuthUtils getInstance()
    {
        if(instance==null)
        {
            instance = new AuthUtils();
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return instance;
    }


    /**
     * Action Login
     *
     * @param email
     * @param password
     */
    public void signIn(String email, String password, Activity activity) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful())
                        {

                            System.out.println("Login Failure");

                        } else {

                            System.out.println("Login Success");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        System.out.println("Login Exceptional");

                    }
                });
    }

    public void createUser(String email, String password, Activity activity) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful())
                        {

                            System.out.println("Create Failure");

                        } else {

                            System.out.println("Create Success");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        System.out.println("Create Exceptional");

                    }
                });
    }


}