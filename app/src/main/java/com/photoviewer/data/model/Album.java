package com.photoviewer.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class Album extends ImageModel{

    @Getter
    @Setter
    @SerializedName("thumb_src")
    private String albumImageUrl;

    @Getter
    @SerializedName("thumb_id")
    private String thumbId;

    @Getter
    @SerializedName("title")
    private String title;

    @Getter
    @SerializedName("description")
    private String description;

    @Getter
    @SerializedName("updated")
    private String updated;

    @Getter
    @SerializedName("size")
    private String size;

    @Getter
    @SerializedName("sizes")
    private List<ImageSize> imageSizes;

    public Album(Album album) {
        albumId = album.getAlbumId();
        albumImageUrl = album.getAlbumImageUrl();
        thumbId = album.getThumbId();
        ownerId = album.getOwnerId();
        title = album.getTitle();
        description = album.getDescription();
        created = album.getCreated();
        updated = album.getUpdated();
        imageSizes = album.getImageSizes();
    }
}
