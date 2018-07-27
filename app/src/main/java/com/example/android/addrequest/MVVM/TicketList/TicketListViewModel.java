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
    private LiveData<List<TicketEntry>> ticketLiveData;
    private List<TicketEntry> ticketList;
    private Observer<List<TicketEntry>> allTicketObserver;
    private Observer<List<TicketEntry>> profileObserver;



    public TicketListViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        //updateDB(GlobalConstants.LOAD_ALL);
        //startTicketListObserver();
    }


    public void updateDB(final TicketAdapter ticketAdapter, int updateCode){
        if (updateCode == GlobalConstants.LOAD_ALL){
            if (profileObserver != null){
                Log.d(TAG,"ProfileObserver removed");
                ticketLiveData.removeObserver(profileObserver);
            }
            ticketLiveData = database.ticketDao().loadAllTickets();
            ticketLiveData.observeForever(allTicketObserver = new Observer<List<TicketEntry>>() {
                @Override
                public void onChanged(@Nullable List<TicketEntry> ticketEntryList) {
                    ticketAdapter.setTickets(ticketEntryList);
                }
            });
        } else if (updateCode == GlobalConstants.LOAD_USER) {
            if (profileObserver != null){
                Log.d(TAG,"allTicketObserver removed");
                ticketLiveData.removeObserver(allTicketObserver);
            }
            ticketLiveData = database.ticketDao().loadUserTickets(UserProfileSettings.getUserID(this.getApplication()));
            ticketLiveData.observeForever(profileObserver = new Observer<List<TicketEntry>>() {
                @Override
                public void onChanged(@Nullable List<TicketEntry> ticketEntryList) {
                    ticketAdapter.setTickets(ticketEntryList);
                }
            });
        } else {
            // No update to ticket DB
        }
    }



    /*
    public void startTicketListObserver(final TicketAdapter adapter) {

        ticketLiveData.observeForever(new Observer<List<TicketEntry>>() {
            @Override
            public void onChanged(@Nullable List<TicketEntry> ticketEntryList) {
                Log.d(TAG, "Ticket list observer changed");
                ticketList = ticketEntryList;
                adapter.setTickets(ticketEntryList);
            }
        });

    }


    public LiveData<List<TicketEntry>> getTicketLiveData() {
        Log.d(TAG, "Getting ticket list" + ticketLiveData.getValue());
        return ticketLiveData;
    }


    public List<TicketEntry> getTicketList() {
        Log.d(TAG, "Getting ticket list" + ticketList);
        return ticketList;
    }
    */


    public void deleteTicket(int ticketId){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Tickets").child(String.valueOf(ticketId)).removeValue();

    }


}
