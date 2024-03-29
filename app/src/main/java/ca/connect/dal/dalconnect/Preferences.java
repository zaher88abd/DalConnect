package ca.connect.dal.dalconnect;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Seyi Eroz on 19/02/2018.
 */

public class Preferences {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Constructor
    public Preferences(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("Pref", 0);
        editor = pref.edit();
    }

    public void saveUserDetails(UserInformation user){
        // Storing User details in shared preferences

        editor.putString("Username", user.getUsername());
        editor.putString("Email", user.getEmail());
        editor.putString("Country", user.getCountry());
        editor.putString("Address", user.getAddress());
        editor.putString("Dalid", user.getDalid());
        editor.putString("Date", user.getDate());
        editor.putString("PhoneNumber", user.getPhonenumber());
        editor.putString("Program", user.getProgram());
        editor.putString("StartTerm", user.getStartTerm());
        editor.putString("UserImage", user.getUserImage());

        // commit changes
        editor.commit();
    }

    public UserInformation getUserDetails(){
        UserInformation user = new UserInformation();

        user.setUsername(pref.getString("Username", null));
        user.setEmail(pref.getString("Email", null));
        user.setCountry(pref.getString("Country", null));
        user.setAddress(pref.getString("Address", null));
        user.setDalid(pref.getString("Dalid", null));
        user.setDate(pref.getString("Date", null));
        user.setPhonenumber(pref.getString("PhoneNumber", null));
        user.setProgram(pref.getString("Program", null));
        user.setStartTerm(pref.getString("StartTerm", null));
        user.setUserImage(pref.getString("UserImage", null));

        return user;
    }

    public void setPortraitId(String portraitId){
        editor.putString("PortraitId", portraitId);

        // commit changes
        editor.commit();
    }

    public String getPortraitId(){
        return pref.getString("PortraitId", null);
    }

    public void setIsFirstTime(boolean isFirstTime){
        editor.putBoolean("IsFirstTime", isFirstTime);

        // commit changes
        editor.commit();
    }

    public boolean getIsFirstTime(){
        return pref.getBoolean("IsFirstTime", false);
    }
}
