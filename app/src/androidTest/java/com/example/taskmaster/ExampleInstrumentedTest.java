package com.example.taskmaster;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void changeUsername() {
        // go to settings activity
        onView(withId(R.id.settings)).perform(click());

        // Type text and then press the button.
        onView(withId(R.id.usernameInput)).perform(typeText("Bara'ah"),
                closeSoftKeyboard());
        onView(withId(R.id.save_btn)).perform(click());
    }

    @Test
    public void testingHeaderText (){
        onView(withId(R.id.usernameHeader)).check(matches(withText("Bara'ah Tasks")));
    }

    @Test
    public void addTask() {
        // go to settings activity
        onView(withId(R.id.addTaskButton)).perform(click());

        // Type text and then press the button.
        onView(withId(R.id.title)).perform(typeText("testing task"),
                closeSoftKeyboard());
        onView(withId(R.id.body)).perform(typeText("testing body"),
                closeSoftKeyboard());

        onView(withId(R.id.button)).perform(click());
    }


}