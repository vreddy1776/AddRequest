package com.example.android.addrequest.MVVM.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.addrequest.MVVM.Login.LoginActivity;
import com.example.android.addrequest.MVVM.TicketList.TicketListActivity;
import com.example.android.addrequest.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Actionbar Title
        getSupportActionBar().setTitle(R.string.main_activity_name);


        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();


        //AuthUI.getInstance().signOut(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(currentUser == null){
                    goToLogin();
                } else {
                    goToTicketList();
                }
            }
        }, 5000);



    }

    public void loginButtonClick(View view){
        if(currentUser == null){
            goToLogin();
        } else {
            goToTicketList();
        }
    }

    private void goToLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToTicketList(){
        Intent intent = new Intent(MainActivity.this, TicketListActivity.class);
        startActivity(intent);
    }





}
