package com.example.covider.frontend;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.covider.R;
import com.example.covider.MainActivity;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.user.User;

@RunWith(AndroidJUnit4.class)
public class LogInViewInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void createUser(){
        // add users
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(1000,"LogInTester", "Aa12345678", 1));
    }

    @Test
    public void TestInitialView() {
        Helpers.checkIsVisible(R.id.log_in_view);
        Helpers.checkIsGone(R.id.nav);
        Helpers.checkIsGone(R.id.map_view);
        Helpers.checkIsGone(R.id.report_view);
        Helpers.checkIsGone(R.id.profile_view);
        Helpers.checkIsGone(R.id.notification_view);
    }

    @Test
    public void TestNonexistentUser() {
        // no input
        Helpers.ClickLogInSubmitButton();
        checkLogInDialogWithText("User does not exist");

        // wrong username without password
        Helpers.SetLogInUsername("abc");
        Helpers.ClickLogInSubmitButton();
        checkLogInDialogWithText("User does not exist");

        // wrong random username with random password
        Helpers.UserLogIn("abc", "123");
        checkLogInDialogWithText("User does not exist");

        // wrong username case with correct password
        Helpers.UserLogIn("logInTester", "Aa12345678");
        checkLogInDialogWithText("User does not exist");
    }

    @Test
    public void TestWrongPassword() {
        Helpers.SetLogInUsername("LogInTester");
        // no password input
        Helpers.ClickLogInSubmitButton();
        checkLogInDialogWithText("Wrong Password");

        // wrong password
        Helpers.SetLogInPassword("123");
        Helpers.ClickLogInSubmitButton();
        checkLogInDialogWithText("Wrong Password");

        // wrong password case
        Helpers.SetLogInPassword("aa12345678");
        Helpers.ClickLogInSubmitButton();
        checkLogInDialogWithText("Wrong Password");
    }

    @Test
    public void TestLogInSuccess() {
        Helpers.UserLogIn("LogInTester", "Aa12345678");
        Helpers.checkIsGone(R.id.log_in_view);
        Helpers.checkIsVisible(R.id.nav);
        Helpers.checkIsVisible(R.id.map_view);
        Helpers.checkIsGone(R.id.report_view);
        Helpers.checkIsGone(R.id.profile_view);
        Helpers.checkIsGone(R.id.notification_view);
        onView(withId(R.id.toggle_view)).check(matches(isChecked()));
        onView(withId(R.id.username))
                .check(matches(withText("Hi, LogInTester!")));
    }

    private void checkLogInDialogWithText(String message) {
        ViewInteraction dialog = onView(withText("Error"));
        dialog
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(message))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText("Cancel")).perform(click());
        onView(isRoot()).inRoot(isDialog()).noActivity();
    }
}
