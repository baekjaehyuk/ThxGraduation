package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import com.thxgraduate.auth.entity.User;
import jakarta.validation.constraints.Size;

public record PostRequest (
        @Size(min = 2, message = "닉네임은 최소 2글자 이상이어야 합니다.")
        String nickName,

        @Size(min = 2, message = "내용은 최소 2글자 이상이어야 합니다.")
        String text,

        CharacterType characterType
) {
    public Post toEntity(User user, String nickName, String text, CharacterType characterType) {
        return Post.builder()
                .user(user)
                .characterType(characterType)
                .nickName(nickName)
                .text(text)
                .build();
    }
}
