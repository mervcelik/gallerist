package com.example.service.impl;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.dto.DtoUser;
import com.example.dto.RefreshTokenRequest;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.jwt.JWTService;
import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserRepository;
import com.example.service.IAuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public DtoUser register(AuthRequest authRequest) {
        DtoUser dtoUser = new DtoUser();

        User savedUser = userRepository.save(createUser(authRequest));

        BeanUtils.copyProperties(savedUser, dtoUser);
        return dtoUser;
    }

    @Override
    public AuthResponse authenticate(AuthRequest input) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword());

            authenticationProvider.authenticate(authenticationToken);

            Optional<User> optional = userRepository.findByUsername(input.getUsername());
            String token = jwtService.generateToken(optional.get());

            RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(optional.get()));

            AuthResponse authResponse= new AuthResponse();
            authResponse.setAccessToken(token);
            authResponse.setRefreshToken(savedRefreshToken.getRefreshToken());

            return authResponse;
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.USERNAME_OR_PASSWORD_INVALID, e.getMessage()));
        }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenRequest.getRefreshToken());

        if(refreshToken.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND, refreshTokenRequest.getRefreshToken()));
        }

        if(!isValidRefreshToken(refreshToken.get().getExpiredDate())) {
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_IS_EXPIRED, refreshTokenRequest.getRefreshToken()));
        }

        User user = refreshToken.get().getUser();

        String accessToken= jwtService.generateToken(user);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(user));


        AuthResponse authResponse= new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(savedRefreshToken.getRefreshToken());

        return authResponse;
    }

    public  boolean isValidRefreshToken(Date expiredDate){
        return new Date().before(expiredDate);
    }


    private User createUser(AuthRequest authRequest) {
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setCreateTime(new Date());
        return user;
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreateTime(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;

    }
}
