package project.files.android.addrequest.Background;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.Ticket;
import project.files.android.addrequest.Utils.C;


/**
 * Firebase DB Listenser Service
 *
 * Starts FirebaseDB listener at login and ends at logout.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class VideoUploadService extends Service {


    private static final String TAG = VideoUploadService.class.getSimpleName();

    /*
    private ChildEventListener mChildEventListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private AppDatabase database;
    */



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


        //uploadVideo();

        /*
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Tickets");

        database = AppDatabase.getInstance();

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().clearAllTickets();
            }
        });

        attachDatabaseReadListener();
        */


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Ticket mTicket = (Ticket) Parcels.unwrap(intent.getParcelableExtra(C.TICKET_KEY));
        int mTicketType = (int) intent.getExtras().get(C.TICKET_TYPE_KEY);

        Log.d(TAG,"mTicket:  " + mTicket);

        uploadVideo(mTicket,mTicketType);

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * End listener after logout.
     */
    /*
    @Override
    public void onDestroy() {
        //detachDatabaseReadListener();
    }
    */


    /**
     * Sync additions, updates, and deletions from Firebase DB to local DB.
     */
    private void attachDatabaseReadListener() {
        /*
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    final Ticket ticket = dataSnapshot.getValue(Ticket.class);

                    if( !database.ticketExists(ticket.getTicketId()) ){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.ticketDao().insertTicket(ticket);
                            }
                        }).start();
                    }

                }


                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    final Ticket ticket = dataSnapshot.getValue(Ticket.class);

                    if( database.ticketExists(ticket.getTicketId()) &&
                            !ticket.getUserId().equals(UserProfileSettings.getUserID()) ){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.ticketDao().updateTicket(ticket);
                            }
                        }).start();
                    }

                }


                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    final Ticket ticket = dataSnapshot.getValue(Ticket.class);

                    if( database.ticketExists(ticket.getTicketId()) ){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.ticketDao().deleteTicketById(ticket.getTicketId());
                            }
                        }).start();
                    }

                }


                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}

            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
        */
    }


    /**
     *
     * @see #onDestroy()
     *
     */
    private void detachDatabaseReadListener() {
        /*
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
        */
    }


    private void uploadVideo(final Ticket tempTicket, final int ticketType){

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference firebaseVideoRef = firebaseStorage.getReference().child("Videos");

        Uri capturedVideoUri = Uri.parse(tempTicket.getTicketVideoLocalUri());
        StorageReference localVideoRef = firebaseVideoRef.child(capturedVideoUri.getLastPathSegment());
        UploadTask uploadTask = localVideoRef.putFile(capturedVideoUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                tempTicket.setTicketVideoPostId(C.VIDEO_EXISTS_TICKET_VIDEO_POST_ID);
                tempTicket.setTicketVideoInternetUrl(taskSnapshot.getDownloadUrl().toString());

                addTicketToDb(tempTicket, ticketType);

                Notifications.ticketPostedNotification(tempTicket.getTicketId());
            }
        });

    }




    public void addTicketToDb(final Ticket ticket, final int ticketType){

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                addTicketToLocalDb(ticket,ticketType);
            }
        });
        AppExecuters.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                addTicketToFirebaseDb(ticket);
            }
        });
    }



    public void addTicketToLocalDb(final Ticket ticket, int ticketType){

        if(ticketType == C.ADD_TICKET_TYPE){
            AppDatabase.getInstance().ticketDao().insertTicket(ticket);
        } else {
            AppDatabase.getInstance().ticketDao().updateTicket(ticket);
        }
    }



    private void addTicketToFirebaseDb(final Ticket ticket){

        FirebaseDatabase fBdatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fBdatabase.getReference("Tickets");
        myRef.child(String.valueOf(ticket.getTicketId())).setValue(ticket);

    }



}
