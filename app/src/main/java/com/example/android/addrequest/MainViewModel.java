package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class MainViewModel extends AndroidViewModel{

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the ticket from the DataBase");
        tickets = database.ticketDao().loadAllTickets();
    }

    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }

}
