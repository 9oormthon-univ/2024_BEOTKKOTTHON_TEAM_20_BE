package com.beotkkot.qtudy.repository.comments;

import com.beotkkot.qtudy.domain.comments.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {


    Page<Comments> findAllByPostId(Long postId, Pageable pageable);

    int countByPostId(Long postid);

    @Transactional
    void deleteByPostId(Long postId);
}
