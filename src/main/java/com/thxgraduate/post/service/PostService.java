package com.thxgraduate.post.service;

import com.thxgraduate.auth.repository.UserRepository;
import com.thxgraduate.post.controller.dto.PostRequest;
import com.thxgraduate.post.controller.dto.PostResponse;
import com.thxgraduate.post.entity.Post;
import com.thxgraduate.post.repository.PostRepository;
import com.thxgraduate.auth.entity.User;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> get(UUID link) {
        User user = userRepository.findByLink(link)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return postRepository.findAllByUserId(user.getId())
                .stream()
                .map(post -> {
                    String message = isAuthenticated() && post.getRevealedMessage() ? post.getMessage() : null;
                    return PostResponse.of(post, message, user.getNickName());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void register(UUID link,PostRequest request) {
        User user = userRepository.findByLink(link)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = request.toEntity(user, request.name(), request.message(), request.characterType());
        postRepository.save(post);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }
}
