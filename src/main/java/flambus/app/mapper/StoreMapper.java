package flambus.app.mapper;

import flambus.app.dto.map.MapResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StoreMapper {
    Long findMostUsedTagIdx(@Param("storeIdx") long storeIdx);

    List<Map<String,Object>> getMapStoreMakrer();

}
