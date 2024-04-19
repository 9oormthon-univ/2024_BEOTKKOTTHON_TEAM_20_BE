package com.beotkkot.qtudy.repository.category;

import com.beotkkot.qtudy.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryId(Long categoryId);

}
