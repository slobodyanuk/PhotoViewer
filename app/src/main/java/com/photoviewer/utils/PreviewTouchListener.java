package com.photoviewer.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.thefinestartist.utils.ui.DisplayUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

public class PreviewTouchListener implements View.OnTouchListener {

    private static final int MAX_CLICK_DURATION = 150;
    private static final int MAX_CLICK_DISTANCE = 5;

    private final OnPreviewAnimCallback mCallback;
    private final int mContainerY;
    private final AnimatorSet mAnimationSet;

    private long pressStartTime;
    private float pressedY;
    private int startTime = 0;

    private boolean firstClick = true;

    private ObjectAnimator mMoveAnimation;
    private ObjectAnimator mRotationAnimation;

    public PreviewTouchListener(OnPreviewAnimCallback callback, int containerY, AnimatorSet animationSet) {
        mCallback = callback;
        mContainerY = containerY;
        mAnimationSet = animationSet;
        if (mAnimationSet != null && mAnimationSet.getChildAnimations().size() != 0) {
            mRotationAnimation = (ObjectAnimator) mAnimationSet
                    .getChildAnimations().get(LayoutEventListener.ROTATE_ANIMATION_INDEX);
            mMoveAnimation = (ObjectAnimator) mAnimationSet
                    .getChildAnimations().get(LayoutEventListener.MOVE_ANIMATION_INDEX);
        }
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressStartTime = System.currentTimeMillis();
                pressedY = event.getY();
                firstClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float eventY = event.getY();
                long pressDuration = System.currentTimeMillis() - pressStartTime;
                if (firstClick) {
                    if (eventY < pressedY) {
                        return true;
                    }
                }
                if (pressDuration > MAX_CLICK_DURATION &&
                        TouchViewUtils.distanceY(pressedY, event.getY()) > MAX_CLICK_DISTANCE) {
                    firstClick = false;
                    mCallback.releaseHandler();
                    for (Animator a : mAnimationSet.getChildAnimations()) {
                        ((ObjectAnimator) a).setCurrentPlayTime(startTime);
                    }
                }
                if (pressedY > mContainerY) {
                    mMoveAnimation.setDuration(700);
                    startTime = (int) ((1000 * (event.getY() - pressedY)) / DisplayUtil.getHeight());
                } else {
                    startTime = (int) (((1000) * event.getY()) / DisplayUtil.getHeight());
                }
                Log.e(TAG, "onTouch: " + startTime);

                break;
            case MotionEvent.ACTION_UP:
                if (startTime <= mMoveAnimation.getDuration() * 0.50f) {
                    mCallback.onPositionUpdate(startTime);
                    mCallback.onReverseAnimation();
                    firstClick = true;
                } else {
                    if (!firstClick) {
                        mCallback.releaseHandler();
                        mRotationAnimation.setCurrentPlayTime(0);
                        mCallback.onBackAnimation();
                    }
                }
                break;
        }
        return true;
    }

    public interface OnPreviewAnimCallback {

        void onReverseAnimation();

        void onBackAnimation();

        void releaseHandler();

        void onPositionUpdate(int startTime);
    }
}
