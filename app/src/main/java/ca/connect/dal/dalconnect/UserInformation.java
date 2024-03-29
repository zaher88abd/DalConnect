package ca.connect.dal.dalconnect;;import android.graphics.Bitmap;

/**
 * Created by Lab Resident on 2018-02-11.
 */

public class UserInformation {

    private String UID;
    private String Username;
    private String Dalid;
    private String Country;
    private String Program;
    private String StartTerm;
    private String Phonenumber;
    private String Address;
    private String Date;
    private String email;
    private String userImage;


    public UserInformation(String username, String dalid, String country, String program, String startTerm, String phonenumber, String address, String date) {
        Username = username;
        Dalid = dalid;
        Country = country;
        Program = program;
        StartTerm = startTerm;
        Phonenumber = phonenumber;
        Address = address;
        Date = date;
    }

    public UserInformation(){

}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDalid() {
        return Dalid;
    }

    public void setDalid(String dalid) {
        Dalid = dalid;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProgram() {
        return Program;
    }

    public void setProgram(String program) {
        Program = program;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartTerm() {
        return StartTerm;
    }

    public void setStartTerm(String startTerm) {
        StartTerm = startTerm;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}







