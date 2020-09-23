package com.ass.sd2550project;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public void playBackgroundMusic(Context context) {
        startService(new Intent(context, SoundPlayerFragment.class));
    }

    public void stopBackgroundMusic(Context context) {
        stopService(new Intent(context, SoundPlayerFragment.class));
    }

}
