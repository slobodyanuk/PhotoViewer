package com.photoviewer.network.errors;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Serhii Slobodianiuk on 12.02.2017.
 */

public class Error {

    @Getter
    @SerializedName("error_code")
    private String errorCode;

    @Getter
    @SerializedName("error_msg")
    private String errorMessage;

}
