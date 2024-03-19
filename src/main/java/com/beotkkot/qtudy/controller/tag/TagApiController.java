package com.beotkkot.qtudy.controller.tag;

import com.beotkkot.qtudy.dto.response.tags.GetTop3TagsDto;
import com.beotkkot.qtudy.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TagApiController {
    private final TagService tagService;

    @GetMapping("/tag/top3")
    public ResponseEntity<? super GetTop3TagsDto> getTop3Tags() {
        ResponseEntity<? super GetTop3TagsDto> response = tagService.getTop3Tags();
        return response;
    }
}
