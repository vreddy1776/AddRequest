package com.example.android.addrequest.MVVM.TicketList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.TicketEntry;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class TicketListViewModel extends AndroidViewModel{

    private LiveData<List<TicketEntry>> tickets;
    private AppDatabase database;

    public TicketListViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
    }

    public void updateDB(){
        tickets = database.ticketDao().loadAllTickets();
    }

    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }

}
