package com.example.android.addrequest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    /**
     * Initialize values.
     */

    // Extra for the ticket ID to be received in the intent
    public static final String EXTRA_TICKET_ID = "extraTicketId";
    // Extra for the ticket ID to be received after rotation
    public static final String INSTANCE_TICKET_ID = "instanceTicketId";
    // Constant for default ticket id to be used when not in update mode
    private static final int DEFAULT_TICKET_ID = -1;

    // Initialize integer for ticket ID
    private int mTicketId = DEFAULT_TICKET_ID;

    // Member variable for the Database
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewModel();

    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }


    /**
     * Navigate to RequesterDetails Activity.
     */
    public void toRequesterDetails(View view) {
        Intent intent = new Intent(MainActivity.this, RequesterDetailsActivity.class);
        startActivity(intent);
    }


    /**
     * Add or update entry when SAVE button is clicked.
     */
    public void onSaveButtonClicked() {

        // Set up ticket
        String title = "test title";
        String description = "test description";
        Date date = new Date();
        final TicketEntry ticket = new TicketEntry(title, description, date);

        // Execute ticket entry
        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTicketId == DEFAULT_TICKET_ID) {
                    // insert new task
                    mDb.ticketDao().insertTicket(ticket);
                }
                finish();
            }
        });

    }

}
