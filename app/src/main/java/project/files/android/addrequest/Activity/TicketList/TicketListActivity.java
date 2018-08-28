package project.files.android.addrequest.Activity.TicketList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;

import project.files.android.addrequest.Adapter.TicketAdapter;
import project.files.android.addrequest.Activity.AddTicket.AddTicketActivity;
import project.files.android.addrequest.Activity.Main.MainActivity;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Background.FirebaseDbListenerService;
import project.files.android.addrequest.Settings.UserProfileSettings;
import project.files.android.addrequest.Utils.C;


/**
 * TicketList Activity
 *
 * Activity containing {@link TicketsFragment} and {@link ProfileFragment}.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class TicketListActivity extends AppCompatActivity implements TicketAdapter.ItemClickListener {

    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = TicketListActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private TicketsFragment ticketsFragment;
    private FloatingActionButton fabButton;
    private TicketListViewModel viewModel;
    private TicketAdapter mAdapter;
    private int ticketType;


    /**
     * Main Activity created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        ticketType = C.VIEW_TICKET_TYPE;

        mAdapter = new TicketAdapter( this);

        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTicketIntent = new Intent(TicketListActivity.this, AddTicketActivity.class);
                addTicketIntent.putExtra(C.TICKET_TYPE_KEY, C.ADD_TICKET_TYPE);
                startActivity(addTicketIntent);
            }
        });

        viewModel = ViewModelProviders.of(this).get(TicketListViewModel.class);
        viewModel.updateDB(mAdapter, C.LOAD_ALL);

        fragmentManager = getSupportFragmentManager();

        openTicketsFragment();

    }


    /**
     * Set up Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.all_tickets_menu).setVisible(false);
        menu.findItem(R.id.user_name_menu).setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_name_menu:
                setProfileMode();
                return true;
            case R.id.all_tickets_menu:
                setAllTicketsMode();
                return true;
            case R.id.sign_out_menu:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setProfileMode(){

        getSupportActionBar().setTitle(R.string.profile_activity_name);

        viewModel.updateDB(mAdapter, C.LOAD_USER);

        openProfileFragment();

        ticketType = C.UPDATE_TICKET_TYPE;
        ticketsFragment.setSwipe(ticketType);

    }



    private void setAllTicketsMode(){

        getSupportActionBar().setTitle(R.string.ticket_list_activity_name);

        viewModel.updateDB(mAdapter, C.LOAD_ALL);

        closeProfileFragment();

        ticketType = C.VIEW_TICKET_TYPE;
        ticketsFragment.setSwipe(ticketType);

    }


    /**
     * Go to user Profile.
     */
    private void openProfileFragment() {

        ProfileFragment profileFragment = new ProfileFragment();
        fragmentManager.beginTransaction()
                .add(R.id.profile_fragment_container,profileFragment, C.PROFILE_FRAGMENT_TAG)
                .commit();

    }


    /**
     * Go to user Profile.
     */
    private void closeProfileFragment() {

        Fragment profileFragment = getSupportFragmentManager().findFragmentByTag(C.PROFILE_FRAGMENT_TAG);
        if(profileFragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(profileFragment)
                    .commit();

    }


    /**
     * Open Tickets Fragment on Activity open.
     */
    private void openTicketsFragment() {

        ticketsFragment = new TicketsFragment();
        fragmentManager.beginTransaction()
                .add(R.id.ticketlist_fragment_container, ticketsFragment, C.TICKETS_FRAGMENT_TAG)
                .commit();

    }


    /**
     * Close Tickets Fragment on Activity close.
     */
    private void closeTicketsFragment() {

        if(ticketsFragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(ticketsFragment)
                    .commit();

    }



    /**
     * Log out, end Firebase DB listener service, go to main activity, and destroy activity
     */
    private void logout() {

        stopService(new Intent(this, FirebaseDbListenerService.class));
        UserProfileSettings.setUserProfileAtLogout();
        AuthUI.getInstance().signOut(this);

        Intent intent = new Intent(TicketListActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }



    /**
     * Intent to AddTicketActivity.
     */
    @Override
    public void onItemClickListener(int itemId) {


        Intent intent = new Intent(this, AddTicketActivity.class);
        intent.putExtra(C.TICKET_ID_KEY, itemId);
        intent.putExtra(C.TICKET_TYPE_KEY, ticketType);
        startActivity(intent);

    }


    public TicketAdapter getTicketAdapter() {
        return this.mAdapter;
    }

    public void swipeTicket(int ticketId){
        viewModel.deleteTicket(ticketId);
    }


    @Override
    public void onBackPressed() {
        //do nothing
    }


    public void goToPay(View view){
        Intent intent = new Intent(this,PayActivity.class);
        startActivity(intent);
    }


}
