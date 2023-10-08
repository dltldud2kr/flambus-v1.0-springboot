package flambus.app.service;


import flambus.app.dto.store.StoreDto;
import flambus.app.dto.store.StoreJounalDto;
import flambus.app.dto.store.StoreMapMarkerDto;
import flambus.app.entity.Store;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StoreService {

    //지도 맵에 표시하기 위한 스토어 마커 데이터를 반환합니다.
    List<StoreMapMarkerDto> getMapStoreMakrerInfo();

    StoreDto getStoreInfo(long storeIdx);

    List<StoreJounalDto> getStoreExpJournal();

    Store getStore(long storeIdx);


}
