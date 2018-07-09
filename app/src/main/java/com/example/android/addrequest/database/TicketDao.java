package com.example.android.addrequest.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TicketDao {

    @Query("SELECT * FROM ticket")
    LiveData<List<TicketEntry>> loadAllTickets();

    @Insert
    void insertTicket(TicketEntry ticketEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTicket(TicketEntry ticketEntry);

    @Delete
    void deleteTicket(TicketEntry ticketEntry);

    @Query("SELECT * FROM ticket WHERE id = :id")
    LiveData<TicketEntry> loadTicketById(int id);

}
