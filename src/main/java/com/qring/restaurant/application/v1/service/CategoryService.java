package com.qring.restaurant.application.v1.service;

import com.qring.restaurant.application.global.exception.ErrorCode;
import com.qring.restaurant.application.global.exception.RestaurantException;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.repository.CategoryRepository;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryEntity createCategory(Long userId, PostCategoryReqDTOV1 dto) {
        // 카테고리 이름 중복 검증
        if (categoryRepository.existsByNameAndDeletedAtIsNull(dto.getCategory().getName())) {
            throw new RestaurantException(ErrorCode.DUPLICATE_ERROR, "이미 존재하는 카테고리 이름입니다.");
        }

        CategoryEntity category = CategoryEntity.builder()
                .name(dto.getCategory().getName())
                .username(String.valueOf(userId))
                .build();
        return categoryRepository.save(category);
    }

    // 모든 카테고리 가져오기
    public List<CategoryEntity> findAllCategoriesByDeletedAtIsNull() {
        return categoryRepository.findAllByDeletedAtIsNull();
    }

    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RestaurantException(ErrorCode.NOT_FOUND_ERROR, "카테고리를 찾을 수 없습니다."));
    }

    public void updateCategory(Long userId, Long id, PutCategoryDTOV1 dto) {
        CategoryEntity existingCategory = categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RestaurantException(ErrorCode.NOT_FOUND_ERROR, "카테고리를 찾을 수 없습니다."));

        // 카테고리 이름 중복 검증
        if (!existingCategory.getName().equals(dto.getCategory().getName())
                && categoryRepository.existsByNameAndDeletedAtIsNull(dto.getCategory().getName())) {
            throw new RestaurantException(ErrorCode.DUPLICATE_ERROR, "이미 존재하는 카테고리 이름입니다.");
        }

        existingCategory.setName(dto.getCategory().getName());
        existingCategory.setModifiedBy(String.valueOf(userId));
        categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long userId, Long id) {
        CategoryEntity category = categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RestaurantException(ErrorCode.NOT_FOUND_ERROR, "카테고리를 찾을 수 없습니다."));
        category.setDeletedAt(LocalDateTime.now());
        category.setDeletedBy(String.valueOf(userId));
        categoryRepository.save(category);
    }
}

