package com.example.android.addrequest.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.addrequest.R;

public class UserProfileSettings {


    private static final String PREFERENCES_KEY_USERID = "preferences_key_userId";
    private static final String PREFERENCES_KEY_USERNAME = "preferences_key_userName";
    private static final String PREFERENCES_KEY_USER_PHOTO_URL = "preferences_key_userPhotoUrl";



    public static String getUserID(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String userid = prefs.getString( PREFERENCES_KEY_USERID , context.getString(R.string.default_userid) );
        return userid;

    }


    public static String getUsername(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString( PREFERENCES_KEY_USERNAME , context.getString(R.string.default_username) );
        return username;

    }


    public static String getUserPhotoURL(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String userPhotoURL = prefs.getString( PREFERENCES_KEY_USER_PHOTO_URL , context.getString(R.string.default_userphotourl) );
        return userPhotoURL;

    }


    public static void setUserProfileAtLogin(Context context, String userId, String userName, String userPhotoUrl){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( PREFERENCES_KEY_USERID , userId );
        editor.putString( PREFERENCES_KEY_USERNAME , userName );
        editor.putString( PREFERENCES_KEY_USER_PHOTO_URL , userPhotoUrl );
        editor.apply();

    }


    public static void setUserProfileAtLogout(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( PREFERENCES_KEY_USERID , context.getString(R.string.default_userid) );
        editor.putString( PREFERENCES_KEY_USERNAME , context.getString(R.string.default_username) );
        editor.putString( PREFERENCES_KEY_USER_PHOTO_URL , context.getString(R.string.default_userphotourl) );
        editor.apply();

    }

}
