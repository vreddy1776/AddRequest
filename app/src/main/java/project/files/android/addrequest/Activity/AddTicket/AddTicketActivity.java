package project.files.android.addrequest.Activity.AddTicket;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

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

import java.util.Date;

import project.files.android.addrequest.Analytics.AppseeFunctions;
import project.files.android.addrequest.Activity.Chat.ChatActivity;
import project.files.android.addrequest.Background.MyApplication;
import project.files.android.addrequest.Background.Notifications;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.DateTimeUtils;
import project.files.android.addrequest.Utils.C;
import project.files.android.addrequest.Utils.IdUtils;


/**
 * AddTicket Activity
 *
 * Activity for adding and updating tickets and videos
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class AddTicketActivity extends AppCompatActivity implements AddTicketContract.View{


    private static final String TAG = AddTicketActivity.class.getSimpleName();

    private int mTicketType = C.VIEW_TICKET_TYPE;
    private AddTicketViewModel viewModel;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 4;
    EditText mTitleText;
    EditText mDescriptionText;
    FrameLayout videoWrapper;
    FrameLayout streamVideo;
    Button saveButton;
    ImageView videoButton;
    ImageView videoDeleteButton;
    ImageButton chatButton;
    final static int VIDEO_REQUEST = 5;
    final static int RESULT_OK = -1;
    public static final String INSTANCE_TICKET_TYPE_KEY = "instanceTicketType";
    public static final String INSTANCE_TICKET_ID_KEY = "instanceTicketId";
    private static final String INSTANCE_PLAY_WHEN_READY_KEY = "playWhenReady";
    private static final String INSTANCE_CURRENT_WINDOW_KEY = "currentWindow";
    private static final String INSTANCE_PLAY_BACK_POSITION_KEY = "playBackPosition";
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private Uri videoUri;
    private int mReceivedTicketId = C.DEFAULT_TICKET_ID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        Notifications.clearAllNotifications(this);

        setupVideoPlayer(savedInstanceState);
        receiveTicketInfo();
        setupViewModelFactory();
        initViews();


        if(mTicketType == C.ADD_TICKET_TYPE){
            viewModel.tempTicket.setTicketId(IdUtils.newID());
        }

    }


    /**
     * Set up Video Player
     */
    private void setupVideoPlayer(Bundle savedInstanceState){

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

    }


    /**
     * Get ticket IdUtils from MainActivity
     */
    private void receiveTicketInfo(){
        Intent intent = getIntent();
        mReceivedTicketId = intent.getIntExtra(C.TICKET_ID_KEY, C.DEFAULT_TICKET_ID);
        mTicketType = intent.getIntExtra(C.TICKET_TYPE_KEY, C.VIEW_TICKET_TYPE);
    }


    /**
     * Set up ViewModelFactory for each instance of AddTicketActivity
     */
    private void setupViewModelFactory(){

        AddTicketViewModelFactory factory = new AddTicketViewModelFactory(this.getApplication(), mReceivedTicketId);
        viewModel = ViewModelProviders.of(this, factory).get(AddTicketViewModel.class);
        viewModel.setup(this, mReceivedTicketId, mTicketType);

    }



    //Removed for aws testing
    /**
     * Save the instance ticket IdUtils in case of screen rotation or app exit
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(INSTANCE_TICKET_TYPE_KEY , mTicketType);
        outState.putInt(INSTANCE_TICKET_ID_KEY , mReceivedTicketId);

        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();

        outState.putBoolean(INSTANCE_PLAY_WHEN_READY_KEY, playWhenReady);
        outState.putInt(INSTANCE_CURRENT_WINDOW_KEY, currentWindow);
        outState.putLong(INSTANCE_PLAY_BACK_POSITION_KEY, playbackPosition);

        super.onSaveInstanceState(outState);
    }



    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {

        setupActionBar();

        mTitleText = findViewById(R.id.editTextTicketTitle);
        mDescriptionText = findViewById(R.id.editTextTicketDescription);
        saveButton = findViewById(R.id.saveButton);
        videoButton = findViewById(R.id.videoButton);
        videoDeleteButton = findViewById(R.id.videoDelete);
        videoWrapper = findViewById(R.id.videoWrapper);
        streamVideo = findViewById(R.id.stream_video);
        chatButton = findViewById(R.id.chatButton);

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

        if(mTicketType == C.VIEW_TICKET_TYPE) {
            mTitleText.setEnabled(false);
            mDescriptionText.setEnabled(false);
            saveButton.setVisibility(View.INVISIBLE);
        } else if(mTicketType == C.UPDATE_TICKET_TYPE){
            saveButton.setText(R.string.update_button);
        } else if(mTicketType == C.ADD_TICKET_TYPE){
            saveButton.setText(R.string.add_button);
            chatButton.setVisibility(View.INVISIBLE);
        } else {
            //do nothing
        }

        updateTitleDescription();

        setVideoView();

    }


    private void setupActionBar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }

    }


    @Override
    public void updateTitleDescription() {
        mTitleText.setText(viewModel.tempTicket.getTicketTitle());
        mDescriptionText.setText(viewModel.tempTicket.getTicketDescription());
    }


    @Override
    public void setVideoView(){

        if ( viewModel.tempTicket.getTicketVideoPostId().equals(C.DEFAULT_TICKET_VIDEO_POST_ID) ) {
            streamVideo.setVisibility(View.INVISIBLE);
            videoButton.setVisibility(View.VISIBLE);
            videoDeleteButton.setVisibility(View.INVISIBLE);
            if (mTicketType == C.VIEW_TICKET_TYPE) {
                videoWrapper.setBackground( (Drawable) ContextCompat.getDrawable(MyApplication.getAppContext(),R.drawable.background_border_solid) );
                videoButton.setBackground( (Drawable) ContextCompat.getDrawable(MyApplication.getAppContext(),R.drawable.ic_no_video) );
                videoButton.setEnabled(false);
            }else {
                videoWrapper.setBackground( (Drawable) ContextCompat.getDrawable(MyApplication.getAppContext(),R.drawable.background_border_dashed) );
                videoButton.setBackground( (Drawable) ContextCompat.getDrawable(MyApplication.getAppContext(),R.drawable.ic_add_video) );
                videoButton.setEnabled(true);
            }
        } else {
            streamVideo.setVisibility(View.VISIBLE);
            videoButton.setVisibility(View.INVISIBLE);
            videoWrapper.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(),R.color.videoBackground));
            if (mTicketType != C.VIEW_TICKET_TYPE) {
                videoDeleteButton.setVisibility(View.VISIBLE);
            }
            if ( viewModel.tempTicket.getTicketVideoPostId().equals(C.VIDEO_EXISTS_TICKET_VIDEO_POST_ID )){
                videoUri = Uri.parse(viewModel.tempTicket.getTicketVideoInternetUrl());
            } else {
                videoUri = Uri.parse(viewModel.tempTicket.getTicketVideoLocalUri());
            }
            currentWindow = 0;
            playbackPosition = 0;
            initializePlayer();
        }
    }

    
    /**
     * Add or update entry when SAVE button is clicked.
     */
    public void onSaveButtonClicked() {

        setTitleDescription();
        setOtherTicketValues();

        AppseeFunctions.saveTicket(mTicketType,
                viewModel.tempTicket.getUserId(),
                viewModel.tempTicket.getUserName(),
                viewModel.tempTicket.getTicketId(),
                viewModel.tempTicket.getTicketTitle());

        viewModel.addTicket(mTicketType);

        finish();

    }


    private void setTitleDescription(){

        // Set title if blank or not
        String title = mTitleText.getText().toString();
        if(title.equals(C.BLANK_TICKET_TITLE)){
            viewModel.tempTicket.setTicketTitle(C.DEFAULT_TICKET_TITLE);
        } else {
            viewModel.tempTicket.setTicketTitle(title);

        }

        // Set description if blank or not
        String description = mDescriptionText.getText().toString();
        if(description.equals(C.BLANK_DESCRIPTION_TITLE)){
            viewModel.tempTicket.setTicketDescription(C.DEFAULT_TICKET_DESCRIPTION);
        } else {
            viewModel.tempTicket.setTicketDescription(description);

        }
    }


    private void setOtherTicketValues(){

        viewModel.tempTicket.setTicketDate(DateTimeUtils.dateToString(new Date()));
        viewModel.tempTicket.setUserId(UserProfileSettings.getUserID());
        viewModel.tempTicket.setUserName(UserProfileSettings.getUsername());
        viewModel.tempTicket.setUserPhotoUrl(UserProfileSettings.getUserPhotoURL());

    }


    public void onVideoDeleteButtonClicked(View view){

        viewModel.tempTicket.setTicketVideoPostId(C.DEFAULT_TICKET_VIDEO_POST_ID);
        viewModel.tempTicket.setTicketVideoLocalUri(C.DEFAULT_TICKET_VIDEO_LOCAL_URI);
        viewModel.tempTicket.setTicketVideoInternetUrl(C.DEFAULT_TICKET_VIDEO_INTERNET_URL);
        setVideoView();
    }


    /**
     * Start Camera Intent when video button is clicked.
     */
    public void onVideoButtonClicked() {
        requestReadExternalStoragePermission();
    }


    private void requestReadExternalStoragePermission(){
        if (ContextCompat.checkSelfPermission(AddTicketActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddTicketActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Optional explanation
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(AddTicketActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            // Permission has already been granted
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(takeVideoIntent,VIDEO_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(takeVideoIntent,VIDEO_REQUEST);
                } else {
                    // permission denied, boo
                }
                return;
            }
        }
    }



    /**
    * Get Result from Camera Video Intent.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(  ( requestCode == VIDEO_REQUEST )  &&  ( resultCode == RESULT_OK )  ){

            videoUri = data.getData();

            viewModel.tempTicket.setTicketVideoPostId(C.VIDEO_CREATED_TICKET_VIDEO_POST_ID);
            viewModel.tempTicket.setTicketVideoLocalUri(videoUri.toString());

            setVideoView();

        }
    }


    public AddTicketViewModel getViewModel() {
        return viewModel;
    }


    public int getTicketId() {
        return viewModel.tempTicket.getTicketId();
    }


    public void goToChat(View view){
        Intent intent = new Intent(AddTicketActivity.this, ChatActivity.class);
        intent.putExtra( C.TICKET_ID_KEY , Integer.toString(viewModel.tempTicket.getTicketId()));
        intent.putExtra( C.TICKET_TITLE_KEY , viewModel.tempTicket.getTicketTitle() );
        startActivity(intent);
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
        player.seekTo(currentWindow, playbackPosition);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                mediaDataSourceFactory, extractorsFactory, null, null);
        player.prepare(mediaSource, true, false);

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





