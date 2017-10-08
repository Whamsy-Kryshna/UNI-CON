package com.help.sd.uni_con;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by SaideepReddy on 8/15/2015.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        // ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "uvEc8lrTmE1QgsapagJzXR04Lws3iw91T5yslUXp", "RLFQVqMYhMoxBq01qQEqoNOOrzAY73FYV1fWT6Hs");


        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);
    }
}
