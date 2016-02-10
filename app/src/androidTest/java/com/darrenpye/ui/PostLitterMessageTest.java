package com.darrenpye.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.darrenpye.R;
import com.darrenpye.litter.LitterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by darrenpye on 16-02-10.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PostLitterMessageTest {

    // Test Message to post
    private String mTestMessage ;


    @Rule
    public ActivityTestRule<LitterActivity> mActivityRule = new ActivityTestRule<>(
            LitterActivity.class);


    @Before
    public void setupTest() {
        // The message that will be posted
        mTestMessage = "Post Litter Test - Performed on "+ (new SimpleDateFormat().format(new Date())) + " - Ignore me!";
    }

    @Test
    public void test_PostLitterMessage() {

        // NOTE: This test assumes the username and password are filled in. The steps to do this could be added here
        // but for the sake of this excerise I'm pre-filling the username and password so whomever evaluates
        // the app doesn't have to bother or know what to use.

        // Click Sign in
        onView(withId(R.id.sign_in_button)).perform(click());

        // Press the litter button
        onView(withId(R.id.litterButton)).perform(click());

        // Enter the test litter post
        onView(withId(R.id.litterMessage)).perform(typeText(mTestMessage), closeSoftKeyboard());

        // Press the post litter button
        onView(withId(R.id.postLitter)).perform(click());
    }
}
