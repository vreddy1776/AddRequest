package com.example.android.addrequest.Utils;

import android.net.Uri;

public class GlobalConstants {

    // Intent Keys
    public static final String TICKET_ID_KEY = "TicketId";
    public static final String TICKET_VIEWTYPE_KEY = "TicketViewType";
    public static final String CHAT_ID_KEY = "TicketViewType";


    // Default Values
    public static final int DEFAULT_TICKET_ID = -1;
    public static final int DEFAULT_VIDEO_ID = -1;
    public static final int DEFAULT_TICKET_VIEWTYPE = -1;
    public static final int EDIT_TICKET_VIEWTYPE = 5;


    // Fragment Tags
    public static final String PROFILE_FRAGMENT_TAG = "ProfileFragment";


    // SQLlite Ticket DB update codes
    public static final int LOAD_ALL = 10;
    public static final int LOAD_USER = 9;


    // Default Ticket Video Info
    public static final int DEFAULT_VIDEO_POST_ID = -1;
    public static final Uri DEFAULT_VIDEO_LOCAL_URI = Uri.parse("no_video_local_uri");
    public static final Uri DEFAULT_VIDEO_INTERNET_URL = Uri.parse("no_video_internet_url");


    // S3 Bucket
    public static final String MAIN_S3_URL = "https://s3.amazonaws.com/addrequest-deployments-mobilehub-1269242402/";



}
