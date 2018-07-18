package com.example.android.addrequest.AddTicketActivityTest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.addrequest.AddTicketActivity;
import com.example.android.addrequest.MainActivity;
import com.example.android.addrequest.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;
import java.net.URISyntaxException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class VideoClickTest {

    private static final String PACKAGE_NAME = "com.android.camera";

    @Rule
    public IntentsTestRule<AddTicketActivity> mActivityRule = new IntentsTestRule<>(AddTicketActivity.class);

    @Before
    public void stubAllExternalIntents() {

        Intent resultData = new Intent();
        String uriString = "";
        URI uri = null;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        resultData.putExtra("df",uri);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

    }

    @Test
    public void clickVideoButton() {

        onView(ViewMatchers.withId(R.id.videoButton)).perform(click());

        intended(toPackage(PACKAGE_NAME));

    }

}
