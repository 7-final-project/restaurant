package com.qring.restaurant.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_category")
public class CategoryEntity {

    @Id
    @Tsid
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

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

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    public CategoryEntity(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    // 카테고리 생성 메서드
    public static CategoryEntity createCategoryEntity(String name, String username) {
        return CategoryEntity.builder()
                .name(name)
                .createdBy(username)
                .build();
    }

    // 카테고리 수정 메서드
    public void modifyCategoryEntity(String name, String username) {
        this.name = name;
        this.modifiedBy = username;
    }

    // 카테고리 삭제 메서드
    public void deleteCategoryEntity(String username) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = username;
    }
}
