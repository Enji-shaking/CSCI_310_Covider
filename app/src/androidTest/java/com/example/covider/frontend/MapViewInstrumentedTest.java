package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.Checks;

import com.example.covider.MainActivity;
import com.example.covider.R;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.building.BuildingManager;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.database.course.CourseManager;
import com.example.covider.database.enrollment.EnrollmentManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.building.Building;
import com.example.covider.model.course.Course;
import com.example.covider.model.enrollment.Enrollment;
import com.example.covider.model.user.User;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MapViewInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Before
    public void LogIn(){
        // add user
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(100,"Enji", "Aa12345678", 1));
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Enji"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("Aa12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // add buildings
        BuildingManager buildingManager = ManagerFactory.getBuildingManagerInstance();
        buildingManager.addOrUpdateBuilding(new Building(200,"sal"));
        buildingManager.addOrUpdateBuilding(new Building(201,"thh"));
        buildingManager.addOrUpdateBuilding(new Building(202,"kap"));
        buildingManager.addOrUpdateBuilding(new Building(203,"rth"));
        // add check-ins
        CheckinManager checkinManager = ManagerFactory.getCheckinManagerInstance();
        checkinManager.addCheckin(100, 200);
        checkinManager.addCheckin(100, 201);
        checkinManager.addCheckin(100, 202);
        // add courses
        CourseManager courseManager = ManagerFactory.getCourseManagerInstance();
        courseManager.addOrUpdateCourse(new Course(60310, "CS310", 200, 0 ));
        courseManager.addOrUpdateCourse(new Course(60350, "CS350", 201, 0 ));
        courseManager.addOrUpdateCourse(new Course(60360, "CS360", 202, 0 ));
        courseManager.addOrUpdateCourse(new Course(60585, "CS585", 203, 0 ));
        // add enrollments
        EnrollmentManager enrollmentManager = ManagerFactory.getEnrollmentManagerInstance();
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 10000, 100, 60310, 1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 10001, 100, 60350, 1));
        enrollmentManager.addOrUpdateEnrollment(new Enrollment( 10002, 100, 60585, 1));
    }

//    @Test
    public void TestToggleView() {
        // initially display map view
        Helpers.checkIsVisible(R.id.usc_map_view);
        Helpers.checkIsGone(R.id.usc_list_view);
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsGone(R.id.usc_map_view);
        Helpers.checkIsVisible(R.id.usc_list_view);
        // toggle to map view again
        onView(withId(R.id.toggle_view))
                .check(matches(isNotChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_map_view);
        Helpers.checkIsGone(R.id.usc_list_view);
    }

    @Test
    public void TestMapView() {
        // initially display map view
        Helpers.checkIsVisible(R.id.usc_map_view);
        // click on esh should display a popup
        onView(withId(R.id.usc_map_esh))
                .perform(scrollTo(), click());
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.esh_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 0")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 0")));

        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
    }

//    @Test
    public void TestListView() {
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);

    }

}
