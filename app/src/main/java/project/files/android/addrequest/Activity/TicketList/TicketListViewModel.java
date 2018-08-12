package project.files.android.addrequest.Activity.TicketList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import project.files.android.addrequest.Adapter.TicketAdapter;
import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.AppExecuters;
import project.files.android.addrequest.Database.Ticket;
import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.GlobalConstants;


/**
 * Tickets Fragment
 *
 * Fragment containing RecyclerView list of tickets.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class TicketListViewModel extends AndroidViewModel {

    private static final String TAG = TicketListViewModel.class.getSimpleName();

    private AppDatabase database;
    private LiveData<List<Ticket>> ticketListLiveData;
    private Observer<List<Ticket>> allTicketsObserver;
    private Observer<List<Ticket>> profileObserver;



    public TicketListViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
    }


    public void updateDB(final TicketAdapter ticketAdapter, int updateCode){
        removeObservers();
        if (updateCode == GlobalConstants.LOAD_ALL){
            ticketListLiveData = database.ticketDao().loadAllTickets();
            ticketListLiveData.observeForever(allTicketsObserver = new Observer<List<Ticket>>() {
                @Override
                public void onChanged(@Nullable List<Ticket> ticketList) {
                    ticketAdapter.setTickets(ticketList);
                }
            });
        } else if (updateCode == GlobalConstants.LOAD_USER) {
            ticketListLiveData = database.ticketDao().loadUserTickets(UserProfileSettings.getUserID(this.getApplication()));
            ticketListLiveData.observeForever(profileObserver = new Observer<List<Ticket>>() {
                @Override
                public void onChanged(@Nullable List<Ticket> ticketList) {
                    ticketAdapter.setTickets(ticketList);
                }
            });
        } else {
            // No update to ticket DB
        }
    }


    public void deleteTicket(final int ticketId){

        final FirebaseDatabase FbDatabase = FirebaseDatabase.getInstance();

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.ticketDao().deleteTicketById(ticketId);
            }
        });

        AppExecuters.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                FbDatabase.getReference("Tickets").child(String.valueOf(ticketId)).removeValue();
            }
        });

    }


    private void removeObservers(){
        if (profileObserver != null){
            Log.d(TAG,"ProfileObserver removed");
            ticketListLiveData.removeObserver(profileObserver);
        }
        if (allTicketsObserver != null){
            Log.d(TAG,"allTicketObserver removed");
            ticketListLiveData.removeObserver(allTicketsObserver);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeObservers();
    }

}
