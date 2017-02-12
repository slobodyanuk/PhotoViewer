package com.photoviewer.utils;

import android.os.Build;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

public class Utils {

    public static boolean isLollipop(){
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
