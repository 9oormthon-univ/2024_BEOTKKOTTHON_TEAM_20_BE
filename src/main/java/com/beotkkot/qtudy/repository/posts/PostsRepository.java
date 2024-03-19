package com.beotkkot.qtudy.repository.posts;

import com.beotkkot.qtudy.domain.posts.Posts;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findAllByKakaoId(Long kakaoId, PageRequest pageRequest);

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:searchWord% OR p.content LIKE %:searchWord% OR p.tag LIKE %:searchWord%")
    List<Posts> findBySearchWord(String searchWord, PageRequest pageRequest);

    List<Posts> findByCategoryId(Long categoryId, PageRequest pageRequest);

    Posts findByPostId(Long postId);

    List<Posts> findAllByPostId(Long postId);

    @Query("SELECT p FROM Posts p WHERE p.postId IN :postIds")
    List<Posts> findByPostIds(@Param("postIds") List<Long> postIds);
}
