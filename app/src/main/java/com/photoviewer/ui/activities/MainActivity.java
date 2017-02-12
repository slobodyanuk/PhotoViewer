package com.photoviewer.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.photoviewer.R;
import com.photoviewer.ui.BaseActivity;
import com.photoviewer.ui.components.buttons.ProgressAnimationState;
import com.photoviewer.ui.components.buttons.ProgressButton;
import com.photoviewer.ui.fragments.AlbumsFragment;
import com.photoviewer.utils.Globals;
import com.photoviewer.utils.PrefsKeys;
import com.pixplicity.easyprefs.library.Prefs;
import com.vk.sdk.VKSdk;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        ProgressButton.GifAnimationListener{

    @BindView(R.id.logout)
    ProgressButton mLogoutButton;

    public static void launch(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null && Prefs.getString(PrefsKeys.ACCESS_TOKEN, "").isEmpty()) {
            SignInActivity.launch(this);
        }else {
            mLogoutButton.setGifAnimationListener(this);
            replaceFragment(R.id.fragment_container, AlbumsFragment.newInstance(), Globals.FRAGMENT_ALBUMS_TAG);
        }
    }

    @OnClick(R.id.logout)
    public void onLogout(){
        mLogoutButton.collapse();
        VKSdk.logout();
        Prefs.remove(PrefsKeys.ACCESS_TOKEN);
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public void onGifAnimationCompleted(int loopNumber, ProgressAnimationState animState) {
        if (animState == ProgressAnimationState.PROGRESS) {
            SignInActivity.launch(this);
        }
    }
}
