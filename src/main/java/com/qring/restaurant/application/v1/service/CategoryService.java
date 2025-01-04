package com.qring.restaurant.application.v1.service;

import com.qring.restaurant.application.global.exception.ErrorCode;
import com.qring.restaurant.application.global.exception.RestaurantException;
import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.repository.CategoryRepository;
import com.qring.restaurant.presentation.v1.req.PostCategoryReqDTOV1;
import com.qring.restaurant.presentation.v1.req.PutCategoryDTOV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 새로운 카테고리 생성
    @Transactional
    public CategoryEntity createCategory(Long userId, PostCategoryReqDTOV1 dto) {
        // 중복 검증
        if (categoryRepository.existsByNameAndDeletedAtIsNull(dto.getCategory().getName())) {
            throw new RestaurantException(ErrorCode.DUPLICATE_ERROR, "이미 존재하는 카테고리 이름입니다.");
        }

        // 엔티티 생성
        return categoryRepository.save(
                CategoryEntity.createCategoryEntity(
                        dto.getCategory().getName(),
                        String.valueOf(userId)
                )
        );
    }

    // 모든 카테고리 조회
    @Transactional(readOnly = true)
    public List<CategoryEntity> findAllCategoryListByDeletedAtIsNull() {
        return categoryRepository.findAllByDeletedAtIsNull();
    }

    // 특정 카테고리 조회
    @Transactional(readOnly = true)
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RestaurantException(ErrorCode.NOT_FOUND_ERROR, "카테고리를 찾을 수 없습니다."));
    }

    // 카테고리 수정
    @Transactional
    public void updateCategory(Long userId, Long id, PutCategoryDTOV1 dto) {
        // 엔티티 조회
        CategoryEntity existingCategory = categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RestaurantException(ErrorCode.NOT_FOUND_ERROR, "카테고리를 찾을 수 없습니다."));

        // 중복 검증
        if (!existingCategory.getName().equals(dto.getCategory().getName())
                && categoryRepository.existsByNameAndDeletedAtIsNull(dto.getCategory().getName())) {
            throw new RestaurantException(ErrorCode.DUPLICATE_ERROR, "이미 존재하는 카테고리 이름입니다.");
        }

        // 수정
        existingCategory.modifyCategoryEntity(dto.getCategory().getName(), String.valueOf(userId));
    }

    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long userId, Long id) {
        // 엔티티 조회
        CategoryEntity categoryEntity = categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RestaurantException(ErrorCode.NOT_FOUND_ERROR, "카테고리를 찾을 수 없습니다."));

        // 삭제
        categoryEntity.deleteCategoryEntity(String.valueOf(userId));
    }
}
