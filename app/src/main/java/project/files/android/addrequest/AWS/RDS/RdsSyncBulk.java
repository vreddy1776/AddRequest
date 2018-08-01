package project.files.android.addrequest.AWS.RDS;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project.files.android.addrequest.Database.AppDatabase;
import project.files.android.addrequest.Database.AppExecuters;

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

                        //final TicketEntry ticket = new TicketEntry(id, title, description, date);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        /*
                        TicketEntry ticket = new TicketEntry(
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
