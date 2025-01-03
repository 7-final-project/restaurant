package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.CategoryEntity;
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

    public static CategoryPostResDTOV1 of(CategoryEntity categoryEntity) {
        return CategoryPostResDTOV1.builder()
                .category(Category.from(categoryEntity))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long id;
        private String name;

        public static Category from(CategoryEntity categoryEntity) {
            return Category.builder()
                    .id(categoryEntity.getId())
                    .name(categoryEntity.getName())
                    .build();
        }
    }
}
