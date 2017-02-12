package com.photoviewer.ui.components.buttons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.photoviewer.R;
import com.photoviewer.utils.Globals;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */

@Accessors(prefix = "m")
public class ProgressButton extends RelativeLayout {

    private GradientDrawable mGradientDrawable;

    @Setter
    private int mProgressColor;

    @Getter
    @Setter
    private TextView mTextButton;

    @Getter
    @Setter
    private ProgressBar mProgressBar;

    @Getter
    @Setter
    private String mText;

    @Setter
    @Getter
    private int mButtonColor;

    private int mParentWidth;
    private int mParentHeight;

    @Getter
    private boolean mProgressRunning;

    @Setter
    private int mTextColor;

    @Setter
    private int mTextSize;

    private GifImageView mGifImageView;

    @Setter
    @Getter
    private String mProgressAnimPath = "animation/progressbar.gif";

    @Getter
    @Setter
    private String mErrorAnimPath = "animation/complete_error_small.gif";

    @Getter
    @Setter
    private String mSuccessAnimPath = "animation/complete_success_small.gif";

    @Getter
    @Setter
    private long mAnimDuration = 300;

    @Setter
    @Getter
    private int mStrokeWidth = 5;

    @Getter
    private ProgressButtonState mButtonState = ProgressButtonState.EXPANDED;

    @Getter
    private ProgressAnimationState mAnimationState = ProgressAnimationState.PROGRESS;

    private AnimatorSet mCollapseAnimationSet;
    private AnimatorSet mExpandAnimationSet;

    private GifDrawable mProgressGifAssets;
    private GifDrawable mCurrentGifAssets;
    private GifDrawable mSuccessGifAssets;
    private GifDrawable mErrorGifAssets;

    private Handler mAnimDelayHandler = new Handler();
    private Handler mTextDelayHandler = new Handler();

    private AnimationListener mGifAnimListener;

    private GifAnimationListener mAnimListener;

    public ProgressButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        final Resources res = getResources();

