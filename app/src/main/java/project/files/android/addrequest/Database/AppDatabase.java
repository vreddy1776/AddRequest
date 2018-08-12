package project.files.android.addrequest.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import project.files.android.addrequest.Utils.DateTimeUtils;


/**
 * AddTicket Activity
 *
 * Activity for adding and updating tickets and videos
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
@Database(entities = {Ticket.class}, version = 1, exportSchema = false)
@TypeConverters(DateTimeUtils.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "ticketlist";
    private static AppDatabase sInstance;

    /** Create only one new database instance if not yet already created. */
    public static AppDatabase getInstance(Context context) {

        // Create new database instance
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }

        // Log and return database instance
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;

    }

    /** Create TicketDao object. */
    public abstract TicketDao ticketDao();


    public boolean ticketExists(int ticketId){

        Cursor cursor = sInstance.query("SELECT * FROM ticket WHERE ticketId = " + ticketId,null);
        int cursorCount = cursor.getCount();

        if( cursorCount == 0){
            return false;
        } else {
            return true;
        }

    }


}
