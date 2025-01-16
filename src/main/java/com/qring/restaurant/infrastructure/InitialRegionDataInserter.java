package com.qring.restaurant.infrastructure;

import com.qring.restaurant.domain.model.RegionEntity;
import com.qring.restaurant.domain.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InitialRegionDataInserter implements CommandLineRunner {

    private final RegionRepository regionRepository;

    public InitialRegionDataInserter(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 초기 지역 데이터 생성
        List<RegionEntity> initialRegions = List.of(
                new RegionEntity("11", "서울특별시", null, "admin"),
                new RegionEntity("21", "부산광역시", null, "admin"),
                new RegionEntity("22", "대구광역시", null, "admin"),
                new RegionEntity("23", "인천광역시", null, "admin"),
                new RegionEntity("24", "광주광역시", null, "admin"),
                new RegionEntity("25", "대전광역시", null, "admin"),
                new RegionEntity("26", "울산광역시", null, "admin"),
                new RegionEntity("29", "세종특별자치시", null, "admin"),
                new RegionEntity("31", "경기도", null, "admin"),
                new RegionEntity("32", "강원특별자치도", null, "admin"),
                new RegionEntity("33", "충청북도", null, "admin"),
                new RegionEntity("34", "충청남도", null, "admin"),
                new RegionEntity("35", "전라북도", null, "admin"),
                new RegionEntity("36", "전라남도", null, "admin"),
                new RegionEntity("37", "경상북도", null, "admin"),
                new RegionEntity("38", "경상남도", null, "admin"),
                new RegionEntity("39", "제주특별자치도", null, "admin")
        );

        for (RegionEntity region : initialRegions) {
            if (!regionRepository.existsByNameAndDeletedAtIsNull(region.getName())) {
                regionRepository.save(region);
            }
        }

        // 부모-자식 지역 설정
        Map<String, List<RegionEntity>> subRegions = Map.of(
                "서울특별시", List.of(
                        new RegionEntity("11010", "종로구", null, "admin"),
                        new RegionEntity("11020", "중구", null, "admin"),
                        new RegionEntity("11030", "용산구", null, "admin"),
                        new RegionEntity("11040", "성동구", null, "admin"),
                        new RegionEntity("11050", "광진구", null, "admin"),
                        new RegionEntity("11060", "동대문구", null, "admin"),
                        new RegionEntity("11070", "중랑구", null, "admin"),
                        new RegionEntity("11080", "성북구", null, "admin"),
                        new RegionEntity("11090", "강북구", null, "admin"),
                        new RegionEntity("11100", "도봉구", null, "admin"),
                        new RegionEntity("11110", "노원구", null, "admin"),
                        new RegionEntity("11120", "은평구", null, "admin"),
                        new RegionEntity("11130", "서대문구", null, "admin"),
                        new RegionEntity("11140", "마포구", null, "admin"),
                        new RegionEntity("11150", "양천구", null, "admin"),
                        new RegionEntity("11160", "강서구", null, "admin"),
                        new RegionEntity("11170", "구로구", null, "admin"),
                        new RegionEntity("11180", "금천구", null, "admin"),
                        new RegionEntity("11190", "영등포구", null, "admin"),
                        new RegionEntity("11200", "동작구", null, "admin"),
                        new RegionEntity("11210", "관악구", null, "admin"),
                        new RegionEntity("11220", "서초구", null, "admin"),
                        new RegionEntity("11230", "강남구", null, "admin"),
                        new RegionEntity("11240", "송파구", null, "admin"),
                        new RegionEntity("11250", "강동구", null, "admin")
                )
        );

        for (Map.Entry<String, List<RegionEntity>> entry : subRegions.entrySet()) {
            Optional<RegionEntity> parentRegionOptional = regionRepository.findByNameAndDeletedAtIsNull(entry.getKey());
            if (parentRegionOptional.isPresent()) {
                RegionEntity parentRegion = parentRegionOptional.get();
                for (RegionEntity subRegion : entry.getValue()) {
                    if (!regionRepository.existsByNameAndDeletedAtIsNull(subRegion.getName())) {
                        subRegion.setParent(parentRegion); // 부모 지역 설정
                        regionRepository.save(subRegion); // 자식 지역 저장
                    }
                }
            }
        }
    }
}
