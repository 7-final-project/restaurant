package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.CategoryEntity;
import com.qring.restaurant.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    @Override
    public boolean existsByNameAndDeletedAtIsNull(String name) {
        return jpaCategoryRepository.existsByNameAndDeletedAtIsNull(name);
    }

    @Override
    public boolean existsByIdAndDeletedAtIsNull(Long id) {
        return jpaCategoryRepository.existsByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<CategoryEntity> findByIdAndDeletedAtIsNull(Long id) {
        return jpaCategoryRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public List<CategoryEntity> findAllByDeletedAtIsNull() {
        return categoryQueryRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public CategoryEntity save(CategoryEntity categoryEntity) {
        return jpaCategoryRepository.save(categoryEntity);
    }
}
