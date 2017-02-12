package com.photoviewer.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewTreeObserver;

import com.photoviewer.data.events.LayoutEvent;
import com.thefinestartist.utils.ui.DisplayUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

public class LayoutEventListener implements ViewTreeObserver.OnGlobalLayoutListener {

    public static final int ROTATE_ANIMATION_INDEX = 0;
    public static final int MOVE_ANIMATION_INDEX = 1;
    public static final int ALPHA_ANIMATION_INDEX = 2;

    private AnimatorSet animationSet;

    private View mLayout;
    private View mRoot;
    private PreviewTouchListener.OnPreviewAnimCallback mCallback;
    private int containerY = -1;

    public LayoutEventListener(View layout, View root, PreviewTouchListener.OnPreviewAnimCallback callback) {
        mLayout = layout;
        mRoot = root;
        mCallback = callback;
        animationSet = new AnimatorSet();
    }

    @Override
    public void onGlobalLayout() {
        if (containerY == -1) {
            containerY = mLayout.getTop();

            ObjectAnimator moveAnimation = ObjectAnimator
                    .ofFloat(mLayout, "y", containerY, DisplayUtil.getHeight() * 0.55f)
                    .setDuration(1000);

            ObjectAnimator rotationAnimation = ObjectAnimator
                    .ofFloat(mLayout, "rotationX", 0, -25)
                    .setDuration(1000);

            ObjectAnimator alphaAnimation = ObjectAnimator
                    .ofFloat(mLayout, "alpha", 1f, 0.5f)
                    .setDuration(1000);

            animationSet.playTogether(rotationAnimation, moveAnimation, alphaAnimation);
            animationSet.setDuration(1000);
            EventBus.getDefault().post(new LayoutEvent(animationSet));
            mRoot.setOnTouchListener(new PreviewTouchListener(mCallback, containerY, animationSet));
        }
        mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public void release() {
        mLayout = null;
        mRoot = null;
        mCallback = null;
        animationSet = null;
        containerY = -1;
    }
}
