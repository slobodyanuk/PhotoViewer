package com.photoviewer.data.events;

import android.animation.AnimatorSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

@AllArgsConstructor
public class LayoutEvent {

    @Getter
    private AnimatorSet animatorSet;
}
