package com.example.android.addrequest.MVVM.AddTicket;

public class FirebaseDbTicket {

    private String userId;
    private String userName;
    private String userPhotoUrl;
    private String ticketId;
    private String ticketTitle;
    private String ticketDescription;
    private String ticketVideoId;


    public FirebaseDbTicket() {
    }

    public FirebaseDbTicket(String userId, String userName, String userPhotoUrl, String ticketId, String ticketTitle, String ticketDescription, String ticketVideoUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketVideoId = ticketVideoId;
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
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

    public String getTicketVideoId() {
        return ticketVideoId;
    }

    public void setTicketVideoId(String ticketVideoId) {
        this.ticketVideoId = ticketVideoId;
    }

}
