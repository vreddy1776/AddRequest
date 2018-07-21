package com.example.android.addrequest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Note:  Rebase to parent: 5dfda53  commit: 41029cbffa6cb59b5cdf6123f2558bb0523147b4  title: Moved MainActivity to TicketListActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, TicketListActivity.class);
        startActivity(intent);




    }


}
