package project.files.android.addrequest.Utils;

import android.net.Uri;


/**
 * Global Constants
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class C {

    // Intent or Bundle Keys
    public static final String TICKET_KEY = "Ticket";
    public static final String TICKET_ID_KEY = "TicketId";
    public static final String TICKET_TITLE_KEY = "TicketTitle";
    public static final String TICKET_TYPE_KEY = "TicketType";


    // Database Child NameUtils
    public static final String CHILD_NAME_TICKETS = "Tickets";
    public static final String CHILD_NAME_MESSAGES = "Messages";



    // Default Values
    public static final int DEFAULT_VIDEO_ID = -1;


    // Ticket ViewTypes
    public static final int ADD_TICKET_TYPE = 5;
    public static final int UPDATE_TICKET_TYPE = 10;
    public static final int VIEW_TICKET_TYPE = 15;


    // Video Id's
    public static final String VIDEO_CREATED_TICKET_VIDEO_POST_ID = "video_created";
    public static final String VIDEO_EXISTS_TICKET_VIDEO_POST_ID = "video_exists";


    // Fragment Tags
    public static final String PROFILE_FRAGMENT_TAG = "ProfileFragment";
    public static final String TICKETS_FRAGMENT_TAG = "TicketsFragment";


    // SQLlite Ticket DB update codes
    public static final int LOAD_ALL = 10;
    public static final int LOAD_USER = 9;


    // Default Ticket Video Info
    public static final int DEFAULT_VIDEO_POST_ID = -1;
    public static final Uri DEFAULT_VIDEO_LOCAL_URI = Uri.parse("no_video_local_uri");
    public static final Uri DEFAULT_VIDEO_INTERNET_URL = Uri.parse("no_video_internet_url");


    // S3 Bucket
    public static final String MAIN_S3_URL = "https://s3.amazonaws.com/addrequest-deployments-mobilehub-1269242402/";


    // Edit Text Values
    public static final String BLANK_TICKET_TITLE = "";
    public static final String BLANK_DESCRIPTION_TITLE = "";



    /* DEFAULT TICKET VALUES  */
    /* ticketId               */    public static final int DEFAULT_TICKET_ID = -1;
    /* ticketTitle            */    public static final String DEFAULT_TICKET_TITLE = " -- no title -- ";
    /* ticketDescription      */    public static final String DEFAULT_TICKET_DESCRIPTION = " -- no description -- ";
    /* ticketDate             */    public static final String DEFAULT_TICKET_DATE = "Sun Jan 01 00:00:01 CDT 0001";
    /* ticketVideoPostId      */    public static final String DEFAULT_TICKET_VIDEO_POST_ID = "no_video";
    /* ticketVideoLocalUri    */    public static final String DEFAULT_TICKET_VIDEO_LOCAL_URI = "no_video_local_uri";
    /* ticketVideoInternetUrl */    public static final String DEFAULT_TICKET_VIDEO_INTERNET_URL = "no_video_internet_url";
    /* userId                 */    public static final String DEFAULT_USER_ID = "no_user_id";
    /* userName               */    public static final String DEFAULT_USER_NAME = "anonymous";
    /* userPhotoUrl           */    public static final String DEFAULT_USER_PHOTO_URL = "no_photo_url";





}
