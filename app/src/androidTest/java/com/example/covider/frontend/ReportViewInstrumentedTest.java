package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
public class ReportViewInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        // add users
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(1100,"ReportViewStudentTester", "12345678", 1));
        userManager.addOrUpdateUser(new User(1101,"ReportViewProfessorTester", "12345678", 0));
    }

    @Test
    public void TestSubmitEmptyReport() {
        Helpers.UserLogIn("ReportViewStudentTester", "12345678");
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        Helpers.checkReportDialogWithText("Error", "Fill out all forms please", "Cancel");
    }

    @Test
    public void TestSubmitPartiallyFilledReport() {
        Helpers.UserLogIn("ReportViewStudentTester", "12345678");
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        onView(withId(R.id.muscle_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.conjunctivitis_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        Helpers.checkReportDialogWithText("Error", "Fill out all forms please", "Cancel");
    }

    @Test
    public void TestReportSelectionProcess() {
        Helpers.UserLogIn("ReportViewStudentTester", "12345678");
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        // check infection buttons
        onView(withId(R.id.infection_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.infection_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.infection_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.infection_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.infection_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.infection_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.infection_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.infection_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.infection_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.infection_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check chills_fever buttons
        onView(withId(R.id.chills_fever_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.chills_fever_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.chills_fever_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.chills_fever_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.chills_fever_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.chills_fever_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.chills_fever_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.chills_fever_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.chills_fever_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.chills_fever_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check taste_smell buttons
        onView(withId(R.id.taste_smell_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.taste_smell_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.taste_smell_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.taste_smell_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.taste_smell_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.taste_smell_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.taste_smell_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.taste_smell_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.taste_smell_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.taste_smell_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check muscle buttons
        onView(withId(R.id.muscle_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.muscle_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.muscle_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.muscle_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.muscle_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.muscle_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.muscle_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.muscle_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.muscle_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.muscle_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check cough buttons
        onView(withId(R.id.cough_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.cough_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.cough_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.cough_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.cough_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.cough_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.cough_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.cough_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.cough_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.cough_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check breathing buttons
        onView(withId(R.id.breathing_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.breathing_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.breathing_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.breathing_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.breathing_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.breathing_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.breathing_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.breathing_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.breathing_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.breathing_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check conjunctivitis buttons
        onView(withId(R.id.conjunctivitis_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.conjunctivitis_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.conjunctivitis_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.conjunctivitis_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.conjunctivitis_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.conjunctivitis_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.conjunctivitis_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.conjunctivitis_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.conjunctivitis_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.conjunctivitis_yes)).check(matches(Helpers.withTextColor(R.color.black)));
        // check gi_symptoms buttons
        onView(withId(R.id.gi_symptoms_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.gi_symptoms_yes)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.gi_symptoms_yes)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.gi_symptoms_no)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.gi_symptoms_no)).check(matches(Helpers.withTextColor(R.color.black)));
        onView(withId(R.id.gi_symptoms_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.gi_symptoms_no)).check(matches(Helpers.withTintColorList(R.color.cardinal)));
        onView(withId(R.id.gi_symptoms_no)).check(matches(Helpers.withTextColor(R.color.gold)));
        onView(withId(R.id.gi_symptoms_yes)).check(matches(Helpers.withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.gi_symptoms_yes)).check(matches(Helpers.withTextColor(R.color.black)));
    }

    @Test
    public void TestSubmitNegativeReport() {
        Helpers.UserLogIn("ReportViewStudentTester", "12345678");
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        // check infection buttons
        onView(withId(R.id.infection_no)).perform(Helpers.clickOnNotDisplayed);
        // check chills_fever buttons
        onView(withId(R.id.chills_fever_no)).perform(Helpers.clickOnNotDisplayed);
        // check taste_smell buttons
        onView(withId(R.id.taste_smell_no)).perform(Helpers.clickOnNotDisplayed);
        // check muscle buttons
        onView(withId(R.id.muscle_no)).perform(Helpers.clickOnNotDisplayed);
        // check cough buttons
        onView(withId(R.id.cough_no)).perform(Helpers.clickOnNotDisplayed);
        // check breathing buttons
        onView(withId(R.id.breathing_no)).perform(Helpers.clickOnNotDisplayed);
        // check conjunctivitis buttons
        onView(withId(R.id.conjunctivitis_no)).perform(Helpers.clickOnNotDisplayed);
        // check gi_symptoms buttons
        onView(withId(R.id.gi_symptoms_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        Helpers.checkReportDialogWithText("Success!", "Your form has been recorded.", "Close");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText("Negative"), 1)));
    }

    @Test
    public void TestStudentSubmitPositiveReport() {
        Helpers.UserLogIn("ReportViewStudentTester", "12345678");
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        // check infection buttons
        onView(withId(R.id.infection_yes)).perform(Helpers.clickOnNotDisplayed);
        // check chills_fever buttons
        onView(withId(R.id.chills_fever_no)).perform(Helpers.clickOnNotDisplayed);
        // check taste_smell buttons
        onView(withId(R.id.taste_smell_no)).perform(Helpers.clickOnNotDisplayed);
        // check muscle buttons
        onView(withId(R.id.muscle_no)).perform(Helpers.clickOnNotDisplayed);
        // check cough buttons
        onView(withId(R.id.cough_no)).perform(Helpers.clickOnNotDisplayed);
        // check breathing buttons
        onView(withId(R.id.breathing_no)).perform(Helpers.clickOnNotDisplayed);
        // check conjunctivitis buttons
        onView(withId(R.id.conjunctivitis_no)).perform(Helpers.clickOnNotDisplayed);
        // check gi_symptoms buttons
        onView(withId(R.id.gi_symptoms_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        Helpers.checkReportDialogWithText("Success!", "Your form has been recorded. Please stay home and quarantine for at least 7 full days.", "Close");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText("Positive"), 1)));
    }

    @Test
    public void TestProfessorSubmitPositiveReport() {
        Helpers.UserLogIn("ReportViewProfessorTester", "12345678");
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        // check infection buttons
        onView(withId(R.id.infection_yes)).perform(Helpers.clickOnNotDisplayed);
        // check chills_fever buttons
        onView(withId(R.id.chills_fever_no)).perform(Helpers.clickOnNotDisplayed);
        // check taste_smell buttons
        onView(withId(R.id.taste_smell_no)).perform(Helpers.clickOnNotDisplayed);
        // check muscle buttons
        onView(withId(R.id.muscle_no)).perform(Helpers.clickOnNotDisplayed);
        // check cough buttons
        onView(withId(R.id.cough_no)).perform(Helpers.clickOnNotDisplayed);
        // check breathing buttons
        onView(withId(R.id.breathing_no)).perform(Helpers.clickOnNotDisplayed);
        // check conjunctivitis buttons
        onView(withId(R.id.conjunctivitis_no)).perform(Helpers.clickOnNotDisplayed);
        // check gi_symptoms buttons
        onView(withId(R.id.gi_symptoms_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        Helpers.checkReportDialogWithText("Success!",
                "Your form has been recorded. Please stay home and quarantine for at least 7 full days. All your students have received notification that their courses are changed to online.",
                "Close");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText("Positive"), 1)));
    }
}

