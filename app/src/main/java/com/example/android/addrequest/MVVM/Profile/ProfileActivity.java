package com.example.android.addrequest.MVVM.Profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.addrequest.Adapter.TicketAdapter;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import com.example.android.addrequest.MVVM.Login.LoginActivity;
import com.example.android.addrequest.MVVM.TicketList.TicketListActivity;
import com.example.android.addrequest.R;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.firebase.ui.auth.AuthUI;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class ProfileActivity extends AppCompatActivity implements TicketAdapter.ItemClickListener {


    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = ProfileActivity.class.getSimpleName();

    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;

    // ViewModel for Main Activity
    private ProfileViewModel viewModel;



    /**
     * Main Activity created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set Actionbar Title
        getSupportActionBar().setTitle(UserProfileSettings.getUsername(this));

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

        final Context context = this;

        /*
         Added a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int position = viewHolder.getAdapterPosition();
                List<TicketEntry> tickets = mAdapter.getTickets();
                viewModel.swipeTicket(context,position,tickets);

            }
        }).attachToRecyclerView(mRecyclerView);


    }



    /**
     * Sign Out Option.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.user_name_menu).setTitle(this.getString(R.string.ticket_list));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_name_menu:
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

        Intent addTicketIntent = new Intent(ProfileActivity.this, TicketListActivity.class);
        startActivity(addTicketIntent);

    }



    /**
     * Log out.
     */
    private void logout() {

        AuthUI.getInstance().signOut(this);

        UserProfileSettings.setUserProfileAtLogout(this);

        Intent addTicketIntent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(addTicketIntent);

    }



    /**
     * Set up the ViewModel.
     */
    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.updateDB(this);
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
        Intent intent = new Intent(ProfileActivity.this, AddTicketActivity.class);
        intent.putExtra(GlobalConstants.TICKET_ID_KEY, itemId);
        intent.putExtra(GlobalConstants.TICKET_VIEWTYPE_KEY, GlobalConstants.EDIT_TICKET_VIEWTYPE);
        Log.d(TAG, "Test - Ticked ID:  " + itemId);
        startActivity(intent);

    }


}
