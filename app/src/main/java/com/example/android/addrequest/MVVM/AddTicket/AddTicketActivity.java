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
import android.widget.ImageView;

import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.Chat.ChatActivity;
import com.example.android.addrequest.R;
import com.example.android.addrequest.SharedPreferences.UserProfileSettings;
import com.example.android.addrequest.Utils.GlobalConstants;
import com.example.android.addrequest.Utils.ID;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private int mTicketType = GlobalConstants.VIEW_TICKET_TYPE;

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







    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    private ImageView ivHideControllerButton;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private int position;

    private Uri videoUri;

    private int mTicketVideoPostId = GlobalConstants.DEFAULT_VIDEO_POST_ID;
    private Uri mTicketVideoLocalUri = GlobalConstants.DEFAULT_VIDEO_LOCAL_URI;
    private Uri mTicketVideoInternetUrl = GlobalConstants.DEFAULT_VIDEO_INTERNET_URL;


    private int ticketId;
    private String ticketTitle;
    private String ticketDescription;
    private String ticketDate;
    private String ticketVideoPostId;
    private String ticketVideoLocalUri;
    private String ticketVideoInternetUrl;
    private String userId;
    private String userName;
    private String userPhotoUrl;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        // Get ticket ID
        receiveTicketID();

        initializeTicketValues();

        // Setup ViewModels for each instance
        setupViewModelFactory();

        // Initialize views
        initViews();


        if(savedInstanceState == null){
            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = 0;
        }else {
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
            currentWindow = savedInstanceState.getInt("currentWindow");
            playbackPosition = savedInstanceState.getLong("playBackPosition");
        }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);


        Log.d(TAG,"ticketId:  " + mTicketId);
        if(mTicketId != GlobalConstants.DEFAULT_VIDEO_POST_ID){
            String url = GlobalConstants.MAIN_S3_URL + mTicketId;
            Log.d(TAG,"url:  " + url);
            videoUri = Uri.parse(url);
            initializePlayer();
        }


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
                //viewModel.getTicket().removeObserver(this);
                //populateTicket(ticketEntry);

                if(mTicketId != GlobalConstants.DEFAULT_TICKET_ID){
                    ticketId = ticketEntry.getTicketId();
                    ticketTitle = ticketEntry.getTicketTitle();
                    ticketDescription = ticketEntry.getTicketDescription();
                    ticketDate = ticketEntry.getTicketDate();
                    ticketVideoPostId = ticketEntry.getTicketVideoPostId();
                    ticketVideoLocalUri = ticketEntry.getTicketVideoLocalUri();
                    ticketVideoInternetUrl = ticketEntry.getTicketVideoInternetUrl();
                    userId = ticketEntry.getUserId();
                    userName = ticketEntry.getUserName();
                    userPhotoUrl = ticketEntry.getUserPhotoUrl();

                    Log.d(TAG,
                            "tickedId:  " + ticketId + "\n" +
                                    "ticketTitle:  " + ticketTitle + "\n" +
                                    "ticketDescription:  " + ticketDescription + "\n" +
                                    "ticketDate:  " + ticketDate + "\n" +
                                    "ticketVideoPostId:  " + ticketVideoPostId + "\n" +
                                    "ticketVideoLocalUri:  " + ticketVideoLocalUri + "\n" +
                                    "ticketVideoInternetUrl:  " + ticketVideoInternetUrl + "\n" +
                                    "userId:  " + userId + "\n" +
                                    "userName:  " + userName + "\n" +
                                    "userPhotoUrl:  " + userPhotoUrl + "\n");
                }



                mTitleText.setText(ticketTitle);
                mDescriptionText.setText(ticketDescription);


            }
        });
    }


    private void initializeTicketValues(){

        ticketId = GlobalConstants.DEFAULT_TICKET_ID;
        ticketTitle = GlobalConstants.DEFAULT_TICKET_TITLE;
        ticketDescription = GlobalConstants.DEFAULT_TICKET_DESCRIPTION;
        ticketDate = GlobalConstants.DEFAULT_TICKET_DATE;
        ticketVideoPostId = GlobalConstants.DEFAULT_TICKET_VIDEO_POST_ID;
        ticketVideoLocalUri = GlobalConstants.DEFAULT_TICKET_VIDEO_LOCAL_URI;
        ticketVideoInternetUrl = GlobalConstants.DEFAULT_TICKET_VIDEO_INTERNET_URL;
        userId = GlobalConstants.DEFAULT_USER_ID;
        userName = GlobalConstants.DEFAULT_USER_NAME;
        userPhotoUrl = GlobalConstants.DEFAULT_USER_PHOTO_URL;

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




        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();

        outState.putBoolean("playWhenReady", playWhenReady);
        outState.putInt("currentWindow", currentWindow);
        outState.putLong("playBackPosition", playbackPosition);




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

        //streamVideo = findViewById(R.id.stream_video);

        //  Removed edit privelages if not accessed from personal ticket page
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
    private void populateTicket(TicketEntry ticket) {
        if (ticket == null) {
            return;
        }


        ticketId = ticket.getTicketId();
        ticketTitle = ticket.getTicketTitle();
        ticketDescription = ticket.getTicketDescription();
        ticketDate = ticket.getTicketDate();
        ticketVideoPostId = ticket.getTicketVideoPostId();
        ticketVideoLocalUri = ticket.getTicketVideoLocalUri();
        ticketVideoInternetUrl = ticket.getTicketVideoInternetUrl();
        userId = ticket.getUserId();
        userName = ticket.getUserName();
        userPhotoUrl = ticket.getUserPhotoUrl();

        Log.d(TAG,
                "tickedId:  " + String.valueOf(ticket.getTicketId()) + "\n" +
                        "ticketTitle:  " + ticket.getTicketTitle() + "\n" +
                        "ticketDescription:  " + ticket.getTicketDescription() + "\n" +
                        "ticketDate:  " + ticket.getTicketDate() + "\n" +
                        "ticketVideoPostId:  " + ticket.getTicketVideoPostId() + "\n" +
                        "ticketVideoLocalUri:  " + ticket.getTicketVideoLocalUri() + "\n" +
                        "ticketVideoInternetUrl:  " + ticket.getTicketVideoInternetUrl() + "\n" +
                        "userId:  " + ticket.getUserId() + "\n" +
                        "userName:  " + ticket.getUserName() + "\n" +
                        "userPhotoUrl:  " + ticket.getUserPhotoUrl() + "\n");


        mTitleText.setText(ticket.getTicketTitle());
        mDescriptionText.setText(ticket.getTicketDescription());
    }



    private void populateUI(){
        mTitleText.setText(ticketTitle);
        mDescriptionText.setText(ticketDescription);
    }


    
    /**
     * Add or update entry when SAVE button is clicked.
     */
    public void onSaveButtonClicked() {

        int ticketId = generateTicketId(mTicketId);
        String ticketTitle = mTitleText.getText().toString();
        String ticketDescription = mDescriptionText.getText().toString();
        String ticketDate = new Date().toString();
        String ticketVideoPostId = generateVideoId(requestCode,resultCode,ticketId);
        String ticketVideoLocalUri = mTicketVideoLocalUri.toString();
        String ticketVideoInternetUrl = mTicketVideoInternetUrl.toString();
        String userId = UserProfileSettings.getUserID(this);
        String userName = UserProfileSettings.getUsername(this);
        String userPhotoUrl = UserProfileSettings.getUserPhotoURL(this);

        viewModel.addTicket(
                ticketId,
                ticketTitle,
                ticketDescription,
                ticketDate,
                ticketVideoPostId,
                userId,
                userName,
                userPhotoUrl);

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

            /*
            saveButton.setClickable(false);
            saveButton.setText(getApplicationContext().getString(R.string.loading_button));
            */

            Uri capturedVideoUri = data.getData();
            String filePath = getPath(capturedVideoUri);
            viewModel.storeVideo(this,filePath);


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Tickets");
            FirebaseDbTicket ticket = new FirebaseDbTicket();
            myRef.child(String.valueOf(viewModel.getTicket())).setValue(ticket);


            videoUri = capturedVideoUri;
            mTicketVideoLocalUri = videoUri;
            initializePlayer();


            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference firebaseVideoRef = firebaseStorage.getReference().child("Videos");
            StorageReference localVideoRef = firebaseVideoRef.child(capturedVideoUri.getLastPathSegment());
            localVideoRef.putFile(capturedVideoUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.d(TAG,"download URL:  " + downloadUrl);
                            mTicketVideoInternetUrl = downloadUrl;
                            saveButton.setClickable(true);
                            if( mTicketId != GlobalConstants.DEFAULT_TICKET_ID ){
                                saveButton.setText(R.string.update_button);
                            } else {
                                saveButton.setText(R.string.add_button);
                            }
                        }
                    });



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


    private int generateTicketId(int ticketId){

        int newTicketId;
        if (ticketId == GlobalConstants.DEFAULT_TICKET_ID) {
            newTicketId = ID.newID();
        } else {
            newTicketId = ticketId;
        }
        return newTicketId;
    }


    private String generateVideoId(int requestCode, int resultCode, int ticketId){

        int ticketVideoId = GlobalConstants.DEFAULT_VIDEO_ID;
        if(  ( requestCode == VIDEO_REQUEST )  &&  ( resultCode == RESULT_OK )  ) {
            ticketVideoId = ticketId;
        }
        String ticketVideoIdString = String.valueOf(ticketVideoId);
        return ticketVideoIdString;

    }


















    private void initializePlayer() {

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.stream_video);
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        player.seekTo(currentWindow, playbackPosition);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource, true, false);

        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayerView.hideController();
            }
        });


    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


}





