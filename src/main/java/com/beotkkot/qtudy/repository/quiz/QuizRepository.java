package com.beotkkot.qtudy.repository.quiz;

import com.beotkkot.qtudy.domain.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByPostId(Long postId);

    Quiz findByQuizId(Long quizId);

    @Query("SELECT q FROM Quiz q WHERE q.tags LIKE %:tagName% ORDER BY q.quizId ASC, RAND() LIMIT 10")
    List<Quiz> findByTagName(@Param("tagName") String tagName);

    @Transactional
    void deleteByPostId(Long postId);
}
