package com.example.android.addrequest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.TicketEntry;

import java.util.Date;

public class AddTicketActivity extends AppCompatActivity{


    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = AddTicketActivity.class.getSimpleName();

    // Ticket ID parameter string
    public static final String TICKET_ID = "TicketId";

    // Constant for default ticket id to be used when not in update mode
    private static final int DEFAULT_TICKET_ID = -1;

    // Initialize integer for ticket ID
    private int mTicketId = DEFAULT_TICKET_ID;

    // Member variable for the ViewModel
    private AddTicketViewModel viewModel;

    // Fields for views
    EditText mTitleText;
    EditText mDescriptionText;
    Button mButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        // Get ticket ID
        receiveTicketID();

        // Setup ViewModels for each instance
        setupViewModelFactory();

        // Initialize views
        initViews();

    }


    /**
     * Get ticket ID from MainActivity
     */
    private void receiveTicketID(){
        Intent intent = getIntent();
        mTicketId = intent.getIntExtra(TICKET_ID, DEFAULT_TICKET_ID);
    }


    /**
     * Set up ViewModelFactory for each instance of AddTicketActivity
     */
    private void setupViewModelFactory(){

        // Declare a AddTicketViewModelFactory using mDb and mTicketId
        AddTicketViewModelFactory factory = new AddTicketViewModelFactory(this.getApplication(), mTicketId);

        // Declare a AddTicketViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above AddTicketViewModel
        viewModel = ViewModelProviders.of(this, factory).get(AddTicketViewModel.class);

        // Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getTicket().observe(this, new Observer<TicketEntry>() {
            @Override
            public void onChanged(@Nullable TicketEntry ticketEntry) {
                viewModel.getTicket().removeObserver(this);
                populateUI(ticketEntry);
            }
        });
    }


    /**
     * Save the instance ticket ID in case of screen rotation or app exit
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTicketId = viewModel.getTicketID();
    }


    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {

        mTitleText = findViewById(R.id.editTextTicketTitle);
        mDescriptionText = findViewById(R.id.editTextTicketDescription);

        mButton = findViewById(R.id.saveButton);
        if( mTicketId != DEFAULT_TICKET_ID ){ mButton.setText(R.string.update_button); }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }


    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param ticket the ticketEntry to populate the UI
     */
    private void populateUI(TicketEntry ticket) {
        if (ticket == null) {
            return;
        }

        mTitleText.setText(ticket.getTitle());
        mDescriptionText.setText(ticket.getDescription());
    }

    
    /**
     * Add or update entry when SAVE button is clicked.
     */
    public void onSaveButtonClicked() {

        // Set up ticket
        String title = mTitleText.getText().toString();
        String description = mDescriptionText.getText().toString();
        Date date = new Date();
        TicketEntry ticket = new TicketEntry(title, description, date);

        // Execute ticket entry
        if (mTicketId == DEFAULT_TICKET_ID) {
            // insert new ticket
            viewModel.addTicket(ticket);
        } else {
            // update ticket
            viewModel.changeTicket(ticket,mTicketId);
        }
        finish();

    }


}