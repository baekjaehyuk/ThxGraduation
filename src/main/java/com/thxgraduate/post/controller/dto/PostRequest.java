package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import com.thxgraduate.auth.entity.User;
import jakarta.validation.constraints.Size;

public record PostRequest (
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이하이어야 합니다.")
        String name,

        @Size(min = 2, max = 500, message = "내용은 2자 이상, 500자 이하이어야 합니다.")
        String message,

        CharacterType characterType
) {
    public Post toEntity(User user, String name, String message, CharacterType characterType) {
        return Post.builder()
                .user(user)
                .characterType(characterType)
                .name(name)
                .message(message)
                .build();
    }
}
