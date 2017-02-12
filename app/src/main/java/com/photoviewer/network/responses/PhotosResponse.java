package com.photoviewer.network.responses;

import com.google.gson.annotations.SerializedName;
import com.photoviewer.data.model.Photo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

@AllArgsConstructor
public class PhotosResponse extends BaseResponse {

    @Getter
    @SerializedName("response")
    private List<Photo> photos;

}
