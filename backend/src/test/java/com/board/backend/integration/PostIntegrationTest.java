package com.board.backend.integration;

import com.board.backend.dto.PostDto;
import com.board.backend.entity.Post;
import com.board.backend.entity.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class PostIntegrationTest {

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
    public void testCreatePost() throws Exception {
        // Given
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        PostDto postDto = new PostDto();
        postDto.setTitle("Test Post");
        postDto.setContent("This is a test post.");
        postDto.setAuthorUsername("testUser");

        // When & Then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Post\", \"content\": \"This is a test post.\", \"authorUsername\": \"testUser\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.content").value("This is a test post."));
    }

    @Test
    public void testGetPostById() throws Exception {
        // Given
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("This is a test post.");
        post.setAuthor(user);
        postRepository.save(post);

        // When & Then
        mockMvc.perform(get("/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.content").value("This is a test post."));
    }

    @Test
    public void testDeletePost() throws Exception {
        // Given
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("This is a test post.");
        post.setAuthor(user);
        postRepository.save(post);

        // When & Then
        mockMvc.perform(delete("/posts/" + post.getId()))
                .andExpect(status().isNoContent());
    }
}