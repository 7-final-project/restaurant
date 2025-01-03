package com.qring.restaurant.domain.repository;

import com.qring.restaurant.domain.model.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    // 카테고리 저장
    CategoryEntity save(CategoryEntity categoryEntity);

    // 특정 ID를 가진 카테고리 조회 (삭제되지 않은 상태)
    Optional<CategoryEntity> findByIdAndDeletedAtIsNull(Long id);

    // DeletedAt이 NULL인 모든 카테고리 조회
    List<CategoryEntity> findAllByDeletedAtIsNull();

    // 특정 ID를 가진 카테고리 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 이름을 가진 카테고리 존재 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);

    // 카테고리 수정
    CategoryEntity updateCategory(CategoryEntity categoryEntity);

    // 카테고리 삭제
    void deleteById(Long id);
}
