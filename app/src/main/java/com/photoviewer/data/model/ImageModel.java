package com.photoviewer.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

public class ImageModel {

    @Getter
    @SerializedName("created")
    public String created;

    @Getter
    @SerializedName("owner_id")
    public String ownerId;

    @Getter
    @SerializedName("aid")
    public String albumId;

}
