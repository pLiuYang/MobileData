package com.assignment.mobiledata

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.assignment.mobiledata.util.RecyclerViewMatcher
import com.assignment.mobiledata.util.ViewVisibilityIdlingResource
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ReposActivityTest {
    // TODO use OkHttp library and mock API response with MockWebServer

    private var progressBarGoneIdlingResource: ViewVisibilityIdlingResource? = null

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun loadRecordsSuccess() {
        activityTestRule.launchActivity(null)
        progressBarGoneIdlingResource = ViewVisibilityIdlingResource(
            activityTestRule.activity.findViewById(R.id.loadingView),
            View.GONE
        )

        IdlingRegistry.getInstance().register(progressBarGoneIdlingResource)
        onView(withId(R.id.recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(
            RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(0, R.id.textViewDataConsumption)
        ).check(ViewAssertions.matches(ViewMatchers.withText("1.543719 (PB) in 2008")))
        onView(
            RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.imageView)
        ).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(
            RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(1, R.id.textViewDataConsumption)
        ).check(ViewAssertions.matches(ViewMatchers.withText("6.228985 (PB) in 2009")))
        onView(
            RecyclerViewMatcher(R.id.recyclerView)
                .atPositionOnView(3, R.id.imageView)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(progressBarGoneIdlingResource)
    }
}
