package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import com.thxgraduate.auth.entity.User;
import jakarta.validation.constraints.Size;

public record PostRequest (
        @Size(min = 2, message = "닉네임은 최소 2글자 이상이어야 합니다.")
        String nickName,

        @Size(min = 2, message = "내용은 최소 2글자 이상이어야 합니다.")
        String text,

        Character character
) {
    public Post toEntity(User user, String nickName, String text, Character character) {
        return Post.builder()
                .user(user)
                .characterType(character)
                .nickName(nickName)
                .text(text)
                .build();
    }
}
