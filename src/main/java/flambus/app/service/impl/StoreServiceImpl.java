package flambus.app.service.impl;

import flambus.app.dto.store.StoreDto;
import flambus.app.dto.store.StoreJounalDto;
import flambus.app.dto.store.StoreMapMarkerDto;
import flambus.app.service.StoreService;
import flambus.app.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private UploadService uploadService;


    @Override
    public List<StoreMapMarkerDto> getMapStoreMakrerInfo() {
        return null;
    }

    /**
     * @title 가게 정보를 요청합니다.
     * @return
     */
    @Override
    public StoreDto getStoreInfo() {
        return null;
    }


    /**
     * @title 해당 가게의 사용자들이 작성한 탐험일지 정보 리스트 보기
     * @return
     */
    @Override
    public List<StoreJounalDto> getStorExpJournal() {
        return null;
    }

}
