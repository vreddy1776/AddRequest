package project.files.android.addrequest.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


/**
 * DOA to access AppDatabase
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
@Dao
public interface TicketDao {

    @Query("SELECT * FROM ticket")
    LiveData<List<TicketEntry>> loadAllTickets();

    @Query("SELECT * FROM ticket WHERE userId = :userId")
    LiveData<List<TicketEntry>> loadUserTickets(String userId);

    @Insert
    void insertTicket(TicketEntry ticketEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTicket(TicketEntry ticketEntry);

    @Delete
    void deleteTicket(TicketEntry ticketEntry);

    @Query("DELETE FROM ticket WHERE ticketId = :ticketId")
    void deleteTicketById(int ticketId);

    @Query("DELETE FROM ticket")
    void clearAllTickets();

    @Query("SELECT * FROM ticket WHERE ticketId = :ticketId")
    LiveData<TicketEntry> loadTicketById(int ticketId);

}
