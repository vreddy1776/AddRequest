package com.example.android.addrequest.MVVM.TicketList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.addrequest.Adapter.TicketAdapter;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class TicketListViewModel extends AndroidViewModel {

    private static final String TAG = TicketListViewModel.class.getSimpleName();

    private AppDatabase database;
    private LiveData<List<TicketEntry>> ticketListLiveData;
    private Observer<List<TicketEntry>> allTicketsObserver;
    private Observer<List<TicketEntry>> profileObserver;



    public TicketListViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
    }


    public void updateDB(final TicketAdapter ticketAdapter, int updateCode){
        removeObservers();
        if (updateCode == GlobalConstants.LOAD_ALL){
            ticketListLiveData = database.ticketDao().loadAllTickets();
            ticketListLiveData.observeForever(allTicketsObserver = new Observer<List<TicketEntry>>() {
                @Override
                public void onChanged(@Nullable List<TicketEntry> ticketEntryList) {
                    ticketAdapter.setTickets(ticketEntryList);
                }
            });
        } else if (updateCode == GlobalConstants.LOAD_USER) {
            ticketListLiveData = database.ticketDao().loadUserTickets(UserProfileSettings.getUserID(this.getApplication()));
            ticketListLiveData.observeForever(profileObserver = new Observer<List<TicketEntry>>() {
                @Override
                public void onChanged(@Nullable List<TicketEntry> ticketEntryList) {
                    ticketAdapter.setTickets(ticketEntryList);
                }
            });
        } else {
            // No update to ticket DB
        }
    }


    public void deleteTicket(int ticketId){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Tickets").child(String.valueOf(ticketId)).removeValue();

    }


    private void removeObservers(){
        if (profileObserver != null){
            Log.d(TAG,"ProfileObserver removed");
            ticketListLiveData.removeObserver(profileObserver);
        }
        if (allTicketsObserver != null){
            Log.d(TAG,"allTicketObserver removed");
            ticketListLiveData.removeObserver(allTicketsObserver);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeObservers();
    }

}
