package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    // 특정 이름으로 삭제되지 않은 카테고리 존재 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);

    // 특정 ID로 삭제되지 않은 카테고리 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 ID로 삭제되지 않은 카테고리 조회
    Optional<CategoryEntity> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 모든 카테고리 조회
    List<CategoryEntity> findAllByDeletedAtIsNull();

    // 카테고리 저장
    CategoryEntity save(CategoryEntity categoryEntity);
}
