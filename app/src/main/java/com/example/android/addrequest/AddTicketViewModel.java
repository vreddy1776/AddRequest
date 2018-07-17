package com.example.android.addrequest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;
import com.example.android.addrequest.sync.SyncVolley;
import com.example.android.addrequest.utils.GenerateID;
import com.example.android.addrequest.utils.S3bucket;
import com.example.android.addrequest.utils.Video;

import java.io.File;
import java.util.Date;

public class AddTicketViewModel extends AndroidViewModel {


    /**
     * Initialize variables.
     */

    // Constant for logging
    private static final String TAG = AddTicketViewModel.class.getSimpleName();

    // Ticket member variable for the TicketEntry object wrapped in a LiveData.
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
    public void addTicket(final TicketEntry newTicket , boolean boolVideoPost){

        Log.d(TAG, "Test - Ticket ID:  " + newTicket.getId());

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().insertTicket(newTicket);
            }

        });

        SyncVolley syncVolley = new SyncVolley();
        syncVolley.add(application, newTicket);

        if(boolVideoPost){
            postVideo(String.valueOf(newTicket.getId()));
        }

    }


    /**
     * Change ticket.
     */
    public void changeTicket(final TicketEntry newTicket, final int mTicketId , boolean boolVideoPost){

        Log.d(TAG, "Test - Ticket ID:  " + newTicket.getId());

        newTicket.setId(mTicketId);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().updateTicket(newTicket);
            }
        });

        SyncVolley syncVolley = new SyncVolley();
        syncVolley.update(application, newTicket);

        if(boolVideoPost){
            postVideo(String.valueOf(newTicket.getId()));
        }

    }


    /**
     * Store video after camera intent.
     */
    private void postVideo(String nameID) {

        S3bucket s3 = new S3bucket();
        s3.accessS3bucket( videoContext , videoFile , nameID );
    }


    /**
     * Store video after camera intent.
     */
    public void storeVideoInfo(Context context, File file){
        videoContext = context;
        videoFile = file;
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
