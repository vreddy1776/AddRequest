package com.example.android.addrequest.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncBulk {

    // Constant for logging
    private static final String TAG = SyncBulk.class.getSimpleName();

    public static void bulkPopulate(JSONArray jsonArray){

        for ( int i = 0 ; i < jsonArray.length() ; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d(TAG, "JSONobject:  " + jsonObject );
                Log.d(TAG, "id:  " + jsonObject.get("id") );
                Log.d(TAG, "title:  " + jsonObject.get("title") );
                Log.d(TAG, "description:  " + jsonObject.get("description") );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
