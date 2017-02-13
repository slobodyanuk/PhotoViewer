package com.photoviewer.utils;

import com.vk.sdk.VKScope;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */
public class Globals {

    public static final int MESSAGE_ANIMATION_DURATION = 2500;
    public static final int BUTTON_ANIMATION_EXPAND = 1000;

    public static final String FRAGMENT_ALBUMS_TAG = "albums";
    public static final String FRAGMENT_PHOTO_TAG = "photo";
    public static final String FRAGMENT_PHOTO_PREVIEW_TAG = "photo_preview";

    public static final String[] USER_SCOPE = new String[]{
            VKScope.PHOTOS
    };
}
