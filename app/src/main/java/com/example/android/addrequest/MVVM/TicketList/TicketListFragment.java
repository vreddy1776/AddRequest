package com.example.android.addrequest.MVVM.TicketList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.addrequest.Adapter.TicketAdapter;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.R;
import com.example.android.addrequest.Utils.GlobalConstants;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TicketListFragment extends Fragment{

    private static final String TAG = TicketListFragment.class.getSimpleName();

    private ItemTouchHelper itemTouchHelper;

    private RecyclerView mRecyclerView;

    public TicketListFragment() {
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

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
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
