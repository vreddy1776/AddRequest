package com.example.android.addrequest.sync;

import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.AppExecuters;
import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.DateConverter;
import com.example.android.addrequest.database.TicketEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class SyncBulk {

    // Constant for logging
    private static final String TAG = SyncBulk.class.getSimpleName();

    // Database
    private AppDatabase database;


    public void bulkPopulate(Context context, JSONArray jsonArray){

        database = AppDatabase.getInstance(context);

        for ( int i = 0 ; i < jsonArray.length() ; i++) {
            try {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Log.d(TAG, "JSONobject:  " + jsonObject );
                Log.d(TAG, "id:  " + jsonObject.get("id") );
                Log.d(TAG, "title:  " + jsonObject.get("title") );
                Log.d(TAG, "description:  " + jsonObject.get("description") );
                Log.d(TAG, "date:  " + jsonObject.get("date") );

                int id = Integer.parseInt(jsonObject.get("id").toString());
                String title = jsonObject.get("title").toString();
                String description = jsonObject.get("description").toString();
                String dateString = jsonObject.get("date").toString();

                Date date = DateConverter.stringToDate(dateString);
                final TicketEntry ticket = new TicketEntry(id, title, description, date);

                AppExecuters.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.ticketDao().insertTicket(ticket);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
