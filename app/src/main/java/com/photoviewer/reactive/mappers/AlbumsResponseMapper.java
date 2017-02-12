package com.photoviewer.reactive.mappers;

import com.photoviewer.data.model.Album;
import com.photoviewer.network.responses.AlbumsResponse;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class AlbumsResponseMapper implements Func1<AlbumsResponse, List<Album>> {

    @Override
    public List<Album> call(AlbumsResponse response) {
        return Observable.from(response.getAlbums())
                .map(new AlbumMapper())
                .toList()
                .toBlocking()
                .first();
    }

}
