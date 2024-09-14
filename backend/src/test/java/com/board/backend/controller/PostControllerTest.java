package com.board.backend.controller;

import com.board.backend.dto.PostDto;
import com.board.backend.entity.Post;
import com.board.backend.entity.User;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePost() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setTitle("Test title");
        postDto.setContent("Test content");
        postDto.setAuthorUsername("testUser");

        User user = new User();
        user.setUsername("testUser");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test title");
        post.setContent("Test content");
        post.setAuthor(user);

        when(userService.findByUsername(any())).thenReturn(Optional.of(user));
        when(postService.savePost(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test title\", \"content\": \"Test content\", \"authorUsername\": \"testUser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    public void testGetAllPosts() throws Exception {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());

        when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetPostById() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test title");

        when(postService.getPostById(anyLong())).thenReturn(Optional.of(post));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test title"));
    }

    @Test
    public void testDeletePost() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());
    }
}