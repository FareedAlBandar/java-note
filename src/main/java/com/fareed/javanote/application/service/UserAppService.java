package com.fareed.javanote.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.fareed.javanote.domain.entity.User;
import com.fareed.javanote.domain.repository.UserRepository;
import com.fareed.javanote.presentation.request.UserRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;




@Service
public class UserAppService  {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private JwtEncoder encoder;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserAppService.class);
    




    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10*365, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String authenticate(UserRequest userRequest) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
            return generateToken(authentication);
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: " + userRequest.getUsername());
            logger.error(e.toString());
            throw e;
        }

    }


    public String register(UserRequest userRequest) {
        User  user = new User(userRequest.getUsername(), "{bcrypt}"+passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
        return authenticate(userRequest);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).get();
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
    
}
