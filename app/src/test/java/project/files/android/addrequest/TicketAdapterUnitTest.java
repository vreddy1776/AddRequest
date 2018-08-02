package project.files.android.addrequest;

import android.util.Log;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import project.files.android.addrequest.Adapter.TicketAdapter;
import project.files.android.addrequest.Database.TicketEntry;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.Utils.ID;
import project.files.android.addrequest.Utils.Name;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class TicketAdapterUnitTest {

    @Test
    public void testItemCount() {

        TicketAdapter ticketAdapter = Mockito.mock(TicketAdapter.class);

        when(ticketAdapter.getItemCount()).thenReturn(2);

        int resultItemCount = ticketAdapter.getItemCount();

        int actualItemCount = 2;
        assertEquals(actualItemCount, resultItemCount);


    }

}