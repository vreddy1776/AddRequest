package project.files.android.addrequest.MVVM.TicketList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import project.files.android.addrequest.R;
import project.files.android.addrequest.Settings.UserProfileSettings;


/**
 * Profile Fragment
 *
 * Section showing username and user profile pic.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView firstNameText = rootView.findViewById(R.id.firstNameText);
        firstNameText.setText(UserProfileSettings.getFirstname(getContext()));

        TextView lastNameText = rootView.findViewById(R.id.lastNameText);
        lastNameText.setText(UserProfileSettings.getLastname(getContext()));

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        ImageView profilePic = rootView.findViewById(R.id.profilePic);

        Glide.with(getContext())
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(profilePic);

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.all_tickets_menu).setVisible(true);
        menu.findItem(R.id.user_name_menu).setVisible(false);
    }

}
