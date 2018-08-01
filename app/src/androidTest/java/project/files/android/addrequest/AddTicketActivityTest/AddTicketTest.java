package project.files.android.addrequest.AddTicketActivityTest;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class AddTicketTest {

    private static final String TICKET_ID_KEY = "TicketId";
    private static final int DEFAULT_TICKET_ID = -1;
    private static final String PACKAGE_NAME = InstrumentationRegistry.getTargetContext().getPackageName();
    private static final String STRING_TO_BE_TYPED = "Light won't turn on!";


    @Rule
    public IntentsTestRule<TicketListActivity> mActivityRule = new IntentsTestRule<>(
            TicketListActivity.class);


    @Before
    public void stubAllExternalIntents() {

        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickTicket() {

        onView(ViewMatchers.withId(R.id.fab)).perform(click());

        onView(withId(R.id.editTextTicketTitle))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        onView(withId(R.id.saveButton))
                .perform(click());

        //onView(anyOf(withId(R.id.ticketlist_fragment_container),isDisplayed())).check(matches(withText("Light won't turn on!")));
        //onData(allOf(withId(R.id.ticketTitle),isDisplayed())).check(matches(withText("Light won't turn on!")));

        /*
        onData(anyOf(withId(R.id.ticketlist_fragment_container),isDisplayed())) // Use Hamcrest matchers to match item
                .inAdapterView(withId(R.id.recyclerViewTickets)) // Specify the explicit id of the ListView
                .check(matches(withText("Light won't turn on!")));
                */

        onData(allOf(is(instanceOf(String.class)),isDisplayed())).check(matches((withText("Light won't turn on!"))));


    }

}
