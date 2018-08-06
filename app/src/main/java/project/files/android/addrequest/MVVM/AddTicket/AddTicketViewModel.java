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
import project.files.android.addrequest.Notification.Notifications;
import project.files.android.addrequest.Utils.GlobalConstants;



/**
 * ViewModel for AddTicketActivity
 *
 * This viewmodel transfers data from local and remote DBs to the AddTicketActivtity for a
 * given ticket.
 *
 * @author Vijay T. Reddy
 * @version 2010.1105
 */
public class AddTicketViewModel extends ViewModel {


    private static final String TAG = AddTicketViewModel.class.getSimpleName();
    private LiveData<TicketEntry> mLiveDataTicket;
    private AppDatabase mAppDatabase;


    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the mAppDatabase and the ticketId
     */
    public void setup(Context context, int ticketId){
        mAppDatabase = AppDatabase.getInstance(context);
        loadLiveDataTicket(ticketId);
    }


    /**
     * Get LivaData ticket from AppDatabase to be observed in AddTicketActivity
     *
     * @param ticketId The ticket ID for the AddTicketActivity session.
     */
    private void loadLiveDataTicket(int ticketId){
        mLiveDataTicket = mAppDatabase.ticketDao().loadTicketById(ticketId);
    }


    /**
     * Getter for LiveDataTicket.
     */
    public LiveData<TicketEntry> getLiveDataTicket() {
        return mLiveDataTicket;
    }


    /**
     * Setter for LiveDataTicket.
     */
    public void setLiveDataTicket(LiveData<TicketEntry> liveDataTicket) {
        mLiveDataTicket = liveDataTicket;
    }


    /**
     * Getter for AppDatabase.
     */
    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }


    /**
     * Setter for AppDatabase.
     */
    public void setAppDatabase(AppDatabase appDatabase) {
        this.mAppDatabase =  appDatabase;
    }


    /**
     * Adds or updates ticket to local and remote DBs with video upload.
     *
     * @param context An AddTicketActivity context for notifications.
     * @param ticket The ticket to be added.
     * @param ticketType View, Add, or Update ticket type for AddTicketActivity session.
     */
    public void addTicket(final Context context, final TicketEntry ticket, final int ticketType){

        // Video Present
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

                    Notifications.ticketPostedNotification(context,ticket.getTicketId());
                }
            });

        // No video present
        } else {

            addTicketToDb(ticket, ticketType);
        }
    }


    /**
     * Calls two threads - one for adding ticket to local DB other to remote DB.
     *
     * @see #addTicket(Context, TicketEntry, int)
     *
     * @param ticket The ticket to be added.
     * @param ticketType View, Add, or Update ticket type for AddTicketActivity session.
     */
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
                addTicketToFirebaseDb(ticket);
            }
        });
    }


    /**
     * Adds ticket to local (SQLlite) DB.
     *
     * @see #addTicketToDb(TicketEntry, int) (TicketEntry, int)
     *
     * @param ticket The ticket to be added.
     * @param ticketType View, Add, or Update ticket type for AddTicketActivity session.
     */
    public void addTicketToLocalDb(final TicketEntry ticket, int ticketType){

        if(ticketType == GlobalConstants.ADD_TICKET_TYPE){
            mAppDatabase.ticketDao().insertTicket(ticket);
        } else {
            mAppDatabase.ticketDao().updateTicket(ticket);
        }
    }


    /**
     * Adds ticket to remote (Firebase) DB.
     *
     * @see #addTicketToDb(TicketEntry, int) (TicketEntry, int)
     *
     * @param ticket The ticket to be added.
     */
    private void addTicketToFirebaseDb(final TicketEntry ticket){

        FirebaseDatabase fBdatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fBdatabase.getReference("Tickets");
        final FirebaseDbTicket fbTicket = createFirebaseTicket(ticket);
        myRef.child(String.valueOf(ticket.getTicketId())).setValue(fbTicket);
    }


    /**
     * Converts local Ticket object to Firebase ticket object to prep for upload to Firebase DB.
     *
     * @see #addTicketToDb(TicketEntry, int) (TicketEntry, int)
     *
     * @param ticket The ticket to be added.
     *
     * @return the FirebaseDB ticket
     */
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
