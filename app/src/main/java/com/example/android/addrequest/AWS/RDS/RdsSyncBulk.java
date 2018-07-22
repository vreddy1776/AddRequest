package com.example.android.addrequest.AWS.RDS;

import android.content.Context;

import com.example.android.addrequest.Database.AppExecuters;
import com.example.android.addrequest.Utils.DateTime;
import com.example.android.addrequest.Database.AppDatabase;
import com.example.android.addrequest.Database.TicketEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class RdsSyncBulk {

    // Constant for logging
    private static final String TAG = RdsSyncBulk.class.getSimpleName();

    // Database
    private AppDatabase database;


    public void bulkPopulate(Context context, final JSONArray jsonArray){

        database = AppDatabase.getInstance(context);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                for ( int i = 0 ; i < jsonArray.length() ; i++) {

                    try {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        
                        int id = Integer.parseInt(jsonObject.get("id").toString());
                        String title = jsonObject.get("title").toString();
                        String description = jsonObject.get("description").toString();
                        String dateString = jsonObject.get("date").toString();

                        Date date = DateTime.stringToDate(dateString);
                        final TicketEntry ticket = new TicketEntry(id, title, description, date);

                        database.ticketDao().insertTicket(ticket);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

}
