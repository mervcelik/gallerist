package com.example.service;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.dto.DtoUser;

public interface IAuthenticationService {

    public DtoUser register(AuthRequest authRequest);
    public AuthResponse authenticate(AuthRequest input);
}
