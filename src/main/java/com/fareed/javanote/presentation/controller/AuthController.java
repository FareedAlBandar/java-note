package com.fareed.javanote.presentation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.fareed.javanote.application.service.UserAppService;
import com.fareed.javanote.presentation.request.UserRequest;
import com.fareed.javanote.presentation.response.TokenResponse;


@Component
@Path("/v1/auth")
public class AuthController {




    @Autowired
    private UserAppService userAppService;


    @POST @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<TokenResponse> login(UserRequest userRequest) throws AuthenticationException {

        String token = userAppService.authenticate(userRequest);
        return new ResponseEntity<TokenResponse>(new TokenResponse(token), null, 200);
    }

    @POST @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    public ResponseEntity<TokenResponse> register(UserRequest userRequest) {
        String token = userAppService.register(userRequest);
        return ResponseEntity.ok(new TokenResponse(token));

    }


}
