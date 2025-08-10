package com.thxgraduate.post.controller.dto;

import com.thxgraduate.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record PostListResponse(
        String owner,
        List<PostDetailResponse> posts
) {
    @Builder
    public record PostDetailResponse(
            String name,
            String message,
            CharacterType characterType,
            LocalDateTime createAt
    ) {
        public static PostDetailResponse of(Post post, String message) {
            return PostDetailResponse.builder()
                    .characterType(post.getCharacterType())
                    .name(post.getName())
                    .message(message)
                    .createAt(post.getCreatedAt())
                    .build();
        }
    }

    public static PostListResponse of(String owner, List<PostDetailResponse> posts) {
        return new PostListResponse(owner, posts);
    }
}