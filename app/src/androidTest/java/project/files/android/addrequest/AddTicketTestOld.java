package project.files.android.addrequest;

import android.content.Intent;
import android.database.Cursor;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketViewModel;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.Utils.GlobalConstants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class AddTicketTestOld {

    private static final String STRING_TO_BE_TYPED = "Light won't turn on!";
    private TicketListActivity mTicketListActivity;
    private AddTicketActivity mAddTicketActivity;

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Rule
    public ActivityTestRule mTicketListActivityRule = new ActivityTestRule<>(
            TicketListActivity.class,
            true,
            true);

    @Rule
    public ActivityTestRule mAddTicketActivityRule = new ActivityTestRule<>(
            AddTicketActivity.class,
            true,    // initialTouchMode
            false);  // launchActivity. False to set intent.

    @Before
    public void intentToActivity() {

        mTicketListActivity = (TicketListActivity) mTicketListActivityRule.getActivity();

        Intent intent = new Intent();
        intent.putExtra(GlobalConstants.TICKET_TYPE_KEY, GlobalConstants.ADD_TICKET_TYPE);
        mAddTicketActivityRule.launchActivity(intent);

        mAddTicketActivity = (AddTicketActivity) mAddTicketActivityRule.getActivity();
    }

    @Test
    public void addTicket() {

        onView(withId(R.id.editTextTicketTitle))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        onView(withId(R.id.saveButton))
                .perform(click());

        AddTicketViewModel addTicketViewModel = mAddTicketActivity.getViewModel();
        int ticketId = mAddTicketActivity.getTicketId();
        AppDatabase actualAppDatabase = addTicketViewModel.getAppDatabase();

        Cursor cursor = actualAppDatabase.query("SELECT * FROM ticket WHERE ticketId = " + ticketId,null);
        String actualTicketTitle = "fail";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            actualTicketTitle = cursor.getString(cursor.getColumnIndex("ticketTitle"));
        }

        String expectedTicketTitle = STRING_TO_BE_TYPED;
        assertEquals(expectedTicketTitle,actualTicketTitle);

        onView(withItemText(STRING_TO_BE_TYPED)).check(matches(isDisplayed()));


    }

}
