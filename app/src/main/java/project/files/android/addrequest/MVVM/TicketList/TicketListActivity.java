package project.files.android.addrequest.MVVM.TicketList;

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
import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.MVVM.Main.MainActivity;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Services.FirebaseDbListenerService;
import project.files.android.addrequest.SharedPreferences.UserProfileSettings;
import project.files.android.addrequest.Utils.GlobalConstants;

public class TicketListActivity extends AppCompatActivity implements TicketAdapter.ItemClickListener {

    /**
     * Initialize values.
     */

    // Constant for logging
    private static final String TAG = TicketListActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private TicketListFragment ticketListFragment;
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

        ticketType = GlobalConstants.VIEW_TICKET_TYPE;

        mAdapter = new TicketAdapter( this, this);

        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTicketIntent = new Intent(TicketListActivity.this, AddTicketActivity.class);
                addTicketIntent.putExtra(GlobalConstants.TICKET_TYPE_KEY, GlobalConstants.ADD_TICKET_TYPE);
                startActivity(addTicketIntent);
            }
        });

        viewModel = ViewModelProviders.of(this).get(TicketListViewModel.class);
        viewModel.updateDB(mAdapter,GlobalConstants.LOAD_ALL);

        fragmentManager = getSupportFragmentManager();
        ticketListFragment = new TicketListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.ticketlist_fragment_container, ticketListFragment)
                .commit();

    }


    /**
     * Set up Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.all_tickets_menu).setVisible(false);
        menu.findItem(R.id.user_name_menu).setVisible(true);
        menu.findItem(R.id.sign_out_menu).setTitle(R.string.sign_out);
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

        viewModel.updateDB(mAdapter,GlobalConstants.LOAD_USER);

        openProfile();

        ticketType = GlobalConstants.UPDATE_TICKET_TYPE;
        ticketListFragment.setSwipe(ticketType);

    }



    private void setAllTicketsMode(){

        getSupportActionBar().setTitle(R.string.ticket_list_activity_name);

        viewModel.updateDB(mAdapter,GlobalConstants.LOAD_ALL);

        closeProfile();

        ticketType = GlobalConstants.VIEW_TICKET_TYPE;
        ticketListFragment.setSwipe(ticketType);

    }


    /**
     * Go to user Profile.
     */
    private void openProfile() {

        ProfileFragment profileFragment = new ProfileFragment();
        fragmentManager.beginTransaction()
                .add(R.id.profile_fragment_container,profileFragment,GlobalConstants.PROFILE_FRAGMENT_TAG)
                .commit();

    }


    /**
     * Go to user Profile.
     */
    private void closeProfile() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(GlobalConstants.PROFILE_FRAGMENT_TAG);
        if(fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();

    }


    /**
     * Log out.
     */
    private void logout() {

        stopService(new Intent(this, FirebaseDbListenerService.class));
        UserProfileSettings.setUserProfileAtLogout(this);
        AuthUI.getInstance().signOut(this);

        Intent intent = new Intent(TicketListActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

    }



    /**
     * Intent to AddTicketActivity.
     */
    @Override
    public void onItemClickListener(int itemId) {


        Intent intent = new Intent(this, AddTicketActivity.class);
        intent.putExtra(GlobalConstants.TICKET_ID_KEY, itemId);
        intent.putExtra(GlobalConstants.TICKET_TYPE_KEY, ticketType);
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

}
