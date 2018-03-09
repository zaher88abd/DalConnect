package ca.connect.dal.dalconnect.model;

import java.util.List;

/**
 * Created by gaoyounan on 2018/3/4.
 */

public class User {

    private String user_name;
    private String email;
    private List<String> groupID;
    private String portrait;
    private String country;

    public User()
    {

    }

    public User(String user_name, String email, String portrait, String country)
    {
        this.user_name = user_name;
        this.email = email;
        this.portrait = portrait;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGroupID() {
        return groupID;
    }

    public void setGroupID(List<String> groupID) {
        this.groupID = groupID;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
