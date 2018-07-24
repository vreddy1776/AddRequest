package com.example.android.addrequest.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

public class FirebaseDbListenerService extends Service {

    private static final String TAG = FirebaseDbListenerService.class.getSimpleName();

    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    private AppDatabase database;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Tickets");

        database = AppDatabase.getInstance(this.getApplication());

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
                Log.d(TAG,"Tickets Cleared");

            }
        });

        attachDatabaseReadListener();

    }


    @Override
    public void onDestroy() {
        detachDatabaseReadListener();
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

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            database.ticketDao().insertTicket(ticket);
                        }
                    }).start();

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

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            database.ticketDao().updateTicket(ticket);
                        }
                    }).start();

                }


                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    FirebaseDbTicket firebaseDbTicket = dataSnapshot.getValue(FirebaseDbTicket.class);
                    final int ticketId = firebaseDbTicket.getTicketId();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            database.ticketDao().deleteTicketById(ticketId);
                        }
                    }).start();

                }


                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }


    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


}
