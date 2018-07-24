package com.example.android.addrequest.MVVM.TicketList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.addrequest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Set the RecyclerView to its corresponding view
        profilePic = rootView.findViewById(R.id.profilePic);
        Glide.with(getContext())
                .load(user.getPhotoUrl())
                .thumbnail(0.1f)
                .into(profilePic);


        // Return the rootView
        return rootView;
    }
    

}
