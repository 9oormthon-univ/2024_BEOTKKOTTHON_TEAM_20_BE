package com.beotkkot.qtudy.service.quiz;

import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.quiz.Quiz;
import com.beotkkot.qtudy.domain.quiz.Review;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.ReviewDetailListItem;
import com.beotkkot.qtudy.dto.object.ReviewListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.GetReviewResponseDto;
import com.beotkkot.qtudy.dto.response.quiz.ReviewResponseDto;
import com.beotkkot.qtudy.repository.posts.PostsRepository;
import com.beotkkot.qtudy.repository.quiz.ReviewRepository;
import com.beotkkot.qtudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final PostsRepository postRepo;

    // 내가 푼 퀴즈 전체 조회
    @Transactional
    public ResponseEntity<? super ReviewResponseDto> getMyQuizList(Long kakao_uid, int page) {
        List<ReviewListItem> reviewListItems = new ArrayList<>();
        int totalPages;
        try {
            PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("createdAt").descending());
            Page<Review> reviews = reviewRepo.findHighestScoreReviewForEachReviewId(kakao_uid, pageRequest);
            totalPages = reviews.getTotalPages();

            for (Review review : reviews.getContent()) {
                Posts post = postRepo.findByPostId(review.getPostId());
                Users user = userRepo.findByKakaoId(post.getKakaoId());
                int totalScore = reviewRepo.findScoreByUserIdAndReviewId(kakao_uid, review.getReviewId());
                reviewListItems.add(ReviewListItem.of(user, review, totalScore));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        ReviewResponseDto responseDto = new ReviewResponseDto(reviewListItems, page, totalPages);
        return responseDto.success(reviewListItems, page, totalPages);
    }

    // 내가 푼 퀴즈 상세 조회
    @Transactional
    public ResponseEntity<? super GetReviewResponseDto> getMyQuiz(Long kakao_uid, String reviewId) {
        List<ReviewDetailListItem> reviewListItems = new ArrayList<>();
        Quiz quiz;
        int totalScore;
        try {
            List<Review> reviews = reviewRepo.findReviewByReviewId(reviewId);
            totalScore = reviewRepo.findScoreByUserIdAndReviewId(kakao_uid, reviewId);

            for (Review review : reviews) {
                quiz = review.getQuiz();
                reviewListItems.add(ReviewDetailListItem.of(quiz, review));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetReviewResponseDto responseDto = new GetReviewResponseDto(reviewListItems, totalScore);
        return responseDto.success(reviewListItems, totalScore);
    }
}
