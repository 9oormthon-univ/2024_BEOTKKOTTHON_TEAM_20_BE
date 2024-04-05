package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.quiz.Review;
import com.beotkkot.qtudy.domain.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListItem {
    private String userName;
    private String userProfile;
    private String reviewId;
    private int totalScore;
    private String type;
    private String createdAt;
    private Long categoryId;
    private List<String> tags;

    public static ReviewListItem of(Users user, Review review, int totalScore) {
        List<String> tag = Arrays.asList(review.getQuiz().getTags().split("\\s*,\\s*"));
        String userName;
        String userProfile;
        if (review.getType().equals("tag")) {
            userName = null;
            userProfile = null;
        }
        else {
            userName = user.getName();
            userProfile = user.getProfileImageUrl();
        }

        return ReviewListItem.builder()
                .userName(userName)
                .userProfile(userProfile)
                .reviewId(review.getReviewId())
                .totalScore(totalScore)
                .type(review.getType())
                .createdAt(review.getCreatedAt())
                .tags(tag)
                .categoryId(review.getCategoryId())
                .build();
    }
}
