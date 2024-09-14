package com.board.backend.repository;

import com.board.backend.entity.Post;
import com.board.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllByAuthorId() {
        // Given
        User author = new User();
        author.setUsername("testUser");
        userRepository.save(author);

        Post post1 = new Post();
        post1.setTitle("Test Post 1");
        post1.setAuthor(author);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Test Post 2");
        post2.setAuthor(author);
        postRepository.save(post2);

        // When
        List<Post> posts = postRepository.findAllByAuthorId(author.getId());

        // Then
        assertEquals(2, posts.size());
        assertEquals("Test Post 1", posts.get(0).getTitle());
        assertEquals("Test Post 2", posts.get(1).getTitle());
    }
}