package com.example.android.addrequest.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ticket")
public class TicketEntry {

    @PrimaryKey(autoGenerate = true)
    private int ticketId;

    private String ticketTitle;
    private String ticketDescription;
    private String ticketDate;
    private String ticketVideoId;
    private String userId;
    private String userName;
    private String userPhotoUrl;


    @Ignore
    public TicketEntry(
            String ticketTitle,
            String ticketDescription,
            String ticketDate,
            String ticketVideoId,
            String userId,
            String userName,
            String userPhotoUrl) {
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketDate = ticketDate;
        this.ticketVideoId = ticketVideoId;
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;

    }

    public TicketEntry(
            int ticketId,
            String ticketTitle,
            String ticketDescription,
            String ticketDate,
            String ticketVideoId,
            String userId,
            String userName,
            String userPhotoUrl) {
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
        this.ticketDescription = ticketDescription;
        this.ticketDate = ticketDate;
        this.ticketVideoId = ticketVideoId;
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

    public String getTicketVideoId() {
        return ticketVideoId;
    }

    public void setTicketVideoId(String ticketVideoId) {
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


}


