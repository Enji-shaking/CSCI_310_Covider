package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.Checks;

import com.example.covider.MainActivity;
import com.example.covider.R;
import com.example.covider.database.ManagerFactory;
import com.example.covider.database.user.UserManager;
import com.example.covider.model.user.User;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
    public void LogIn(){
        // add users
        UserManager userManager = ManagerFactory.getUserManagerInstance();
        userManager.addOrUpdateUser(new User(100,"Enji", "Aa12345678", 1));
    }

    @Test
    public void TestSubmitEmptyReport() {
        Helpers.StudentUserLogIn();
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        checkReportDialogWithText("Error", "Fill out all forms please", "Cancel");
    }

    @Test
    public void TestSubmitPartiallyFilledReport() {
        Helpers.StudentUserLogIn();
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        onView(withId(R.id.muscle_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.conjunctivitis_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.submit_health_form)).perform(Helpers.clickOnNotDisplayed);
        checkReportDialogWithText("Error", "Fill out all forms please", "Cancel");
    }

    @Test
    public void TestReportSelectionProcess() {
        Helpers.StudentUserLogIn();
        onView(withId(R.id.report)).perform(click());
        onView(withId(R.id.report_view)).check(matches(isDisplayed()));
        // check infection buttons
        onView(withId(R.id.infection_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.infection_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.infection_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.infection_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.infection_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.infection_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.infection_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.infection_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.infection_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.infection_yes)).check(matches(withTextColor(R.color.black)));
        // check chills_fever buttons
        onView(withId(R.id.chills_fever_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.chills_fever_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.chills_fever_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.chills_fever_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.chills_fever_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.chills_fever_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.chills_fever_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.chills_fever_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.chills_fever_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.chills_fever_yes)).check(matches(withTextColor(R.color.black)));
        // check taste_smell buttons
        onView(withId(R.id.taste_smell_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.taste_smell_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.taste_smell_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.taste_smell_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.taste_smell_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.taste_smell_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.taste_smell_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.taste_smell_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.taste_smell_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.taste_smell_yes)).check(matches(withTextColor(R.color.black)));
        // check muscle buttons
        onView(withId(R.id.muscle_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.muscle_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.muscle_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.muscle_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.muscle_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.muscle_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.muscle_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.muscle_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.muscle_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.muscle_yes)).check(matches(withTextColor(R.color.black)));
        // check cough buttons
        onView(withId(R.id.cough_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.cough_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.cough_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.cough_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.cough_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.cough_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.cough_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.cough_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.cough_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.cough_yes)).check(matches(withTextColor(R.color.black)));
        // check breathing buttons
        onView(withId(R.id.breathing_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.breathing_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.breathing_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.breathing_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.breathing_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.breathing_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.breathing_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.breathing_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.breathing_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.breathing_yes)).check(matches(withTextColor(R.color.black)));
        // check conjunctivitis buttons
        onView(withId(R.id.conjunctivitis_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.conjunctivitis_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.conjunctivitis_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.conjunctivitis_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.conjunctivitis_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.conjunctivitis_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.conjunctivitis_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.conjunctivitis_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.conjunctivitis_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.conjunctivitis_yes)).check(matches(withTextColor(R.color.black)));
        // check gi_symptoms buttons
        onView(withId(R.id.gi_symptoms_yes)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.gi_symptoms_yes)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.gi_symptoms_yes)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.gi_symptoms_no)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.gi_symptoms_no)).check(matches(withTextColor(R.color.black)));
        onView(withId(R.id.gi_symptoms_no)).perform(Helpers.clickOnNotDisplayed);
        onView(withId(R.id.gi_symptoms_no)).check(matches(withTintColorList(R.color.cardinal)));
        onView(withId(R.id.gi_symptoms_no)).check(matches(withTextColor(R.color.gold)));
        onView(withId(R.id.gi_symptoms_yes)).check(matches(withTintColorList(R.color.switch_off_track)));
        onView(withId(R.id.gi_symptoms_yes)).check(matches(withTextColor(R.color.black)));
    }

    @Test
    public void TestSubmitNegativeReport() {
        Helpers.StudentUserLogIn();
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
        checkReportDialogWithText("Success!", "Your form has been recorded.", "Close");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText("Negative"), 1)));
    }

    @Test
    public void TestStudentSubmitPositiveReport() {
        Helpers.StudentUserLogIn();
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
        checkReportDialogWithText("Success!", "Your form has been recorded. Please stay home and quarantine for at least 7 full days.", "Close");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText("Positive"), 2)));
    }

    @Test
    public void TestProfessorSubmitPositiveReport() {
        Helpers.ProfessorUserLogIn();
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
        checkReportDialogWithText("Success!",
                "Your form has been recorded. Please stay home and quarantine for at least 7 full days. All your students have received notification that their courses are changed to online.",
                "Close");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));
        onView(isRoot()).check(matches(Helpers.withViewCount(withText("Positive"), 1)));
    }

    public static Matcher<View> withTintColorList(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            public boolean matchesSafely(Button b) {
                return b.getBackgroundTintList().equals(ColorStateList.valueOf(b.getContext().getResources().getColor(color)));
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with tint background color: ");
            }
        };
    }

    public static Matcher<View> withTextColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            public boolean matchesSafely(Button b) {
                return b.getTextColors().equals(ColorStateList.valueOf(b.getContext().getResources().getColor(color)));
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with button text color: ");
            }
        };
    }

    private void checkReportDialogWithText(String title, String message, String option) {
        ViewInteraction dialog = onView(withText(title));
        dialog
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(message))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(option))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(option)).perform(click());
        onView(isRoot()).inRoot(isDialog()).noActivity();
    }
}

