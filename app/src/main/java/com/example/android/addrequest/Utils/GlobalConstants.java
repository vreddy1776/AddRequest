package com.example.android.addrequest.Utils;

import android.net.Uri;

public class GlobalConstants {

    // Intent Keys
    public static final String TICKET_ID_KEY = "TicketId";
    public static final String TICKET_VIEWTYPE_KEY = "TicketViewType";
    public static final String CHAT_ID_KEY = "TicketViewType";


    // Default Values
    public static final int DEFAULT_VIDEO_ID = -1;
    public static final int DEFAULT_TICKET_VIEWTYPE = -1;
    public static final int EDIT_TICKET_VIEWTYPE = 5;


    // Ticket ViewTypes
    public static final int ADD_TICKET_TYPE = 5;
    public static final int EDIT_TICKET_TYPE = 10;
    public static final int VIEW_TICKET_TYPE = 15;


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


    /* DEFAULT TICKET VALUES  */
    /* ticketId               */    public static final int DEFAULT_TICKET_ID = -1;
    /* ticketTitle            */    public static final String DEFAULT_TICKET_TITLE = " -- no title -- ";
    /* ticketDescription      */    public static final String DEFAULT_TICKET_DESCRIPTION = " -- no description -- ";
    /* ticketDate             */    public static final String DEFAULT_TICKET_DATE = "Sun Jan 01 00:00:01 CDT 0001";
    /* ticketVideoPostId      */    public static final String DEFAULT_TICKET_VIDEO_POST_ID = "-1";
    /* ticketVideoLocalUri    */    public static final String DEFAULT_TICKET_VIDEO_LOCAL_URI = "no_video_local_uri";
    /* ticketVideoInternetUrl */    public static final String DEFAULT_TICKET_VIDEO_INTERNET_URL = "no_video_internet_url";
    /* userId                 */    public static final String DEFAULT_USER_ID = "no_user_id";
    /* userName               */    public static final String DEFAULT_USER_NAME = "anonymous";
    /* userPhotoUrl           */    public static final String DEFAULT_USER_PHOTO_URL = "no_photo_url";





}
