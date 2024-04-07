package com.nocta.login.service.payload.response;

public record LoginResponse(boolean success, String message, User user) {
}
