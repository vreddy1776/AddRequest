package com.example.android.addrequest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder>{


    /**
     * Constructor for the TicketAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public TicketAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false);

        return new TicketViewHolder(view);

    }


    /**
     * Inner class for creating ViewHolders.
     */
    class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the task description and priority TextViews
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
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);

        }

        @Override
        public void onClick(View v) {
            // Not in use
        }
    }


}
