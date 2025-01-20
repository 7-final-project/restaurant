package com.qring.restaurant.domain.model;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_region")
public class RegionEntity {

    @Id
    @Tsid
    @Column(name = "region_id")
    private Long id;

    // 지역 코드 (예: 11, 11010)
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    // 지역 이름 (예: 서울특별시, 종로구)
    @Column(name = "name", nullable = false)
    private String name;

    // 계층의 깊이 (예: 서울특별시 = 0, 종로구 = 1)
    @Column(name = "depth", nullable = false)
    private int depth;

    // 부모 지역 (null일 경우 최상위 지역)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RegionEntity parent;

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

    // 지역 생성자
    @Builder
    public RegionEntity(String code, String name, RegionEntity parent, String createdBy) {
        this.code = code;
        this.name = name;
        this.parent = parent;
        this.depth = (parent != null) ? parent.getDepth() + 1 : 0;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    // 부모 지역 생성 메서드
    public static RegionEntity createRegionEntity(String code, String name, String username) {
        return RegionEntity.builder()
                .code(code)
                .name(name)
                .createdBy(username)
                .build();
    }

    // 자식 지역 생성 메서드
    public static RegionEntity createRegionEntity(String code, String name,RegionEntity parentEntity, String username) {
        return RegionEntity.builder()
                .code(code)
                .name(name)
                .parent(parentEntity)
                .createdBy(username)
                .build();
    }
}