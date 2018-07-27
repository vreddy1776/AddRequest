package com.example.android.addrequest.MVVM.AddTicket;

import android.app.Application;
import android.app.Notification;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.addrequest.AWS.S3.S3bucket;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.Notification.Notifications;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.Executor;

public class AddTicketViewModel extends AndroidViewModel {


    /**
     * Initialize variables.
     */

    // Constant for logging
    private static final String TAG = AddTicketViewModel.class.getSimpleName();

    // RequestsDO member variable for the TicketEntry object wrapped in a LiveData.
    private LiveData<TicketEntry> ticket;

    // Initialize database
    private AppDatabase database;


    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the database and the ticketId
     */
    public AddTicketViewModel(Application application, int ticketId) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        loadTicket(ticketId);

    }


    /**
     * Load ticket.
     */
    private void loadTicket(int ticketId){
        ticket = database.ticketDao().loadTicketById(ticketId);
    }


    /**
     * Add ticket.
     */
    public void addTicket(
            final int ticketId,
            final String ticketTitle,
            final String ticketDescription,
            final String ticketDate,
            final String ticketVideoPostId,
            final String ticketVideoLocalUri,
            final String userId,
            final String userName,
            final String userPhotoUrl){

        Log.d(TAG,"ticketVideoPostId:  " + ticketVideoPostId);

        final Context context = this.getApplication();

        if (ticketVideoPostId.equals(GlobalConstants.VIDEO_CREATED_TICKET_VIDEO_POST_ID)){

            Uri capturedVideoUri = Uri.parse(ticketVideoLocalUri);

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference firebaseVideoRef = firebaseStorage.getReference().child("Videos");
            StorageReference localVideoRef = firebaseVideoRef.child(capturedVideoUri.getLastPathSegment());
            UploadTask uploadTask = localVideoRef.putFile(capturedVideoUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    Log.d(TAG,"Exception:  " + exception);

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String ticketVideoInternetUrl;

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();;
                    Log.d(TAG,"download URL:  " + downloadUrl);
                    ticketVideoInternetUrl = downloadUrl.toString();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Tickets");
                    FirebaseDbTicket ticket = new FirebaseDbTicket(
                            ticketId,
                            ticketTitle,
                            ticketDescription,
                            ticketDate,
                            ticketVideoPostId,
                            ticketVideoLocalUri,
                            ticketVideoInternetUrl,
                            userId,
                            userName,
                            userPhotoUrl);
                    myRef.child(String.valueOf(ticketId)).setValue(ticket);
                    Notifications.ticketPostedNotification(context,ticketId);


                }
            });

        } else {

            String ticketVideoInternetUrl = GlobalConstants.DEFAULT_TICKET_VIDEO_INTERNET_URL;

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("Tickets");
            final FirebaseDbTicket ticket = new FirebaseDbTicket(
                    ticketId,
                    ticketTitle,
                    ticketDescription,
                    ticketDate,
                    ticketVideoPostId,
                    ticketVideoLocalUri,
                    ticketVideoInternetUrl,
                    userId,
                    userName,
                    userPhotoUrl);

            AppExecuters.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    myRef.child(String.valueOf(ticketId)).setValue(ticket);
                    Notifications.ticketPostedNotification(context,ticketId);
                }
            });



        }

    }


    /**
    * Getter for the ticket variable.
    */
    public LiveData<TicketEntry> getTicket() {
        return ticket;
    }



}
