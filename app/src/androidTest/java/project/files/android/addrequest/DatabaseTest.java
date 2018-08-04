package project.files.android.addrequest;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.TicketEntry;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketViewModel;

import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest extends Assert {

    private AddTicketViewModel mViewModel;
    private Activity mActivity;
    private TicketEntry mTicket;
    private AppDatabase mDatabase;

    @Before
    public void setup() {

        mActivity = mock(Activity.class);
        mViewModel = new AddTicketViewModel();
        mTicket = new TicketEntry(
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

        Context context = InstrumentationRegistry.getTargetContext();
        //mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mDatabase = AppDatabase.getInstance(context);
    }

    @Test
    public void insertTicketTest() {
        //viewModel.onClickButtonClicks(activity);
        //verify(mActivity).startActivity(new Intent(activity, ClickCountActivity.class));
        mDatabase.ticketDao().clearAllTickets();
        mDatabase.ticketDao().insertTicket(mTicket);

        Cursor cursor = mDatabase.query("SELECT * FROM ticket WHERE ticketId = " + 104601350,null);
        //cursor.getString(cursor.getColumnIndex("ticketTitle"));
        String actualTicketTitle = "fail";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            actualTicketTitle = cursor.getString(cursor.getColumnIndex("ticketTitle"));
        }



        //cursor.getString(0);

        /*
        LiveData<TicketEntry> receivedLiveData = mDatabase.ticketDao().loadTicketById(104601350);
        final TicketEntry[] receivedTicket = new TicketEntry[1];
        receivedLiveData.observeForever(new Observer<TicketEntry>() {
            @Override
            public void onChanged(@Nullable TicketEntry ticketEntry) {
                receivedTicket[0] = ticketEntry;
            }
        });
        */

        //TicketEntry receivedTicket = receivedLiveData.getValue();
        assertEquals(mTicket.getTicketTitle(),actualTicketTitle);

    }


}
