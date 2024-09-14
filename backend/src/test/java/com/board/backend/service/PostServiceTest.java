package com.board.backend.service;

import com.board.backend.entity.Post;
import com.board.backend.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllPosts() {
        List<Post> posts = new ArrayList<>();
        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();
        assertEquals(posts, result);
        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void testGetPostById() {
        Post post = new Post();
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        Optional<Post> result = postService.getPostById(1L);
        assertTrue(result.isPresent());
        assertEquals(post, result.get());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void testSavePost() {
        Post post = new Post();
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post savedPost = postService.savePost(post);
        assertEquals(post, savedPost);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testDeletePost() {
        postService.deletePost(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }
}