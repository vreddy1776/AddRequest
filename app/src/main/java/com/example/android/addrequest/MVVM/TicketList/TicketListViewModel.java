package com.example.android.addrequest.MVVM.TicketList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class TicketListViewModel extends AndroidViewModel{

    private static final String TAG = TicketListViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;
    private AppDatabase database;


    public TicketListViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());

    }

    public void updateDB(int updateCode){
        if (updateCode == GlobalConstants.LOAD_ALL){
            tickets = database.ticketDao().loadAllTickets();
        } else if (updateCode == GlobalConstants.LOAD_USER) {
            tickets = database.ticketDao().loadUserTickets(UserProfileSettings.getUserID(this.getApplication()));
        } else {
            // No update to ticket DB
        }
    }

    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }


    public void deleteTicket(int ticketId){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Tickets").child(String.valueOf(ticketId)).removeValue();

    }

}
