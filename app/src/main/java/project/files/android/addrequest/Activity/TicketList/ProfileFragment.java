package project.files.android.addrequest.Activity.TicketList;

import android.app.Activity;
import android.content.Intent;
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

    public static final int PICK_IMAGE = 1;
    private ImageView profilePic;

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
        firstNameText.setText(UserProfileSettings.getFirstname());

        TextView lastNameText = rootView.findViewById(R.id.lastNameText);
        lastNameText.setText(UserProfileSettings.getLastname());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        profilePic = rootView.findViewById(R.id.profilePic);

        Glide.with(getContext())
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(profilePic);

        profilePic.setOnClickListener(photoClickListener);

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


    private View.OnClickListener photoClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Glide.with(getContext())
                    .load(data.getData())
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
        }
    }

}
