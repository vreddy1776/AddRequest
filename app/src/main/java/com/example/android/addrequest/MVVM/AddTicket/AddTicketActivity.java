package com.example.android.addrequest.MVVM.AddTicket;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.Chat.ChatActivity;
import com.example.android.addrequest.R;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.example.android.addrequest.Utils.ID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;



public class AddTicketActivity extends AppCompatActivity{


    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = AddTicketActivity.class.getSimpleName();

    // Initialize integer for ticket ID and viewtype
    private int mTicketId = GlobalConstants.DEFAULT_TICKET_ID;
    private int mTicketViewType = GlobalConstants.DEFAULT_TICKET_VIEWTYPE;

    public static final String MAIN_URL = "https://s3.amazonaws.com/addrequest-deployments-mobilehub-1269242402/";

    // Member variable for the ViewModel
    private AddTicketViewModel viewModel;

    // Fields for views
    EditText mTitleText;
    EditText mDescriptionText;

    FrameLayout streamVideo;

    // Buttons
    Button saveButton;
    Button videoButton;

    // Video Intent activity codes
    final static int VIDEO_REQUEST = 5;
    final static int RESULT_OK = -1;

    // Video codes
    private int requestCode;
    private int resultCode;

    // OnSavedInstance Parameter Strings
    public static final String INSTANCE_TICKET_VIEWTYPE_KEY = "instanceTicketViewType";
    public static final String INSTANCE_TICKET_ID_KEY = "instanceTicketId";
    public static final String INSTANCE_REQUEST_CODE_KEY = "instanceRequestCode";
    public static final String INSTANCE_RESULT_CODE_KEY = "instanceResultCode";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        // Get ticket ID
        receiveTicketID();

        // Setup ViewModels for each instance
        setupViewModelFactory();

        // Initialize views
        initViews();



        /*
        VideoFragment videoFragment = new VideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.stream_video,videoFragment)
                .commit();
                */


/*

        String URL = MAIN_URL + mTicketId;
        boolean urlCheck =  Patterns.WEB_URL.matcher(URL).matches();
        if( urlCheck ) {
            Log.d(TAG,"urlCheck:  " + urlCheck);
            if (savedInstanceState == null) {
                Uri videoUri = Uri.parse(URL);
                String videoTitle = mTitleText.toString();
                ExoPlayerFragment mExoPlayerFragment = ExoPlayerFragment.newInstance(videoUri, videoTitle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.stream_video, mExoPlayerFragment, ExoPlayerFragment.TAG)
                        .commit();
            }
        } else {
            streamVideo.setVisibility(View.INVISIBLE);
            Log.d(TAG,"urlCheck:  " + urlCheck);
        }
        */



    }


    /**
     * Get ticket ID from MainActivity
     */
    private void receiveTicketID(){
        Intent intent = getIntent();
        mTicketId = intent.getIntExtra(GlobalConstants.TICKET_ID_KEY, GlobalConstants.DEFAULT_TICKET_ID);
        mTicketViewType = intent.getIntExtra(GlobalConstants.TICKET_VIEWTYPE_KEY, GlobalConstants.DEFAULT_TICKET_VIEWTYPE);
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TICKET_VIEWTYPE_KEY , mTicketViewType);
        outState.putInt(INSTANCE_TICKET_ID_KEY , mTicketId);
        outState.putInt(INSTANCE_REQUEST_CODE_KEY , requestCode);
        outState.putInt(INSTANCE_RESULT_CODE_KEY , resultCode);
        super.onSaveInstanceState(outState);
    }




    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {

        mTitleText = findViewById(R.id.editTextTicketTitle);
        mDescriptionText = findViewById(R.id.editTextTicketDescription);

        saveButton = findViewById(R.id.saveButton);
        if( mTicketId != GlobalConstants.DEFAULT_TICKET_ID ){ saveButton.setText(R.string.update_button); }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        videoButton = findViewById(R.id.videoButton);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVideoButtonClicked();
            }
        });

        streamVideo = findViewById(R.id.stream_video);

        setViewType();

    }


    /**
     * Removed edit privelages if not accessed from personal ticket page
     */
    private void setViewType(){

        if(mTicketViewType != GlobalConstants.EDIT_TICKET_VIEWTYPE) {
            mTitleText.setEnabled(false);
            mDescriptionText.setEnabled(false);
            saveButton.setVisibility(View.INVISIBLE);
            videoButton.setVisibility(View.INVISIBLE);
        }

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

        mTitleText.setText(ticket.getTicketTitle());
        mDescriptionText.setText(ticket.getTicketDescription());
    }

    
    /**
     * Add or update entry when SAVE button is clicked.
     */
    public void onSaveButtonClicked() {

        // Set up ticket
        int id = ID.newID();
        String title = mTitleText.getText().toString();
        String description = mDescriptionText.getText().toString();
        Date date = new Date();
        Log.d(TAG, "Test - Ticked ID:  " + mTicketId);

        // Flag to post video if one was saved
        boolean boolVideoPost = false;
        Log.d(TAG, "requestCode: " + requestCode + "  ;  resultCode: " + resultCode);
        if(  ( requestCode == VIDEO_REQUEST )  &&  ( resultCode == RESULT_OK )  ) {
            boolVideoPost = true;
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Execute ticket entry
        if (mTicketId == GlobalConstants.DEFAULT_TICKET_ID) {
            // insert new ticket
            //TicketEntry ticket = new TicketEntry(id, title, description, date);
            // Set up ticket

            TicketEntry ticket = new TicketEntry(
                    id,
                    title,
                    description,
                    date.toString(),
                    Integer.toString(id),
                    user.getUid(),
                    user.getDisplayName(),
                    user.getPhotoUrl().toString());

            viewModel.addTicket(this,ticket,boolVideoPost);
        } else {
            // update ticket
            //TicketEntry ticket = new TicketEntry(title, description, date);
            TicketEntry ticket = new TicketEntry(
                    title,
                    description,
                    date.toString(),
                    Integer.toString(id),
                    user.getUid(),
                    user.getDisplayName(),
                    user.getPhotoUrl().toString());

            viewModel.changeTicket(this,ticket,mTicketId,boolVideoPost);
        }

        // Close Activity
        finish();

    }


    /**
     * Start Camera Intent when video button is clicked.
     */
    public void onVideoButtonClicked() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent,VIDEO_REQUEST);
    }



    /**
    * Get Result from Camera Video Intent.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "requestCode: " + requestCode + "  ;  resultCode: " + resultCode);
        this.requestCode = requestCode;
        this.resultCode = resultCode;

        if(  ( requestCode == VIDEO_REQUEST )  &&  ( resultCode == RESULT_OK )  ){
            Uri capturedVideoUri = data.getData();
            String filePath = getPath(capturedVideoUri);
            viewModel.storeVideo(this,filePath);
        }

    }


    /**
     * Use Media Content resolver to convert URI to file.
     */
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }


    public void goToChat(View view){
        Intent intent = new Intent(AddTicketActivity.this, ChatActivity.class);
        intent.putExtra( GlobalConstants.CHAT_ID_KEY , Integer.toString(mTicketId) );
        startActivity(intent);
    }

}
