package com.qring.restaurant.application.v1.res;

import com.qring.restaurant.domain.model.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchResDTOV1 {

    private CategoryPage categoryPage;

    public static CategorySearchResDTOV1 of(Page<CategoryEntity> categoryEntityPage) {
        return CategorySearchResDTOV1.builder()
                .categoryPage(CategoryPage.from(categoryEntityPage))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPage {

        private List<Category> content;
        private PageDetails page;

        public static CategoryPage from(Page<CategoryEntity> categoryEntityPage) {
            return CategoryPage.builder()
                    .content(Category.from(categoryEntityPage.getContent()))
                    .page(PageDetails.from(categoryEntityPage))
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

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PageDetails {

            private int size;
            private int number;
            private long totalElements;
            private int totalPages;

            public static PageDetails from(Page<CategoryEntity> categoryEntityPage) {
                return PageDetails.builder()
                        .size(categoryEntityPage.getSize())
                        .number(categoryEntityPage.getNumber())
                        .totalElements(categoryEntityPage.getTotalElements())
                        .totalPages(categoryEntityPage.getTotalPages())
                        .build();
            }
        }
    }
}
