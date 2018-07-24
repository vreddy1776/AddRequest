package com.example.android.addrequest.MVVM.TicketList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.addrequest.Adapter.TicketAdapter;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import com.example.android.addrequest.MVVM.Login.LoginActivity;
import com.example.android.addrequest.MVVM.Profile.ProfileActivity;
import com.example.android.addrequest.R;
import com.example.android.addrequest.Services.FirebaseDbListenerService;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.firebase.ui.auth.AuthUI;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TicketListActivity extends AppCompatActivity implements TicketAdapter.ItemClickListener {


    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = TicketListActivity.class.getSimpleName();

    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;

    // ViewModel for Main Activity
    private TicketListViewModel viewModel;



    /**
     * Main Activity created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTickets);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TicketAdapter(this , this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        // Setup ViewModel
        setupViewModel();

        /*
        Set the Floating Action Button (FAB) to its corresponding View.
        Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
        to launch the AddTicketActivity.
        */
        FloatingActionButton fabButton = findViewById(R.id.fab);
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
        menu.findItem(R.id.username).setTitle(UserProfileSettings.getUsername(this));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.username:
                goToProfile();
                return true;
            case R.id.sign_out_menu:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Go to user Profile.
     */
    private void goToProfile() {

        Intent addTicketIntent = new Intent(TicketListActivity.this, ProfileActivity.class);
        startActivity(addTicketIntent);

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
     * Set up the ViewModel.
     */
    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(TicketListViewModel.class);
        viewModel.updateDB();
        viewModel.getTickets().observe(this, new Observer<List<TicketEntry>>() {
            @Override
            public void onChanged(@Nullable List<TicketEntry> ticketEntries) {
                Log.d(TAG, "Updating list of tickets from LiveData in ViewModel");
                mAdapter.setTickets(ticketEntries);
            }
        });

    }


    /**
     * Intent to AddTicketActivity.
     */
    @Override
    public void onItemClickListener(int itemId) {

        // Launch AddTicketActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(TicketListActivity.this, AddTicketActivity.class);
        intent.putExtra(GlobalConstants.TICKET_ID_KEY, itemId);
        intent.putExtra(GlobalConstants.TICKET_VIEWTYPE_KEY, GlobalConstants.DEFAULT_TICKET_VIEWTYPE);
        Log.d(TAG, "Test - Ticked ID:  " + itemId);
        startActivity(intent);

    }


}
