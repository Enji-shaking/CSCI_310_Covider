package com.example.covider.frontend;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.Checks;

import com.example.covider.MainActivity;
import com.example.covider.R;
import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.building.BuildingManager;
import com.example.covider.database.checkin.CheckinManager;
import com.example.covider.database.report.ReportManager;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.building.Building;
import com.example.covider.model.report.UserDailyReport;
import com.example.covider.model.user.User;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class MapViewInstrumentedTest {
    private ArrayList<Long> checkIns;
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Before
    public void setup(){
        checkIns = new ArrayList<>();
        // add user
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(100,"Enji", "Aa12345678", 1));
        userManager.addOrUpdateUser(new User(200,"Tester", "12345678", 0));
        // add buildings
        BuildingManager buildingManager = ManagerFactory.getBuildingManagerInstance();
        buildingManager.addOrUpdateBuilding(new Building(301,"cpa"));
        // add check-ins
        CheckinManager checkinManager = ManagerFactory.getCheckinManagerInstance();
        checkIns.add(checkinManager.addCheckin(100, 200));
        checkIns.add(checkinManager.addCheckin(101, 200));
        checkIns.add(checkinManager.addCheckin(100, 301));
        checkIns.add(checkinManager.addCheckin(102, 301));
        ReportManager reportManager = ManagerFactory.getReportManagerInstance();
        reportManager.addOrUpdateReport(new UserDailyReport(301, 101, 0, "", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(302, 102, 0, "Symptom", System.currentTimeMillis()));
    }

    @After
    public void tearDown() {
        CheckinManager checkinManager = ManagerFactory.getCheckinManagerInstance();
        for (long checkInId : checkIns)
        {
            checkinManager.deleteCheckin(checkInId);
        }
    }

    @Test
    public void TestToggleView() {
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Enji"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("Aa12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
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
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Enji"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("Aa12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // initially display map view
        Helpers.checkIsVisible(R.id.usc_map_view);
        // click on esh should display a no risk popup
        onView(withId(R.id.usc_map_esh)).perform(Helpers.clickOnNotDisplayed);
        ViewInteraction temp = onView(withId(R.id.pop_up_building_risk_circle));
        temp.check(matches(isDisplayed()));
        temp.check(matches(withTintColor(R.color.success_green)));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.esh_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 0")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 0")));
        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
        // test cancel button
        onView(withId(R.id.return_button)).perform(click());
        temp.check(doesNotExist());
        // test check in button
        onView(withId(R.id.usc_map_esh)).perform(Helpers.clickOnNotDisplayed);
        temp = onView(withId(R.id.pop_up_building_risk_circle));
        onView(withId(R.id.check_in_button)).perform(click());
        temp.check(doesNotExist());
        // check check-in data
        onView(withId(R.id.usc_map_esh)).perform(Helpers.clickOnNotDisplayed);
        temp = onView(withId(R.id.pop_up_building_risk_circle));
        temp.check(matches(withTintColor(R.color.high_risk_opaque)));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.esh_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.check_in_button)).perform(click());
        // check in again should not change anything
        onView(withId(R.id.usc_map_esh)).perform(Helpers.clickOnNotDisplayed);
        temp = onView(withId(R.id.pop_up_building_risk_circle));
        temp.check(matches(withTintColor(R.color.high_risk_opaque)));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.esh_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.return_button)).perform(click());


        // click on sal should display a low risk popup
        onView(withId(R.id.usc_map_sal)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sal_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 1")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.low_risk_opaque)));
        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
        onView(withId(R.id.return_button)).perform(click());

        // click on cpa should display a medium risk popup
        onView(withId(R.id.usc_map_cpa)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.cpa_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 1")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.medium_risk_opaque)));
        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
    }

    @Test
    public void TestListView3x3() {
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Enji"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("Aa12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);
        // check daily schedule
        Helpers.checkIsVisible(R.id.daily_schedule_title);
        onView(withId(R.id.daily_schedule_buildings))
                .check(matches(Helpers.withLinearLayoutSize(3)));
        // check frequent visit
        Helpers.checkIsVisible(R.id.frequently_visited_title);
        onView(withId(R.id.frequently_visited_buildings))
                .check(matches(Helpers.withLinearLayoutSize(3)));
        // should have 3 sal entries, 3 thh entries, 2 rth entries, and 2 kap entries
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.sal_display), 3)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.thh_display), 3)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.rth_display), 2)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.kap_display), 2)));

    }

    @Test
    public void TestListView2x2() {
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Zhihan"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);
        // check daily schedule
        Helpers.checkIsVisible(R.id.daily_schedule_title);
        onView(withId(R.id.daily_schedule_buildings))
                .check(matches(Helpers.withLinearLayoutSize(2)));
        // check frequent visit
        Helpers.checkIsVisible(R.id.frequently_visited_title);
        onView(withId(R.id.frequently_visited_buildings))
                .check(matches(Helpers.withLinearLayoutSize(2)));
        // should have 3 sal entries, 2 thh entries, and 2 kap entries
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.sal_display), 3)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.thh_display), 2)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.kap_display), 2)));

    }

    @Test
    public void TestListView1x1() {
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Shuning"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);
        // check daily schedule
        Helpers.checkIsVisible(R.id.daily_schedule_title);
        onView(withId(R.id.daily_schedule_buildings))
                .check(matches(Helpers.withLinearLayoutSize(1)));
        // check frequent visit
        Helpers.checkIsVisible(R.id.frequently_visited_title);
        onView(withId(R.id.frequently_visited_buildings))
                .check(matches(Helpers.withLinearLayoutSize(1)));
        // should have 2 sal entries and 2 cpa entries
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.sal_display), 2)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.cpa_display), 2)));

    }

    @Test
    public void TestListView0x0() {
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Tester"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);
        // check daily schedule
        Helpers.checkIsGone(R.id.daily_schedule_title);
        onView(withId(R.id.daily_schedule_buildings))
                .check(matches(Helpers.withLinearLayoutSize(0)));
        // check frequent visit
        Helpers.checkIsGone(R.id.frequently_visited_title);
        onView(withId(R.id.frequently_visited_buildings))
                .check(matches(Helpers.withLinearLayoutSize(0)));

    }

    @Test
    public void TestListViewCheckIn() {
        // log in
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Tester"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);

        // click on sgm should display a no risk popup
        onView(withId(R.id.usc_map_sgm)).perform(Helpers.clickOnNotDisplayed);
        ViewInteraction temp = onView(withId(R.id.pop_up_building_risk_circle));
        temp.check(matches(isDisplayed()));
        temp.check(matches(withTintColor(R.color.success_green)));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sgm_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 0")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 0")));
        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
        // test cancel button
        onView(withId(R.id.return_button)).perform(click());
        temp.check(doesNotExist());
        // test check in button
        onView(withId(R.id.usc_map_sgm)).perform(Helpers.clickOnNotDisplayed);
        temp = onView(withId(R.id.pop_up_building_risk_circle));
        onView(withId(R.id.check_in_button)).perform(click());
        temp.check(doesNotExist());
        // check frequent visit
        Helpers.checkIsVisible(R.id.frequently_visited_title);
        onView(withId(R.id.frequently_visited_buildings))
                .check(matches(Helpers.withLinearLayoutSize(1)));
        // should have 2 sgm entries
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.sgm_display), 2)));
        // check check-in data
        onView(withId(R.id.usc_map_sgm)).perform(Helpers.clickOnNotDisplayed);
        temp = onView(withId(R.id.pop_up_building_risk_circle));
        temp.check(matches(withTintColor(R.color.high_risk_opaque)));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sgm_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.check_in_button)).perform(click());
        // check in again should not change anything
        onView(withId(R.id.usc_map_sgm)).perform(Helpers.clickOnNotDisplayed);
        temp = onView(withId(R.id.pop_up_building_risk_circle));
        temp.check(matches(withTintColor(R.color.high_risk_opaque)));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sgm_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.return_button)).perform(click());


        // click on sal should display a low risk popup
        onView(withId(R.id.usc_map_sal)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.sal_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 1")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.low_risk_opaque)));
        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
        onView(withId(R.id.return_button)).perform(click());

        // click on cpa should display a medium risk popup
        onView(withId(R.id.usc_map_cpa)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
        onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.cpa_display)));
        onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
        onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
        onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 1")));
        onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
        onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.medium_risk_opaque)));
        Helpers.checkIsVisible(R.id.check_in_button);
        Helpers.checkIsVisible(R.id.return_button);
    }

    public static Matcher<View> withTintColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public boolean matchesSafely(ImageView v) {
                PorterDuffColorFilter filter2 = new PorterDuffColorFilter(
                        ContextCompat.getColor(v.getContext(), color), PorterDuff.Mode.SRC_ATOP);
                return filter2.equals(v.getColorFilter());
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with tint color: ");
            }
        };
    }

}
