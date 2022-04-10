package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.covider.MainActivity;
import com.example.covider.R;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.course.Course;
import com.example.covider.model.user.User;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileViewInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        CourseManager courseManager = ManagerFactory.getCourseManagerInstance();
        courseManager.addOrUpdateCourse(new Course(60350, "CS350", 201, 1 ));
        courseManager.addOrUpdateCourse(new Course(60360, "CS360", 202, 1 ));
    }

    @Test
    public void TestStudentBasicComponents() {
        {
            Helpers.UserLogIn("Enji", "12345678");
            // go to profile page
            onView(withId(R.id.profile)).perform(click());
            onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        }
        {
            Helpers.checkIsVisible(R.id.log_out_button);
            Helpers.checkIsVisible(R.id.username);
            Helpers.checkIsVisible(R.id.course_title);
            Helpers.checkIsVisible(R.id.test_records_title);
            onView(withId(R.id.courses))
                    .check(matches(Helpers.withLinearLayoutSize(3)));
            onView(withId(R.id.test_records))
                    .check(matches(Helpers.withLinearLayoutSize(1)));
        }
        {
            onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.in_person), 2)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.online), 1)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText("CS310"), 1)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText("CS350"), 1)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText("CS585"), 1)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText("Positive"), 1)));
        }
        {
            // test in-person course pop up
            onView(first(withText(R.string.in_person))).perform(Helpers.clickOnNotDisplayed);
            ViewInteraction temp = onView(withId(R.id.popup_course_status));
            temp.check(matches(isDisplayed()));
            temp.check(matches(Helpers.withButtonString("In Person")));
            temp.check(matches(Helpers.withTextColor(R.color.in_person_green)));
            onView(withId(R.id.popup_course_title)).check(matches(Helpers.withTextviewString("CS310")));
            onView(withId(R.id.pop_up_positive_students)).check(matches(withText("Positive Students: 3")));
            onView(withId(R.id.pop_up_high_risk_students)).check(matches(withText("High Risk Students: 0")));
            onView(withId(R.id.pop_up_low_risk_students)).check(matches(withText("Low Risk Students: 0")));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sal_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 2")));
            Helpers.checkIsVisible(R.id.return_button);
            // test go back button
            onView(withId(R.id.return_button)).perform(click());
            temp.check(doesNotExist());
        }
        {
            // test online course pop up
            onView(first(withText(R.string.online))).perform(Helpers.clickOnNotDisplayed);
            ViewInteraction temp = onView(withId(R.id.popup_course_status));
            temp.check(matches(isDisplayed()));
            temp.check(matches(Helpers.withButtonString("Online")));
            temp.check(matches(Helpers.withTextColor(R.color.online_red)));
            onView(withId(R.id.popup_course_title)).check(matches(Helpers.withTextviewString("CS350")));
            onView(withId(R.id.pop_up_positive_students)).check(matches(withText("Positive Students: 1")));
            onView(withId(R.id.pop_up_high_risk_students)).check(matches(withText("High Risk Students: 0")));
            onView(withId(R.id.pop_up_low_risk_students)).check(matches(withText("Low Risk Students: 0")));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.thh_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 2")));
            Helpers.checkIsGone(R.id.change_course_status_button);
            Helpers.checkIsVisible(R.id.return_button);
            // test go back button
            onView(withId(R.id.return_button)).perform(click());
            temp.check(doesNotExist());
        }
        {
            // test log out button
            onView(withId(R.id.log_out_button)).perform(Helpers.clickOnNotDisplayed);
            Helpers.checkIsVisible(R.id.log_in_view);
            Helpers.checkIsGone(R.id.nav);
            Helpers.checkIsGone(R.id.map_view);
            Helpers.checkIsGone(R.id.report_view);
            Helpers.checkIsGone(R.id.profile_view);
            Helpers.checkIsGone(R.id.notification_view);
        }
    }

    @Test
    public void TestProfessorBasicComponents() {
        {
            Helpers.UserLogIn("Negar", "12345678");
            // go to profile page
            onView(withId(R.id.profile)).perform(click());
            onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        }
        {
            Helpers.checkIsVisible(R.id.log_out_button);
            Helpers.checkIsVisible(R.id.username);
            Helpers.checkIsVisible(R.id.course_title);
            Helpers.checkIsVisible(R.id.test_records_title);
            onView(withId(R.id.courses))
                    .check(matches(Helpers.withLinearLayoutSize(2)));
            onView(withId(R.id.test_records))
                    .check(matches(Helpers.withLinearLayoutSize(0)));
        }
        {
            onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.assess), 2)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText("CS310"), 1)));
            onView(isRoot()).check(matches(Helpers.withViewCount(withText("CS360"), 1)));
        }
        {
            // test in-person course pop up
            onView(first(withText(R.string.assess))).perform(Helpers.clickOnNotDisplayed);
            ViewInteraction temp = onView(withId(R.id.popup_course_status));
            temp.check(matches(isDisplayed()));
            temp.check(matches(Helpers.withButtonString("In Person")));
            temp.check(matches(Helpers.withTextColor(R.color.in_person_green)));
            onView(withId(R.id.popup_course_title)).check(matches(Helpers.withTextviewString("CS310")));
            onView(withId(R.id.pop_up_positive_students)).check(matches(withText("Positive Students: 3")));
            onView(withId(R.id.pop_up_high_risk_students)).check(matches(withText("High Risk Students: 0")));
            onView(withId(R.id.pop_up_low_risk_students)).check(matches(withText("Low Risk Students: 0")));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sal_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 2")));
            Helpers.checkIsVisible(R.id.change_course_status_button);
            onView(withId(R.id.change_course_status_button)).check(matches(withText(R.string.change_online)));
            Helpers.checkIsVisible(R.id.return_button);
            // test go back button
            onView(withId(R.id.return_button)).perform(click());
            temp.check(doesNotExist());
            // test change in person class online
            onView(first(withText(R.string.assess))).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.popup_course_status));
            onView(withId(R.id.change_course_status_button)).perform(click());
            // cancel change
            checkProfileDialog();
            onView(withText("Cancel")).perform(click());
            onView(isRoot()).inRoot(isDialog()).noActivity();
            // confirm change
            onView(withId(R.id.change_course_status_button)).perform(click());
            checkProfileDialog();
            onView(withText("Yes")).perform(click());
            onView(isRoot()).inRoot(isDialog()).noActivity();
            temp.check(doesNotExist());
            // check status after change
            onView(first(withText(R.string.assess))).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.popup_course_status));
            temp.check(matches(isDisplayed()));
            temp.check(matches(Helpers.withButtonString("Online")));
            temp.check(matches(Helpers.withTextColor(R.color.online_red)));
            onView(withId(R.id.popup_course_title)).check(matches(Helpers.withTextviewString("CS310")));
            onView(withId(R.id.pop_up_positive_students)).check(matches(withText("Positive Students: 3")));
            onView(withId(R.id.pop_up_high_risk_students)).check(matches(withText("High Risk Students: 0")));
            onView(withId(R.id.pop_up_low_risk_students)).check(matches(withText("Low Risk Students: 0")));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sal_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 2")));
            Helpers.checkIsVisible(R.id.change_course_status_button);
            onView(withId(R.id.change_course_status_button)).check(matches(withText(R.string.change_offline)));
            Helpers.checkIsVisible(R.id.return_button);
            onView(withId(R.id.return_button)).perform(click());
            temp.check(doesNotExist());
        }
        {
            // test online course pop up
            onView(second(withText(R.string.assess))).perform(Helpers.clickOnNotDisplayed);
            ViewInteraction temp = onView(withId(R.id.popup_course_status));
            temp.check(matches(isDisplayed()));
            temp.check(matches(Helpers.withButtonString("Online")));
            temp.check(matches(Helpers.withTextColor(R.color.online_red)));
            onView(withId(R.id.popup_course_title)).check(matches(Helpers.withTextviewString("CS360")));
            onView(withId(R.id.pop_up_positive_students)).check(matches(withText("Positive Students: 1")));
            onView(withId(R.id.pop_up_high_risk_students)).check(matches(withText("High Risk Students: 0")));
            onView(withId(R.id.pop_up_low_risk_students)).check(matches(withText("Low Risk Students: 0")));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.kap_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            Helpers.checkIsVisible(R.id.change_course_status_button);
            onView(withId(R.id.change_course_status_button)).check(matches(withText(R.string.change_offline)));
            Helpers.checkIsVisible(R.id.return_button);
            // test go back button
            onView(withId(R.id.return_button)).perform(click());
            temp.check(doesNotExist());
            // test change online class in person
            onView(second(withText(R.string.assess))).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.popup_course_status));
            onView(withId(R.id.change_course_status_button)).perform(click());
            // cancel change
            checkProfileDialog();
            onView(withText("Cancel")).perform(click());
            onView(isRoot()).inRoot(isDialog()).noActivity();
            // confirm change
            onView(withId(R.id.change_course_status_button)).perform(click());
            checkProfileDialog();
            onView(withText("Yes")).perform(click());
            onView(isRoot()).inRoot(isDialog()).noActivity();
            temp.check(doesNotExist());
            // check status after change
            onView(second(withText(R.string.assess))).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.popup_course_status));
            temp.check(matches(isDisplayed()));
            temp.check(matches(Helpers.withButtonString("In Person")));
            temp.check(matches(Helpers.withTextColor(R.color.in_person_green)));
            onView(withId(R.id.popup_course_title)).check(matches(Helpers.withTextviewString("CS360")));
            onView(withId(R.id.pop_up_positive_students)).check(matches(withText("Positive Students: 1")));
            onView(withId(R.id.pop_up_high_risk_students)).check(matches(withText("High Risk Students: 0")));
            onView(withId(R.id.pop_up_low_risk_students)).check(matches(withText("Low Risk Students: 0")));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.kap_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            Helpers.checkIsVisible(R.id.change_course_status_button);
            onView(withId(R.id.change_course_status_button)).check(matches(withText(R.string.change_online)));
            Helpers.checkIsVisible(R.id.return_button);
            onView(withId(R.id.return_button)).perform(click());
            temp.check(doesNotExist());
        }
        {
            // test log out button
            onView(withId(R.id.log_out_button)).perform(Helpers.clickOnNotDisplayed);
            Helpers.checkIsVisible(R.id.log_in_view);
            Helpers.checkIsGone(R.id.nav);
            Helpers.checkIsGone(R.id.map_view);
            Helpers.checkIsGone(R.id.report_view);
            Helpers.checkIsGone(R.id.profile_view);
            Helpers.checkIsGone(R.id.notification_view);
        }
    }

    private void checkProfileDialog() {
        ViewInteraction dialog = onView(withText("Change Course Status"));
        dialog
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText("Are you sure to change course status?"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText("Yes"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText("Cancel"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    private Matcher<View> first(final Matcher<View> matcher) {
        return new BaseMatcher<View>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("should return first matching item");
            }
        };
    }

    public static Matcher<View> second(final Matcher<View> matcher) {
        return new BaseMatcher<View>() {
            int counter = 0;
            @Override
            public boolean matches(final Object item) {
                if (matcher.matches(item)) {
                    if(counter == 1) {
                        counter++;
                        return true;
                    }
                    counter++;
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Element at hierarchy position 1");
            }
        };
    }
}
