package ca.connect.dal.dalconnect.util;

import com.google.firebase.database.FirebaseDatabase;


import ca.connect.dal.dalconnect.model.User;

/**
 * Created by gaoyounan on 2018/3/7.
 */

public class UsersGenrator
{
    public void createDemoUsers()
    {
        User user = null;
        String country = null;
        for(int i = 0; i < 10; i++)
        {
            AuthUtils.getInstance().createUser("user"+i+"@dalconnect.com","123456", null);

            if(i%3 ==0)
            {
                country = "China";
            }
            else if(i%3 == 1)
            {
                country = "India";
            }
            else
            {
                country = "Nigeria";
            }

            user = new User("user"+i,"user"+i+"@dalconnect.com", "portrait.png",country);
            FirebaseDatabase.getInstance().getReference().child("Dal_Chat/" + user.getUser_name()).setValue(user);
        }
    }

    public void deleteDemoUsers()
    {
        //AuthUtils.getInstance()
    }

}
