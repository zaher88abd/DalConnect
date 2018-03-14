package ca.connect.dal.dalconnect.util;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by gaoyounan on 2018/2/10.
 */

public class AuthUtils {

    private static AuthUtils instance;
    private static FirebaseAuth mAuth = null;
    private static DatabaseReference mDatabase;


    private AuthUtils() {}

    public String generateRoomId(String a, String b)
    {
        String id = null;
        a = a.toLowerCase();
        b = b.toLowerCase();
        if(a.compareTo(b) > 0)
        {
            id = b + "-" + a;
        }
        else
        {
            id = a + "-" + b;
        }
        return id;
    }

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

    public FirebaseUser getCurrentUser()
    {
        return mAuth.getCurrentUser();
    }

    public String getCurrentUserDisplayName()
    {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null)
        {
            return null;
        }

        if(user.getDisplayName()!=null)
        {
            return user.getDisplayName();
        }

        String email = user.getEmail();
        return email.split("@")[0];
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