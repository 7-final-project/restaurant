package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchResDTOV1 {

    private List<Category> categories;

    public static CategorySearchResDTOV1 of(List<CategoryEntity> categoryEntityList) {
        return CategorySearchResDTOV1.builder()
                .categories(Category.from(categoryEntityList))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {

        private Long id;
        private String name;

        public static List<Category> from(List<CategoryEntity> categoryEntityList) {
            return categoryEntityList.stream()
                    .map(Category::from)
                    .collect(Collectors.toList());
        }

        public static Category from(CategoryEntity categoryEntity) {
            return Category.builder()
                    .id(categoryEntity.getId())
                    .name(categoryEntity.getName())
                    .build();
        }
    }
}
