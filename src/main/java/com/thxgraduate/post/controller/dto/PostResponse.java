package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostResponse(
        String name,
        String message,
        CharacterType characterType,
        LocalDateTime createAt
) {
    public static PostResponse from(Post post, String message) {
        return PostResponse.builder()
                .characterType(post.getCharacterType())
                .name(post.getName())
                .message(message)
                .createAt(post.getCreatedAt())
                .build();
    }
}
