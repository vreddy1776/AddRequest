package com.example.android.addrequest.MVVM.TicketList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.AWS.DynamoDB.DynamoDB;
import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.TicketEntry;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class TicketListViewModel extends AndroidViewModel{

    // Constant for logging
    private static final String TAG = TicketListViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;

    private AppDatabase database;

    public TicketListViewModel(Application application) {
        super(application);

        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the ticket from the DataBase");

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
            }
        });

    }


    public void updateDB(Context context){

        DynamoDB db = new DynamoDB();
        db.commDynamoDB(context);
        db.scanTickets(context);

        tickets = database.ticketDao().loadAllTickets();

    }


    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }

}
