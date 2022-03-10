package utils.login;

import database.ManagerFactory;
import database.user.UserManager;
import model.user.User;

public class LoginHelper {
    public static boolean verify(long userId, String password){
        // TODO: find a correct context for below (or not?)
        // TODO: ManagerFactory.initialize(context..?)
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

