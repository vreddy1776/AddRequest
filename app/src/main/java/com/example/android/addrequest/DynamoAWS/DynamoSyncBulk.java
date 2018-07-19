package com.example.android.addrequest.DynamoAWS;

import android.content.Context;
import android.util.Log;

import com.example.android.addrequest.AppExecuters;
import com.example.android.addrequest.database.AppDatabase;
import com.example.android.addrequest.database.DateConverter;
import com.example.android.addrequest.database.TicketEntry;
import com.example.android.addrequest.utils.GenerateID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class DynamoSyncBulk {

    // Constant for logging
    private static final String TAG = DynamoSyncBulk.class.getSimpleName();

    // Database
    private AppDatabase database;


    public void bulkPopulate(Context context, String string){

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(string);
        } catch (JSONException e) {
            Log.d(TAG, "error:  " + e);
        }

        final JSONArray finalJsonArray = jsonArray;

        Log.d(TAG, "finalJsonArray:  " + finalJsonArray);


        database = AppDatabase.getInstance(context);

        AppExecuters.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < finalJsonArray.length() ; i++) {

                    try {

                        JSONObject jsonObject = finalJsonArray.getJSONObject(i);
                        
                        int id = GenerateID.convertID(jsonObject.get("_id").toString());
                        String title = jsonObject.get("_title").toString();
                        String description = jsonObject.get("_description").toString();
                        String dateString = jsonObject.get("_date").toString();

                        Date date = DateConverter.stringToDate(dateString);
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
