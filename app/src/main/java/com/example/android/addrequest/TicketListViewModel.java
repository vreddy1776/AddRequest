package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.DynamoAWS.DynamoDB;
import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class TicketListViewModel extends AndroidViewModel{

    // Constant for logging
    private static final String TAG = TicketListViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;

    private AppDatabase database;

    private Application application;

    public TicketListViewModel(Application application) {
        super(application);

        this.application = application;

        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the ticket from the DataBase");

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
            }
        });

        /*
        SyncVolley syncVolley = new SyncVolley();
        syncVolley.select(application);
        */




    }


    public void updateDB(Context context){

        DynamoDB db = new DynamoDB();
        db.accessDynamoDB(context);
        db.scanTickets(context);

        tickets = database.ticketDao().loadAllTickets();

    }


    public void swipeTicket(Context context, final int position, final List<TicketEntry> tickets){

        TicketEntry ticket = tickets.get(position);
        int id = ticket.getId();
        Log.d(TAG, "Test - Ticket ID:  " + id);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().deleteTicket(tickets.get(position));
            }
        });

        /*
        SyncVolley syncVolley = new SyncVolley();
        syncVolley.delete(application,id);
        */

        DynamoDB db = new DynamoDB();
        db.accessDynamoDB(context);
        db.deleteTicket(id);

    }

    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }

}
