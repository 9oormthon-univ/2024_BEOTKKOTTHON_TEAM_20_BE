package com.beotkkot.qtudy.repository.scrap;

import com.beotkkot.qtudy.domain.scrap.Scrap;
import com.beotkkot.qtudy.domain.primaryKey.ScrapPk;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, ScrapPk> {
    Scrap findByPostIdAndUserId(Long postId, Long uid);

    @Query("SELECT s.postId FROM Scrap s WHERE s.userId = :userId")
    List<Long> findPostIdsByUserId(@Param("userId") Long userId, PageRequest pageRequest);

    @Transactional
    void deleteByPostId(Long postId);
}
