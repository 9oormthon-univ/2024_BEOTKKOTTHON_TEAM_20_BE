package com.beotkkot.qtudy.repository.interests;

import com.beotkkot.qtudy.domain.interests.Interests;
import com.beotkkot.qtudy.domain.primaryKey.InterestsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestsRepository extends JpaRepository<Interests, InterestsPK> {

    List<Interests> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
