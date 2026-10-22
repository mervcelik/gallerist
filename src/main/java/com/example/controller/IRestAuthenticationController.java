package com.example.controller;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.dto.DtoUser;
import com.example.dto.RefreshTokenRequest;
import com.example.model.RefreshToken;

public interface IRestAuthenticationController {

    public RootEntity<DtoUser> register(AuthRequest authRequest);
    public RootEntity<AuthResponse> authenticate(AuthRequest input);
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest input);
}
