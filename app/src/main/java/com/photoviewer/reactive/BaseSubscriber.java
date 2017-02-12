package com.photoviewer.reactive;

import com.google.gson.Gson;
import com.photoviewer.network.errors.ErrorResponse;
import com.photoviewer.reactive.listeners.OnSubscribeCompleteListener;
import com.photoviewer.reactive.listeners.OnSubscribeNextListener;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Sergiy on 24.01.2017.
 */

public class BaseSubscriber<T, V> extends Subscriber<V> {

    private OnSubscribeCompleteListener completeListener;
    private OnSubscribeNextListener nextListener;

    public BaseSubscriber(T listener) {
        this.completeListener = (OnSubscribeCompleteListener) listener;
        this.nextListener = (OnSubscribeNextListener) listener;
    }

    @Override
    public void onCompleted() {
        completeListener.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof HttpException) {
                HttpException exception = (HttpException) e;
                Response response = exception.response();
                Gson gson = new Gson();
                try {
                    completeListener.onError(gson
                            .fromJson(response.errorBody().string(), ErrorResponse.class)
                            .getMessage());
                } catch (IOException e1) {
                    completeListener.onError("Failed");
                }
            } else {
                completeListener.onError((e.getMessage().isEmpty()) ? "Failed" : e.getMessage());
            }
        }catch (Throwable throwable){
            e.printStackTrace();
            completeListener.onError((e.getMessage().isEmpty()) ? "Failed" : e.getMessage());
            throwable.printStackTrace();
        }
    }

    @Override
    public void onNext(V t) {
        nextListener.onNext(t);
        onCompleted();
    }
}