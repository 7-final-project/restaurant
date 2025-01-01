package com.qring.restaurant.application.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPostResDTOV1 {

    private Category category;

    public static CategoryPostResDTOV1 of(Long id, String name) {
        return CategoryPostResDTOV1.builder()
                .category(Category.from(id, name))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long id;
        private String name;

        public static Category from(Long id, String name) {
            return Category.builder()
                    .id(id)
                    .name(name)
                    .build();
        }
    }
}
