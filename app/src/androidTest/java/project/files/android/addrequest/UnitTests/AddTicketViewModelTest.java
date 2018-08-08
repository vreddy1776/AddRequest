package project.files.android.addrequest.UnitTests;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.TicketEntry;
import project.files.android.addrequest.Activity.AddTicket.AddTicketViewModel;
import project.files.android.addrequest.Utils.GlobalConstants;


@RunWith(AndroidJUnit4.class)
public class AddTicketViewModelTest extends Assert {


    private AddTicketViewModel mAddTicketViewModel;
    private TicketEntry mInsertedTicket;
    private TicketEntry mUpdatedTicket;
    private AppDatabase mDatabase;


    @Before
    public void setup() {

        mAddTicketViewModel = new AddTicketViewModel();

        mInsertedTicket = new TicketEntry(
                104601350,
                "Test Ticket Title",
                "test ticket description",
                "08-17-2016",
                "no_video",
                "no_video_local_uri",
                "no_video_internet_url",
                "234f783fdsb2248ahH2",
                "Bob Smith",
                "http://testurl.com");

        mUpdatedTicket = new TicketEntry(
                104601350,
                "Other Title",
                "some other description",
                "11-21-2003",
                "video_created",
                "1543.local.uri",
                "http://video.com",
                "dfe764dbhdng99",
                "Joey Miller",
                "http://userPhoto.com");

        Context context = InstrumentationRegistry.getTargetContext();
        mDatabase = AppDatabase.getInstance(context);
        mDatabase.ticketDao().clearAllTickets();
        mAddTicketViewModel.setAppDatabase(mDatabase);

    }


    @Test
    public void addTicketTest() {

        mAddTicketViewModel.addTicketToLocalDb(mInsertedTicket, GlobalConstants.ADD_TICKET_TYPE);
        AppDatabase actualAppDatabase = mAddTicketViewModel.getAppDatabase();

        Cursor cursor = actualAppDatabase.query("SELECT * FROM ticket WHERE ticketId = " + mInsertedTicket.getTicketId(),null);
        String actualTicketTitle = "fail";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            actualTicketTitle = cursor.getString(cursor.getColumnIndex("ticketTitle"));
        }

        assertEquals(mInsertedTicket.getTicketTitle(),actualTicketTitle);

    }



    @Test
    public void modifyTicketTest() {

        mAddTicketViewModel.addTicketToLocalDb(mInsertedTicket, GlobalConstants.ADD_TICKET_TYPE);
        mAddTicketViewModel.addTicketToLocalDb(mUpdatedTicket, GlobalConstants.UPDATE_TICKET_TYPE);
        AppDatabase actualAppDatabase = mAddTicketViewModel.getAppDatabase();

        Cursor cursor = actualAppDatabase.query("SELECT * FROM ticket WHERE ticketId = " + mUpdatedTicket.getTicketId(),null);
        String actualTicketTitle = "fail";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            actualTicketTitle = cursor.getString(cursor.getColumnIndex("ticketTitle"));
        }

        assertEquals(mUpdatedTicket.getTicketTitle(),actualTicketTitle);

    }



}
