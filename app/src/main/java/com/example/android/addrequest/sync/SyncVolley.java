package com.example.android.addrequest.sync;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.addrequest.database.DateConverter;
import com.example.android.addrequest.database.TicketEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class SyncVolley {

    // Constant for logging
    private static final String TAG = SyncVolley.class.getSimpleName();

    // URL
    private static final String MAIN_URL =
            "http://ec2-18-219-46-19.us-east-2.compute.amazonaws.com/";

    private static final String SELECT = "select.php";

    private static final String ADD = "add.php";

    private static final String UPDATE = "update.php";

    private static final String DELETE = "delete.php";



    // Add and Update Parameters
    // URL Params
    private static final String START = "?";
    private static final String SET = "=";
    private static final String AND = "&";
    // Field Params
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";


    public void select(final Context context){

        String URL = MAIN_URL + SELECT;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, new JSONArray(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Volley - rawJSON:  "  + response.toString());
                        SyncBulk syncBulk = new SyncBulk();
                        syncBulk.bulkPopulate(context,response);
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


    public void add(final Context context, TicketEntry ticket){

        String id = String.valueOf(ticket.getId());
        String title = ticket.getTitle();
        String description = ticket.getDescription();
        String date = DateConverter.dateToString(ticket.getUpdatedAt());

        String URL = getAddURL(id, title, description, date);
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Volley - response:  "  + response);
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


    public void update(final Context context, TicketEntry ticket){

        String id = String.valueOf(ticket.getId());
        String title = ticket.getTitle();
        String description = ticket.getDescription();
        String date = DateConverter.dateToString(ticket.getUpdatedAt());

        String URL = getUpdateURL(id, title, description, date);
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Volley - response:  "  + response);
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


    public void delete(final Context context, int id){

        String stringId = String.valueOf(id);

        String URL = getDeleteURL(stringId);
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Volley - response:  "  + response);
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


    private String getAddURL(String id, String title, String description, String date){

        String addURL = MAIN_URL + ADD + START +
                ID + SET + id + AND +
                TITLE + SET + title + AND +
                DESCRIPTION + SET + description + AND +
                DATE + SET + date;

        Log.d(TAG, "Add URL is:  "  + addURL);

        return addURL;

    }


    private String getUpdateURL(String id, String title, String description, String date){

        String updateURL = MAIN_URL + UPDATE + START +
                ID + SET + id + AND +
                TITLE + SET + title + AND +
                DESCRIPTION + SET + description + AND +
                DATE + SET + date;

        Log.d(TAG, "Update URL is:  "  + updateURL);

        return updateURL;

    }


    private String getDeleteURL(String id){

        String deleteURL = MAIN_URL + DELETE + START +
                ID + SET + id;

        Log.d(TAG, "Delete URL is:  "  + deleteURL);

        return deleteURL;

    }


}
