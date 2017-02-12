package com.photoviewer.data.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

@Parcel
public class Photo extends ImageModel{

    @Getter
    @SerializedName("pid")
    private String photoId;

    @Getter
    @SerializedName("text")
    private String text;

    @Getter
    @SerializedName("src_small")
    private String imageUrlSmall;

    @Getter
    @SerializedName("src")
    private String imageUrl;

    @Getter
    @SerializedName("src_big")
    private String imageUrlBig;

}
