package com.example.android.addrequest.MVVM.TicketList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.addrequest.Adapter.TicketAdapter;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import com.example.android.addrequest.MVVM.Login.LoginActivity;
import com.example.android.addrequest.R;
import com.example.android.addrequest.Services.FirebaseDbListenerService;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.firebase.ui.auth.AuthUI;

import java.util.List;

public class TicketListActivity extends AppCompatActivity implements  TicketAdapter.ItemClickListener {


    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = TicketListActivity.class.getSimpleName();

    private FragmentManager fragmentManager;

    private FloatingActionButton fabButton;

    private TicketListViewModel viewModel;

    private TicketAdapter mAdapter;


    // Member variables for the adapter and RecyclerView

    // ViewModel for Main Activity



    /**
     * Main Activity created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);


        mAdapter = new TicketAdapter( this, this);

        setupViewModel();


        fragmentManager = getSupportFragmentManager();

        TicketListFragment ticketListFragment = new TicketListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.ticketlist_fragment_container, ticketListFragment)
                .commit();


        /*
        Set the Floating Action Button (FAB) to its corresponding View.
        Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
        to launch the AddTicketActivity.
        */
        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTicketActivity
                Intent addTicketIntent = new Intent(TicketListActivity.this, AddTicketActivity.class);
                addTicketIntent.putExtra(GlobalConstants.TICKET_VIEWTYPE_KEY, GlobalConstants.EDIT_TICKET_VIEWTYPE);
                startActivity(addTicketIntent);
            }
        });

    }



    /**
     * Set up Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.all_tickets_menu).setVisible(false);
        menu.findItem(R.id.user_name_menu).setVisible(true);
        menu.findItem(R.id.user_name_menu).setTitle(UserProfileSettings.getUsername(this));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_name_menu:
                profileMode();
                return true;
            case R.id.all_tickets_menu:
                allTicketsMode();
                return true;
            case R.id.sign_out_menu:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void profileMode(){

        viewModel.updateDB(GlobalConstants.LOAD_USER);
        fabButton.setVisibility(View.INVISIBLE);
        openProfile();

    }



    private void allTicketsMode(){

        viewModel.updateDB(GlobalConstants.LOAD_ALL);
        fabButton.setVisibility(View.VISIBLE);
        closeProfile();

    }



    /**
     * Go to user Profile.
     */
    private void openProfile() {

        ProfileFragment profileFragment = new ProfileFragment();
        fragmentManager.beginTransaction()
                .add(R.id.profile_fragment_container,profileFragment,GlobalConstants.PROFILE_FRAGMENT_TAG)
                .commit();

    }


    /**
     * Go to user Profile.
     */
    private void closeProfile() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(GlobalConstants.PROFILE_FRAGMENT_TAG);
        if(fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();

    }


    /**
     * Log out.
     */
    private void logout() {

        stopService(new Intent(this, FirebaseDbListenerService.class));


        AuthUI.getInstance().signOut(this);

        UserProfileSettings.setUserProfileAtLogout(this);

        Intent addTicketIntent = new Intent(TicketListActivity.this, LoginActivity.class);
        startActivity(addTicketIntent);

    }





    /**
     * Intent to AddTicketActivity.
     */
    @Override
    public void onItemClickListener(int itemId) {

        // Launch AddTicketActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(this, AddTicketActivity.class);
        intent.putExtra(GlobalConstants.TICKET_ID_KEY, itemId);
        intent.putExtra(GlobalConstants.TICKET_VIEWTYPE_KEY, GlobalConstants.DEFAULT_TICKET_VIEWTYPE);
        Log.d(TAG, "Test - Ticked ID:  " + itemId);
        startActivity(intent);

    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(TicketListViewModel.class);
        viewModel.updateDB(GlobalConstants.LOAD_ALL);
        viewModel.getTickets().observe(this, new Observer<List<TicketEntry>>() {
            @Override
            public void onChanged(@Nullable List<TicketEntry> ticketEntries) {
                Log.d(TAG, "Updating list of tickets from LiveData in ViewModel");
                mAdapter.setTickets(ticketEntries);
            }
        });
    }

    public TicketAdapter getTicketAdapter() {
        return this.mAdapter;
    }
}
