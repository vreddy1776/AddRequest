package com.example.android.addrequest.sync;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class SyncVolley {

    // Constant for logging
    private static final String TAG = SyncVolley.class.getSimpleName();

    private static final String MAIN_URL =
            "http://ec2-18-191-155-187.us-east-2.compute.amazonaws.com/requests/";

    public static void getJSON(Context context){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MAIN_URL, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Volley - rawJSON:  "  + response.toString());
                        SyncBulk.bulkPopulate(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Volley - error:  "  + error.toString());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

}
