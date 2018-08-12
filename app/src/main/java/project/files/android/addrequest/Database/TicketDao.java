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

    @Query("SELECT * FROM Ticket")
    LiveData<List<Ticket>> loadAllTickets();

    @Query("SELECT * FROM Ticket WHERE userId = :userId")
    LiveData<List<Ticket>> loadUserTickets(String userId);

    @Insert
    void insertTicket(Ticket ticket);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTicket(Ticket ticket);

    @Delete
    void deleteTicket(Ticket ticket);

    @Query("DELETE FROM Ticket WHERE ticketId = :ticketId")
    void deleteTicketById(int ticketId);

    @Query("DELETE FROM Ticket")
    void clearAllTickets();

    @Query("SELECT * FROM Ticket WHERE ticketId = :ticketId")
    LiveData<Ticket> loadTicketById(int ticketId);

}
