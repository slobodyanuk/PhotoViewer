package com.photoviewer.network.responses;

import com.photoviewer.network.errors.Error;

import lombok.Getter;

public class BaseResponse{

    @Getter
    private String message;

    @Getter
    private Error error;
}
