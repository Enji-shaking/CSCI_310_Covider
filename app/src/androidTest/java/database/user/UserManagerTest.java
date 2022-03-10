package database.user;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import model.user.User;

public class UserManagerTest{
    UserManager um;
    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        instrumentationContext.deleteDatabase("covider");
        um = UserManager.getInstance(instrumentationContext);
    }

    @Test
    public void testDefaultUsers(){
        User user = um.getUser(1);
        assertEquals(user.getName(), "Mark");
        User user2 = um.getUser(2);
        assertEquals(user2.getName(), "Enji");
        User user3 = um.getUser(3);
        assertEquals(user3.getName(), "ZSN");
    }
}