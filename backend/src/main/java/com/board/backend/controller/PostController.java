package com.board.backend.controller;

import com.board.backend.dto.PostDto;
import com.board.backend.entity.Post;
import com.board.backend.entity.User;
import com.board.backend.service.PostService;
import com.board.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // 게시물 생성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostDto postDto) {
        // User 찾기 (작성자)
        User author = userService.findByUsername(postDto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + postDto.getAuthorUsername()));

        // 새로운 Post 객체 생성 및 저장
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(author);  // 작성자 설정
        post.setCreatedAt(postDto.getCreatedAt());

        Post savedPost = postService.savePost(post);
        return ResponseEntity.ok(savedPost);
    }

    // 모든 게시물 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 게시물 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + id));
        return ResponseEntity.ok(post);
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody @Valid PostDto postDto) {
        Post existingPost = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + id));

        // 수정할 내용 업데이트
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        Post updatedPost = postService.savePost(existingPost);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + id));

        postService.deletePost(post.getId());
        return ResponseEntity.noContent().build();
    }
}
