package com.example.mobiletodoapp.thuyen_services.main_screen;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mobiletodoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WholeTesting {

    @Rule
    public ActivityScenarioRule<PlashActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(PlashActivity.class);

    @Test
    public void wholeTesting() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("phuctq8@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginButton), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view),
                                        0),
                                10),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.my_day),
                        childAtPosition(
                                allOf(withId(R.id.activitiesButtons),
                                        childAtPosition(
                                                withId(R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rcv_uncompleted_list),
                        childAtPosition(
                                withId(R.id.content),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout1),
                                        childAtPosition(
                                                withId(R.id.header),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.rcv_tasksgroup),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                allOf(withId(R.id.header),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.btn_add_tasksgroup),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edt_group_title),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_add_tasksgroup),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("BTL"), closeSoftKeyboard());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.tv_add_tasksgroup), withText("T?o nh�m"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layout_add_tasksgroup),
                                        1),
                                1),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.rcv_tasksgroup),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction linearLayout3 = onView(
                allOf(withId(R.id.btn_show_add_task_layout),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        linearLayout3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edt_task_title),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cl_add_task),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Bai tap lon mob dev"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edt_description),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cl_add_task),
                                        0),
                                10),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("dl hom nay 30/11"), closeSoftKeyboard());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.btn_add_task), withText("Th�m"),
                        childAtPosition(
                                allOf(withId(R.id.cl_add_task),
                                        childAtPosition(
                                                withId(R.id.inc_add_task_layout),
                                                1)),
                                2),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.rcv_task_list),
                        childAtPosition(
                                withId(R.id.content),
                                0)));
        recyclerView4.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout4 = onView(
                allOf(withId(R.id.add_myday),
                        childAtPosition(
                                allOf(withId(R.id.content),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                1)),
                                0),
                        isDisplayed()));
        linearLayout4.perform(click());

        ViewInteraction linearLayout5 = onView(
                allOf(withId(R.id.add_important),
                        childAtPosition(
                                allOf(withId(R.id.content),
                                        childAtPosition(
                                                withId(R.id.constraintLayout),
                                                1)),
                                1),
                        isDisplayed()));
        linearLayout5.perform(click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout1),
                                        childAtPosition(
                                                withId(R.id.header),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                allOf(withId(R.id.header),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageView5.perform(click());

        ViewInteraction linearLayout6 = onView(
                allOf(withId(R.id.calendar),
                        childAtPosition(
                                allOf(withId(R.id.activitiesButtons),
                                        childAtPosition(
                                                withId(R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        linearLayout6.perform(click());

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.calendarRecyclerView),
                        childAtPosition(
                                withId(R.id.mainCalendarView),
                                3)));
        recyclerView5.perform(actionOnItemAtPosition(32, click()));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnAddEvent), withText("S? ki?n m?i"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomSheet),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.switchFullDay),
                        childAtPosition(
                                allOf(withId(R.id.allDayContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                2),
                        isDisplayed()));
        switch_.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.etDescription),
                        childAtPosition(
                                allOf(withId(R.id.descriptionContainer),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                5)),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("di choi voi ny"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnAdd), withText("Th�m"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        pressBack();

        ViewInteraction recyclerView6 = onView(
                allOf(withId(R.id.calendarRecyclerView),
                        childAtPosition(
                                withId(R.id.mainCalendarView),
                                3)));
        recyclerView6.perform(actionOnItemAtPosition(32, click()));

        pressBack();

        ViewInteraction linearLayout7 = onView(
                allOf(withId(R.id.pomodoro),
                        childAtPosition(
                                allOf(withId(R.id.activitiesButtons),
                                        childAtPosition(
                                                withId(R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        linearLayout7.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btn_move_to_pomodoro), withText("B?t ??u l�m vi?c"),
                        childAtPosition(
                                allOf(withId(R.id.content),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                2)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.refreshButton),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.refreshButton),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.play_pause),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageView6 = onView(
                allOf(withId(R.id.configButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.timerLayout),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView6.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.saveConfig), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.configLayout),
                                        2),
                                17),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatImageView7 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.configLayout),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView7.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.pauseButton),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.refreshButton),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withId(R.id.refreshButton),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        ViewInteraction appCompatImageButton7 = onView(
                allOf(withId(R.id.play_pause),
                        childAtPosition(
                                allOf(withId(R.id.timerLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton7.perform(click());

        ViewInteraction appCompatImageView8 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.timerLayout),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageView8.perform(click());

        ViewInteraction appCompatImageView9 = onView(
                allOf(withId(R.id.btn_back_to_previous),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView9.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
