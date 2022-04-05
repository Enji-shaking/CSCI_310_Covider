package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.covider.R;

import org.hamcrest.Matcher;

public class Helpers {
    // https://stackoverflow.com/questions/28834579/click-on-not-fully-visible-imagebutton-with-espresso
    protected static ViewAction clickOnNotDisplayed = new ViewAction() {
        @Override
        public Matcher<View> getConstraints() {
            return ViewMatchers.isEnabled(); // no constraints, they are checked above
        }

        @Override
        public String getDescription() {
            return "click plus button";
        }

        @Override
        public void perform(UiController uiController, View view) {
            view.performClick();
        }
    };

    public static void StudentUserLogIn() {
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Enji"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("Aa12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
    }

    public static void ProfessorUserLogIn() {
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Negar"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
    }

    public static void checkIsVisible(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public static void checkIsGone(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
