package com.example.android.addrequest.MVVM.TicketList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.addrequest.Adapter.TicketAdapter;
import com.example.android.addrequest.Database.TicketEntry;
import com.example.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import com.example.android.addrequest.R;
import com.example.android.addrequest.Utils.GlobalConstants;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TicketListFragment extends Fragment implements  TicketAdapter.ItemClickListener{

    private static final String TAG = TicketListFragment.class.getSimpleName();


    private RecyclerView mRecyclerView;

    private TicketAdapter mAdapter;

    private TicketListViewModel viewModel;


    public TicketListFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_ticket_list, container, false);


        // Set the RecyclerView to its corresponding view
        mRecyclerView = rootView.findViewById(R.id.recyclerViewTickets);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TicketAdapter( getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);


        // Setup ViewModel
        setupViewModel();


        // Return the rootView
        return rootView;
    }

    /**
     * Intent to AddTicketActivity.
     */
    @Override
    public void onItemClickListener(int itemId) {

        // Launch AddTicketActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(this.getActivity(), AddTicketActivity.class);
        intent.putExtra(GlobalConstants.TICKET_ID_KEY, itemId);
        intent.putExtra(GlobalConstants.TICKET_VIEWTYPE_KEY, GlobalConstants.DEFAULT_TICKET_VIEWTYPE);
        Log.d(TAG, "Test - Ticked ID:  " + itemId);
        startActivity(intent);

    }


    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(TicketListViewModel.class);
        viewModel.updateDB();
        viewModel.getTickets().observe(this, new Observer<List<TicketEntry>>() {
            @Override
            public void onChanged(@Nullable List<TicketEntry> ticketEntries) {
                Log.d(TAG, "Updating list of tickets from LiveData in ViewModel");
                mAdapter.setTickets(ticketEntries);
            }
        });

    }



}
