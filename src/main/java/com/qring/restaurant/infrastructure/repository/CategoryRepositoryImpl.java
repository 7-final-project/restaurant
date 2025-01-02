package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.model.QCategoryEntity;
import com.qring.restaurant.domain.repository.CategoryRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JPAQueryFactory queryFactory; // QueryDSL 사용
    private final JpaCategoryRepository jpaCategoryRepository; // JPA Repository 사용

    private final QCategoryEntity categoryEntity = QCategoryEntity.categoryEntity;

    @Override
    public CategoryEntity save(CategoryEntity categoryEntity) {
        // JPA를 사용하여 카테고리 저장
        return jpaCategoryRepository.save(categoryEntity);
    }

    @Override
    public Optional<CategoryEntity> findByIdAndDeletedAtIsNull(Long id) {
        // JPA를 사용하여 ID로 카테고리 조회
        return jpaCategoryRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<CategoryEntity> findAllByConditions(String name, String sort, Pageable pageable) {
        // QueryDSL을 사용하여 동적 쿼리 생성 및 실행
        QueryResults<CategoryEntity> results = queryFactory
                .selectFrom(categoryEntity)
                .where(
                        nameContains(name), // 이름 조건
                        categoryEntity.deletedAt.isNull() // 삭제되지 않은 상태
                )
                .orderBy(getOrderSpecifier(sort)) // 정렬 조건
                .offset(pageable.getOffset()) // 페이지 시작
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetchResults(); // 결과 조회

        // 결과를 Page 객체로 변환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public boolean existsByIdAndDeletedAtIsNull(Long id) {
        // JPA를 사용하여 존재 여부 확인
        return jpaCategoryRepository.existsByIdAndDeletedAtIsNull(id);
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity categoryEntity) {
        // 기존 카테고리를 수정하여 저장
        Optional<CategoryEntity> existingCategory = jpaCategoryRepository.findByIdAndDeletedAtIsNull(categoryEntity.getId());
        if (existingCategory.isPresent()) {
            CategoryEntity categoryToUpdate = existingCategory.get();
            categoryToUpdate.setName(categoryEntity.getName()); // 이름 업데이트
            categoryToUpdate.setModifiedBy(categoryEntity.getModifiedBy()); // 수정자 업데이트
            categoryToUpdate.setModifiedAt(LocalDateTime.now()); // 수정 시간 업데이트
            return jpaCategoryRepository.save(categoryToUpdate);
        }
        throw new IllegalArgumentException("해당 ID의 카테고리를 찾을 수 없습니다.");
    }

    @Override
    public void deleteById(Long id) {
        // JPA를 사용하여 카테고리 논리 삭제
        jpaCategoryRepository.findByIdAndDeletedAtIsNull(id).ifPresent(category -> {
            category.setDeletedAt(LocalDateTime.now()); // 삭제 시간 설정
            jpaCategoryRepository.save(category); // 저장
        });
    }

    // 이름 검색 조건 생성
    private BooleanExpression nameContains(String name) {
        return name == null ? null : categoryEntity.name.containsIgnoreCase(name);
    }

    // 정렬 조건 생성
    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        if ("oldest".equalsIgnoreCase(sort)) {
            return categoryEntity.createdAt.asc(); // 오래된 순
        } else {
            return categoryEntity.createdAt.desc(); // 최신순 (기본값)
        }
    }
}
