package com.photoviewer.reactive.listeners;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public interface OnSubscribeCompleteListener {

    void onCompleted();
    void onError(String ex);

}
