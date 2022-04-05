package com.example.covider.database.user;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.model.user.User;

public class UserManagerTest{
    UserManager um;
    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        instrumentationContext.deleteDatabase("covider");
        Config.Change_Test();
        ManagerFactory.initialize(instrumentationContext);
        um = ManagerFactory.getUserManagerInstance();
    }

    @Test
    public void testAddAndDeleteUsers(){
        long id1 = um.addUser("Mark", "password", 1);
        long id2 = um.addUser("Enji", "password", 1);
        long id3 = um.addUser("ZSN", "password", 0);
        User user = um.getUser(id1);
        assertEquals(user.getName(), "Mark");
        User user2 = um.getUser(id2);
        assertEquals(user2.getName(), "Enji");
        User user3 = um.getUser(id3);
        assertEquals(user3.getName(), "ZSN");
        um.deleteUser(id1);
        um.deleteUser(id2);
        um.deleteUser(id3);
        user = um.getUser(id1);
        assertNull(user);
        user2 = um.getUser(id2);
        assertNull(user2);
        user3 = um.getUser(id3);
        assertNull(user3);
    }

    @After
    public void clean(){
        Config.Change_Normal();
    }
}