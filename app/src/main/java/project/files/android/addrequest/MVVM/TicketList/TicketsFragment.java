package project.files.android.addrequest.MVVM.TicketList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.files.android.addrequest.Adapter.TicketAdapter;
import project.files.android.addrequest.Database.TicketEntry;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.DividerItemDecorator;
import project.files.android.addrequest.Utils.GlobalConstants;


/**
 * Tickets Fragment
 *
 * Fragment containing RecyclerView list of tickets.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class TicketsFragment extends Fragment{

    private static final String TAG = TicketsFragment.class.getSimpleName();

    private ItemTouchHelper itemTouchHelper;

    private RecyclerView mRecyclerView;

    public TicketsFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_ticket_list, container, false);

        final TicketListActivity ticketListActivity = (TicketListActivity) this.getActivity();

        final TicketAdapter ticketAdapter = ticketListActivity.getTicketAdapter();

        // Set the RecyclerView to its corresponding view
        mRecyclerView = rootView.findViewById(R.id.recyclerViewTickets);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and attach it to the RecyclerView
        mRecyclerView.setAdapter(ticketAdapter);

        // Remove divider lines
        mRecyclerView.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.no_divider)));


        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                List<TicketEntry> tickets = ticketAdapter.getTickets();
                TicketEntry ticket = tickets.get(position);
                ticketListActivity.swipeTicket(ticket.getTicketId());

            }
        });

        return rootView;
    }




    public void setSwipe(int ticketType){

        if (ticketType == GlobalConstants.UPDATE_TICKET_TYPE){
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        } else {
            itemTouchHelper.attachToRecyclerView(null);
        }

    }

}


