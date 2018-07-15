package com.example.android.addrequest;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.S3ClientOptions;
import com.example.android.addrequest.database.TicketEntry;
import com.example.android.addrequest.utils.GenerateID;

import java.io.File;
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

    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                // Obtain the reference to the AWSCredentialsProvider and AWSConfiguration objects
                credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
                configuration = AWSMobileClient.getInstance().getConfiguration();

                // Use IdentityManager#getUserID to fetch the identity id.
                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                    @Override
                    public void onIdentityId(String identityId) {
                        Log.d("YourMainActivity", "Identity ID = " + identityId);

                        // Use IdentityManager#getCachedUserID to
                        //  fetch the locally cached identity id.
                        final String cachedIdentityId =
                                IdentityManager.getDefaultIdentityManager().getCachedUserID();
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.d("YourActivity", "Error in retrieving the identity" + exception);
                    }
                });
            }
        }).execute();
        uploadWithTransferUtility();





        // Removed to test video uploading
        /*
        // Get ticket ID
        receiveTicketID();

        // Setup ViewModels for each instance
        setupViewModelFactory();

        // Initialize views
        initViews();
        */

    }



    private void uploadWithTransferUtility() {

        String OLD_PATHNAME = "/path/to/file/localFile.txt";
        String FILENAME = "GKU000097.mp4";
        String PATHNAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/" + FILENAME;

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "addrequest-deployments-mobilehub-1269242402",
                        FILENAME,
                        new File(PATHNAME));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.d("YourActivity", "complete");

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.d("YourActivity", "error:  " + ex);
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
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


    //Removed for aws testing
    /**
     * Save the instance ticket ID in case of screen rotation or app exit
     */
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTicketId = viewModel.getTicketID();
    }
    */



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
        int id = GenerateID.newID();
        String title = mTitleText.getText().toString();
        String description = mDescriptionText.getText().toString();
        Date date = new Date();

        Log.d(TAG, "Test - Ticked ID:  " + mTicketId);

        // Execute ticket entry
        if (mTicketId == DEFAULT_TICKET_ID) {
            // insert new ticket
            TicketEntry ticket = new TicketEntry(id, title, description, date);
            viewModel.addTicket(ticket);
        } else {
            // update ticket
            TicketEntry ticket = new TicketEntry(title, description, date);
            viewModel.changeTicket(ticket,mTicketId);
        }
        finish();

    }


}
