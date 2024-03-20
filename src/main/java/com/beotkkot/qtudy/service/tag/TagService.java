package com.beotkkot.qtudy.service.tag;

import com.beotkkot.qtudy.domain.tags.Tags;
import com.beotkkot.qtudy.dto.object.TagListItem;
import com.beotkkot.qtudy.dto.response.ResponseDto;
import com.beotkkot.qtudy.dto.response.tags.GetTagsResponseDto;
import com.beotkkot.qtudy.repository.tags.TagsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagsRepository tagRepo;

    @Transactional
    public ResponseEntity<? super GetTagsResponseDto> getTop3Tags() {
        List<TagListItem> top3List = new ArrayList<>();
        try {
            List<Tags> tags = tagRepo.findTop3ByOrderByCountDesc();
            for (Tags tag : tags) {
                top3List.add(TagListItem.of(tag));
            }
            log.info(String.valueOf(top3List));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetTagsResponseDto responseDto = new GetTagsResponseDto(top3List);
        return responseDto.success(top3List);
    }

    @Transactional
    public ResponseEntity<? super GetTagsResponseDto> getTagsByCategory(Long categoryId) {
        List<TagListItem> tagList = new ArrayList<>();
        try {
            List<Tags> tags = tagRepo.findByCategoryId(categoryId);
            for (Tags tag : tags) {
                tagList.add(TagListItem.of(tag));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        GetTagsResponseDto responseDto = new GetTagsResponseDto(tagList);
        return responseDto.success(tagList);
    }
}
