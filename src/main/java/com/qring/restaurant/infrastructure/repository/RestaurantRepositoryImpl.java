package com.qring.restaurant.infrastructure.repository;

import com.qring.restaurant.domain.model.RestaurantEntity;
import com.qring.restaurant.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final JpaRestaurantRepository jpaRestaurantRepository;
    private final RestaurantQueryRepository restaurantQueryRepository;

    @Override
    public boolean existsByIdAndDeletedAtIsNull(Long id) {
        return jpaRestaurantRepository.existsByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Optional<RestaurantEntity> findByIdAndDeletedAtIsNull(Long id) {
        return jpaRestaurantRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Page<RestaurantEntity> findAllByConditions(Long userId, String name, String sort, String address, String category, Pageable pageable) {
        return restaurantQueryRepository.findAllByConditions(userId, name, sort, address, category, pageable);
    }

    @Override
    public List<RestaurantEntity> findAllByDeletedAtIsNull() {
        return restaurantQueryRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public RestaurantEntity save(RestaurantEntity restaurantEntity) {
        return jpaRestaurantRepository.save(restaurantEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaRestaurantRepository.findByIdAndDeletedAtIsNull(id).ifPresent(restaurant -> {
            restaurant.setDeletedAt(LocalDateTime.now());
            jpaRestaurantRepository.save(restaurant);
        });
    }

    @Override
    public void updateRestaurant(RestaurantEntity restaurantEntity) {
        jpaRestaurantRepository.save(restaurantEntity);
    }
}
