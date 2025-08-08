package com.thxgraduate.post.service;

import com.thxgraduate.auth.repository.UserRepository;
import com.thxgraduate.post.controller.dto.PostRequest;
import com.thxgraduate.post.entity.Post;
import com.thxgraduate.post.repository.PostRepository;
import com.thxgraduate.auth.entity.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Post> get(UUID link) {
        User user = userRepository.findByLink(link);
        return postRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public void register(UUID link,PostRequest request) {
        User user = userRepository.findByLink(link);
        Post post = request.toEntity(user, request.nickName(), request.text(), request.characterType());
        postRepository.save(post);
    }
}
