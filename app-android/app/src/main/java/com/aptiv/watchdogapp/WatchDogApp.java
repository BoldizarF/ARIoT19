package com.aptiv.watchdogapp;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class WatchDogApp extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
