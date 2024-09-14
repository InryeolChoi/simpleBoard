package com.board.backend.integration;

import com.board.backend.dto.CommentDto;
import com.board.backend.entity.Comment;
import com.board.backend.entity.Post;
import com.board.backend.entity.User;
import com.board.backend.repository.CommentRepository;
import com.board.backend.repository.PostRepository;
import com.board.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class CommentIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Object[]{}).build();
    }

    @Test
    public void testAddComment() throws Exception {
        // Given
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setAuthor(user);
        postRepository.save(post);

        CommentDto commentDto = new CommentDto();
        commentDto.setContent("This is a test comment.");
        commentDto.setAuthorUsername("testUser");

        // When & Then
        mockMvc.perform(post("/posts/" + post.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"content\": \"This is a test comment.\", \"authorUsername\": \"testUser\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a test comment."));
    }

    @Test
    public void testDeleteComment() throws Exception {
        // Given
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setAuthor(user);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Test Comment");
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // When & Then
        mockMvc.perform(delete("/posts/" + post.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isNoContent());
    }
}