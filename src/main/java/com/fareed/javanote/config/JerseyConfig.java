package com.fareed.javanote.config;


import java.util.Collections;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.fareed.javanote.presentation.controller.*;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        
        register(MultiPartFeature.class);

        register(AuthController.class);
        register(NoteController.class);
        setProperties(Collections.singletonMap("jersey.config.server.response.setStatusOverSendError", true));

    }
}
