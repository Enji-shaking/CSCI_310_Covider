package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.core.internal.deps.guava.base.Predicate;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.core.internal.deps.guava.collect.Lists;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.TreeIterables;

import com.example.covider.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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

    protected static void StudentUserLogIn() {
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Enji"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("Aa12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
    }

    protected static void ProfessorUserLogIn() {
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText("Negar"));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText("12345678"));
        onView(withId(R.id.log_in_submit)).perform(click());
    }

    protected static void checkIsVisible(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    protected static void checkIsGone(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    protected static Matcher<View> withLinearLayoutSize (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((LinearLayout) view).getChildCount() == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("LinearLayout should have " + size + " items");
            }
        };
    }

    protected static Matcher<View> withViewCount(final Matcher<View> viewMatcher, final int expectedCount) {
        return new TypeSafeMatcher<View>() {
            int actualCount = -1;

            @Override
            public void describeTo(Description description) {
                if (actualCount >= 0) {
                    description.appendText("With expected number of items: " + expectedCount);
                    description.appendText("\n With matcher: ");
                    viewMatcher.describeTo(description);
                    description.appendText("\n But got: " + actualCount);
                }
            }

            @Override
            protected boolean matchesSafely(View root) {
                actualCount = 0;
                Iterable<View> iterable = TreeIterables.breadthFirstViewTraversal(root);
                actualCount = Lists.newArrayList(Iterables.filter(iterable, withMatcherPredicate(viewMatcher))).size();
                return actualCount == expectedCount;
            }
        };
    }

    private static Predicate<View> withMatcherPredicate(final Matcher<View> matcher) {
        return new Predicate<View>() {
            @Override
            public boolean apply(@Nullable View view) {
                return matcher.matches(view);
            }
        };
    }
}
