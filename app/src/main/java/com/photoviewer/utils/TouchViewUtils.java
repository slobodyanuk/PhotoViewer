package com.photoviewer.utils;

import android.util.DisplayMetrics;

import static com.thefinestartist.utils.service.ServiceUtil.getWindowManager;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */
public class TouchViewUtils {

    public static double distanceY(float coordinate1, float coordinate2) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double yDist = Math.pow(Math.abs(coordinate1 - coordinate2) / dm.ydpi, 2);
        return Math.sqrt(yDist) * dm.ydpi;
    }

}
