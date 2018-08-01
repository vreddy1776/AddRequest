package project.files.android.addrequest.AWS.DynamoDB;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.AppExecuters;
import project.files.android.addrequest.Utils.DateTime;
import project.files.android.addrequest.Utils.ID;

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
                        
                        int id = ID.convertID(jsonObject.get("_id").toString());
                        String title = jsonObject.get("_title").toString();
                        String description = jsonObject.get("_description").toString();
                        String dateString = jsonObject.get("_date").toString();

                        Date date = DateTime.stringToDate(dateString);
                        //final TicketEntry ticket = new TicketEntry(id, title, description, date);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        /*
                        ticketEntry ticket = new TicketEntry(
                                user.getUid(),
                                user.getDisplayName(),
                                user.getPhotoUrl().toString(),
                                Integer.toString(id),
                                title,
                                description,
                                date.toString(),
                                "none");
                                */

                        //database.ticketDao().insertTicket(ticket);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

}
