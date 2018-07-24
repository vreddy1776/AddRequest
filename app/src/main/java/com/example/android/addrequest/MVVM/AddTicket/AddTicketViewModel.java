package com.example.android.addrequest.MVVM.AddTicket;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.AWS.DynamoDB.DynamoDB;
import com.example.android.addrequest.AWS.S3.S3bucket;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class AddTicketViewModel extends AndroidViewModel {


    /**
     * Initialize variables.
     */

    // Constant for logging
    private static final String TAG = AddTicketViewModel.class.getSimpleName();

    // RequestsDO member variable for the TicketEntry object wrapped in a LiveData.
    private LiveData<TicketEntry> ticket;

    private int ticketID;

    // Initialize database
    private AppDatabase database;

    // Viewmodel Application context
    private Application application;

    // Video Parameters
    private Context videoContext;
    private File videoFile;



    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the database and the ticketId
     */
    public AddTicketViewModel(Application application, int ticketId) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        this.ticketID = ticketId;
        loadTicket(ticketId);

        this.application = application;

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
    public void addTicket(Context context, final TicketEntry newTicket , boolean boolVideoPost){

        Log.d(TAG, "Test - RequestsDO ID:  " + newTicket.getTicketId());

        /*
        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().insertTicket(newTicket);
            }

        });
        */

        /*
        RdsVolley syncVolley = new RdsVolley();
        syncVolley.add(application, newTicket);
        */

        int id = newTicket.getTicketId();
        String userID = UserProfileSettings.getUserID(context);
        String title = newTicket.getTicketTitle();
        String description = newTicket.getTicketDescription();
        String date = String.valueOf(newTicket.getTicketDate());


        /*
        DynamoDB db = new DynamoDB();
        db.commDynamoDB(context);
        db.createTicket(id, userID, title, description, date);
        */


        // Set up ticket
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tickets");
        FirebaseDbTicket ticket = new FirebaseDbTicket(
                id,
                title,
                description,
                date,
                Integer.toString(id),
                user.getUid(),
                user.getDisplayName(),
                user.getPhotoUrl().toString());
        myRef.child(String.valueOf(id)).setValue(ticket);


        if(boolVideoPost){
            postVideo(String.valueOf(newTicket.getTicketId()));
        }

    }


    /**
     * Change ticket.
     */
    public void changeTicket(Context context, final TicketEntry newTicket, final int mTicketId , boolean boolVideoPost){

        Log.d(TAG, "Test - RequestsDO ID:  " + newTicket.getTicketId());

        newTicket.setTicketId(mTicketId);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().updateTicket(newTicket);
            }
        });

        /*
        RdsVolley syncVolley = new RdsVolley();
        syncVolley.update(application, newTicket);
        */

        int id = newTicket.getTicketId();
        String title = newTicket.getTicketTitle();
        String description = newTicket.getTicketDescription();
        String date = String.valueOf(newTicket.getTicketDate());

        DynamoDB db = new DynamoDB();
        db.commDynamoDB(context);
        db.updateTicket(id, title, description, date);

        if(boolVideoPost){
            postVideo(String.valueOf(newTicket.getTicketId()));
        }

    }


    /**
     * Store video after camera intent.
     */
    private void postVideo(final String nameID) {

        // Run thread to create video file
        AppExecuters.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                S3bucket s3 = new S3bucket();
                s3.accessS3bucket( videoContext , videoFile , nameID );
            }
        });
    }


    /**
     * Store video after camera intent.
     */
    public void storeVideo(Context context, final String filePath){

        // Store AddTicketActivity context for S3 posting when saved
        videoContext = context;

        // Run thread to create video file
        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                videoFile = file;
            }
        });


    }


    /**
    * Getter for the ticket variable.
    */
    public LiveData<TicketEntry> getTicket() {
        return ticket;
    }


    /**
     * Getter for the ticket ID.
     */
    public int getTicketID() {
        return ticketID;
    }

}
