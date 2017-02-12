package com.photoviewer.data.presenters.impl;

import com.photoviewer.data.model.Album;
import com.photoviewer.data.presenters.AlbumPresenter;
import com.photoviewer.data.presenters.views.AlbumsView;
import com.photoviewer.network.RestClient;
import com.photoviewer.reactive.BaseTask;
import com.photoviewer.reactive.listeners.OnSubscribeCompleteListener;
import com.photoviewer.reactive.listeners.OnSubscribeNextListener;
import com.photoviewer.reactive.mappers.AlbumsResponseMapper;
import com.photoviewer.ui.BaseFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import rx.Subscription;

import static com.photoviewer.utils.PrefsKeys.ACCESS_TOKEN;
import static com.photoviewer.utils.PrefsKeys.USER_ID;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class AlbumPresenterImpl implements AlbumPresenter, OnSubscribeCompleteListener, OnSubscribeNextListener {

    private AlbumsView mAlbumsView;

    private Subscription mSubscription;

    public AlbumPresenterImpl(AlbumsView albumsView) {
        mAlbumsView = albumsView;
    }

    @Override
    public void executeAlbums() {
        mSubscription = new BaseTask<>()
                .execute(this, RestClient
                        .getApiService()
                        .getAlbums(Prefs.getString(USER_ID, ""), Prefs.getString(ACCESS_TOKEN, ""), "r", "1")
                        .map(new AlbumsResponseMapper())
                        .compose(((BaseFragment) mAlbumsView).bindToLifecycle()));
    }

    @Override
    public void onCompleted() {
        if (!mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
    }

    @Override
    public void onError(String ex) {
        mAlbumsView.onError(ex);
    }

    @Override
    public void onNext(Object t) {
        mAlbumsView.onAlbumsSuccess((List<Album>) t);
    }

}
