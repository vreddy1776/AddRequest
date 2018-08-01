package project.files.android.addrequest.MVVM.Main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Services.FirebaseDbListenerService;
import project.files.android.addrequest.SharedPreferences.UserProfileSettings;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();

        Fabric.with(this, new Crashlytics());
        Appsee.start(getString(R.string.com_appsee_apikey));
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthWithGoogle();

    }


    private void setupActionBar(){

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }

    }


    private void firebaseAuthWithGoogle() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    gotoTicketList();
                } else {
                    // User is signed out
                    goToLogin();
                }
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                gotoTicketList();

            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    private void gotoTicketList(){

        final String userId = mFirebaseAuth.getCurrentUser().getUid();
        final String userName = mFirebaseAuth.getCurrentUser().getDisplayName();
        final String userPhotoUrl = mFirebaseAuth.getCurrentUser().getPhotoUrl().toString();

        UserProfileSettings.setUserProfileAtLogin(this,
                userId,
                userName,
                userPhotoUrl);

        Appsee.addEvent("User Sign-In", new HashMap<String, Object>() {{
            put("User Id", userId);
            put("User Name", userName);
        }});

        startService(new Intent(this, FirebaseDbListenerService.class));

        Intent intent = new Intent(MainActivity.this, TicketListActivity.class);
        startActivity(intent);

    }


    private void goToLogin(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setProviders(
                                        AuthUI.GOOGLE_PROVIDER)
                                .build(),
                        RC_SIGN_IN);
            }
        }, 3000);   //3 second

    }



    @Override
    public void onBackPressed() {
        // do nothing
    }

}
