package com.example.android.addrequest.MVVM.AddTicket;

import android.arch.persistence.room.Ignore;

public class FirebaseDbTicket {


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
    public FirebaseDbTicket() {
    }


    public FirebaseDbTicket(
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

}
