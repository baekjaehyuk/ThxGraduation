package com.thxgraduate.auth.controller.dto;

public record TokenPair(
        String owner,
        String accessToken,
        String refreshToken
) {

}