package com.example.android.addrequest.MVVM.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.addrequest.MVVM.Login.LoginActivity;
import com.example.android.addrequest.R;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {

    // Rebase to 1 parent 0676892 commit 9383e433ff031143acbd1a70e826df586cd52592 - "Updating Video Player"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Actionbar Title
        getSupportActionBar().setTitle(R.string.main_activity_name);

        AuthUI.getInstance().signOut(this);

    }

    public void goToLogin(View view){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}
