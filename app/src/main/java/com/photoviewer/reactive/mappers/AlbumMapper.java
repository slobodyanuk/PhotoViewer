package com.photoviewer.reactive.mappers;

import com.photoviewer.data.model.Album;
import com.photoviewer.data.model.ImageSize;

import rx.functions.Func1;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class AlbumMapper implements Func1<Album, Album> {

    private String imageUrl = "";

    @Override
    public Album call(Album album) {
        for (ImageSize size : album.getImageSizes()) {
            if (size.getType().equalsIgnoreCase("r")) {
                imageUrl = size.getUrl();
                break;
            }
        }
        Album albumWithImage = new Album(album);
        albumWithImage.setAlbumImageUrl(imageUrl);
        return albumWithImage;
    }

}
