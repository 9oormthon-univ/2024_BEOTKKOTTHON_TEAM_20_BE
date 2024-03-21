package com.beotkkot.qtudy.service.mypage;

import com.beotkkot.qtudy.domain.category.Category;
import com.beotkkot.qtudy.domain.interests.Interests;
import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.PostListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.GetMyInterestResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.GetMyPageAllResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.GetMyPageInfoResponseDto;
import com.beotkkot.qtudy.dto.response.mypage.MyInterestResponseDto;
import com.beotkkot.qtudy.repository.category.CategoryRepository;
import com.beotkkot.qtudy.repository.interests.InterestsRepository;
import com.beotkkot.qtudy.repository.posts.PostsRepository;
import com.beotkkot.qtudy.repository.scrap.ScrapRepository;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final PostsRepository postRepo;
    private final UserRepository userRepo;
    private final ScrapRepository scrapRepo;
    private final CategoryRepository categoryRepo;
    private final InterestsRepository interestsRepo;

    // 관심사 저장
    @Transactional
    public ResponseEntity<? super MyInterestResponseDto> saveMyInterests(Long kakao_uid, List<Long> interests) {

        try {
            if (userRepo.findByKakaoId(kakao_uid) == null) return MyInterestResponseDto.notExistedUser();
            List<Interests> findInterests = interestsRepo.findAllByUserId(kakao_uid);
            if ((interests.size() > 3) || (findInterests.size() > 0)) return MyInterestResponseDto.inputFailed();

            for (Long categoryId : interests) {
                // 사용자의 관심사 3개를 interestsRepo에 저장한다.
                // 1. CategoryRepo에서 사용자가 선택한 interests에 해당되는 관심사 조회
                Optional<Category> category = categoryRepo.findById(categoryId);
                if (!category.isPresent()) { // 존재하지 않는 카테고리일 경우
                    return MyInterestResponseDto.databaseError();
                }
                // 2. 존재하는 카테고리이고, 사용자가 아직 관심사를 등록하지 않았다면, 해당하는 사용자의 관심사를 등록
                Interests interest = new Interests(kakao_uid, categoryId);
                interestsRepo.save(interest);
            }
        } catch (Exception exception) {
            log.info("error ", exception.getMessage());
            return MyInterestResponseDto.databaseError();
        }
        MyInterestResponseDto responseDto = new MyInterestResponseDto();
        return responseDto.success();
    }

    // 관심 분야 목록 조회
    public ResponseEntity<? super GetMyInterestResponseDto> getMyInterests(Long kakao_uid) {

        List<Long> interestIds = new ArrayList<>();
        try {
            if (userRepo.findByKakaoId(kakao_uid) == null) return GetMyInterestResponseDto.notExistedUser();
            List<Interests> interests = interestsRepo.findAllByUserId(kakao_uid);
            for (Interests interest : interests) {
                interestIds.add(interest.getCategoryId());
            }
        } catch (Exception exception) {
            log.info("error ", exception.getMessage());
            return GetMyInterestResponseDto.databaseError();
        }

        return GetMyInterestResponseDto.success(interestIds);
    }

    // 마이페이지에서 자신의 정보 조회 (관심사, 사용자 이름, 이메일, 사용자 프로필이미지)
    public ResponseEntity<? super GetMyPageInfoResponseDto> getMyPageInfo(Long kakao_uid, String email) {

        Users user;
        try {
            // 유저가 존재하지 않으면 에러
            if (userRepo.findByKakaoId(kakao_uid) == null) return GetMyPageInfoResponseDto.notExistedUser();
            // 존재하면 유저 가져오기
            user = userRepo.findByKakaoId(kakao_uid);
        } catch (Exception exception) {
            log.info("error " + exception.getMessage());
            return GetMyPageInfoResponseDto.databaseError();
        }
        return GetMyPageInfoResponseDto.success(user, email);
    }

    public ResponseEntity<? super GetMyPageAllResponseDto> getAllPost(Long kakao_uid, int page) {
        List<PostListItem> postListItems = new ArrayList<>();
        int totalPages;
        try {
            PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("createdAt").descending());
            Page<Posts> posts = postRepo.findAllByKakaoId(kakao_uid, pageRequest);
            totalPages = posts.getTotalPages();
            for (Posts post : posts.getContent())
                postListItems.add(PostListItem.of(post));
        } catch (Exception exception) {
            log.info("error " + exception.getMessage());
            return ResponseDto.databaseError();
        }

        GetMyPageAllResponseDto responseDto = new GetMyPageAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page, totalPages);
    }

    public ResponseEntity<? super GetMyPageAllResponseDto> getAllScrapPost(Long kakao_uid, int page) {

        List<PostListItem> postListItems = new ArrayList<>();
        int totalPages;
        try {
            if (userRepo.findByKakaoId(kakao_uid) == null) return GetMyPageAllResponseDto.notExistedUser();

            PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("createdAt").descending());
            List<Long> postIdList = scrapRepo.findPostIdsByUserId(kakao_uid);
            Page<Posts> posts = postRepo.findByPostIds(postIdList, pageRequest);
            totalPages = posts.getTotalPages();

            if (posts == null) return GetMyPageAllResponseDto.notExistedPost();

            for (Posts post : posts.getContent())
                postListItems.add(PostListItem.of(post));
        } catch (Exception exception) {
            log.info("error " + exception.getMessage());
            return ResponseDto.databaseError();
        }

        GetMyPageAllResponseDto responseDto = new GetMyPageAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page, totalPages);
    }

    @Transactional
    public ResponseEntity<? super MyInterestResponseDto> patchMyInterests(Long kakao_uid, List<Long> interests) {
        try {
            if (userRepo.findByKakaoId(kakao_uid) == null) return MyInterestResponseDto.notExistedUser();
            if (interests.size() > 3) return MyInterestResponseDto.inputFailed();

            // 기존의 관심사를 모두 삭제
            interestsRepo.deleteAllByUserId(kakao_uid);

            // 새로운 관심사 추가
            for (Long categoryId : interests) {
                Optional<Category> category = categoryRepo.findById(categoryId);
                if (!category.isPresent()) { // 존재하지 않는 카테고리일 경우
                    return MyInterestResponseDto.databaseError();
                }
                Interests newInterest = new Interests(kakao_uid, categoryId);
                interestsRepo.save(newInterest);
            }
        } catch (Exception exception) {
            log.info("error ", exception.getMessage());
            return MyInterestResponseDto.databaseError();
        }
        MyInterestResponseDto responseDto = new MyInterestResponseDto();
        return responseDto.success();
    }
}
