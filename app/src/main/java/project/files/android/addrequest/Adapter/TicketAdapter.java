package project.files.android.addrequest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import project.files.android.addrequest.Background.MyApplication;
import project.files.android.addrequest.Database.Ticket;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.C;
import project.files.android.addrequest.Utils.NameUtils;


/**
 * Message Adapter
 *
 * {@link project.files.android.addrequest.Adapter.TicketAdapter} exposes list of tickets
 * to {@link project.files.android.addrequest.Activity.TicketList.TicketsFragment}
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder>{

    private static final String TAG = TicketAdapter.class.getSimpleName();

    final private ItemClickListener mItemClickListener;
    private List<Ticket> mTicketList;


    /**
     * Constructor for the TicketAdapter that initializes the Context.
     *
     * @param listener the ItemClickListener
     */
    public TicketAdapter(ItemClickListener listener) {
        mItemClickListener = listener;
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

        // Inflate the item_ticket to a view
        View view = LayoutInflater.from(/*mContext*/ MyApplication.getAppContext())
                .inflate(R.layout.item_ticket, parent, false);

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
        Ticket ticket = mTicketList.get(position);
        String ticketTitle = ticket.getTicketTitle();
        String ticketDescription = ticket.getTicketDescription();
        String ticketDate = ticket.getTicketDate();
        String ticketVideoInternetUrl = ticket.getTicketVideoInternetUrl();
        String ticketVideoPostId = ticket.getTicketVideoPostId();
        String userName = ticket.getUserName();
        String userPhotoUrl = ticket.getUserPhotoUrl();

        //Set values
        holder.ticketTitleView.setText(ticketTitle);
        holder.ticketDescriptionView.setText(ticketDescription);
        holder.ticketDateView.setText(ticketDate);


        if(ticketVideoPostId.equals(C.DEFAULT_TICKET_VIDEO_POST_ID)){
            holder.ticketVideoThumbnailView.setVisibility(View.INVISIBLE);
        } else {
            holder.ticketVideoThumbnailView.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.isMemoryCacheable();
            Log.d(TAG, "url:  " + ticketVideoInternetUrl);
            Glide.with(MyApplication.getAppContext())
                    .load(ticketVideoInternetUrl)
                    .thumbnail(0.1f)
                    .into(holder.ticketVideoThumbnailView);
        }


        Glide.with(MyApplication.getAppContext())
                .load(userPhotoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userProfilePicView);

        holder.userNameTextView.setText(NameUtils.getFirstName(userName));

    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTicketList == null) {
            return 0;
        }
        return mTicketList.size();
    }


    /**
     * Returns a list of tickets for an Activity to delete.
     */
    public List<Ticket> getTickets() {
        return mTicketList;
    }


    /**
     * When data changes, this method updates the list of tickets
     * and notifies the adapter to use the new values on it
     */
    public void setTickets(List<Ticket> ticketList) {
        mTicketList = ticketList;
        notifyDataSetChanged();
    }


    /**
     * Inner class for creating ViewHolders.
     */
    class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the ticket description and priority TextViews
        TextView ticketTitleView;
        TextView ticketDescriptionView;
        TextView ticketDateView;
        ImageView ticketVideoThumbnailView;
        ImageView userProfilePicView;
        TextView userNameTextView;


        /**
         * Constructor for the TicketViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public TicketViewHolder(View itemView) {

            super(itemView);

            ticketTitleView = itemView.findViewById(R.id.ticketTitle);
            ticketDescriptionView = itemView.findViewById(R.id.ticketDescription);
            ticketDateView = itemView.findViewById(R.id.ticketUpdatedAt);
            ticketVideoThumbnailView = itemView.findViewById(R.id.videoThumbnail);
            userProfilePicView = itemView.findViewById(R.id.ticketUserProfilePic);
            userNameTextView = itemView.findViewById(R.id.ticketUserNameText);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int elementId = mTicketList.get(getAdapterPosition()).getTicketId();
            mItemClickListener.onItemClickListener(elementId);
        }

    }


}
