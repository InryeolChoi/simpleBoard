package com.board.backend.repository;

import com.board.backend.entity.Comment;
import com.board.backend.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testFindAllByPostId() {
        // Given
        Post post = new Post();
        post.setTitle("Test Post");
        postRepository.save(post);

        Comment comment1 = new Comment();
        comment1.setContent("Test Comment 1");
        comment1.setPost(post);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Test Comment 2");
        comment2.setPost(post);
        commentRepository.save(comment2);

        // When
        List<Comment> comments = commentRepository.findAllByPostId(post.getId());

        // Then
        assertEquals(2, comments.size());
        assertEquals("Test Comment 1", comments.get(0).getContent());
        assertEquals("Test Comment 2", comments.get(1).getContent());
    }
}