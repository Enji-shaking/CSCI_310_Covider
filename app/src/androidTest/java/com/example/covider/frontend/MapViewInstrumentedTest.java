package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
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
    public void setup(){
        // add user
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(201,"MapViewTesterStudent1", "12345678", 1));
        userManager.addOrUpdateUser(new User(205,"MapViewTesterStudent2", "12345678", 1));
        userManager.addOrUpdateUser(new User(206,"MapViewTesterStudent3", "12345678", 1));
        userManager.addOrUpdateUser(new User(202,"MapViewPositiveTester", "12345678", 1));
        userManager.addOrUpdateUser(new User(203,"MapViewNegativeTester", "12345678", 1));
        userManager.addOrUpdateUser(new User(204,"MapViewSymptomTester", "12345678", 1));
        // add buildings
        BuildingManager buildingManager = ManagerFactory.getBuildingManagerInstance();
        buildingManager.addOrUpdateBuilding(new Building(301,"cpa"));
        buildingManager.addOrUpdateBuilding(new Building(302,"mrc"));
        buildingManager.addOrUpdateBuilding(new Building(303,"ugw"));
        buildingManager.addOrUpdateBuilding(new Building(304,"jff"));
        buildingManager.addOrUpdateBuilding(new Building(305,"nrc"));
        buildingManager.addOrUpdateBuilding(new Building(306,"dml"));
        buildingManager.addOrUpdateBuilding(new Building(307,"mhc"));
        // add check-ins
        CheckinManager checkinManager = ManagerFactory.getCheckinManagerInstance();
        checkinManager.addCheckin(102, 307);
        checkinManager.addCheckin(202, 303);
        checkinManager.addCheckin(202, 305);
        checkinManager.addCheckin(203, 303);
        checkinManager.addCheckin(203, 305);
        checkinManager.addCheckin(202, 301);
        checkinManager.addCheckin(202, 306);
        checkinManager.addCheckin(204, 301);
        checkinManager.addCheckin(204, 306);
        ReportManager reportManager = ManagerFactory.getReportManagerInstance();
        reportManager.addOrUpdateReport(new UserDailyReport(202, 202, 1, "", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(203, 203, 0, "", System.currentTimeMillis()));
        reportManager.addOrUpdateReport(new UserDailyReport(204, 204, 0, "Symptom", System.currentTimeMillis()));
    }

    @Test
    public void TestToggleView() {
        // log in
        Helpers.UserLogIn("MapViewTesterStudent1", "12345678");
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
        Helpers.UserLogIn("MapViewTesterStudent1", "12345678");
        // initially display map view
        Helpers.checkIsVisible(R.id.usc_map_view);
        {
            // click on mrc should display a no risk popup
            onView(withId(R.id.usc_map_mrc)).perform(Helpers.clickOnNotDisplayed);
            ViewInteraction temp = onView(withId(R.id.pop_up_building_risk_circle));
            temp.check(matches(isDisplayed()));
            temp.check(matches(withTintColor(R.color.success_green)));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.mrc_display)));
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
            onView(withId(R.id.usc_map_mrc)).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.pop_up_building_risk_circle));
            onView(withId(R.id.check_in_button)).perform(click());
            {
                // test questionnaire
                ViewInteraction questionnaire = onView(withText(R.string.check_in_questionnaire_title));
                questionnaire.check(matches(isDisplayed()));
                Helpers.checkIsVisible(R.id.submit_questionnaire);
                Helpers.checkIsVisible(R.id.questionnaire_return_button);
                {
                    // empty questionnaire
                    onView(withId(R.id.submit_questionnaire)).perform(click());
                    Helpers.checkReportDialogWithText("Error", "Please answer all questions.", "Cancel");
                }
                {
                    // partially filled questionnaire
                    onView(withId(R.id.questionnaire_symptoms_no)).perform(click());
                    onView(withId(R.id.submit_questionnaire)).perform(click());
                    Helpers.checkReportDialogWithText("Error", "Please answer all questions.", "Cancel");
                }
                {
                    // test questionnaire return button
                    onView(withId(R.id.questionnaire_return_button)).perform(click());
                    questionnaire.check(doesNotExist());
                    temp.check(matches(isDisplayed()));
                }
                onView(withId(R.id.check_in_button)).perform(click());
                questionnaire = onView(withText(R.string.check_in_questionnaire_title));
                questionnaire.check(matches(isDisplayed()));
                {
                    // test selection
                    // check mask buttons
                    onView(withId(R.id.questionnaire_mask_yes)).perform(click());
                    onView(withId(R.id.questionnaire_mask_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_mask_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_mask_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_mask_no)).check(matches(Helpers.withTextColor(R.color.black)));
                    onView(withId(R.id.questionnaire_mask_no)).perform(click());
                    onView(withId(R.id.questionnaire_mask_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_mask_no)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_mask_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_mask_yes)).check(matches(Helpers.withTextColor(R.color.black)));
                    // check sanitizer buttons
                    onView(withId(R.id.questionnaire_sanitizer_yes)).perform(click());
                    onView(withId(R.id.questionnaire_sanitizer_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_sanitizer_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_sanitizer_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_sanitizer_no)).check(matches(Helpers.withTextColor(R.color.black)));
                    onView(withId(R.id.questionnaire_sanitizer_no)).perform(click());
                    onView(withId(R.id.questionnaire_sanitizer_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_sanitizer_no)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_sanitizer_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_sanitizer_yes)).check(matches(Helpers.withTextColor(R.color.black)));
                    // check distance buttons
                    onView(withId(R.id.questionnaire_distance_yes)).perform(click());
                    onView(withId(R.id.questionnaire_distance_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_distance_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_distance_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_distance_no)).check(matches(Helpers.withTextColor(R.color.black)));
                    onView(withId(R.id.questionnaire_distance_no)).perform(click());
                    onView(withId(R.id.questionnaire_distance_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_distance_no)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_distance_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_distance_yes)).check(matches(Helpers.withTextColor(R.color.black)));
                    // check symptoms buttons
                    onView(withId(R.id.questionnaire_symptoms_yes)).perform(click());
                    onView(withId(R.id.questionnaire_symptoms_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_symptoms_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_symptoms_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_symptoms_no)).check(matches(Helpers.withTextColor(R.color.black)));
                    onView(withId(R.id.questionnaire_symptoms_no)).perform(click());
                    onView(withId(R.id.questionnaire_symptoms_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
                    onView(withId(R.id.questionnaire_symptoms_no)).check(matches(Helpers.withTextColor(R.color.gold)));
                    onView(withId(R.id.questionnaire_symptoms_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
                    onView(withId(R.id.questionnaire_symptoms_yes)).check(matches(Helpers.withTextColor(R.color.black)));
                }
                {
                    onView(withId(R.id.submit_questionnaire)).perform(click());
                    Helpers.checkReportDialogWithText("Success!", "Thanks for your submission!", "Close");
                    questionnaire.check(doesNotExist());
                }
            }
            temp.check(doesNotExist());
            // check check-in data
            onView(withId(R.id.usc_map_mrc)).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.pop_up_building_risk_circle));
            temp.check(matches(withTintColor(R.color.high_risk_opaque)));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.mrc_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.check_in_button)).perform(click());
            submitAnsweredQuestionnaire();
            // check in again should not change anything
            onView(withId(R.id.usc_map_mrc)).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.pop_up_building_risk_circle));
            temp.check(matches(withTintColor(R.color.high_risk_opaque)));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.mrc_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.return_button)).perform(click());
        }

        {
            // click on ugw should display a low risk popup
            onView(withId(R.id.usc_map_ugw)).perform(Helpers.clickOnNotDisplayed);
            onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.ugw_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 1")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.low_risk_opaque)));
            Helpers.checkIsVisible(R.id.check_in_button);
            Helpers.checkIsVisible(R.id.return_button);
            onView(withId(R.id.return_button)).perform(click());
        }

        {
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
            onView(withId(R.id.return_button)).perform(click());
        }
    }

    @Test
    public void TestListView3x3() {
        // log in
        Helpers.UserLogIn("Enji", "12345678");
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
        Helpers.UserLogIn("Zhihan", "12345678");
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
        Helpers.UserLogIn("Shuning", "12345678");
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
        // should have 2 sal entries and 2 mhc entries
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.sal_display), 2)));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.mhc_display), 2)));

    }

    @Test
    public void TestListView0x0() {
        // log in
        Helpers.UserLogIn("MapViewTesterStudent2", "12345678");
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
        Helpers.UserLogIn("MapViewTesterStudent3", "12345678");
        // toggle to list view
        onView(withId(R.id.toggle_view))
                .check(matches(isChecked()))
                .perform(click());
        Helpers.checkIsVisible(R.id.usc_list_view);

        {
            // click on jff should display a no risk popup
            onView(withId(R.id.usc_map_jff)).perform(Helpers.clickOnNotDisplayed);
            ViewInteraction temp = onView(withId(R.id.pop_up_building_risk_circle));
            temp.check(matches(isDisplayed()));
            temp.check(matches(withTintColor(R.color.success_green)));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.jff_display)));
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
            onView(withId(R.id.usc_map_jff)).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.pop_up_building_risk_circle));
            onView(withId(R.id.check_in_button)).perform(click());
            submitAnsweredQuestionnaire();
            temp.check(doesNotExist());
            // check frequent visit
            Helpers.checkIsVisible(R.id.frequently_visited_title);
            onView(withId(R.id.frequently_visited_buildings))
                    .check(matches(Helpers.withLinearLayoutSize(1)));
            // should have 2 jff entries
            onView(isRoot()).check(matches(Helpers.withViewCount(withText(R.string.jff_display), 2)));
            // check check-in data
            onView(withId(R.id.usc_map_jff)).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.pop_up_building_risk_circle));
            temp.check(matches(withTintColor(R.color.high_risk_opaque)));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.jff_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.check_in_button)).perform(click());
            submitAnsweredQuestionnaire();
            // check in again should not change anything
            onView(withId(R.id.usc_map_jff)).perform(Helpers.clickOnNotDisplayed);
            temp = onView(withId(R.id.pop_up_building_risk_circle));
            temp.check(matches(withTintColor(R.color.high_risk_opaque)));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.jff_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 1")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.return_button)).perform(click());
        }


        {
            // click on nrc should display a low risk popup
            onView(withId(R.id.usc_map_nrc)).perform(Helpers.clickOnNotDisplayed);
            onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.nrc_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 1")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 0")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.low_risk_opaque)));
            Helpers.checkIsVisible(R.id.check_in_button);
            Helpers.checkIsVisible(R.id.return_button);
            onView(withId(R.id.return_button)).perform(click());
        }

        {
            // click on dml should display a medium risk popup
            onView(withId(R.id.usc_map_dml)).perform(Helpers.clickOnNotDisplayed);
            onView(withId(R.id.pop_up_building_risk_circle)).check(matches(isDisplayed()));
            onView(withId(R.id.pop_up_building_name)).check(matches(withText(R.string.dml_display)));
            onView(withId(R.id.pop_up_total_visitors)).check(matches(withText("Total Visitors: 2")));
            onView(withId(R.id.pop_up_low_risk_visitors)).check(matches(withText("Low Risk Visitors: 0")));
            onView(withId(R.id.pop_up_high_risk_visitors)).check(matches(withText("High Risk Visitors: 1")));
            onView(withId(R.id.pop_up_positive_visitors)).check(matches(withText("Positive Visitors: 1")));
            onView(withId(R.id.pop_up_building_risk_circle)).check(matches(withTintColor(R.color.medium_risk_opaque)));
            Helpers.checkIsVisible(R.id.check_in_button);
            Helpers.checkIsVisible(R.id.return_button);
        }
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

    private static void submitAnsweredQuestionnaire() {
        onView(withId(R.id.questionnaire_mask_yes)).perform(click());
        onView(withId(R.id.questionnaire_sanitizer_yes)).perform(click());
        onView(withId(R.id.questionnaire_distance_yes)).perform(click());
        onView(withId(R.id.questionnaire_symptoms_no)).perform(click());
        onView(withId(R.id.submit_questionnaire)).perform(click());
        onView(withText("Close")).perform(click());
    }

}
