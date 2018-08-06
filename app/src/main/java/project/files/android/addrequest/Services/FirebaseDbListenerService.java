package project.files.android.addrequest.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.AppExecuters;
import project.files.android.addrequest.Database.TicketEntry;
import project.files.android.addrequest.Database.FirebaseDbTicket;
import project.files.android.addrequest.Settings.UserProfileSettings;


/**
 * Firebase DB Listenser Service
 *
 * Starts FirebaseDB listener at login and ends at logout.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
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


    /**
     * Start after login by clearing tickets in local DB starting the read listener.
     */
    @Override
    public void onCreate() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Tickets");

        database = AppDatabase.getInstance(this.getApplication());

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
            }
        });

        attachDatabaseReadListener();

    }


    /**
     * End listener after logout.
     */
    @Override
    public void onDestroy() {
        detachDatabaseReadListener();
    }


    /**
     * Sync additions, updates, and deletions from Firebase DB to local DB.
     */
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
                            firebaseDbTicket.getTicketVideoPostId(),
                            firebaseDbTicket.getTicketVideoLocalUri(),
                            firebaseDbTicket.getTicketVideoInternetUrl(),
                            firebaseDbTicket.getUserId(),
                            firebaseDbTicket.getUserName(),
                            firebaseDbTicket.getUserPhotoUrl());

                    final int ticketId = firebaseDbTicket.getTicketId();
                    if( !database.ticketExists(ticketId) ){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.ticketDao().insertTicket(ticket);
                            }
                        }).start();
                    }

                }


                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    FirebaseDbTicket firebaseDbTicket = dataSnapshot.getValue(FirebaseDbTicket.class);
                    final TicketEntry ticket = new TicketEntry(
                            firebaseDbTicket.getTicketId(),
                            firebaseDbTicket.getTicketTitle(),
                            firebaseDbTicket.getTicketDescription(),
                            firebaseDbTicket.getTicketDate(),
                            firebaseDbTicket.getTicketVideoPostId(),
                            firebaseDbTicket.getTicketVideoLocalUri(),
                            firebaseDbTicket.getTicketVideoInternetUrl(),
                            firebaseDbTicket.getUserId(),
                            firebaseDbTicket.getUserName(),
                            firebaseDbTicket.getUserPhotoUrl());

                    final int ticketId = firebaseDbTicket.getTicketId();
                    final String userId = firebaseDbTicket.getUserId();

                    if( (database.ticketExists(ticketId)) && (!userId.equals(UserProfileSettings.getUserID(getApplicationContext()))) ){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.ticketDao().updateTicket(ticket);
                            }
                        }).start();
                    }

                }


                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    FirebaseDbTicket firebaseDbTicket = dataSnapshot.getValue(FirebaseDbTicket.class);
                    final int ticketId = firebaseDbTicket.getTicketId();

                    if( database.ticketExists(ticketId) ){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.ticketDao().deleteTicketById(ticketId);
                            }
                        }).start();
                    }

                }


                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }


    /**
     *
     * @see #onDestroy()
     *
     */
    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


}
