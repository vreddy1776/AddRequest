package project.files.android.addrequest.AddTicketActivityTest;

import android.content.Intent;
import android.database.Cursor;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketViewModel;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.GlobalConstants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class UpdateTicketTest {

    private static final String STRING_TO_BE_TYPED = "Light won't turn on!";
    private AddTicketActivity mAddTicketActivity;
    private TicketListActivity mTicketListActivity;

    @Rule
    public ActivityTestRule mTicketListActivityRule = new ActivityTestRule<>(
            TicketListActivity.class,
            true,
            false);

    @Rule
    public ActivityTestRule mAddTicketActivityRule = new ActivityTestRule<>(
            AddTicketActivity.class,
            true,
            false);

    @Before
    public void intentToActivity() {

        Intent intent = new Intent();
        mTicketListActivityRule.launchActivity(intent);

        mTicketListActivity = (TicketListActivity) mTicketListActivityRule.getActivity();

        //mTicketListActivity.onItemClickListener();

        /*
        Intent intent = new Intent();
        intent.putExtra(GlobalConstants.TICKET_TYPE_KEY, GlobalConstants.ADD_TICKET_TYPE);
        mAddTicketActivityRule.launchActivity(intent);

        mAddTicketActivity = (AddTicketActivity) mAddTicketActivityRule.getActivity();
        */

    }

    @Test
    public void updateTicket() {

        onView(withId(R.id.recyclerViewTickets)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));


        /*
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
        */

    }

}
