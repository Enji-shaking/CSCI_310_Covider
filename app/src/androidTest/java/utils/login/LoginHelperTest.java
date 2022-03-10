package utils.login;

import static org.junit.Assert.*;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import database.ManagerFactory;

public class LoginHelperTest {

    @Test
    public void testVerify() {
        ManagerFactory.initialize(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertTrue(LoginHelper.verify(1, "password"));
        assertFalse(LoginHelper.verify(1, "dummy"));
        assertFalse(LoginHelper.verify(-1, ""));
    }
}