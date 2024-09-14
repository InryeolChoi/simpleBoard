package com.board.backend.controller;

import com.board.backend.dto.CommentDto;
import com.board.backend.entity.Comment;
import com.board.backend.entity.Post;
import com.board.backend.entity.User;
import com.board.backend.service.CommentService;
import com.board.backend.service.PostService;
import com.board.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent("Test comment");
        commentDto.setAuthorUsername("testUser");

        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setUsername("testUser");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setPost(post);
        comment.setAuthor(user);

        when(postService.getPostById(anyLong())).thenReturn(Optional.of(post));
        when(userService.findByUsername(any())).thenReturn(Optional.of(user));
        when(commentService.saveComment(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"Test comment\", \"authorUsername\": \"testUser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test comment"));
    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/posts/1/comments/1"))
                .andExpect(status().isNoContent());
    }
}
