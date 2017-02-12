package com.photoviewer.data.presenters.impl;

import android.os.Looper;

import com.photoviewer.data.model.Photo;
import com.photoviewer.data.presenters.PhotosPresenter;
import com.photoviewer.data.presenters.views.PhotosView;
import com.photoviewer.network.RestClient;
import com.photoviewer.reactive.BaseTask;
import com.photoviewer.reactive.listeners.OnSubscribeCompleteListener;
import com.photoviewer.reactive.listeners.OnSubscribeNextListener;
import com.photoviewer.reactive.mappers.PhotosResponseMapper;
import com.photoviewer.ui.BaseFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import rx.Subscription;

import static com.photoviewer.utils.PrefsKeys.ACCESS_TOKEN;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class PhotosPresenterImpl implements PhotosPresenter, OnSubscribeCompleteListener, OnSubscribeNextListener {

    private PhotosView mPhotosView;

    private Subscription mSubscription;

    public PhotosPresenterImpl(PhotosView mPhotosView) {
        this.mPhotosView = mPhotosView;
    }

    @Override
    public void executePhotos(String albumId) {
        mSubscription = new BaseTask<>()
                .execute(this, RestClient
                        .getApiService()
                        .getPhotos(albumId, Prefs.getString(ACCESS_TOKEN, ""))
                        .takeWhile(photosResponse -> {
                            if (photosResponse.getError() == null) {
                                return true;
                            } else {
                                Looper.prepare();
                                onError(photosResponse.getError().getErrorMessage());
                                Looper.loop();
                                return false;
                            }
                        })
                        .map(new PhotosResponseMapper())
                        .compose(((BaseFragment) mPhotosView).bindToLifecycle()));
    }

    @Override
    public void onCompleted() {
        if (!mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
    }

    @Override
    public void onError(String ex) {
        mPhotosView.onError(ex);
    }

    @Override
    public void onNext(Object t) {
        mPhotosView.onPhotosSuccess((List<Photo>) t);
    }

}
