package com.beotkkot.qtudy.repository.comments;

import com.beotkkot.qtudy.domain.comments.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    // int countByPostId(Long postid);

//    @Transactional
//    void deleteByPostId(Long postId);
}
