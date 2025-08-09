package com.thxgraduate.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.thxgraduate.post.controller.dto.PostRequest;
import com.thxgraduate.post.controller.dto.PostResponse;
import com.thxgraduate.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시글 API", description = "게시글 등록 및 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @Operation(
        summary = "게시글 등록",
        description = "특정 링크(UUID)에 게시글을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "게시글 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
        }
    )
    @PostMapping("/{link}")
    public void register(
        @Parameter(description = "링크 UUID", required = true)
        @PathVariable(value = "link") UUID link,
        @Valid @RequestBody PostRequest request
    ) {
        postService.register(link, request);
    }

    @Operation(
        summary = "게시글 조회",
        description = "특정 링크(UUID)에 해당하는 게시글 목록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 반환"),
            @ApiResponse(responseCode = "404", description = "게시글 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
        }
    )
    @GetMapping("/{link}")
    public List<PostResponse> get(
        @Parameter(description = "링크 UUID", required = true) @PathVariable(value = "link") UUID link
    ) {
        return postService.get(link);
    }
}
