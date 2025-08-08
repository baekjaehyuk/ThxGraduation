package com.thxgraduate.post.controller;

import com.thxgraduate.post.controller.dto.PostRequest;
import com.thxgraduate.post.controller.dto.PostResponse;
import com.thxgraduate.post.entity.Post;
import com.thxgraduate.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    @PostMapping("/{link}")
    public void register(@PathVariable UUID link,  @Valid @RequestBody PostRequest request) {
        postService.register(link, request);
    }

    @GetMapping("/{link}")
    public List<PostResponse> get(@PathVariable UUID link) {
        List<Post> posts = postService.get(link);
        return posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }
}
