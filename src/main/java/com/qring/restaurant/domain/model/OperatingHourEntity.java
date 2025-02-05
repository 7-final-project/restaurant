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
import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_operating_hour")
public class OperatingHourEntity {

    @Id
    @Tsid
    @Column(name = "operating_hour_id")
    private Long id;

    @Column(name = "operation_day_of_week")
    private String operationDayOfWeek;

    @Column(name = "open_at")
    private LocalTime openAt;

    @Column(name = "closed_at")
    private LocalTime closedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public OperatingHourEntity(String operationDayOfWeek, LocalTime openAt, LocalTime closedAt, String username) {
        this.operationDayOfWeek = operationDayOfWeek;
        this.openAt = openAt;
        this.closedAt = closedAt;
        this.createdBy = username;
        this.modifiedBy = username;
    }

    // == 연관관계 메서드 == //
    public void updateRestaurant(RestaurantEntity restaurantEntity) {
        this.restaurantEntity = restaurantEntity;
    }

    // == 생성 메서드 == //
    public static OperatingHourEntity createOperatingHourEntity(String operationDayOfWeek, LocalTime openAt, LocalTime closedAt, String username) {
        return OperatingHourEntity.builder()
                .operationDayOfWeek(operationDayOfWeek)
                .openAt(openAt)
                .closedAt(closedAt)
                .username(username)
                .build();
    }

    // == 운영 시간 업데이트 메서드 == //
    public void updateOperatingHourEntity(String operationDayOfWeek, LocalTime openAt, LocalTime closedAt) {
        this.operationDayOfWeek = operationDayOfWeek;
        this.openAt = openAt;
        this.closedAt = closedAt;
    }

    public void deleteOperatingHourEntity(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

}
