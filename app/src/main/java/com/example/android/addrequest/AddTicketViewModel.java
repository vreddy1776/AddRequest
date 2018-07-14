package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;

public class AddTicketViewModel extends AndroidViewModel {


    /**
     * Initialize variables.
     */

    // Ticket member variable for the TicketEntry object wrapped in a LiveData.
    private LiveData<TicketEntry> ticket;

    private int ticketID;

    // Initialize database
    private AppDatabase database;

    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the database and the ticketId
     */
    public AddTicketViewModel(Application application, int ticketId) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        this.ticketID = ticketId;
        loadTicket(ticketId);

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

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().insertTicket(newTicket);
            }
        });
    }


    /**
     * Change ticket.
     */
    public void changeTicket(final TicketEntry newTicket, final int mTicketId){

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newTicket.setId(mTicketId);
                database.ticketDao().updateTicket(newTicket);
            }
        });

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
