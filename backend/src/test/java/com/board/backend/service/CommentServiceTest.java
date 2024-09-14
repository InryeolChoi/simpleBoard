package com.board.backend.service;

import com.board.backend.entity.Comment;
import com.board.backend.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCommentsByPostId() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findAllByPostId(anyLong())).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByPostId(1L);
        assertEquals(comments, result);
        verify(commentRepository, times(1)).findAllByPostId(1L);
    }

    @Test
    public void testSaveComment() {
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = commentService.saveComment(comment);
        assertEquals(comment, savedComment);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testDeleteComment() {
        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).deleteById(1L);
    }
}