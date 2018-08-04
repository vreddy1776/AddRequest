package project.files.android.addrequest;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.TicketDao;
import project.files.android.addrequest.Database.TicketEntry;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private TicketDao mTicketDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        //mDb = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        //mTicketDao = mDb.getUserDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        /*
        User user = TestUtil.createUser(3);
        user.setName("george");
        mTicketDao.insert(user);
        List<TicketEntry> byName = mTicketDao.findUsersByName("george");
        assertThat(byName.get(0), equalTo(user));
        */
    }
}