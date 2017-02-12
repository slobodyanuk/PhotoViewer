package com.photoviewer.utils;

import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

@Accessors(prefix = "m")
@AllArgsConstructor
public class GlideRequestListener implements RequestListener {

    @Setter
    private ProgressBar mProgressBar;

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
        if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);
        return false;
    }
}
