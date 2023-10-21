package flambus.app.service.impl;

import flambus.app.dto.map.MapResponse;
import flambus.app.mapper.StoreMapper;
import flambus.app.repository.StoreRepository;
import flambus.app.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    /**
     * @return
     * @title 맵에 표시할 마커(가게) 정보를 반환합x니다.
     */
    @Override
    public List<MapResponse.MapStoreMarker> getStoreInfoByMap() {
        List<Map<String, Object>> marker = storeMapper.getMapStoreMakrer();

        List<MapResponse.MapStoreMarker> dto = new ArrayList();

        for (Map<String, Object> data : marker) {

            dto.add(MapResponse.MapStoreMarker.builder()
                    .storeIdx((Long) data.get("idx"))
                    .location(MapResponse.Location.builder()
                            .lng((Float) data.get("latitude"))
                            .lat((Float) data.get("logitude"))
                            .build())
                    .journalCount((Long) data.get("review_count"))
                    .build());
        }

        return dto;

    }
}
