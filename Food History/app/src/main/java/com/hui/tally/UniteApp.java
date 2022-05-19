package com.hui.tally;

import android.app.Application;

import com.hui.tally.db.DBManager;

/** active all classes */
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // initialization
        DBManager.initDB(getApplicationContext());
    }
}
