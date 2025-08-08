package com.thxgraduate.post.repository;

import com.thxgraduate.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserId(Long id);
}
