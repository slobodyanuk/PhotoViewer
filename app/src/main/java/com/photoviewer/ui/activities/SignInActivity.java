package com.photoviewer.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.photoviewer.R;
import com.photoviewer.ui.BaseActivity;
import com.photoviewer.ui.components.buttons.ProgressAnimationState;
import com.photoviewer.ui.components.buttons.ProgressButton;
import com.photoviewer.utils.Globals;
import com.photoviewer.utils.PrefsKeys;
import com.pixplicity.easyprefs.library.Prefs;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.BindView;
import butterknife.OnClick;

import static com.photoviewer.utils.Globals.BUTTON_ANIMATION_EXPAND;

public class SignInActivity extends BaseActivity implements
        ProgressButton.GifAnimationListener {

    @BindView(R.id.sign_in)
    ProgressButton mSignButton;

    public static void launch(Context context) {
        Intent i = new Intent(context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSignButton.setGifAnimationListener(this);
    }

    @OnClick(R.id.sign_in)
    public void onSignIn(){
        mSignButton.collapse();
        VKSdk.login(this, Globals.USER_SCOPE);
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                mSignButton.onSuccess();
                Prefs.putString(PrefsKeys.ACCESS_TOKEN, res.accessToken);
                Prefs.putString(PrefsKeys.USER_ID, res.userId);
            }

            @Override
            public void onError(VKError error) {
                mSignButton.onErrorWithDelay(BUTTON_ANIMATION_EXPAND);
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onGifAnimationCompleted(int loopNumber, ProgressAnimationState animState) {
        if (animState == ProgressAnimationState.SUCCESS
                && !Prefs.getString(PrefsKeys.ACCESS_TOKEN, "").isEmpty()) {
            MainActivity.launch(this);
        }
    }
}
