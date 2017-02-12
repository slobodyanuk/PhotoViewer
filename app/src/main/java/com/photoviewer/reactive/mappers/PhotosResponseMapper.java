package com.photoviewer.reactive.mappers;

import com.photoviewer.data.model.Photo;
import com.photoviewer.network.responses.PhotosResponse;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class PhotosResponseMapper implements Func1<PhotosResponse, List<Photo>> {

    @Override
    public List<Photo> call(PhotosResponse response) {
        return Observable.from(response.getPhotos())
                .toList()
                .toBlocking()
                .first();
    }

}
