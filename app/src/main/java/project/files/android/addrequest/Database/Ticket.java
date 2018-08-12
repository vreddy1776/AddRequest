package project.files.android.addrequest.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import project.files.android.addrequest.Utils.C;


/**
 * Local DB constructor
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
@Entity(tableName = "ticket")
public class Ticket {

    @PrimaryKey(autoGenerate = true)
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


    @Ignore
    public Ticket() {
        this.ticketId = C.DEFAULT_TICKET_ID;
        this.ticketTitle = C.BLANK_TICKET_TITLE;
        this.ticketDescription = C.BLANK_DESCRIPTION_TITLE;
        this.ticketDate = C.DEFAULT_TICKET_DATE;
        this.ticketVideoPostId = C.DEFAULT_TICKET_VIDEO_POST_ID;
        this.ticketVideoLocalUri = C.DEFAULT_TICKET_VIDEO_LOCAL_URI;
        this.ticketVideoInternetUrl = C.DEFAULT_TICKET_VIDEO_INTERNET_URL;
        this.userId = C.DEFAULT_USER_ID;
        this.userName = C.DEFAULT_USER_NAME;
        this.userPhotoUrl = C.DEFAULT_USER_PHOTO_URL;

    }


    @Ignore
    public Ticket(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.ticketTitle = ticket.getTicketTitle();
        this.ticketDescription = ticket.getTicketDescription();
        this.ticketDate = ticket.getTicketDate();
        this.ticketVideoPostId = ticket.getTicketVideoPostId();
        this.ticketVideoLocalUri = ticket.getTicketVideoLocalUri();
        this.ticketVideoInternetUrl = ticket.getTicketVideoInternetUrl();
        this.userId = ticket.getUserId();
        this.userName = ticket.getUserName();
        this.userPhotoUrl = ticket.getUserPhotoUrl();

    }


    @Ignore
    public Ticket(
            String ticketTitle,
            String ticketDescription,
            String ticketDate,
            String ticketVideoPostId,
            String ticketVideoLocalUri,
            String ticketVideoInternetUrl,
            String userId,
            String userName,
            String userPhotoUrl) {
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketDate = ticketDate;
        this.ticketVideoPostId = ticketVideoPostId;
        this.ticketVideoLocalUri = ticketVideoLocalUri;
        this.ticketVideoInternetUrl = ticketVideoInternetUrl;
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;

    }


    public Ticket(
            int ticketId,
            String ticketTitle,
            String ticketDescription,
            String ticketDate,
            String ticketVideoPostId,
            String ticketVideoLocalUri,
            String ticketVideoInternetUrl,
            String userId,
            String userName,
            String userPhotoUrl) {
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketDate = ticketDate;
        this.ticketVideoPostId = ticketVideoPostId;
        this.ticketVideoLocalUri = ticketVideoLocalUri;
        this.ticketVideoInternetUrl = ticketVideoInternetUrl;
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;

    }


    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getTicketVideoPostId() {
        return ticketVideoPostId;
    }

    public void setTicketVideoPostId(String ticketVideoPostId) {
        this.ticketVideoPostId = ticketVideoPostId;
    }

    public String getTicketVideoLocalUri() {
        return ticketVideoLocalUri;
    }

    public void setTicketVideoLocalUri(String ticketVideoLocalUri) {
        this.ticketVideoLocalUri = ticketVideoLocalUri;
    }

    public String getTicketVideoInternetUrl() {
        return ticketVideoInternetUrl;
    }

    public void setTicketVideoInternetUrl(String ticketVideoInternetUrl) {
        this.ticketVideoInternetUrl = ticketVideoInternetUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public void setTicket(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.ticketTitle = ticket.getTicketTitle();
        this.ticketDescription = ticket.getTicketDescription();
        this.ticketDate = ticket.getTicketDate();
        this.ticketVideoPostId = ticket.getTicketVideoPostId();
        this.ticketVideoLocalUri = ticket.getTicketVideoLocalUri();
        this.ticketVideoInternetUrl = ticket.getTicketVideoInternetUrl();
        this.userId = ticket.getUserId();
        this.userName = ticket.getUserName();
        this.userPhotoUrl = ticket.getUserPhotoUrl();

    }

}


