package com.photoviewer.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.photoviewer.R;
import com.photoviewer.data.events.LayoutEvent;
import com.photoviewer.data.model.Photo;
import com.photoviewer.ui.BaseActivity;
import com.photoviewer.ui.BaseFragment;
import com.photoviewer.utils.LayoutEventListener;
import com.photoviewer.utils.PreviewTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import butterknife.BindView;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

public class PreviewImageFragment extends BaseFragment implements PreviewTouchListener.OnPreviewAnimCallback {

    private static final String PARAM_PHOTO = "photo";
    private static final int ANIMATION_REVERSE_STEP = 4;

    @BindView(R.id.root)
    LinearLayout mRoot;

    @BindView(R.id.image_container)
    RelativeLayout mImageContainer;

    @BindView(R.id.image)
    ImageView mImageView;

    private Photo mPhoto;

    private int startTime = 0;

    private Handler mHandler = new Handler();

    private AnimatorSet mAnimatorSet;
    private LayoutEventListener mLayoutEventListener;


    public static Fragment newInstance(Photo photo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_PHOTO, Parcels.wrap(photo));
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateToolbar();
        if (getArguments() != null) {
            mPhoto = Parcels.unwrap(getArguments().getParcelable(PARAM_PHOTO));
        }

        showImage(mPhoto.getImageUrlBig());
        initAnimation();
    }

    private void initAnimation() {
        mLayoutEventListener = new LayoutEventListener(mImageContainer, mRoot, this);
        mImageContainer
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutEventListener);

    }

    private void showImage(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .error(android.R.drawable.stat_notify_error)
                .fitCenter()
                .dontTransform()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageView.setImageBitmap(resource);
                    }
                });
    }

    @Override
    public void updateToolbar() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).unableHomeButton();
            ((BaseActivity) getActivity()).disableLogoutButton();
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.fragment_preview));
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_preview;
    }

    @Subscribe
    public void onEvent(LayoutEvent event){
        mAnimatorSet = event.getAnimatorSet();
    }

    @Override
    public void onReverseAnimation() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (startTime <= 0) {
                    mHandler.removeCallbacksAndMessages(null);
                } else {
                    if (mAnimatorSet != null) {
                        for (Animator a : mAnimatorSet.getChildAnimations()) {
                            ((ObjectAnimator) a).setCurrentPlayTime(startTime -= ANIMATION_REVERSE_STEP);
                        }
                    }
                    mHandler.postDelayed(this, 15);
                }
            }
        }, 0);
    }

    @Override
    public void onBackAnimation() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void releaseHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onPositionUpdate(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseHandler();
        if (mLayoutEventListener != null) mLayoutEventListener.release();
    }

}
