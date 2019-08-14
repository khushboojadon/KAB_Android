package com.appflowsolutions.kab.Activities;

import android.app.Application;
import android.os.SystemClock;

public class KABApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);

    }
}
