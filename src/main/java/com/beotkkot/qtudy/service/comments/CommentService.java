package com.beotkkot.qtudy.service.comments;

import com.beotkkot.qtudy.domain.comments.Comments;
import com.beotkkot.qtudy.domain.posts.Posts;
import com.beotkkot.qtudy.dto.object.CommentListItem;
import com.beotkkot.qtudy.dto.request.comments.CommentsRequestDto;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.comments.CommentsResponseDto;
import com.beotkkot.qtudy.dto.response.comments.GetCommentsAllResponseDto;
import com.beotkkot.qtudy.repository.comments.CommentsRepository;
import com.beotkkot.qtudy.repository.posts.PostsRepository;
import com.beotkkot.qtudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentsRepository commentRepo;
    private final PostsRepository postRepo;
    private final UserRepository userRepo;

    @Transactional
    public ResponseEntity<? super CommentsResponseDto> saveComment(Long postId, Long userUid, CommentsRequestDto dto) {

        try {
            if (!postRepo.existsById(postId)) {
                // 존재하지 않는 포스트
                return CommentsResponseDto.notExistedPost();
            } else if (userRepo.findByKakaoId(userUid) == null) {
                // 존재하지 않는 유저
                return CommentsResponseDto.notExistedUser();
            } else {
                // 댓글 엔티티 생성
                Comments comment = dto.toEntity(postId);
                comment.setContent(dto.getContent());
                comment.setUserUid(userUid);

                // 댓글 저장
                commentRepo.save(comment);

                // postRespo의 commentCount 업데이트
                int commentCount = commentRepo.countByPostId(postId);
                List<Posts> posts = postRepo.findAllByPostId(postId);
                for (Posts post : posts) {
                    post.setCommentCount(commentCount);
                }
            }
        } catch (Exception exception) {
            log.info("error " + exception.getMessage());
            return ResponseDto.databaseError();
        }
        return CommentsResponseDto.success();
    }

    public ResponseEntity<? super GetCommentsAllResponseDto> getAllComment(Long postId, int page) {
        List<CommentListItem> commentListItems = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, 4, Sort.by("createdAt").descending());
        try {
            Posts post = postRepo.findByPostId(postId);
            List<Comments> comments = commentRepo.findAllByPostId(postId, pageable).getContent();

            for (Comments comment : comments) {
                commentListItems.add(CommentListItem.of(comment));
            }
        } catch (Exception exception) {
            log.info("error ", exception.getMessage());
            return ResponseDto.databaseError();
        }
        GetCommentsAllResponseDto responseDto = new GetCommentsAllResponseDto(commentListItems, page);
        return responseDto.success(commentListItems, page);
    }

    @Transactional
    public ResponseEntity<? super CommentsResponseDto> patchComment(Long postId, Long commentId, Long userUid, CommentsRequestDto dto) {

        try {
            if (userRepo.findByKakaoId(userUid) == null) {
                return CommentsResponseDto.notExistedUser();
            } else if (!postRepo.existsById(postId)) {
                return CommentsResponseDto.notExistedPost();
            } else if (!commentRepo.existsById(commentId)) {
                return CommentsResponseDto.notExistedComment();
            } else {
                // 댓글 수정
                Comments comment = commentRepo.findById(commentId).get();
                comment.setContent(dto.getContent());
                System.out.println("content = " + comment.getContent());
            }
        } catch (Exception exception) {
            log.info("error " + exception.getMessage());
            return ResponseDto.databaseError();
        }
        return CommentsResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super CommentsResponseDto> deleteComment(Long postId, Long commentId, Long userUid) {
        try {
            if (userRepo.findByKakaoId(userUid) == null) {
                return CommentsResponseDto.notExistedUser();
            } else if (!postRepo.existsById(postId)) {
                return CommentsResponseDto.notExistedPost();
            } else if (!commentRepo.existsById(commentId)) {
                return CommentsResponseDto.notExistedComment();
            } else {
                // 댓글 삭제
                Comments comment = commentRepo.findById(commentId).get();
                commentRepo.delete(comment);

                // postRespo의 commentCount 업데이트
                int commentCount = commentRepo.countByPostId(postId);
                Posts post = postRepo.findByPostId(postId);
                post.setCommentCount(commentCount);
            }
        } catch (Exception exception) {
            log.info("error " + exception.getMessage());
            return ResponseDto.databaseError();
        }
        return CommentsResponseDto.success();
    }
}
