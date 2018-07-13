package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import com.example.android.addrequest.database.AppDatabase;

public class AddTicketViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    // Two member variables: one for the database and one for the ticketId
    private final Application mApplication;
    private final int mTicketId;


    // Initialize the member variables in the constructor with the parameters received
    public AddTicketViewModelFactory(Application application, int ticketId) {
        mApplication = application;
        mTicketId = ticketId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddTicketViewModel(mApplication, mTicketId);
    }


}
