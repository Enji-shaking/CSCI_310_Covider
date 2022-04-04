package com.example.covider.frontend;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.util.Checks;

import com.example.covider.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Helpers {
    public static void checkIsVisible(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public static void checkIsGone(int id) {
        onView(withId(id)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
