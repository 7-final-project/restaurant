package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // 특정 이름으로 삭제되지 않은 카테고리 존재 여부 확인
    boolean existsByNameAndDeletedAtIsNull(String name);

    // 특정 ID로 삭제되지 않은 카테고리 존재 여부 확인
    boolean existsByIdAndDeletedAtIsNull(Long id);

    // 특정 ID로 삭제되지 않은 카테고리 조회
    Optional<CategoryEntity> findByIdAndDeletedAtIsNull(Long id);
}
