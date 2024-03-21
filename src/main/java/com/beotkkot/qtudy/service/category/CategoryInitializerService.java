package com.beotkkot.qtudy.service.category;

import com.beotkkot.qtudy.domain.category.Category;
import com.beotkkot.qtudy.repository.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryInitializerService {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void initializeCategories() {
        String[] categoryNames = {
                "경영학", "교육", "광고 및 미디어", "법학", "사회과학",
                "식품 및 체육", "언어 및 문학", "인문학", "약학", "예술 및 디자인",
                "자연과학", "전기 및 전자기학", "컴퓨터공학", "환경", "정치 및 외교"
        };

        for (long i = 1L; i < 16L; i++) {
            if (!categoryRepository.existsById(i)) {
                Category category = new Category(i, categoryNames[(int) (i - 1)]);
                categoryRepository.save(category);
            }
        }
    }
}
