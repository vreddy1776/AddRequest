package com.example.android.addrequest.MVVM.Profile;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.AWS.DynamoDB.DynamoDB;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class ProfileViewModel extends AndroidViewModel{

    // Constant for logging
    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;

    private AppDatabase database;

    private Application application;

    public ProfileViewModel(Application application) {
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
        RdsVolley syncVolley = new RdsVolley();
        syncVolley.select(application);
        */




    }


    public void updateDB(Context context){

        DynamoDB db = new DynamoDB();
        db.commDynamoDB(context);
        db.scanTicketsWithUserID(context, UserProfileSettings.getUserID(context));

        tickets = database.ticketDao().loadAllTickets();

    }


    public void swipeTicket(Context context, final int position, final List<TicketEntry> tickets){

        TicketEntry ticket = tickets.get(position);
        int id = ticket.getId();
        Log.d(TAG, "Test - RequestsDO ID:  " + id);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().deleteTicket(tickets.get(position));
            }
        });

        /*
        RdsVolley syncVolley = new RdsVolley();
        syncVolley.delete(application,id);
        */

        DynamoDB db = new DynamoDB();
        db.commDynamoDB(context);
        db.deleteTicket(id);

    }

    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }

}
