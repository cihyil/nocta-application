package com.nocta.user.management.service;

import com.nocta.user.management.payload.response.LoginResponse;
import com.nocta.user.management.payload.request.LoginRequest;
import com.nocta.user.management.payload.request.SignupRequest;
import com.nocta.user.management.payload.response.MessageResponse;

public interface UserService {

    LoginResponse login(LoginRequest loginRequest);

    MessageResponse register(SignupRequest signUpRequest);

}
