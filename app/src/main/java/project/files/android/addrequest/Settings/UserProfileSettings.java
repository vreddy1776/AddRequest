package project.files.android.addrequest.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import project.files.android.addrequest.Background.MyApplication;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.NameUtils;


/**
 * User Profile Settings
 *
 * Stores user profile settings as SharedPreferences.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class UserProfileSettings {


    private static final String PREFERENCES_KEY_USERID = "preferences_key_userId";
    private static final String PREFERENCES_KEY_USERNAME = "preferences_key_userName";
    private static final String PREFERENCES_KEY_FIRSTNAME = "preferences_key_firstName";
    private static final String PREFERENCES_KEY_LASTNAME = "preferences_key_lastName";
    private static final String PREFERENCES_KEY_USER_PHOTO_URL = "preferences_key_userPhotoUrl";



    public static String getUserID(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String userid = prefs.getString( PREFERENCES_KEY_USERID , MyApplication.getAppContext().getString(R.string.default_userid) );
        return userid;

    }


    public static String getUsername(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String username = prefs.getString( PREFERENCES_KEY_USERNAME , MyApplication.getAppContext().getString(R.string.default_username) );
        return username;

    }

    public static String getFirstname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String firstname = prefs.getString( PREFERENCES_KEY_FIRSTNAME , MyApplication.getAppContext().getString(R.string.default_username) );
        return firstname;
    }


    public static String getLastname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String lastname = prefs.getString( PREFERENCES_KEY_LASTNAME , MyApplication.getAppContext().getString(R.string.default_username) );
        return lastname;
    }


    public static String getUserPhotoURL(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String userPhotoURL = prefs.getString( PREFERENCES_KEY_USER_PHOTO_URL , MyApplication.getAppContext().getString(R.string.default_userphotourl) );
        return userPhotoURL;

    }


    public static void setUserProfileAtLogin(String userId, String userName, String userPhotoUrl){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( PREFERENCES_KEY_USERID , userId );
        editor.putString( PREFERENCES_KEY_USERNAME , userName );
        editor.putString( PREFERENCES_KEY_USER_PHOTO_URL , userPhotoUrl );
        editor.putString( PREFERENCES_KEY_FIRSTNAME , NameUtils.getFirstName(userName) );
        editor.putString( PREFERENCES_KEY_LASTNAME , NameUtils.getLastName(userName) );

        editor.apply();

    }


    public static void setUserProfileAtLogout(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( PREFERENCES_KEY_USERID , MyApplication.getAppContext().getString(R.string.default_userid) );
        editor.putString( PREFERENCES_KEY_USERNAME , MyApplication.getAppContext().getString(R.string.default_username) );
        editor.putString( PREFERENCES_KEY_USER_PHOTO_URL , MyApplication.getAppContext().getString(R.string.default_userphotourl) );
        editor.putString( PREFERENCES_KEY_FIRSTNAME , MyApplication.getAppContext().getString(R.string.default_username) );
        editor.putString( PREFERENCES_KEY_LASTNAME , MyApplication.getAppContext().getString(R.string.default_username) );

        editor.apply();

    }

}
