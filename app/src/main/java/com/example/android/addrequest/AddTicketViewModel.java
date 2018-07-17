package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;
import com.example.android.addrequest.sync.SyncVolley;
import com.example.android.addrequest.utils.GenerateID;
import com.example.android.addrequest.utils.S3bucket;

import java.util.Date;

public class AddTicketViewModel extends AndroidViewModel {


    /**
     * Initialize variables.
     */

    // Constant for logging
    private static final String TAG = AddTicketViewModel.class.getSimpleName();

    // Ticket member variable for the TicketEntry object wrapped in a LiveData.
    private LiveData<TicketEntry> ticket;

    private int ticketID;

    // Initialize database
    private AppDatabase database;

    // Viewmodel Application context
    private Application application;


    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the database and the ticketId
     */
    public AddTicketViewModel(Application application, int ticketId) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        this.ticketID = ticketId;
        loadTicket(ticketId);

        this.application = application;

    }


    /**
     * Load ticket.
     */
    private void loadTicket(int ticketId){
        ticket = database.ticketDao().loadTicketById(ticketId);
    }


    /**
     * Add ticket.
     */
    public void addTicket(final TicketEntry newTicket){

        Log.d(TAG, "Test - Ticket ID:  " + newTicket.getId());

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().insertTicket(newTicket);
            }

        });

        SyncVolley syncVolley = new SyncVolley();
        syncVolley.add(application, newTicket);

    }


    /**
     * Change ticket.
     */
    public void changeTicket(final TicketEntry newTicket, final int mTicketId){

        Log.d(TAG, "Test - Ticket ID:  " + newTicket.getId());

        newTicket.setId(mTicketId);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().updateTicket(newTicket);
            }
        });

        SyncVolley syncVolley = new SyncVolley();
        syncVolley.update(application, newTicket);

    }


    /**
     * Getter for the ticket variable.
     */
    public LiveData<TicketEntry> getTicket() {
        return ticket;
    }


    /**
     * Getter for the ticket ID.
     */
    public int getTicketID() {
        return ticketID;
    }


}
