package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final JpaRestaurantRepository jpaRestaurantRepository;
    private final RestaurantQueryRepository restaurantQueryRepository;

    @Override
    public Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id) {
        return jpaRestaurantRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<RestaurantEntity> findRestaurantPageByDeletedAtIsNullWithConditions(Long userId, String name, String sort, String address, String category, Pageable pageable) {
        return restaurantQueryRepository.findRestaurantPageByDeletedAtIsNullWithConditions(userId, name, sort, address, category, pageable);
    }

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity) {
        return jpaRestaurantRepository.save(restaurantEntity);
    }

    @Override
    public List<Long> findIdListByUserIdAndDeletedAtIsNull(Long userId) {
        String jpql = "SELECT o.id FROM RestaurantEntity o WHERE o.userId = :userId AND o.deletedAt IS NULL";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
