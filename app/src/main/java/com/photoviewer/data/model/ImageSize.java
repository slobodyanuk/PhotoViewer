package com.photoviewer.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public class ImageSize {

    @Getter
    @SerializedName("src")
    private String url;

    @Getter
    @SerializedName("width")
    private String width;

    @Getter
    @SerializedName("height")
    private String height;

    @Getter
    @SerializedName("type")
    private String type;

}
