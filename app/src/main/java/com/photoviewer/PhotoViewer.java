package com.photoviewer;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;
import com.thefinestartist.Base;
import com.vk.sdk.VKSdk;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */

public class PhotoViewer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VKSdk.initialize(this);
        Base.initialize(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
