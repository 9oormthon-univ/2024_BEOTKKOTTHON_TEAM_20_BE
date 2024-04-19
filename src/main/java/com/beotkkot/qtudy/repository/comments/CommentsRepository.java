package com.beotkkot.qtudy.repository.comments;

import com.beotkkot.qtudy.domain.comments.Comments;
import com.beotkkot.qtudy.domain.posts.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {


    Page<Comments> findByPost_PostId(Long postId, Pageable pageable);

    int countByPost_PostId(Long postId);

    @Transactional
    void deleteByPost_PostId(Long postId);
}
