package com.beotkkot.qtudy.dto.object;

import com.beotkkot.qtudy.domain.tags.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagListItem {
    private String name;
    private int count;

    public static TagListItem of(Tags tag) {

        return TagListItem.builder()
                .name(tag.getName())
                .count(tag.getCount())
                .build();
    }
}
