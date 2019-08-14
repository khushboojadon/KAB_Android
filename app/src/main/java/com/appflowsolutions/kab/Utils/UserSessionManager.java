package com.appflowsolutions.kab.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import com.appflowsolutions.kab.Activities.LoginActivity;

import java.util.HashMap;


public class UserSessionManager
{
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREFER_NAME = "appLoginManagmentPref";
    // All Shared Preferences Keys
    public static final String KEY_USERID= "KEY_USERID";
    public static final String KEY_TOKEN= "KEY_TOKEN";
    public static final String KEY_USERLOGINID= "KEY_USERLOGINID";
    public static final String KEY_USERTYPE= "KEY_USERTYPE";
    // Constructor
    public UserSessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    //Create login session
    public void createUserLoginSession(String token, String userLoginID, String userId, String userType)
    {
        // Storing name in pref
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERLOGINID,userLoginID);
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_USERTYPE, userType);
        Log.d("Create login session", editor.putString(KEY_TOKEN, token).toString());
        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails()
    {
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, "-1"));
        user.put(KEY_USERLOGINID, pref.getString(KEY_USERLOGINID, null));
        user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));
        Log.d("getUserDetails", user.toString());
        // return user
        return user;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        Log.d("Remove login session", editor.toString());

        Global.USER=null;
        Global.USERLOGINID="";
        Global.USERTOKEN="";
        Global.USERTYPE="";
        Global.USERID=0;
        editor.clear();
        editor.commit();
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
}
