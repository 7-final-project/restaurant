package com.qring.restaurant.application.res;

import com.qring.restaurant.domain.model.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTableGetResDTOV1 {

    private List<Category> categoryList;

    public static CategoryTableGetResDTOV1 of(List<CategoryEntity> categoryEntityList) {
        return CategoryTableGetResDTOV1.builder()
                .categoryList(Category.from(categoryEntityList))
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
                    .toList();
        }

        public static Category from(CategoryEntity categoryEntity) {
            return Category.builder()
                    .id(categoryEntity.getId())
                    .name(categoryEntity.getName())
                    .build();
        }
    }
}
