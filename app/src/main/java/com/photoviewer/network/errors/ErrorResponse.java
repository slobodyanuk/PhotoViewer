package com.photoviewer.network.errors;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class ErrorResponse {

    @Getter
    private String message;

    @Getter
    private Error error;
}
