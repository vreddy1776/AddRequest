package com.example.android.addrequest;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.addrequest.database.AppDatabase;

public class AddTicketViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    // Two member variables: one for the database and one for the taskId
    private final AppDatabase mDb;
    private final int mTicketId;


    // Initialize the member variables in the constructor with the parameters received
    public AddTicketViewModelFactory(AppDatabase database, int ticketId) {
        mDb = database;
        mTicketId = ticketId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddTicketViewModel(mDb, mTicketId);
    }


}
