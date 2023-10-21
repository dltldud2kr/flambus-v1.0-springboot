package flambus.app.service;


import flambus.app.dto.store.StoreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StoreService {

    StoreDto getStoreInfo(long storeIdx);

}