        mProgressColor = res.getColor(R.color.colorPrimaryDark);
        mButtonColor = res.getColor(R.color.colorPrimaryDark);
        mTextColor = res.getColor(android.R.color.white);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ProgressButton, defStyleAttr, 0);

            mProgressColor = a.getColor(R.styleable.ProgressButton_progressColor, mProgressColor);
            mProgressColor = a.getColor(R.styleable.ProgressButton_buttonColor, mButtonColor);
            mText = a.getString(R.styleable.ProgressButton_text);
            mTextColor = a.getColor(R.styleable.ProgressButton_textColor, mTextColor);
            mTextSize = a.getInt(R.styleable.ProgressButton_textSize, mTextSize);

            initButtonBorders();
            initButtonStyle(context);

            initProgressStyle(context);

            a.recycle();
        }
    }

    private void initButtonBorders() {
        mGradientDrawable = new GradientDrawable();
        mGradientDrawable.setStroke(mStrokeWidth, mButtonColor);
        mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
    }

    private void initButtonStyle(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        mTextButton = new TextView(context);
        mTextButton.setText(mText);
        mTextButton.setGravity(Gravity.CENTER);
        mTextButton.setLines(1);
        mTextButton.setTextColor(mTextColor);
        mTextButton.setLayoutParams(params);
        addView(mTextButton);
    }

    private void initProgressStyle(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_IN_PARENT);

        mGifImageView = new GifImageView(context);
        mGifImageView.setVisibility(GONE);
        mGifImageView.setPadding(mStrokeWidth, mStrokeWidth, mStrokeWidth, mStrokeWidth);
        mGifImageView.setLayoutParams(params);


        initGifAssets();
        addView(mGifImageView);
    }

    private void initGifAssets() {
        try {
            mProgressGifAssets = new GifDrawable(getContext().getAssets(), mProgressAnimPath);
            mProgressGifAssets.getPaint().setAntiAlias(true);

            mErrorGifAssets = new GifDrawable(getContext().getAssets(), mErrorAnimPath);
            mErrorGifAssets.getPaint().setAntiAlias(true);
            mErrorGifAssets.setSpeed(1.5f);

            mSuccessGifAssets = new GifDrawable(getContext().getAssets(), mSuccessAnimPath);
            mSuccessGifAssets.getPaint().setAntiAlias(true);
            mSuccessGifAssets.setSpeed(1.5f);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mParentWidth = w;
        mParentHeight = h;
        mTextSize = mParentHeight / 4;

        mGradientDrawable.setCornerRadius(mParentHeight / 2);

        mTextButton.setTextSize(mTextSize);
        setBackground(mGradientDrawable);
        if (mCollapseAnimationSet == null && mExpandAnimationSet == null) {
            initCollapseAnimation();
            initExpandAnimation();
        }
    }

    private void initCollapseAnimation() {
        ValueAnimator collapseAnimation = ValueAnimator.ofInt(mParentWidth, mParentHeight);

        ObjectAnimator textCollapseAnimation = ObjectAnimator.ofFloat(mTextButton, "alpha", 1f, 0f);
        textCollapseAnimation.setDuration((long) (mAnimDuration / 1.5));

        collapseAnimation.addUpdateListener(valueAnimator -> {
            int val = (int) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = val;
            mProgressRunning = true;
            setLayoutParams(layoutParams);
        });

        collapseAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgress();
            }
        });

        mCollapseAnimationSet = new AnimatorSet();
        mCollapseAnimationSet.playTogether(collapseAnimation, textCollapseAnimation);
        mCollapseAnimationSet.setDuration(mAnimDuration);
    }

    private void initExpandAnimation() {
        ValueAnimator expandAnimation = ValueAnimator.ofInt(mParentHeight, mParentWidth);
        ObjectAnimator textExpandAnimation = ObjectAnimator.ofFloat(mTextButton, "alpha", 0f, 1f);
        textExpandAnimation.setDuration((long) (mAnimDuration / 1.5));

        expandAnimation.addUpdateListener(valueAnimator -> {
            int val = (int) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = val;
            mProgressRunning = false;
            setLayoutParams(layoutParams);
        });

        mExpandAnimationSet = new AnimatorSet();
        mExpandAnimationSet.playTogether(expandAnimation, textExpandAnimation);
        mExpandAnimationSet.setDuration(mAnimDuration);
    }

    public void showProgress() {
        if (mGifImageView != null) {
            mGifImageView.setVisibility(VISIBLE);
        }
    }

    public void hideProgress() {
        if (mGifImageView != null) {
            mGifImageView.setVisibility(GONE);
        }
    }

    public void collapse() {
        setEnabled(true);
        if (mCollapseAnimationSet != null && !mCollapseAnimationSet.isRunning()) {
            if (mExpandAnimationSet != null) {
                mExpandAnimationSet.cancel();
            }
            mButtonState = ProgressButtonState.COLLAPSED;
            onProgress();
            mCollapseAnimationSet.start();
        }
    }

    public void expand() {
        setEnabled(true);
        if (mExpandAnimationSet != null && !mExpandAnimationSet.isRunning()) {
            if (mCollapseAnimationSet != null) {
                mCollapseAnimationSet.cancel();
            }
            hideProgress();
            mButtonState = ProgressButtonState.EXPANDED;
            mExpandAnimationSet.start();
        }
    }

    public void expandWithDelay(int buttonAnimDelay) {
        if (mAnimDelayHandler == null) {
            expand();
        } else {
            mGifAnimListener = loopNumber -> {
                mAnimDelayHandler.removeCallbacksAndMessages(null);
                mAnimDelayHandler.postDelayed(() -> {
                    setEnabled(true);
                    if (mExpandAnimationSet != null && !mExpandAnimationSet.isRunning()) {
                        if (mCollapseAnimationSet != null) {
                            mCollapseAnimationSet.cancel();
                        }
                        hideProgress();
                        mButtonState = ProgressButtonState.EXPANDED;
                        mExpandAnimationSet.start();
                    }
                    mCurrentGifAssets.removeAnimationListener(mGifAnimListener);
                }, buttonAnimDelay);
            };
            mCurrentGifAssets.addAnimationListener(mGifAnimListener);
        }
    }

    public void onError() {
        if (mButtonState == ProgressButtonState.EXPANDED) {
            return;
        }
        if (mGifImageView != null && mErrorGifAssets != null) {
            recycleGifAssets();
            setEnabled(true);
            initGifAssets();
            mGifImageView.setVisibility(VISIBLE);
            mErrorGifAssets.setCornerRadius(mParentHeight / 2);
            mGifImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mGifImageView.setImageDrawable(mErrorGifAssets);
            mCurrentGifAssets = mErrorGifAssets;
            mAnimationState = ProgressAnimationState.ERROR;
            initAnimationListener();
            mErrorGifAssets.start();
        }
    }

    public void onErrorWithDelay(int animDuration) {
        onError();
        expandWithDelay(animDuration);
    }

    public void resetText(int animDuration) {
        mTextDelayHandler.postDelayed(() -> mTextButton.setText(mText), Globals.MESSAGE_ANIMATION_DURATION + animDuration);
    }

    public void onSuccess() {
        if (mButtonState == ProgressButtonState.EXPANDED) {
            return;
        }
        if (mGifImageView != null && mSuccessGifAssets != null) {
            setEnabled(true);
            recycleGifAssets();
            initGifAssets();
            mGifImageView.setVisibility(VISIBLE);
            mSuccessGifAssets.setCornerRadius(mParentHeight / 2);
            mGifImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mGifImageView.setImageDrawable(mSuccessGifAssets);
            mAnimationState = ProgressAnimationState.SUCCESS;
            mCurrentGifAssets = mSuccessGifAssets;
            initAnimationListener();
            mSuccessGifAssets.start();
        }
    }

    public void onProgress() {
        if (mGifImageView != null && mProgressGifAssets != null) {
            setEnabled(false);
            recycleGifAssets();
            initGifAssets();
            mGifImageView.setVisibility(VISIBLE);
            mGifImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mGifImageView.setImageDrawable(mProgressGifAssets);
            mAnimationState = ProgressAnimationState.PROGRESS;
            mCurrentGifAssets = mProgressGifAssets;
            initAnimationListener();
            mProgressGifAssets.start();
        }
    }

    private void recycleGifAssets() {
        if (mProgressGifAssets != null) {
            mSuccessGifAssets.recycle();
            mProgressGifAssets.recycle();
            mErrorGifAssets.recycle();
        }
    }

    private void initAnimationListener() {
        if (mAnimListener != null) {
            mGifAnimListener = loopNumber -> {
                mAnimListener.onGifAnimationCompleted(loopNumber, mAnimationState);
                mCurrentGifAssets.removeAnimationListener(mGifAnimListener);
            };
            mCurrentGifAssets.addAnimationListener(mGifAnimListener);
        }
    }

    private void setProgressAnimPath(String path) {
        this.mProgressAnimPath = path;
        if (mGifImageView != null) {
            removeView(mGifImageView);
        }
        initProgressStyle(getContext());
    }

    public void setGifAnimationListener(GifAnimationListener listener) {
        this.mAnimListener = listener;
    }

    public interface GifAnimationListener {
        void onGifAnimationCompleted(int loopNumber, ProgressAnimationState animState);
    }

}
