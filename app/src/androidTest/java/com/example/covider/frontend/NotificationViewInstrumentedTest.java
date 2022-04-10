package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.covider.MainActivity;
import com.example.covider.R;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.notification.NotificationManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.user.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NotificationViewInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Before
    public void LogIn(){
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(800,"NotificationTester", "12345678", 1));
        NotificationManager notificationManager = ManagerFactory.getNotificationManagerInstance();
        notificationManager.addNotification(100,800,"You got close contact with a positive patient, BEWARE!");
        Helpers.UserLogIn("NotificationTester", "12345678");
    }

    @Test
    public void TestNotification() {
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
        onView(withId(R.id.notifications_container))
                .check(matches(Helpers.withLinearLayoutSize(1)));
        onView(isRoot()).check(matches(Helpers.withViewCount(
                withText("You got close contact with a positive patient, BEWARE!"), 1)));
    }

    @Test
    public void TestNotificationRemove() {
        onView(withId(R.id.notification)).perform(click());
        Helpers.checkIsVisible(R.id.notification_view);
        onView(hasSibling(withText("You got close contact with a positive patient, BEWARE!"))).perform(click());
        onView(withId(R.id.notifications_container))
                .check(matches(Helpers.withLinearLayoutSize(0)));
        onView(isRoot()).check(matches(Helpers.withViewCount(
                withText("You got close contact with a positive patient, BEWARE!"), 0)));
    }
}
