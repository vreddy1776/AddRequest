package com.example.android.addrequest.MVVM.AddTicket;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.android.addrequest.AWS.S3.S3bucket;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Database.TicketEntry;
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
    public void addTicket(
            int ticketId,
            String ticketTitle,
            String ticketDescription,
            String ticketDate,
            String ticketVideoId,
            String userId,
            String userName,
            String userPhotoUrl){

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tickets");
        FirebaseDbTicket ticket = new FirebaseDbTicket(
                ticketId,
                ticketTitle,
                ticketDescription,
                ticketDate,
                ticketVideoId,
                userId,
                userName,
                userPhotoUrl);
        myRef.child(String.valueOf(ticketId)).setValue(ticket);

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
