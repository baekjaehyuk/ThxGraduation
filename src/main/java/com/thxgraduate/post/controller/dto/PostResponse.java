package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostResponse(
        String nickName,
        String text,
        CharacterType characterType,
        LocalDateTime createAt
) {
    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .characterType(post.getCharacterType())
                .nickName(post.getNickName())
                .text(post.getText())
                .createAt(post.getCreatedAt())
                .build();
    }
}
