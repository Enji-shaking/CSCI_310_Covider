package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.core.internal.deps.guava.base.Predicate;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
import androidx.test.espresso.core.internal.deps.guava.collect.Lists;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.internal.util.Checks;

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

    protected static void UserLogIn(String username, String password) {
        onView(withId(R.id.log_in_username))
                .perform(clearText(), replaceText(username));
        onView(withId(R.id.log_in_password))
                .perform(clearText(), replaceText(password));
        onView(withId(R.id.log_in_submit)).perform(click());
    }

    protected static void StudentUserLogIn() {
        UserLogIn("Enji", "12345678");
    }

    protected static void ProfessorUserLogIn() {
        UserLogIn("Negar", "12345678");
    }

    protected static void checkIsVisible(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    protected static void checkIsGone(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    protected static Matcher<View> withTextColor(final int color) {
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

    protected static Matcher<View> withButtonString (final String str) {
        Checks.checkNotNull(str);
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            public boolean matchesSafely(Button b) {
                return String.valueOf(b.getText()).equals(str);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("Button with text: ");
            }
        };
    }

    protected static Matcher<View> withTextviewString (final String str) {
        Checks.checkNotNull(str);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public boolean matchesSafely(TextView t) {
                return String.valueOf(t.getText()).equals(str);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("TextView with text: ");
            }
        };
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
