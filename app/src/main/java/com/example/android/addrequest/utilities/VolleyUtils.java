package com.example.android.addrequest.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.addrequest.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyUtils {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    private static String queryResponse;

    public static String getJSON(Context context){

        JSONObject uploadData = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://cblunt.github.io/blog-android-volley/response.json", uploadData,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        queryResponse = response.toString();
                        Log.d(TAG, "queryResponse:  "  + queryResponse);

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        queryResponse = error.toString();
                        Log.d(TAG, "error:  "  + queryResponse);


                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        Log.d(TAG, "uploadData:  "  + uploadData);

        return queryResponse;

    }

}
