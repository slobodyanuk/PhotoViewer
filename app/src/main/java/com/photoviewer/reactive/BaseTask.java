package com.photoviewer.reactive;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sergiy on 24.01.2017.
 */

public class BaseTask<T> {

    private Scheduler mSubscribeScheduler = Schedulers.io();
    private Scheduler mObserveScheduler = AndroidSchedulers.mainThread();

    public Subscription execute(T activity, Observable<?> observable) {
        return observable
                .subscribeOn(mSubscribeScheduler)
                .doOnError(Throwable::printStackTrace)
                .observeOn(mObserveScheduler)
                .subscribe(new BaseSubscriber<>(activity));
    }

    public void setSubscribeScheduler(Scheduler scheduler) {
        this.mSubscribeScheduler = scheduler;
    }

    public void setObserveScheduler(Scheduler scheduler) {
        this.mObserveScheduler = scheduler;
    }
}
