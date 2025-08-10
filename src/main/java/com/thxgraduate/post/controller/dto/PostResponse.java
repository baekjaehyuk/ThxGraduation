package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostResponse(
        String owner,
        String name,
        String message,
        CharacterType characterType,
        LocalDateTime createAt
) {
    public static PostResponse of(Post post, String message, String owner) {
        return PostResponse.builder()
                .owner(owner)
                .characterType(post.getCharacterType())
                .name(post.getName())
                .message(message)
                .createAt(post.getCreatedAt())
                .build();
    }
}
