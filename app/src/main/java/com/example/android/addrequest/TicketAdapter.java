package com.example.android.addrequest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.addrequest.database.TicketEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder>{


    /**
     * Returns the number of items to display.
     */

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Class variables for the List that holds ticket data and the Context
    private List<TicketEntry> mTicketEntries;
    private Context mContext;

    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    /**
     * Constructor for the TicketAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public TicketAdapter(Context context /*, ItemClickListener listener*/) {
        mContext = context;
        //mItemClickListener = listener;
    }


    /**
     * Set up ItemClickListener.
     */
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TicketViewHolder that holds the view for each ticket
     */
    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the ticket_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.ticket_layout, parent, false);

        return new TicketViewHolder(view);

    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {

        // Determine the values of the wanted data
        TicketEntry ticketEntry = mTicketEntries.get(position);
        String title = ticketEntry.getTitle();
        String description = ticketEntry.getDescription();
        String updatedAt = dateFormat.format(ticketEntry.getUpdatedAt());

        //Set values
        holder.ticketTitleView.setText(title);
        holder.ticketDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);

    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTicketEntries == null) {
            return 0;
        }
        return mTicketEntries.size();
    }


    /**
     * Returns a list of tickets for an Activity to delete.
     */
    public List<TicketEntry> getTickets() {
        return mTicketEntries;
    }


    /**
     * When data changes, this method updates the list of ticketEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTickets(List<TicketEntry> ticketEntries) {
        mTicketEntries = ticketEntries;
        notifyDataSetChanged();
    }


    /**
     * Inner class for creating ViewHolders.
     */
    class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the ticket description and priority TextViews
        TextView ticketTitleView;
        TextView ticketDescriptionView;
        TextView updatedAtView;

        /**
         * Constructor for the TicketViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public TicketViewHolder(View itemView) {

            super(itemView);

            ticketTitleView = itemView.findViewById(R.id.ticketTitle);
            ticketDescriptionView = itemView.findViewById(R.id.ticketDescription);
            updatedAtView = itemView.findViewById(R.id.ticketUpdatedAt);

        }

        @Override
        public void onClick(View v) {
            // Not in use
        }

    }


}
