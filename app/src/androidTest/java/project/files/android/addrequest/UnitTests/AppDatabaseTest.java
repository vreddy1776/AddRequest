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
import project.files.android.addrequest.Database.Ticket;


@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest extends Assert {


    private Ticket mInsertedTicket;
    private Ticket mUpdatedTicket;
    private AppDatabase mDatabase;


    @Before
    public void setup() {

        mInsertedTicket = new Ticket(
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

        mUpdatedTicket = new Ticket(
                104601350,
                "Other Title",
                "some other description",
                "11-21-2003",
                "video_created",
                "1543.local.uri",
                "http://video.com",
                "dfe764dbhdng99",
                "Bob Smith",
                "http://userPhoto.com");

        mDatabase = AppDatabase.getInstance();
    }


    @Test
    public void insertTicketTest() {

        mDatabase.ticketDao().clearAllTickets();
        mDatabase.ticketDao().insertTicket(mInsertedTicket);

        Cursor cursor = mDatabase.query("SELECT * FROM ticket WHERE ticketId = " + mInsertedTicket.getTicketId(),null);
        String actualTicketTitle = "fail";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            actualTicketTitle = cursor.getString(cursor.getColumnIndex("ticketTitle"));
        }

        assertEquals(mInsertedTicket.getTicketTitle(),actualTicketTitle);
    }


    @Test
    public void updateTicketTest() {

        mDatabase.ticketDao().clearAllTickets();
        mDatabase.ticketDao().insertTicket(mInsertedTicket);
        mDatabase.ticketDao().updateTicket(mUpdatedTicket);

        Cursor cursor = mDatabase.query("SELECT * FROM ticket WHERE ticketId = " + mUpdatedTicket.getTicketId(),null);
        String actualTicketTitle = "fail";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            actualTicketTitle = cursor.getString(cursor.getColumnIndex("ticketTitle"));
        }

        assertEquals(mUpdatedTicket.getTicketTitle(),actualTicketTitle);
    }


    @Test
    public void deleteTicketTest() {

        mDatabase.ticketDao().clearAllTickets();
        mDatabase.ticketDao().insertTicket(mInsertedTicket);
        mDatabase.ticketDao().deleteTicketById(mInsertedTicket.getTicketId());
        assertFalse(mDatabase.ticketExists(mInsertedTicket.getTicketId()));

    }


}
