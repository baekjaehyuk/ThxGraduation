package com.thxgraduate.auth.controller.dto;

public record TokenPair(
        String accessToken,
        String refreshToken
) {

}