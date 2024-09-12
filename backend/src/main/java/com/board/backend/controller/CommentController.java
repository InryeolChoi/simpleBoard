package com.board.backend.controller;

import com.board.backend.dto.CommentDto;
import com.board.backend.entity.*;
import com.board.backend.service.CommentService;
import com.board.backend.service.PostService;
import com.board.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody @Valid CommentDto commentDto) {
        // Post와 User 찾기
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid postId: " + postId));
        User author = userService.findByUsername(commentDto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + commentDto.getAuthorUsername())); // userService에서 username으로 User를 찾음

        // 새로운 Comment 객체 생성
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPost(post);  // Post 연결
        comment.setAuthor(author);  // User 연결
        comment.setCreatedAt(LocalDateTime.now());  // 댓글 작성 시간 설정

        // Comment 저장
        Comment savedComment = commentService.saveComment(comment);

        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
