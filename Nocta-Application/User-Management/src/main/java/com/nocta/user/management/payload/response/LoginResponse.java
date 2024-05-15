package com.nocta.user.management.payload.response;

public record LoginResponse(boolean success, String message, UserResponse userResponse) {
}
