package com.thxgraduate.post.service;

import com.thxgraduate.auth.repository.UserRepository;
import com.thxgraduate.post.controller.dto.PostListResponse;
import com.thxgraduate.post.controller.dto.PostListResponse.PostDetailResponse;
import com.thxgraduate.post.controller.dto.PostRequest;
import com.thxgraduate.post.entity.Post;
import com.thxgraduate.post.repository.PostRepository;
import com.thxgraduate.auth.entity.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PostListResponse get(UUID link) {
        User user = userRepository.findByLink(link)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<PostDetailResponse> postDetails = postRepository.findAllByUserId(user.getId())
                .stream()
                .map(post -> {
                    String message = isAuthenticated(user.getId()) && post.getRevealedMessage()
                            ? post.getMessage()
                            : null;
                    return PostDetailResponse.of(post, message);
                })
                .toList();

        return PostListResponse.of(user.getNickName(), postDetails);
    }

    @Transactional
    public void register(UUID link,PostRequest request) {
        User user = userRepository.findByLink(link)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = request.toEntity(user, request.name(), request.message(), request.characterType());
        postRepository.save(post);
    }

    private boolean isAuthenticated(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return id.toString().equals(authentication.getPrincipal());
    }
}
