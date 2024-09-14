package com.board.backend.repository;

import com.board.backend.entity.Like;
import com.board.backend.entity.Post;
import com.board.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByPostIdAndUserId() {
        // Given
        Post post = new Post();
        post.setTitle("Test Post");
        postRepository.save(post);

        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        // When
        Optional<Like> foundLike = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());

        // Then
        assertTrue(foundLike.isPresent());
        assertEquals(post.getId(), foundLike.get().getPost().getId());
        assertEquals(user.getId(), foundLike.get().getUser().getId());
    }
}