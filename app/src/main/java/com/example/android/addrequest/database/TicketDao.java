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
    LiveData<List<TicketEntry>> loadAllTasks();

    @Insert
    void insertTask(TicketEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TicketEntry taskEntry);

    @Delete
    void deleteTask(TicketEntry taskEntry);

    @Query("SELECT * FROM ticket WHERE id = :id")
    LiveData<TicketEntry> loadTaskById(int id);

}
