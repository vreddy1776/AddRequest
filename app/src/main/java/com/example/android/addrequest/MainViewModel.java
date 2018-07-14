package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;
import com.example.android.addrequest.sync.SyncVolley;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class MainViewModel extends AndroidViewModel{

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;

    private AppDatabase database;

    public MainViewModel(Application application) {
        super(application);

        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the ticket from the DataBase");

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
            }
        });

        SyncVolley syncVolley = new SyncVolley();
        syncVolley.select(application);

        tickets = database.ticketDao().loadAllTickets();


    }

    public void swipeTicket(final int position, final List<TicketEntry> tickets){
        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().deleteTicket(tickets.get(position));
            }
        });
    }

    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }

}
