package com.photoviewer.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.photoviewer.R;
import com.photoviewer.ui.components.buttons.ProgressButton;
import com.photoviewer.utils.PrefsKeys;
import com.pixplicity.easyprefs.library.Prefs;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @BindView(R.id.logout)
    ProgressButton mLogoutButton;

    private Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResources());
        mBinder = ButterKnife.bind(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitleTextColor(Color.WHITE);
        }
        unableLogoutButton();
        disableHomeButton();
    }

    public void setToolbarTitle(String title){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    public void replaceFragment(int container, Fragment fragment, String tag){
        disableHomeButton();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    public void unableHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void disableHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void disableLogoutButton(){
        if (mLogoutButton != null){
            mLogoutButton.setVisibility(View.GONE);
        }
    }

    public void unableLogoutButton(){
        if (mLogoutButton != null){
            mLogoutButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int i = getSupportFragmentManager().getBackStackEntryCount();
        if (i >= 1) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager()
                    .getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(str);
            if (currentFragment != null) {
                currentFragment.updateToolbar();
            }
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Prefs.remove(PrefsKeys.RECYCLER_ALBUM_POSITION);
        Prefs.remove(PrefsKeys.RECYCLER_PHOTO_POSITION);
        mBinder.unbind();
    }

    protected abstract int getLayoutResources();

}
