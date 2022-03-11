package com.example.covider.login;

import com.example.covider.database.ManagerFactory;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.user.User;

public class LoginHelper {
    public static boolean verify(long userId, String password){
        // Otherwise, um would be null
        UserManager um = ManagerFactory.getUserManagerInstance();
        User u = um.getUser(userId);
        if(u == null){
            return false;
        }
        if(!password.equals(u.getPassword())){
            return false;
        }
        return true;
    }

}

