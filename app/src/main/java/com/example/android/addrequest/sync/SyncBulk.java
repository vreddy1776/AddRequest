package com.example.android.addrequest.sync;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncBulk {

    // Constant for logging
    private static final String TAG = SyncBulk.class.getSimpleName();

    public static void bulkPopulate(JSONObject jsonObject){

        try {
            Log.d(TAG, "JSONobject for message:  "  + jsonObject.get("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
