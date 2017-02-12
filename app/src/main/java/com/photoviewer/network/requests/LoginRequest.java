package com.photoviewer.network.requests;

import lombok.AllArgsConstructor;

/**
 * Created by Sergiy on 30.01.2017.
 */
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;

}