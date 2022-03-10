package model.user;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.ArrayList;

import database.ManagerFactory;
import database.notification.NotificationManager;

public class ProfessorTest {
    @Test
    public void testNotifyOnline(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ManagerFactory.initialize(instrumentationContext);
        NotificationManager nm = ManagerFactory.getNotificationManagerInstance();
        Professor p = new Professor(3, "ZSN", "password");
        ArrayList<Long> list = p.notifyOnline();
        try{
            assertEquals(4, list.size());
        }finally {
            if(list!=null){
                for(long i: list){
                    nm.deleteNotification(i);
                }
            }
        }
    }
}