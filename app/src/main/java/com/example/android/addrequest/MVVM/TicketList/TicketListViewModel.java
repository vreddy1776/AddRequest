package com.example.android.addrequest.MVVM.TicketList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.AddTicket.FirebaseDbTicket;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * ViewModel for MainActivity.
 */

public class TicketListViewModel extends AndroidViewModel{

    // Constant for logging
    private static final String TAG = TicketListViewModel.class.getSimpleName();

    private LiveData<List<TicketEntry>> tickets;

    private AppDatabase database;

    private ChildEventListener mChildEventListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    public TicketListViewModel(Application application) {
        super(application);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Tickets");

        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the ticket from the DataBase");

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
            }
        });

        attachDatabaseReadListener();


    }


    public void updateDB(Context context){

        /*
        DynamoDB db = new DynamoDB();
        db.commDynamoDB(context);
        db.scanTickets(context);
        */

        tickets = database.ticketDao().loadAllTickets();

    }


    public LiveData<List<TicketEntry>> getTickets() {
        return tickets;
    }



    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    FirebaseDbTicket firebaseDbTicket = dataSnapshot.getValue(FirebaseDbTicket.class);
                    final TicketEntry ticket = new TicketEntry(
                            firebaseDbTicket.getTicketId(),
                            firebaseDbTicket.getTicketTitle(),
                            firebaseDbTicket.getTicketDescription(),
                            firebaseDbTicket.getTicketDate(),
                            firebaseDbTicket.getTicketVideoId(),
                            firebaseDbTicket.getUserId(),
                            firebaseDbTicket.getUserName(),
                            firebaseDbTicket.getUserPhotoUrl());

                    AppExecuters.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.ticketDao().insertTicket(ticket);
                        }

                    });

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    FirebaseDbTicket firebaseDbTicket = dataSnapshot.getValue(FirebaseDbTicket.class);
                    final TicketEntry ticket = new TicketEntry(
                            firebaseDbTicket.getTicketId(),
                            firebaseDbTicket.getTicketTitle(),
                            firebaseDbTicket.getTicketDescription(),
                            firebaseDbTicket.getTicketDate(),
                            firebaseDbTicket.getTicketVideoId(),
                            firebaseDbTicket.getUserId(),
                            firebaseDbTicket.getUserName(),
                            firebaseDbTicket.getUserPhotoUrl());

                    AppExecuters.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.ticketDao().updateTicket(ticket);
                        }

                    });

                }
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    FirebaseDbTicket firebaseDbTicket = dataSnapshot.getValue(FirebaseDbTicket.class);
                    final int ticketId = firebaseDbTicket.getTicketId();

                    AppExecuters.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.ticketDao().deleteTicketById(ticketId);
                        }
                    });

                }
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }



}
