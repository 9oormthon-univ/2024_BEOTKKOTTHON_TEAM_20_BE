package com.beotkkot.qtudy.service.posts;

import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.domain.scrap.Scrap;
import com.beotkkot.qtudy.domain.tags.Tags;
import com.beotkkot.qtudy.domain.user.Users;
import com.beotkkot.qtudy.dto.object.PostListItem;
import com.beotkkot.qtudy.dto.request.posts.PostsRequestDto;
import com.beotkkot.qtudy.dto.response.*;
import com.beotkkot.qtudy.dto.response.posts.*;
import com.beotkkot.qtudy.repository.comments.CommentsRepository;
import com.beotkkot.qtudy.repository.posts.PostsRepository;
import com.beotkkot.qtudy.repository.scrap.ScrapRepository;
import com.beotkkot.qtudy.repository.tags.TagsRepository;
import com.beotkkot.qtudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepo;
    private final UserRepository userRepo;
    private final TagsRepository tagRepo;
    private final ScrapRepository scrapRepo;
    private final SummaryService summaryService;
    private final CommentsRepository commentsRepo;

    @Transactional
    public ResponseEntity<? super PostsResponseDto> savePost(Long kakao_uid, PostsRequestDto dto) {
        Long postId;
        List<Tags> newTagList = new ArrayList<>();
        List<String> increasedTag = new ArrayList<>();
        try {

            if (userRepo.findByKakaoId(kakao_uid) != null) {

                // 포스트 엔티티 생성
                Posts post = dto.toEntity(kakao_uid);

                // 태그 처리
                List<String> postTags = dto.getTag();

                for (String tagName : postTags) {
                    Optional<Tags> existingTag = tagRepo.findByName(tagName);
                    if (existingTag.isPresent()) {
                        // 기존에 있는 태그인 경우 count를 증가시킴
                        Tags tag = existingTag.get();
                        tag.increaseTagCount();
                        increasedTag.add(tagName);
                    } else {
                        // 새로운 태그인 경우 태그를 생성하고 count를 1로 초기화함
                        Tags newTag = new Tags();
                        newTag.setName(tagName);
                        newTag.setCount(1); // 새로운 태그의 count를 1로 초기화
                        newTag.setCategoryId(dto.getCategoryId());

                        newTagList.add(newTag);
                    }
                }

                // 저장된 태그 목록을 포스트에 설정
                String tagString = String.join(",", postTags);
                post.setTag(tagString);

                // postRepo에 해당 유저가 작성한 글에 대한 요약본 저장하는 부분 추가
                String summary = summaryService.summary(dto.getContent());
                post.setContent(dto.getContent());
                post.setSummary(summary);

                // 포스트 저장 후 postId 반환
                Posts savedPost = postsRepo.save(post);
                postId = savedPost.getPostId();

                tagRepo.saveAll(newTagList);
            } else {
                return PostsResponseDto.notExistUser();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            for (String tagName : increasedTag) {
                Optional<Tags> existingTag = tagRepo.findByName(tagName);
                Tags tag = existingTag.get();
                tag.decreaseTagCount();
            }
            return ResponseDto.databaseError();
        }
        return PostsResponseDto.success(postId);
    }

    @Transactional
    public ResponseEntity<? super GetPostsResponseDto> getPost(Long postId) {
        Posts post;
        Users user;
        try {
            if (postsRepo.existsById(postId)) {
                post = postsRepo.findById(postId).get();
                user = userRepo.findByKakaoId(post.getKakaoId());
            } else {
                return GetPostsResponseDto.noExistPost();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetPostsResponseDto.success(post, user);
    }

    @Transactional
    public ResponseEntity<? super GetSummaryResponseDto> getSummary(Long postId) {
        Posts post;
        String summary;
        try {
            if (postsRepo.existsById(postId)) {
                post = postsRepo.findByPostId(postId);
                summary = post.getSummary();
            } else {
                return GetSummaryResponseDto.noExistPost();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetSummaryResponseDto.success(postId, summary);
    }

    @Transactional
    public ResponseEntity<? super GetPostsAllResponseDto> getAllPost(int page) {
        List<PostListItem> postListItems = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("createdAt").descending());
        int totalPages;
        try {
            Page<Posts> posts = postsRepo.findAll(pageRequest);
            totalPages = posts.getTotalPages();
            for (Posts post : posts.getContent())
                postListItems.add(PostListItem.of(post));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostsAllResponseDto responseDto = new GetPostsAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page, totalPages);
    }

    @Transactional
    public ResponseEntity<? super PostsResponseDto> patchPost(Long postId, Long kakao_uid, PostsRequestDto dto) {
        try {
            Optional<Posts> postOptional = postsRepo.findById(postId);
            if (!postOptional.isPresent()) return PostsResponseDto.notExistedPost();

            Posts post = postOptional.get();
            if (userRepo.findByKakaoId(kakao_uid) == null) return PostsResponseDto.notExistUser();

            Long writerId = post.getKakaoId();
            if (!writerId.equals(kakao_uid)) return PostsResponseDto.noPermission();

            // 업데이트되기 이전의 태그 목록
            List<String> existingTags = Arrays.asList(post.getTag().split(","));

            // 업데이트된 태그 목록
            List<String> updatedTags = dto.getTag();

            // 태그 카운트를 증감시키기 위한 로직
            for (String tagName : existingTags) {
                if (!updatedTags.contains(tagName)) {
                    // 태그가 삭제된 경우 카운트 감소
                    Optional<Tags> tagOptional = tagRepo.findByName(tagName);
                    tagOptional.ifPresent(Tags::decreaseTagCount);
                }
            }
            for (String tagName : updatedTags) {
                if (!existingTags.contains(tagName)) {
                    Optional<Tags> existTag = tagRepo.findByName(tagName);
                    if (existTag.isPresent()) {
                        // 기존에 있는 태그인 경우 count를 증가시킴
                        Tags tag = existTag.get();
                        tag.increaseTagCount();
                    } else {
                        // 새로운 태그인 경우 태그를 생성하고 count를 1로 초기화함
                        Tags newTag = new Tags();
                        newTag.setName(tagName);
                        newTag.setCount(1); // 새로운 태그의 count를 1로 초기화

                        // 새로운 태그를 저장
                        tagRepo.save(newTag);
                    }
                }
            }

            post.patchPost(dto);

            // 요약
            String summary = summaryService.summary(dto.getContent());
            post.setContent(dto.getContent());
            post.setSummary(summary);

            postsRepo.save(post);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PostsResponseDto.success(postId);
    }

    @Transactional
    public ResponseEntity<? super PostsResponseDto> deletePost(Long postId, Long kakao_uid) {
        Posts post = postsRepo.findById(postId).get();
        try{
            if (!postsRepo.existsById(postId)) return PostsResponseDto.notExistedPost();
            if (userRepo.findByKakaoId(kakao_uid) == null) return PostsResponseDto.notExistUser();

            Long writerId = post.getKakaoId();
            boolean isWriter = writerId.equals(kakao_uid);
            if (!isWriter) return PostsResponseDto.noPermission();

            scrapRepo.deleteByPostId(postId);
            commentsRepo.deleteByPostId(postId);

            // 관련된 hash tag -1
            List<String> tagNameList = Arrays.asList(post.getTag().split("\\s*,\\s*"));
            List<Tags> tagList = tagRepo.findByNames(tagNameList);
            for (Tags tag: tagList)
                tag.decreaseTagCount();

            postsRepo.delete(post);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PostsResponseDto.success(postId);
    }

    @Transactional
    public ResponseEntity<? super GetPostsAllResponseDto> getMyPost(Long kakao_uid, int page) {
        List<PostListItem> postListItems = new ArrayList<>();
        int totalPages;
        try {
            PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("createdAt").descending());
            Page<Posts> posts = postsRepo.findAllByKakaoId(kakao_uid, pageRequest);
            totalPages = posts.getTotalPages();
            for (Posts post : posts.getContent())
                postListItems.add(PostListItem.of(post));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostsAllResponseDto responseDto = new GetPostsAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page, totalPages);
    }

    @Transactional
    public ResponseEntity<? super GetPostsAllResponseDto> getSearchPost(String searchWord, int page) {
        List<PostListItem> postListItems = new ArrayList<>();
        int totalPages;
        try {
            PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("createdAt").descending());
            Page<Posts> posts = postsRepo.findBySearchWord(searchWord, pageRequest);
            totalPages = posts.getTotalPages();
            for (Posts post : posts.getContent())
                postListItems.add(PostListItem.of(post));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostsAllResponseDto responseDto = new GetPostsAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page, totalPages);
    }

    @Transactional
    public ResponseEntity<? super GetPostsAllResponseDto> getCategorySearchPost(List<Long> categories, int page) {
        List<PostListItem> postListItems = new ArrayList<>();
        int totalPages;
        try {
            PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("createdAt").descending());
            Page<Posts> posts = postsRepo.findByCategoryIds(categories, pageRequest);
            totalPages = posts.getTotalPages();

            for (Posts post : posts.getContent()) {
                postListItems.add(PostListItem.of(post));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostsAllResponseDto responseDto = new GetPostsAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page, totalPages);
    }

    // 스크랩
    @Transactional
    public ResponseEntity<? super PutScrapResponseDto> putScrap(Long postId, Long kakao_uid) {
        try {
            if (userRepo.findByKakaoId(kakao_uid) == null) return PutScrapResponseDto.notExistUser();
            Posts post = postsRepo.findByPostId(postId);
            if (post == null) return PutScrapResponseDto.notExistedPost();

            Scrap scrap = scrapRepo.findByPostIdAndUserId(postId, kakao_uid);

            // 존재하지 않는다면 추가. 존재한다면 삭제
            if (scrap == null) {
                scrap = new Scrap(kakao_uid, postId);
                scrapRepo.save(scrap);
                post.increaseScrapCount();
            }
            else {
                scrapRepo.delete(scrap);
                post.decreaseScrapCount();
            }

            postsRepo.save(post);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PutScrapResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super GetPostsAllResponseDto> getAllScrapPost(Long kakao_uid, int page) {
        List<PostListItem> postListItems = new ArrayList<>();
        int totalPages;
        try {
            if (userRepo.findByKakaoId(kakao_uid) == null) return PutScrapResponseDto.notExistUser();

            PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("createdAt").descending());
            List<Long> postIdList = scrapRepo.findPostIdsByUserId(kakao_uid);
            Page<Posts> posts = postsRepo.findByPostIds(postIdList, pageRequest);
            totalPages = posts.getTotalPages();

            if (posts == null) return PutScrapResponseDto.notExistedPost();

            for (Posts post : posts.getContent())
                postListItems.add(PostListItem.of(post));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetPostsAllResponseDto responseDto = new GetPostsAllResponseDto(postListItems, page, totalPages);
        return responseDto.success(postListItems, page,totalPages);
    }
}
