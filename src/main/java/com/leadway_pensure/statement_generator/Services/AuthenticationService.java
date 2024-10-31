package com.leadway_pensure.statement_generator.Services;

import com.leadway_pensure.statement_generator.API.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationClient authenticationClient;

    public boolean login(String username, String password) {
        return authenticationClient.authenticate(username, password);
    }
}