package com.example.android.addrequest.MVVM.TicketList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.addrequest.R;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TicketListFragment extends Fragment{

    private static final String TAG = TicketListFragment.class.getSimpleName();


    private RecyclerView mRecyclerView;


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
        mRecyclerView.setAdapter(((TicketListActivity)this.getActivity()).getTicketAdapter());

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);


        // Setup ViewModel


        // Return the rootView
        return rootView;
    }








}
