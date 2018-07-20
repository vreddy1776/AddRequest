package com.example.android.addrequest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.android.addrequest.VideoPlayer.VideoPlayerActivity;
import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;
import com.example.android.addrequest.sync.SyncVolley;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements TicketAdapter.ItemClickListener {


    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TicketAdapter mAdapter;

    // ViewModel for Main Activity
    private MainViewModel viewModel;


    /**
     * Main Activity created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Actionbar Title
        getSupportActionBar().setTitle(R.string.main_activity_name);

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
                Intent addTicketIntent = new Intent(MainActivity.this, AddTicketActivity.class);
                startActivity(addTicketIntent);
            }
        });

    }


    /**
     * Set up the ViewModel.
     */
    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
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
        Intent intent = new Intent(MainActivity.this, AddTicketActivity.class);
        intent.putExtra(AddTicketActivity.TICKET_ID, itemId);
        Log.d(TAG, "Test - Ticked ID:  " + itemId);
        startActivity(intent);

    }


}
