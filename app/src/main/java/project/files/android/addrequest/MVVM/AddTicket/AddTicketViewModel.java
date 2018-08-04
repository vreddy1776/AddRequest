package project.files.android.addrequest.MVVM.AddTicket;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.AppExecuters;
import project.files.android.addrequest.Database.TicketEntry;
import project.files.android.addrequest.Utils.GlobalConstants;

public class AddTicketViewModel extends ViewModel {


    /**
     * Initialize variables.
     */

    // Constant for logging
    private static final String TAG = AddTicketViewModel.class.getSimpleName();

    // RequestsDO member variable for the TicketEntry object wrapped in a LiveData.
    private LiveData<TicketEntry> ticket;

    // Initialize mAppDatabase
    private AppDatabase mAppDatabase;


    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the mAppDatabase and the ticketId
     */
    public void setup(Context context, int ticketId){
        mAppDatabase = AppDatabase.getInstance(context);
        loadTicket(ticketId);
    }


    /**
     * Getter for the ticket variable.
     */
    public LiveData<TicketEntry> getTicket() {
        return ticket;
    }


    /**
     * Load ticket.
     */
    private void loadTicket(int ticketId){
        ticket = mAppDatabase.ticketDao().loadTicketById(ticketId);
    }


    /**
     * Getter for the ticket variable.
     */
    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }


    /**
     * Getter for the ticket variable.
     */
    public void setAppDatabase(AppDatabase appDatabase) {
        this.mAppDatabase =  appDatabase;
    }


    /**
     * Add ticket.
     */
    public void addTicket(final TicketEntry ticket, final int ticketType){

        if (ticket.getTicketVideoPostId().equals(GlobalConstants.VIDEO_CREATED_TICKET_VIDEO_POST_ID)){

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference firebaseVideoRef = firebaseStorage.getReference().child("Videos");

            Uri capturedVideoUri = Uri.parse(ticket.getTicketVideoLocalUri());
            StorageReference localVideoRef = firebaseVideoRef.child(capturedVideoUri.getLastPathSegment());
            UploadTask uploadTask = localVideoRef.putFile(capturedVideoUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ticket.setTicketVideoPostId(GlobalConstants.VIDEO_EXISTS_TICKET_VIDEO_POST_ID);
                    ticket.setTicketVideoInternetUrl(taskSnapshot.getDownloadUrl().toString());

                    addTicketToDb(ticket, ticketType);

                    //Notifications.ticketPostedNotification(context,ticketId);
                }
            });
        } else {

            addTicketToDb(ticket, ticketType);
        }

    }


    private void addTicketToDb(final TicketEntry ticket, final int ticketType){

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                addTicketToLocalDb(ticket,ticketType);
            }
        });
        AppExecuters.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                addTicketToFirebaseDb(ticket,ticketType);
            }
        });
    }


    public void addTicketToLocalDb(final TicketEntry ticket, int ticketType){

        if(ticketType == GlobalConstants.ADD_TICKET_TYPE){
            mAppDatabase.ticketDao().insertTicket(ticket);
        } else {
            mAppDatabase.ticketDao().updateTicket(ticket);
        }
    }


    private void addTicketToFirebaseDb(final TicketEntry ticket, int ticketType){

        FirebaseDatabase fBdatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fBdatabase.getReference("Tickets");
        final FirebaseDbTicket fbTicket = createFirebaseTicket(ticket);
        myRef.child(String.valueOf(ticket.getTicketId())).setValue(fbTicket);
    }


    private FirebaseDbTicket createFirebaseTicket(TicketEntry ticket){

        FirebaseDbTicket fBTicket = new FirebaseDbTicket(
                ticket.getTicketId(),
                ticket.getTicketTitle(),
                ticket.getTicketDescription(),
                ticket.getTicketDate(),
                ticket.getTicketVideoPostId(),
                ticket.getTicketVideoLocalUri(),
                ticket.getTicketVideoInternetUrl(),
                ticket.getUserId(),
                ticket.getUserName(),
                ticket.getUserPhotoUrl());

        return fBTicket;
    }


}
