package com.chatapp.chitchat;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Sourabh on 27-Mar-16.
 */
public class ChitChatApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("chitchat123abc")
                .clientKey(null)
                .server("http://parse-server-chitchat.azurewebsites.net/parse/")   // '/' important after 'parse'
                .build());
        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.i("parse: ", "Object saved successfully!");
                }
                else {
                    Log.i("parse","Exception caught!: " + e);
                    //e.printStackTrace();
                }
            }
        });
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        */
    }
}
