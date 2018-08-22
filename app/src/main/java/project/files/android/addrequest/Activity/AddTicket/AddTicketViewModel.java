package project.files.android.addrequest.Activity.AddTicket;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import project.files.android.addrequest.Background.FirebaseDbListenerService;
import project.files.android.addrequest.Background.MyApplication;
import project.files.android.addrequest.Background.VideoUploadService;
import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Background.AppExecuters;
import project.files.android.addrequest.Database.Ticket;
import project.files.android.addrequest.Background.Notifications;
import project.files.android.addrequest.Utils.C;



/**
 * ViewModel for AddTicketActivity
 *
 * This viewmodel transfers data from local and remote DBs to the AddTicketActivity for a
 * given ticket.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class AddTicketViewModel extends ViewModel {


    private static final String TAG = AddTicketViewModel.class.getSimpleName();
    private LiveData<Ticket> mLiveDataTicket;
    private Observer<Ticket> ticketObserver;
    private AppDatabase mAppDatabase;
    public Ticket tempTicket;


    /**
     * Constructor where you call loadTicketById of the ticketDao to initialize the tickets variable.
     * Note: The constructor receives the mAppDatabase and the ticketId
     */
    public void setup(@NonNull final AddTicketContract.View view, int ticketId, final int ticketType){

        mAppDatabase = AppDatabase.getInstance();
        tempTicket = new Ticket();
        loadLiveDataTicket(ticketId);

        removeObserver();
        mLiveDataTicket.observeForever(ticketObserver = new Observer<Ticket>() {
            @Override
            public void onChanged(@Nullable Ticket ticket) {

                if(ticketType != C.ADD_TICKET_TYPE){

                    tempTicket.setTicket(ticket);
                    view.updateTitleDescription();
                    view.setVideoView();

                }
            }
        });

    }


    /**
     * Get LivaData ticket from AppDatabase to be observed in AddTicketActivity
     *
     * @param ticketId The ticket IdUtils for the AddTicketActivity session.
     */
    private void loadLiveDataTicket(int ticketId){
        mLiveDataTicket = mAppDatabase.ticketDao().loadTicketById(ticketId);
    }


    /**
     * Getter for LiveDataTicket.
     */
    public LiveData<Ticket> getLiveDataTicket() {
        return mLiveDataTicket;
    }


    /**
     * Setter for LiveDataTicket.
     */
    public void setLiveDataTicket(LiveData<Ticket> liveDataTicket) {
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
     * Calls two threads - one for adding ticket to local DB other to remote DB.
     **
     * @param ticket The ticket to be added.
     * @param ticketType View, Add, or Update ticket type for AddTicketActivity session.
     */
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


    /**
     * Adds ticket to local (SQLlite) DB.
     *
     * @see #addTicketToDb(Ticket, int) (Ticket, int)
     *
     * @param ticket The ticket to be added.
     * @param ticketType View, Add, or Update ticket type for AddTicketActivity session.
     */
    public void addTicketToLocalDb(final Ticket ticket, int ticketType){

        if(ticketType == C.ADD_TICKET_TYPE){
            mAppDatabase.ticketDao().insertTicket(ticket);
        } else {
            mAppDatabase.ticketDao().updateTicket(ticket);
        }
    }


    /**
     * Adds ticket to remote (Firebase) DB.
     *
     * @see #addTicketToDb(Ticket, int) (Ticket, int)
     *
     * @param ticket The ticket to be added.
     */
    private void addTicketToFirebaseDb(final Ticket ticket){

        FirebaseDatabase fBdatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = fBdatabase.getReference("Tickets");
        myRef.child(String.valueOf(ticket.getTicketId())).setValue(ticket);

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeObserver();
    }


    private void removeObserver(){
        if (ticketObserver != null){
            mLiveDataTicket.removeObserver(ticketObserver);
        }
    }

}
