package com.example.android.addrequest.MVVM.AddTicket;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
    public static final String INSTANCE_TICKET_TYPE_KEY = "instanceTicketType";
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



    private int mTicketId = GlobalConstants.DEFAULT_TICKET_ID;
    private String mTicketTitle = GlobalConstants.DEFAULT_TICKET_TITLE;
    private String mTicketDescription = GlobalConstants.DEFAULT_TICKET_DESCRIPTION;
    private String mTicketDate = GlobalConstants.DEFAULT_TICKET_DATE;
    private String mTicketVideoPostId = GlobalConstants.DEFAULT_TICKET_VIDEO_POST_ID;
    private String mTicketVideoLocalUri = GlobalConstants.DEFAULT_TICKET_VIDEO_LOCAL_URI;
    private String mTicketVideoInternetUrl = GlobalConstants.DEFAULT_TICKET_VIDEO_INTERNET_URL;
    private String mUserId = GlobalConstants.DEFAULT_USER_ID;
    private String mUserName = GlobalConstants.DEFAULT_USER_NAME;
    private String mUserPhotoUrl = GlobalConstants.DEFAULT_USER_PHOTO_URL;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        receiveTicketID();
        setupViewModelFactory();
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








        if(mTicketType == GlobalConstants.ADD_TICKET_TYPE){
            mTicketId = ID.newID();
        }

    }


    /**
     * Get ticket ID from MainActivity
     */
    private void receiveTicketID(){
        Intent intent = getIntent();
        mTicketId = intent.getIntExtra(GlobalConstants.TICKET_ID_KEY, GlobalConstants.DEFAULT_TICKET_ID);
        mTicketType = intent.getIntExtra(GlobalConstants.TICKET_TYPE_KEY, GlobalConstants.VIEW_TICKET_TYPE);
    }


    /**
     * Set up ViewModelFactory for each instance of AddTicketActivity
     */
    private void setupViewModelFactory(){

        AddTicketViewModelFactory factory = new AddTicketViewModelFactory(this.getApplication(), mTicketId);
        viewModel = ViewModelProviders.of(this, factory).get(AddTicketViewModel.class);
        viewModel.getTicket().observe(this, new Observer<TicketEntry>() {
            @Override
            public void onChanged(@Nullable TicketEntry ticketEntry) {

                if(mTicketType != GlobalConstants.ADD_TICKET_TYPE){
                    mTicketId = ticketEntry.getTicketId();
                    mTicketTitle = ticketEntry.getTicketTitle();
                    mTicketDescription = ticketEntry.getTicketDescription();
                    mTicketDate = ticketEntry.getTicketDate();
                    mTicketVideoPostId = ticketEntry.getTicketVideoPostId();
                    mTicketVideoLocalUri = ticketEntry.getTicketVideoLocalUri();
                    mTicketVideoInternetUrl = ticketEntry.getTicketVideoInternetUrl();
                    mUserId = ticketEntry.getUserId();
                    mUserName = ticketEntry.getUserName();
                    mUserPhotoUrl = ticketEntry.getUserPhotoUrl();

                    mTitleText.setText(mTicketTitle);
                    mDescriptionText.setText(mTicketDescription);

                }
                    
            }
        });
    }




    //Removed for aws testing
    /**
     * Save the instance ticket ID in case of screen rotation or app exit
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(INSTANCE_TICKET_TYPE_KEY , mTicketType);
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
        videoButton = findViewById(R.id.videoButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVideoButtonClicked();
            }
        });

        if(mTicketType == GlobalConstants.VIEW_TICKET_TYPE) {
            mTitleText.setEnabled(false);
            mDescriptionText.setEnabled(false);
            saveButton.setVisibility(View.INVISIBLE);
            videoButton.setVisibility(View.INVISIBLE);
        } else if(mTicketType == GlobalConstants.UPDATE_TICKET_TYPE){
            saveButton.setText(R.string.update_button);
        } else if(mTicketType == GlobalConstants.ADD_TICKET_TYPE){
            mTicketTitle = getString(R.string.edit_ticket_title);
            mTicketDescription = getString(R.string.edit_ticket_description);
            mTitleText.setText(mTicketTitle);
            mDescriptionText.setText(mTicketDescription);
        } else {
            //do nothing
        }

    }







    
    /**
     * Add or update entry when SAVE button is clicked.
     */
    public void onSaveButtonClicked() {

        /*
        int ticketId = generateTicketId(mTicketId);
        String ticketTitle = mTitleText.getText().toString();
        String ticketDescription = mDescriptionText.getText().toString();
        String ticketDate = new Date().toString();
        String ticketVideoPostId = generateVideoId(requestCode,resultCode,ticketId);
        String userId = UserProfileSettings.getUserID(this);
        String userName = UserProfileSettings.getUsername(this);
        String userPhotoUrl = UserProfileSettings.getUserPhotoURL(this);
        */

        int ticketId = mTicketId;
        String ticketTitle = mTicketTitle;
        String ticketDescription = mTicketDescription;
        String ticketDate = mTicketDate;
        String ticketVideoPostId = generateVideoId(requestCode,resultCode,ticketId);
        String userId = mUserId;
        String userName = mUserName;
        String userPhotoUrl = mUserPhotoUrl;

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

            Uri capturedVideoUri = data.getData();
            String filePath = getPath(capturedVideoUri);
            viewModel.storeVideo(this,filePath);

            videoUri = capturedVideoUri;
            mTicketVideoLocalUri = videoUri.toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Tickets");
            myRef.child(String.valueOf(mTicketId)).child("ticketVideoLocalUri").setValue(mTicketVideoLocalUri);

            initializePlayer();

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference firebaseVideoRef = firebaseStorage.getReference().child("Videos");
            StorageReference localVideoRef = firebaseVideoRef.child(capturedVideoUri.getLastPathSegment());
            localVideoRef.putFile(capturedVideoUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.d(TAG,"download URL:  " + downloadUrl);
                            mTicketVideoInternetUrl = downloadUrl.toString();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Tickets");
                            myRef.child(String.valueOf(mTicketId)).child("mTicketVideoInternetUrl").setValue(mTicketVideoInternetUrl);

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





