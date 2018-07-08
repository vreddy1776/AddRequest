package com.example.android.addrequest.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

/**
 * Master local database.
 */

@Database(entities = {TicketEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
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

}
