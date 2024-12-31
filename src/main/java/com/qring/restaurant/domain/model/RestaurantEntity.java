package com.qring.restaurant.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_restaurant")
public class RestaurantEntity {

    @Id @Tsid
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "tel", nullable = false)
    private String tel;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "address_details")
    private String addressDetails;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "restaurantEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OperatingHourEntity> operatingHourEntityList = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public RestaurantEntity(Long userId, String name, int capacity, String tel, String address, String addressDetails, CategoryEntity categoryEntity, String username) {
        this.userId = userId;
        this.name = name;
        this.capacity = capacity;
        this.tel = tel;
        this.address = address;
        this.addressDetails = addressDetails;
        this.category = categoryEntity;
        this.createdBy = username;
        this.modifiedBy = username;
    }

    // == 연관관계 메서드 == //
    public void addOperatingHour(List<OperatingHourEntity> operatingHourEntityList) {
        this.operatingHourEntityList.addAll(operatingHourEntityList);
        operatingHourEntityList.forEach(operatingHourEntity -> operatingHourEntity.updateRestaurant(this));
    }

    // == 생성 메서드 == //
    public static RestaurantEntity createRestaurantEntity(Long userId, String name, int capacity, String tel, String address, String addressDetails,
                                                          CategoryEntity categoryEntity, List<OperatingHourEntity> operatingHourEntityList, String username) {

        RestaurantEntity restaurantEntityForSave = RestaurantEntity.builder()
                .userId(userId)
                .name(name)
                .capacity(capacity)
                .tel(tel)
                .address(address)
                .addressDetails(addressDetails)
                .categoryEntity(categoryEntity)
                .username(username)
                .build();

        restaurantEntityForSave.addOperatingHour(operatingHourEntityList);

        return restaurantEntityForSave;
    }

}
