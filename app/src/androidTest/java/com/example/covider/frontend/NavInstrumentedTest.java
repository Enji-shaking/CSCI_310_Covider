package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.covider.MainActivity;
import com.example.covider.R;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.user.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Before
    public void LogIn(){
        // add users
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(900,"NavTester", "12345678", 1));
        Helpers.UserLogIn("NavTester", "12345678");
    }

    @Test
    public void TestMapButton() {
        Helpers.checkIsVisible(R.id.nav);
        Helpers.checkIsGone(R.id.log_in_view);
        // click map view should display map view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsVisible(R.id.map_view);
        // click report view should not display map view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsGone(R.id.map_view);
        // click map view should display map view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsVisible(R.id.map_view);
        // click profile view should not display map view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsGone(R.id.map_view);
        // click map view should display map view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsVisible(R.id.map_view);
        // click notification view should not display map view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsGone(R.id.map_view);
        // click map view should display map view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsVisible(R.id.map_view);
        // click map view again should still display map view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsVisible(R.id.map_view);
    }

    @Test
    public void TestReportButton() {
        Helpers.checkIsVisible(R.id.nav);
        Helpers.checkIsGone(R.id.log_in_view);
        // click report view should display report view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsVisible(R.id.report_view);
        // click map view should not display report view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsGone(R.id.report_view);
        // click report view should display report view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsVisible(R.id.report_view);
        // click profile view should not display report view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsGone(R.id.report_view);
        // click report view should display report view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsVisible(R.id.report_view);
        // click notification view should not display report view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsGone(R.id.report_view);
        // click report view should display report view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsVisible(R.id.report_view);
        // click report view again should still display report view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsVisible(R.id.report_view);
    }

    @Test
    public void TestProfileButton() {
        Helpers.checkIsVisible(R.id.nav);
        Helpers.checkIsGone(R.id.log_in_view);
        // click profile view should display profile view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsVisible(R.id.profile_view);
        // click map view should not display profile view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsGone(R.id.profile_view);
        // click profile view should display profile view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsVisible(R.id.profile_view);
        // click report view should not display profile view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsGone(R.id.profile_view);
        // click profile view should display profile view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsVisible(R.id.profile_view);
        // click notification view should not display profile view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsGone(R.id.profile_view);
        // click profile view should display profile view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsVisible(R.id.profile_view);
        // click profile view again should still display profile view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsVisible(R.id.profile_view);
    }

    @Test
    public void TestNotificationButton() {
        Helpers.checkIsVisible(R.id.nav);
        Helpers.checkIsGone(R.id.log_in_view);
        // click notification view should display notification view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
        // click map view should not display notification view
        onView(withId(R.id.map)).perform(click());
        Helpers.checkIsGone(R.id.notification_view);
        // click notification view should display notification view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
        // click report view should not display notification view
        onView(withId(R.id.report)).perform(click());
        Helpers.checkIsGone(R.id.notification_view);
        // click notification view should display notification view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
        // click profile view should not display notification view
        onView(withId(R.id.profile)).perform(click());
        Helpers.checkIsGone(R.id.notification_view);
        // click notification view should display notification view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
        // click notification view again should still display notification view
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
    }
}
