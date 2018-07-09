package com.example.android.addrequest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;

public class AddTicketViewModel extends ViewModel {


    /**
     * Ticket member variable for the TicketEntry object wrapped in a LiveData.
     */
    private LiveData<TicketEntry> ticket;


    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the database and the ticketId
     */
    public AddTicketViewModel(AppDatabase database, int ticketId) {
        ticket = database.ticketDao().loadTicketById(ticketId);
    }


    /**
     * Getter for the ticket variable.
     */
    public LiveData<TicketEntry> getTicket() {
        return ticket;
    }


}
