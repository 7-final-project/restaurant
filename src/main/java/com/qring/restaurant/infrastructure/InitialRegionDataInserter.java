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

        // 부모 지역 데이터 생성
        List<String[]> initialRegionsData = List.of(
                new String[]{"11", "서울특별시"},
                new String[]{"21", "부산광역시"},
                new String[]{"22", "대구광역시"},
                new String[]{"23", "인천광역시"},
                new String[]{"24", "광주광역시"},
                new String[]{"25", "대전광역시"},
                new String[]{"26", "울산광역시"},
                new String[]{"29", "세종특별자치시"},
                new String[]{"31", "경기도"},
                new String[]{"32", "강원특별자치도"},
                new String[]{"33", "충청북도"},
                new String[]{"34", "충청남도"},
                new String[]{"35", "전라북도"},
                new String[]{"36", "전라남도"},
                new String[]{"37", "경상북도"},
                new String[]{"38", "경상남도"},
                new String[]{"39", "제주특별자치도"}
        );

        for (String[] regionData : initialRegionsData) {
            String code = regionData[0];
            String name = regionData[1];
            if (!regionRepository.existsByNameAndDeletedAtIsNull(name)) {
                regionRepository.save(RegionEntity.createRegionEntity(
                        code,
                        name,
                        "admin"));
            }
        }

        // 부모-자식 지역 설정
        Map<String, List<String[]>> subRegions = Map.of(
                "서울특별시", List.of(
                        new String[]{"11010", "종로구"},
                        new String[]{"11020", "중구"},
                        new String[]{"11030", "용산구"},
                        new String[]{"11040", "성동구"},
                        new String[]{"11050", "광진구"},
                        new String[]{"11060", "동대문구"},
                        new String[]{"11070", "중랑구"},
                        new String[]{"11080", "성북구"},
                        new String[]{"11090", "강북구"},
                        new String[]{"11100", "도봉구"},
                        new String[]{"11110", "노원구"},
                        new String[]{"11120", "은평구"},
                        new String[]{"11130", "서대문구"},
                        new String[]{"11140", "마포구"},
                        new String[]{"11150", "양천구"},
                        new String[]{"11160", "강서구"},
                        new String[]{"11170", "구로구"},
                        new String[]{"11180", "금천구"},
                        new String[]{"11190", "영등포구"},
                        new String[]{"11200", "동작구"},
                        new String[]{"11210", "관악구"},
                        new String[]{"11220", "서초구"},
                        new String[]{"11230", "강남구"},
                        new String[]{"11240", "송파구"},
                        new String[]{"11250", "강동구"}
                )
        );

        for (Map.Entry<String, List<String[]>> entry : subRegions.entrySet()) {
            Optional<RegionEntity> parentRegionOptional = regionRepository.findByNameAndDeletedAtIsNull(entry.getKey());
            if (parentRegionOptional.isPresent()) {
                RegionEntity parentRegion = parentRegionOptional.get();
                for (String[] subRegionData : entry.getValue()) {
                    String code = subRegionData[0];
                    String name = subRegionData[1];
                    if (!regionRepository.existsByNameAndDeletedAtIsNull(name)) {
                        RegionEntity subRegion = RegionEntity.createRegionEntity(
                                code,
                                name,
                                parentRegion,
                                "admin");
                        regionRepository.save(subRegion);
                    }
                }
            }
        }
    }
}
